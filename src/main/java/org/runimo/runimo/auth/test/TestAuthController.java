package org.runimo.runimo.auth.test;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.common.response.SuccessResponse;
import org.runimo.runimo.user.enums.UserHttpResponseCode;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile({"test", "dev"})
@RestController
@RequestMapping("/api/v1/auth/test")
@RequiredArgsConstructor
public class TestAuthController {

  private final TestAuthService testAuthService;

  @PostMapping("/login")
  public ResponseEntity<SuccessResponse<TestAuthResponse>> login(
      @RequestBody TestAuthRequest request
  ) {
    TestAuthResponse response = testAuthService.login(request.userId());

      return ResponseEntity.ok(
          SuccessResponse.of(
              UserHttpResponseCode.LOGIN_SUCCESS,
            response
          )
      );
  }

  @PostMapping("/signup")
  public ResponseEntity<SuccessResponse<TestAuthResponse>> signUp() {
    TestAuthResponse response = testAuthService.signUp();
    return ResponseEntity.ok(
        SuccessResponse.of(
            UserHttpResponseCode.SIGNUP_SUCCESS,
            response
        )
    );
  }

}
