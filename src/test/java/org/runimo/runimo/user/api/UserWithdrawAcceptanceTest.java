package org.runimo.runimo.user.api;

import static io.restassured.RestAssured.given;
import static org.runimo.runimo.TestConsts.TEST_USER_UUID;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.runimo.runimo.CleanUpUtil;
import org.runimo.runimo.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserWithdrawAcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    private CleanUpUtil cleanUpUtil;


    @Autowired
    private TokenUtils tokenUtils;
    private String token;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        token = tokenUtils.createTokenByUserPublicId(TEST_USER_UUID);
    }

    @AfterEach
    void tearDown() {
        cleanUpUtil.cleanUpUserInfos();
    }


    @Test
    @Sql(scripts = "/sql/user_mypage_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 회원_탈퇴_성공_시_유저_조회_불가() {

        given()
            .header("Authorization", token)
            .when()
            .get("/api/v1/main")
            .then()
            .statusCode(HttpStatus.OK.value());

        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)

            .when()
            .delete("/api/v1/users")

            .then()
            .log().all()
            .statusCode(204);

        given()
            .header("Authorization", token)
            .when()
            .get("/api/v1/main")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value());

    }

}
