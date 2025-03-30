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

}

