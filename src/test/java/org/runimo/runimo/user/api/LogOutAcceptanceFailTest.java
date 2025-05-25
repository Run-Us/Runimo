package org.runimo.runimo.user.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import com.auth0.jwt.interfaces.DecodedJWT;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.runimo.runimo.auth.jwt.JwtResolver;
import org.runimo.runimo.exceptions.code.CustomResponseCode;
import org.runimo.runimo.user.enums.UserHttpResponseCode;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class LogOutAcceptanceFailTest {

    @LocalServerPort
    int port;

    @MockitoSpyBean
    private JwtResolver jwtResolver;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 로그아웃_실패_토큰_인증_불가() {
        CustomResponseCode responseCode = UserHttpResponseCode.TOKEN_INVALID;

        given()
            .header("Authorization", "--invalid token value--")
            .contentType(ContentType.JSON)

            .when()
            .post("/api/v1/auth/log-out")

            .then()
            .log().all()
            .statusCode(responseCode.getHttpStatusCode().value())

            .body("code", equalTo(responseCode.getCode()))
            .body("message", equalTo(responseCode.getClientMessage()));
    }

    @Test
    void 로그아웃_실패_사용자_찾을_수_없음() {
        CustomResponseCode responseCode = UserHttpResponseCode.USER_NOT_FOUND;
        doReturn("wrong user public id").when(jwtResolver).getUserIdFromJwtToken(any());

        given()
            .header("Authorization", "--some token value--")
            .contentType(ContentType.JSON)

            .when()
            .post("/api/v1/auth/log-out")

            .then()
            .log().all()
            .statusCode(responseCode.getHttpStatusCode().value())

            .body("code", equalTo(responseCode.getCode()))
            .body("message", equalTo(responseCode.getClientMessage()));
    }

}