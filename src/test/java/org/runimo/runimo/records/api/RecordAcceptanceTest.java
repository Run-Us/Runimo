package org.runimo.runimo.records.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.runimo.runimo.CleanUpUtil;
import org.runimo.runimo.auth.jwt.JwtTokenFactory;
import org.runimo.runimo.records.controller.requests.RecordSaveRequest;
import org.runimo.runimo.records.service.usecases.dtos.SegmentPace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class RecordAcceptanceTest {

  @LocalServerPort
  int port;

  @Autowired
  private JwtTokenFactory jwtTokenFactory;
  private static final String USER_UUID = "test-user-uuid-1";
  private static final String AUTH_HEADER_PREFIX = "Bearer ";
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private CleanUpUtil cleanUpUtil;

  @BeforeEach
  void setUp() {
    RestAssured.port = port;
  }

  @AfterEach
  void tearDown() {
    cleanUpUtil.cleanUpUserInfos();
  }

  @Test
  @Sql(scripts = "/sql/user_item_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  void 달리기_기록_저장_성공_기록_id_반환() throws Exception {
    // given
    String token = "Bearer " + jwtTokenFactory.generateAccessToken("test-user-uuid-1");

    // 구간별 페이스 데이터
    List<SegmentPace> segmentPaces = List.of(
        new SegmentPace(1.0, 732000),
        new SegmentPace(2.0, 650000),
        new SegmentPace(3.0, 605000),
        new SegmentPace(4.0, 615000),
        new SegmentPace(0.1, 60000)
    );

    // RecordSaveRequest 객체 생성
    RecordSaveRequest request = new RecordSaveRequest(
        LocalDateTime.of(2025, 3, 30, 9, 30, 0),
        LocalDateTime.of(2025, 3, 30, 10, 0, 0),
        5000L,
        360000L,
        segmentPaces
    );

    // when & then
    given()
        .header("Authorization", token)
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(request))
        .when()
        .post("/api/v1/records")
        .then()
        .log().all()
        .statusCode(201)
        .body("payload.saved_id", notNullValue());
  }

  @Test
  @Sql(scripts = "/sql/user_item_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  void 달리기_기록_조회_성공_구간별_페이스_포함() throws Exception {
    // given
    String token = "Bearer " + jwtTokenFactory.generateAccessToken("test-user-uuid-1");

    // 선행 조건: 달리기 기록 저장
    List<SegmentPace> segmentPaces = List.of(
        new SegmentPace(1.0, 732000),
        new SegmentPace(2.0, 650000),
        new SegmentPace(3.0, 605000),
        new SegmentPace(4.0, 615000),
        new SegmentPace(0.1, 60000)
    );

    RecordSaveRequest request = new RecordSaveRequest(
        LocalDateTime.of(2025, 3, 30, 9, 30, 0),
        LocalDateTime.of(2025, 3, 30, 10, 0, 0),
        5000L,
        360000L,
        segmentPaces
    );

    given()
        .header("Authorization", token)
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(request))
        .when()
        .post("/api/v1/records")
        .then()
        .log().all()
        .statusCode(201);

    // when & then
    given()
        .header("Authorization", token)
        .contentType(ContentType.JSON)
        .when()
        .get("/api/v1/records/1")
        .then()
        .log().all()
        .statusCode(200)
        .body("payload.record_id", equalTo(1))
        .body("payload.segment_pace_list.size()", greaterThanOrEqualTo(1))
        .body("payload.segment_pace_list[0].distance", equalTo(1.0f))
        .body("payload.segment_pace_list[0].pace", equalTo(732000));
  }



  @Test
  @WithMockUser(username = USER_UUID)
  @Sql(scripts = "/sql/weekly_record_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  void 주간_달리기_거리_조회_성공시_정확한_정보를_반환한다() {
    // given
    String token = AUTH_HEADER_PREFIX + jwtTokenFactory.generateAccessToken(USER_UUID);

    // when & then
    given()
        .header("Authorization", token)
        .contentType(ContentType.JSON)
        .param("startDate", "2025-03-31")
        .param("endDate", "2025-04-06")
        .when()
        .get("/api/v1/records/stats/weekly")
        .then()
        .log().all()
        .statusCode(200)
        .body("code", equalTo("MY_PAGE_DATA_FETCHED"))
        .body("payload.daily_stats.size()", equalTo(7))
        .body("payload.daily_stats[0].date", equalTo("2025-03-31"))
        .body("payload.daily_stats[0].distance", equalTo(1000))
        .body("payload.daily_stats[1].date", equalTo("2025-04-01"))
        .body("payload.daily_stats[1].distance", equalTo(2000))
        .body("payload.daily_stats[2].date", equalTo("2025-04-02"))
        .body("payload.daily_stats[2].distance", equalTo(3000))
        .body("payload.daily_stats[3].date", equalTo("2025-04-03"))
        .body("payload.daily_stats[3].distance", equalTo(4000))
        .body("payload.daily_stats[4].date", equalTo("2025-04-04"))
        .body("payload.daily_stats[4].distance", equalTo(5000))
        .body("payload.daily_stats[5].date", equalTo("2025-04-05"))
        .body("payload.daily_stats[5].distance", equalTo(6000))
        .body("payload.daily_stats[6].date", equalTo("2025-04-06"))
        .body("payload.daily_stats[6].distance", equalTo(7000));
  }

  @Test
  @Sql(scripts = "/sql/weekly_record_partial_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  void 주간_달리기_거리_조회_성공시_일부_날짜에_데이터가_없을때_0을_반환한다() {
    // given
    String token = AUTH_HEADER_PREFIX + jwtTokenFactory.generateAccessToken(USER_UUID);

    // when & then
    given()
        .header("Authorization", token)
        .contentType(ContentType.JSON)
        .param("startDate", "2025-03-31")
        .param("endDate", "2025-04-06")
        .when()
        .get("/api/v1/records/stats/weekly")
        .then()
        .log().ifValidationFails()
        .statusCode(200)
        .body("code", equalTo("MY_PAGE_DATA_FETCHED"))
        .body("payload.daily_stats.size()", equalTo(5))
        .body("payload.daily_stats[0].date", equalTo("2025-03-31"))
        .body("payload.daily_stats[0].distance", equalTo(1000))
        .body("payload.daily_stats[1].date", equalTo("2025-04-01"))
        .body("payload.daily_stats[1].distance", equalTo(2000))
        .body("payload.daily_stats[2].date", equalTo("2025-04-04"))
        .body("payload.daily_stats[2].distance", equalTo(5000))
        .body("payload.daily_stats[3].date", equalTo("2025-04-05"))
        .body("payload.daily_stats[3].distance", equalTo(6000))
        .body("payload.daily_stats[4].date", equalTo("2025-04-06"))
        .body("payload.daily_stats[4].distance", equalTo(7000));
  }

  @Test
  @WithMockUser(username = USER_UUID)
  @Sql(scripts = "/sql/weekly_record_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  void 월간_기록_통계_조회_성공시_정확한_정보를_반환한다() {
    // given
    String token = AUTH_HEADER_PREFIX + jwtTokenFactory.generateAccessToken(USER_UUID);

    // when & then
    given()
        .header("Authorization", token)
        .contentType(ContentType.JSON)
        .param("year", 2025)
        .param("month", 4)
        .when()
        .get("/api/v1/records/stats/monthly")
        .then()
        .log().all()
        .statusCode(200)
        .body("code", equalTo("MY_PAGE_DATA_FETCHED"))
        .body("payload.daily_stats.size()", equalTo(6))
        .body("payload.daily_stats[0].date", equalTo("2025-04-01"))
        .body("payload.daily_stats[0].distance", equalTo(2000))
        .body("payload.daily_stats[1].date", equalTo("2025-04-02"))
        .body("payload.daily_stats[1].distance", equalTo(3000));
  }

  @Test
  @WithMockUser(username = USER_UUID)
  @Sql(scripts = "/sql/weekly_record_partial_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  void 월간_기록_통계_조회_잘못된_요청_데이터() {
    // given
    String token = AUTH_HEADER_PREFIX + jwtTokenFactory.generateAccessToken(USER_UUID);

    // when & then
    given()
        .header("Authorization", token)
        .contentType(ContentType.JSON)
        .param("year", 2025)
        .param("month", 13) // 잘못된 월
        .when()
        .get("/api/v1/records/stats/monthly")
        .then()
        .log().all()
        .statusCode(400);
  }

  @Test
  void 월간_기록_통계_조회_인증_실패() {
    // when & then
    given()
        .contentType(ContentType.JSON)
        .param("year", 2025)
        .param("month", 4)
        .when()
        .get("/api/v1/records/stats/monthly")
        .then()
        .log().all()
        .statusCode(401);
  }
}

