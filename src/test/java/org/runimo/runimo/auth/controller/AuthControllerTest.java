package org.runimo.runimo.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.runimo.runimo.auth.controller.request.KakaoLoginRequest;
import org.runimo.runimo.auth.exceptions.UserJwtException;
import org.runimo.runimo.auth.jwt.JwtResolver;
import org.runimo.runimo.auth.service.OidcService;
import org.runimo.runimo.auth.service.SignUpUsecase;
import org.runimo.runimo.auth.service.TokenRefreshService;
import org.runimo.runimo.auth.service.dto.AuthResult;
import org.runimo.runimo.auth.service.dto.AuthStatus;
import org.runimo.runimo.auth.service.dto.SignupUserResponse;
import org.runimo.runimo.auth.service.dto.TokenPair;
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
        mockMvc.perform(
                multipart("/api/v1/auth/signup")
                    .param("request",
                        "{\"registerToken\":\"invalid-token\", \"nickname\":\"RunimoUser\"}"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.code").value(UserHttpResponseCode.TOKEN_INVALID.getCode()))
            .andExpect(
                jsonPath("$.message").value(UserHttpResponseCode.TOKEN_INVALID.getClientMessage()));
    }

    @Test
    @DisplayName("디바이스 토큰 없이 회원가입 요청시 201 응답 (구버전 앱 호환)")
    void 회원가입_디바이스_토큰_없음_201응답() throws Exception {
        // given - 디바이스 토큰 없는 회원가입도 성공해야 함
        given(signUpUsecase.register(any())).willReturn(
            new SignupUserResponse(
                1L, "RunimoUser", "profile_url", new TokenPair("access_token", "refresh_token"),
                "exmaple_egg_name",
                "example_egg_type",
                "example_egg_url",
                "ECODE"
            )
        );

        // when & then
        mockMvc.perform(
                multipart("/api/v1/auth/signup")
                    .param("request",
                        "{\"registerToken\":\"valid-token\", \"nickname\":\"RunimoUser\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.payload.egg_code").value("ECODE"))
            .andExpect(jsonPath("$.code")
                .value(UserHttpResponseCode.SIGNUP_SUCCESS.getCode()));
    }

    @Test
    @DisplayName("디바이스 토큰 있으나 플랫폼 없이 회원가입 요청시 400 응답")
    void 회원가입_플랫폼_없음_400응답() throws Exception {
        //given
        given(signUpUsecase.register(any()))
            .willThrow(new IllegalArgumentException("디바이스 토큰이 있으면 플랫폼도 필수입니다."));

        mockMvc.perform(
                multipart("/api/v1/auth/signup")
                    .param("request",
                        "{\"registerToken\":\"valid-token\", \"nickname\":\"RunimoUser\", \"deviceToken\":\"valid_device_token\"}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("정상적인 회원가입 요청시 201 응답")
    void 회원가입_성공_201응답() throws Exception {
        // given
        given(signUpUsecase.register(any())).willReturn(
            new SignupUserResponse(
                1L, "RunimoUser", "profile_url", new TokenPair("access_token", "refresh_token"),
                "exmaple_egg_name",
                "example_egg_type",
                "example_egg_url",
                "ECODE"
            )
        );

        // when & then
        mockMvc.perform(
                multipart("/api/v1/auth/signup")
                    .param("request",
                        "{\"registerToken\":\"valid-token\", \"nickname\":\"RunimoUser\", \"deviceToken\":\"valid_device_token\", \"devicePlatform\":\"FCM\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.code").value(UserHttpResponseCode.SIGNUP_SUCCESS.getCode()));
    }
}
