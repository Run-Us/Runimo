package org.runimo.runimo.user.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.runimo.runimo.TestConsts.TEST_USER_UUID;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
class MyPageAcceptanceTest {

    @LocalServerPort
    private Integer port;
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
    void 마이페이지_조회_시_프로필정보와_최근_달리기가_표시된다() {

        int diff = (int) LocalDateTime.parse("2025-03-29T13:00:00")
            .until(LocalDateTime.now(), ChronoUnit.DAYS);
        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)
            .when()
            .get("/api/v1/users/me")
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .body("code", equalTo("MY_PAGE_DATA_FETCHED"))
            .body("payload.nickname", equalTo("Daniel"))
            .body("payload.profile_image_url", equalTo("https://example.com/images/user1.png"))
            .body("payload.total_distance_in_meters", equalTo(10000))
            .body("payload.latest_run_date_before", equalTo(diff))
            .body("payload.latest_running_record_nullable.title", equalTo("record-title-2"))
            .body("payload.latest_running_record_nullable.start_date_time",
                equalTo("2025-03-29T13:00:00"))
            .body("payload.latest_running_record_nullable.distance_in_meters", equalTo(2345))
            .body("payload.latest_running_record_nullable.duration_in_seconds", equalTo(3600))
            .body("payload.latest_running_record_nullable.average_pace_in_miliseconds",
                equalTo(6700));
    }

    @Test
    @Sql(scripts = "/sql/user_device_token_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 유저_토큰이_저장된_유저가_알림_허용_상태를_변경하는_경우() {
        // given
        String requestBody = """
            {
                "allowed": false,
                "device_token": "existing_device_token_12345",
                "platform": "FCM"
            }
            """;

        // when & then
        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .patch("/api/v1/users/me/notifications/permission")
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .body("code", equalTo("NOTIFICATION_ALLOW_UPDATED"));

        // 변경된 상태 확인
        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)
            .when()
            .get("/api/v1/users/me/notifications/permission")
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .body("code", equalTo("NOTIFICATION_ALLOW_FETCHED"))
            .body("payload.allowed", equalTo(false));
    }

    @Test
    @Sql(scripts = "/sql/user_device_token_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 유저_토큰이_없는_사용자가_알림_허용_요청을_보내는_경우_새로_저장된다() {
        // given
        String requestBody = """
            {
                "allowed": true,
                "device_token": "new_device_token_67890",
                "platform": "APNS"
            }
            """;

        // when & then
        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .patch("/api/v1/users/me/notifications/permission")
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .body("code", equalTo("NOTIFICATION_ALLOW_UPDATED"));

        // 생성된 토큰 정보 확인
        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)
            .when()
            .get("/api/v1/users/me/notifications/permission")
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .body("code", equalTo("NOTIFICATION_ALLOW_FETCHED"))
            .body("payload.allowed", equalTo(true));
    }

    @Test
    @Sql(scripts = "/sql/user_device_token_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 유저_토큰이_저장된_유저의_토큰정보가_바뀐_경우_새로운_토큰으로_업데이트된다() {
        // given - 기존 토큰: "existing_device_token_12345", FCM, true
        // 새로운 토큰으로 변경 요청
        String requestBody = """
            {
                "allowed": true,
                "device_token": "updated_device_token_99999",
                "platform": "APNS"
            }
            """;

        // when & then
        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .patch("/api/v1/users/me/notifications/permission")
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .body("code", equalTo("NOTIFICATION_ALLOW_UPDATED"));

        // 업데이트된 정보 확인
        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)
            .when()
            .get("/api/v1/users/me/notifications/permission")
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .body("code", equalTo("NOTIFICATION_ALLOW_FETCHED"))
            .body("payload.allowed", equalTo(true));
    }

    @Test
    @Sql(scripts = "/sql/user_device_token_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 잘못된_플랫폼으로_요청하는_경우_400_에러가_발생한다() {
        // given
        String requestBody = """
            {
                "allowed": true,
                "device_token": "some_device_token",
                "platform": "INVALID_PLATFORM"
            }
            """;

        // when & then - 현재 DevicePlatform.fromString에서 IllegalArgumentException 발생
        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .patch("/api/v1/users/me/notifications/permission")
            .then()
            .log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @Sql(scripts = "/sql/user_mypage_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 디바이스_토큰이_없는_사용자가_알림_허용_조회시_404_에러가_발생한다() {
        // given - 토큰이 저장되지 않은 상태

        // when & then
        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)
            .when()
            .get("/api/v1/users/me/notifications/permission")
            .then()
            .log().all()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("code", equalTo("DEVICE_TOKEN_NOT_FOUND"));
    }

}
