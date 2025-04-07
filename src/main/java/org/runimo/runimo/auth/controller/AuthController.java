package org.runimo.runimo.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.auth.controller.request.AppleLoginRequest;
import org.runimo.runimo.auth.controller.request.AuthSignupRequest;
import org.runimo.runimo.auth.controller.request.KakaoLoginRequest;
import org.runimo.runimo.auth.service.OidcService;
import org.runimo.runimo.auth.service.SignUpUsecase;
import org.runimo.runimo.auth.service.TokenRefreshService;
import org.runimo.runimo.auth.service.dtos.AuthResponse;
import org.runimo.runimo.auth.service.dtos.SignupUserResponse;
import org.runimo.runimo.auth.service.dtos.TokenPair;
import org.runimo.runimo.common.response.SuccessResponse;
import org.runimo.runimo.user.enums.UserHttpResponseCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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
      @ApiResponse(responseCode = "401", description = "인증 실패"),
      @ApiResponse(responseCode = "404", description = "등록되지 않은 사용자")
  })
  @PostMapping("/kakao")
  public ResponseEntity<SuccessResponse<AuthResponse>> kakaoLogin(
      @RequestBody KakaoLoginRequest request
  ) {
    AuthResponse res = oidcService.kakaoLogin(request.oidcToken());
    return ResponseEntity.ok().body(
        SuccessResponse.of(
            UserHttpResponseCode.LOGIN_SUCCESS,
            res
        )
    );
  }

  @Operation(summary = "애플 소셜 로그인", description = "애플 OIDC 토큰을 이용하여 로그인합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "로그인 성공"),
      @ApiResponse(responseCode = "401", description = "인증 실패"),
      @ApiResponse(responseCode = "404", description = "등록되지 않은 사용자")
  })
  @PostMapping("/apple")
  public ResponseEntity<SuccessResponse<AuthResponse>> appleLogin(
      @RequestBody final AppleLoginRequest request
  ) {
    AuthResponse res = oidcService.appleLogin(request.authCode(), request.codeVerifier());
    return ResponseEntity.ok().body(
        SuccessResponse.of(
            UserHttpResponseCode.LOGIN_SUCCESS,
            res
        )
    );
  }

  @Operation(summary = "사용자 회원가입 및 로그인", description = "사용자가 OIDC 토큰을 사용하여 회원가입 후 로그인합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "회원가입 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
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
      @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰")
  })
  @PostMapping("/refresh")
  public ResponseEntity<SuccessResponse<TokenPair>> refreshToken(
      @RequestHeader("Authorization") String refreshToken) {
    String token =  refreshToken.replace("Bearer ", "");
    TokenPair newTokens = tokenRefreshService.refreshAccessToken(token);
    return ResponseEntity.ok().body(
        SuccessResponse.of(
            UserHttpResponseCode.REFRESH_SUCCESS,
            newTokens
        )
    );
  }
}
