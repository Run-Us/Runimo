package org.runimo.runimo.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.runimo.runimo.auth.controller.dtos.KakaoLoginRequest;
import org.runimo.runimo.auth.exceptions.UserJwtException;
import org.runimo.runimo.auth.jwt.JwtResolver;
import org.runimo.runimo.auth.service.OidcService;
import org.runimo.runimo.auth.service.SignUpUsecase;
import org.runimo.runimo.auth.service.TokenRefreshService;
import org.runimo.runimo.auth.service.dtos.AuthResult;
import org.runimo.runimo.auth.service.dtos.AuthStatus;
import org.runimo.runimo.auth.service.dtos.TokenPair;
import org.runimo.runimo.configs.ControllerTest;
import org.runimo.runimo.user.UserFixtures;
import org.runimo.runimo.user.enums.UserHttpResponseCode;
import org.runimo.runimo.user.service.UserFinder;
import org.runimo.runimo.user.service.UserRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@ControllerTest(controllers = {AuthController.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OidcService oidcService;

    @MockitoBean
    private TokenRefreshService tokenRefreshService;

    @MockitoBean
    private SignUpUsecase signUpUsecase;

    @MockitoBean
    private UserRegisterService userRegisterService;

    @MockitoBean
    private UserFinder userFinder;

    @MockitoBean
    private JwtResolver jwtResolver;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 카카오_로그인_INVALID_401응답() throws Exception {
        // given
        given(oidcService.kakaoLogin(any())).willThrow(
            UserJwtException.of(UserHttpResponseCode.LOGIN_FAIL_INVALID));
        // when & then
        mockMvc.perform(post("/api/v1/auth/kakao")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"oidcToken\":\"invalid-token\"}"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.code").value(UserHttpResponseCode.LOGIN_FAIL_INVALID.getCode()))
            .andExpect(jsonPath("$.message").value(
                UserHttpResponseCode.LOGIN_FAIL_INVALID.getClientMessage()));
    }

    @Test
    void 카카오_로그인_미등록_사용자_404응답() throws Exception {
        // given
        KakaoLoginRequest request = new KakaoLoginRequest("valid-token");

        given(oidcService.kakaoLogin(any())).willReturn(
            AuthResult.signupNeeded(AuthStatus.SIGNUP_NEEDED, "signup-token")
        );

        // when & then
        mockMvc.perform(post("/api/v1/auth/kakao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value(
                UserHttpResponseCode.LOGIN_FAIL_NOT_SIGN_IN.getCode()))
            .andExpect(jsonPath("$.message").value(
                UserHttpResponseCode.LOGIN_FAIL_NOT_SIGN_IN.getClientMessage()));
    }

    @Test
    void 카카오_로그인_성공_200응답() throws Exception {
        // given
        AuthResult authResult = AuthResult.success(
            AuthStatus.LOGIN_SUCCESS,
            UserFixtures.getDefaultUser(),
            new TokenPair(
                "access-token",
                "refresh-token")
        );
        given(oidcService.kakaoLogin(any())).willReturn(authResult);

        // when & then
        mockMvc.perform(post("/api/v1/auth/kakao")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"oidcToken\":\"valid-token\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(UserHttpResponseCode.LOGIN_SUCCESS.getCode()))
            .andExpect(jsonPath("$.payload.nickname").value("test"))
            .andExpect(jsonPath("$.payload.img_url").value("test"))
            .andExpect(jsonPath("$.payload.access_token").value("access-token"))
            .andExpect(jsonPath("$.payload.refresh_token").value("refresh-token"));
    }

    @Test
    void 카카오_로그인_서버에러_500응답() throws Exception {
        // given
        given(oidcService.kakaoLogin(any())).willThrow(new RuntimeException("Unexpected error"));

        // when & then
        mockMvc.perform(post("/api/v1/auth/kakao")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"oidcToken\":\"valid-token\"}"))
            .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("유효하지 않은 토큰으로 회원가입 시 401 응답")
    void 회원가입_토큰_INVALID_401응답() throws Exception {
        // given
        given(signUpUsecase.register(any()))
            .willThrow(UserJwtException.of(UserHttpResponseCode.TOKEN_INVALID));

        // when & then
        mockMvc.perform(post("/api/v1/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"register_token\":\"invalid-token\", \"nickname\":\"RunimoUser\", \"img_url\":\"https://example.com/image.jpg\"}"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.code").value(UserHttpResponseCode.TOKEN_INVALID.getCode()))
            .andExpect(
                jsonPath("$.message").value(UserHttpResponseCode.TOKEN_INVALID.getClientMessage()));
    }
}
