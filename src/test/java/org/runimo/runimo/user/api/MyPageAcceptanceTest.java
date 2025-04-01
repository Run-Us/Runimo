package org.runimo.runimo.user.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.runimo.runimo.CleanUpUtil;
import org.runimo.runimo.auth.jwt.JwtTokenFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class MyPageAcceptanceTest {

  @LocalServerPort
  private Integer port;
  @Autowired
  private CleanUpUtil cleanUpUtil;
  @Autowired
  private JwtTokenFactory jwtTokenFactory;

  @BeforeEach
  void setUp() {
    RestAssured.port = port;
  }

  @AfterEach
  void tearDown() {
    cleanUpUtil.cleanUpUserInfos();
  }


  @Test
  @Sql(scripts = "/sql/user_mypage_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  void 마이페이지_조회_시_프로필정보와_최근_달리기가_표시된다() {

    String accessToken = "Bearer " + jwtTokenFactory.generateAccessToken("test-user-uuid-1");
    int diff = (int) LocalDateTime.parse("2025-03-29T13:00:00").until(LocalDateTime.now(), ChronoUnit.DAYS);
    given()
        .header("Authorization", accessToken)
        .contentType(ContentType.JSON)
        .when()
        .get("/api/v1/users/me")
        .then()
        .log().all()
        .statusCode(HttpStatus.OK.value())
        .body("code", equalTo("USH2001"))
        .body("payload.nickname", equalTo("Daniel"))
        .body("payload.profile_image_url", equalTo("https://example.com/images/user1.png"))
        .body("payload.total_distance_in_meters", equalTo(10000))
        .body("payload.latest_run_date_before", equalTo(diff))
        .body("payload.latest_running_record.title", equalTo("record-title-2"))
        .body("payload.latest_running_record.start_date_time", equalTo("2025-03-29T13:00:00"))
        .body("payload.latest_running_record.distance_in_meters", equalTo(2345))
        .body("payload.latest_running_record.duration_in_seconds", equalTo(3600))
        .body("payload.latest_running_record.average_pace_in_miliseconds", equalTo(6700));
  }

}
