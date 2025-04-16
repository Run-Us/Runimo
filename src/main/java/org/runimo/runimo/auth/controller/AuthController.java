package org.runimo.runimo.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.auth.controller.request.AppleLoginRequest;
import org.runimo.runimo.auth.controller.request.AuthSignupRequest;
import org.runimo.runimo.auth.controller.request.KakaoLoginRequest;
import org.runimo.runimo.auth.service.OidcService;
import org.runimo.runimo.auth.service.SignUpUsecase;
import org.runimo.runimo.auth.service.TokenRefreshService;
import org.runimo.runimo.auth.service.dto.AuthResponse;
import org.runimo.runimo.auth.service.dto.AuthResult;
import org.runimo.runimo.auth.service.dto.AuthStatus;
import org.runimo.runimo.auth.service.dto.SignupUserResponse;
import org.runimo.runimo.auth.service.dto.TokenPair;
import org.runimo.runimo.common.response.ErrorResponse;
import org.runimo.runimo.common.response.Response;
import org.runimo.runimo.common.response.SuccessResponse;
import org.runimo.runimo.exceptions.RegisterErrorResponse;
import org.runimo.runimo.user.enums.UserHttpResponseCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth API", description = "인증 관련 API 모음")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final OidcService oidcService;
    private final TokenRefreshService tokenRefreshService;
    private final SignUpUsecase signUpUsecase;

    @Operation(summary = "카카오 소셜 로그인", description = "카카오 OIDC 토큰을 이용하여 로그인합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그인 성공"),
        @ApiResponse(responseCode = "401", description = "인증 실패",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "등록되지 않은 사용자",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = RegisterErrorResponse.class)))
    })
    @PostMapping("/kakao")
    public ResponseEntity<Response> kakaoLogin(
        @RequestBody KakaoLoginRequest request
    ) {
        AuthResult res = oidcService.kakaoLogin(request.oidcToken());
        return buildAuthResponse(res);
    }

    @Operation(summary = "애플 소셜 로그인", description = "애플 OIDC 토큰을 이용하여 로그인합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그인 성공"),
        @ApiResponse(responseCode = "401", description = "인증 실패",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "등록되지 않은 사용자",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = RegisterErrorResponse.class)))
    })
    @PostMapping("/apple")
    public ResponseEntity<Response> appleLogin(
        @RequestBody final AppleLoginRequest request
    ) {
        AuthResult res = oidcService.appleLogin(request.authCode(), request.codeVerifier());
        return buildAuthResponse(res);
    }

    @Operation(summary = "사용자 회원가입 및 로그인", description = "사용자가 OIDC 토큰을 사용하여 회원가입 후 로그인합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "회원가입 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 데이터",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "이미 존재하는 사용자")
    })
    @PostMapping("/signup")
    public ResponseEntity<SuccessResponse<SignupUserResponse>> signupAndLogin(
        @Valid @RequestBody AuthSignupRequest request) {
        SignupUserResponse authResult = signUpUsecase.register(
            request.toUserSignupCommand()
        );
        return ResponseEntity.created(URI.create("/api/v1/user" + authResult.userId()))
            .body(SuccessResponse.of(
                UserHttpResponseCode.SIGNUP_SUCCESS,
                authResult)
            );
    }

    @Operation(summary = "토큰 갱신", description = "리프레시 토큰을 사용하여 액세스 토큰을 갱신합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "토큰 갱신 성공"),
        @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping("/refresh")
    public ResponseEntity<SuccessResponse<TokenPair>> refreshToken(
        @RequestHeader("Authorization") String refreshToken) {
        String token = refreshToken.replace("Bearer ", "");
        TokenPair newTokens = tokenRefreshService.refreshAccessToken(token);
        return ResponseEntity.ok().body(
            SuccessResponse.of(
                UserHttpResponseCode.REFRESH_SUCCESS,
                newTokens
            )
        );
    }


    /**
     * 로그인 결과에 따라 적절한 응답을 생성합니다. 회원가입이 필요하면 404 응답을 반환하고, 로그인 성공 시 200 응답을 반환합니다.
     */
    private ResponseEntity<Response> buildAuthResponse(AuthResult res) {
        if (res.status() == AuthStatus.SIGNUP_NEEDED) {
            return ResponseEntity.status(404)
                .body(new RegisterErrorResponse(UserHttpResponseCode.LOGIN_FAIL_NOT_SIGN_IN,
                    res.registerToken()));
        }
        return ResponseEntity.ok(
            SuccessResponse.of(UserHttpResponseCode.LOGIN_SUCCESS,
                new AuthResponse(res.nickname(), res.imgUrl(), res.accessToken(),
                    res.refreshToken()))
        );
    }
}
