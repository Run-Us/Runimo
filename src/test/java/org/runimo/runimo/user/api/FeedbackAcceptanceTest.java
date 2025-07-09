package org.runimo.runimo.user.api;

import static org.runimo.runimo.TestConsts.TEST_USER_UUID;

import io.restassured.RestAssured;
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
public class FeedbackAcceptanceTest {

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
    @Sql(scripts = "/sql/user_default_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 피드백_저장시_성공() {
        // given
        String feedbackBody = """
            {
              "rate": 3,
              "feedback": "이것은 피드백입니다."
            }
            """;

        // when & then
        RestAssured.given()
            .header("Authorization", token)
            .contentType("application/json")
            .body(feedbackBody)
            .when()
            .post("/api/v1/feedback")
            .then()
            .log().ifError()
            .statusCode(HttpStatus.CREATED.value());


    }


}
