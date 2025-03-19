package org.runimo.runimo.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.common.response.SuccessResponse;
import org.runimo.runimo.user.controller.request.AuthLoginRequest;
import org.runimo.runimo.user.controller.request.AuthSignupRequest;
import org.runimo.runimo.user.controller.request.UseItemRequest;
import org.runimo.runimo.user.domain.SocialProvider;
import org.runimo.runimo.user.enums.UserHttpResponseCode;
import org.runimo.runimo.user.service.dtos.*;
import org.runimo.runimo.user.service.usecases.UseItemUsecase;
import org.runimo.runimo.user.service.usecases.UserOAuthUsecase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Tag(name = "USER", description = "사용자 관련 API")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final UserOAuthUsecase userOAuthUsecase;
  private final UseItemUsecase useItemUsecase;

  @Operation(summary = "사용자 로그인", description = "사용자가 OIDC 토큰을 사용하여 로그인합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "로그인 성공",
          content = @Content(schema = @Schema(implementation = AuthResponse.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
      @ApiResponse(responseCode = "401", description = "인증 실패")
  })
  @PostMapping("/login")
  public ResponseEntity<SuccessResponse<AuthResponse>> login(
      @Valid @RequestBody AuthLoginRequest request
  ) {
    TokenPair authResult = userOAuthUsecase.validateAndLogin(
        request.oidcToken(),
        SocialProvider.valueOf(request.provider())
    );
    return ResponseEntity.ok().body(
        SuccessResponse.of(
            UserHttpResponseCode.LOGIN_SUCCESS,
            new AuthResponse(authResult)
        ));
  }

  @Operation(summary = "사용자 회원가입 및 로그인", description = "사용자가 OIDC 토큰을 사용하여 회원가입 후 로그인합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "회원가입 성공",
          content = @Content(schema = @Schema(implementation = AuthResponse.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
      @ApiResponse(responseCode = "409", description = "이미 존재하는 사용자")
  })
  @PostMapping("/signup")
  public ResponseEntity<SuccessResponse<AuthResponse>> signupAndLogin(
      @Valid @RequestBody AuthSignupRequest request) {
    SignupUserInfo authResult = userOAuthUsecase.validateAndSignup(
        request.toUserSignupCommand(),
        request.oidcToken(),
        SocialProvider.valueOf(request.provider())
    );
    return ResponseEntity.created(URI.create("/api/v1/user" + authResult.userId()))
        .body(SuccessResponse.of(
            UserHttpResponseCode.SIGNUP_SUCCESS,
            new AuthResponse(authResult.tokenPair())));
  }

  @Operation(summary = "아이템 사용", description = "사용자가 아이템을 사용합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "아이템 사용 성공",
          content = @Content(schema = @Schema(implementation = UseItemResponse.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
      @ApiResponse(responseCode = "401", description = "인증 실패"),
      @ApiResponse(responseCode = "404", description = "아이템 없음")
  })
  @PostMapping("/me/items/use")
  public ResponseEntity<SuccessResponse<UseItemResponse>> useItem(
      @UserId Long userId,
      @Valid @RequestBody UseItemRequest request
  ) {
    UseItemResponse useItemResponse = useItemUsecase.useItem(
        new UseItemCommand(userId, request.itemId(), request.quantity())
    );
    return ResponseEntity.ok().body(
        SuccessResponse.of(
            UserHttpResponseCode.USE_ITEM_SUCCESS,
            useItemResponse
        ));
  }
}
