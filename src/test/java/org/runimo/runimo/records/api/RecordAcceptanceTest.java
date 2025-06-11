package org.runimo.runimo.records.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.runimo.runimo.CleanUpUtil;
import org.runimo.runimo.TokenUtils;
import org.runimo.runimo.records.controller.request.RecordSaveRequest;
import org.runimo.runimo.records.controller.request.RecordUpdateRequest;
import org.runimo.runimo.records.service.usecases.dtos.SegmentPace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class RecordAcceptanceTest {

    private static final String USER_UUID = "test-user-uuid-1";
    @LocalServerPort
    int port;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CleanUpUtil cleanUpUtil;

    @Autowired
    private TokenUtils tokenUtils;

    private String token;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        token = tokenUtils.createTokenByUserPublicId(USER_UUID);
    }

    @AfterEach
    void tearDown() {
        cleanUpUtil.cleanUpUserInfos();
    }

    @Test
    @Sql(scripts = "/sql/user_item_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 달리기_기록_저장_성공_기록_id_반환() throws Exception {
        // given
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
            1800L,
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
            1800L,
            360000L,
            segmentPaces
        );

        String recordId = String.valueOf(given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(request))
            .when()
            .post("/api/v1/records")
            .then()
            .log().all()
            .statusCode(201)
            .extract()
            .path("payload.saved_id")
            .toString());

        // when & then
        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)
            .when()
            .get("/api/v1/records/{recordId}", recordId)
            .then()
            .log().all()
            .statusCode(200)
            .body("payload.record_id", notNullValue())
            .body("payload.segment_pace_list.size()", greaterThanOrEqualTo(1))
            .body("payload.segment_pace_list[0].distance", equalTo(1.0f))
            .body("payload.segment_pace_list[0].pace", equalTo(732000));
    }


    @Test
    @WithMockUser(username = USER_UUID)
    @Sql(scripts = "/sql/weekly_record_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 주간_달리기_거리_조회_성공시_정확한_정보를_반환한다() {
        // given
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
            .body("payload.daily_stats[0].distance_in_meters", equalTo(1000))
            .body("payload.daily_stats[1].date", equalTo("2025-04-01"))
            .body("payload.daily_stats[1].distance_in_meters", equalTo(2000))
            .body("payload.daily_stats[2].date", equalTo("2025-04-02"))
            .body("payload.daily_stats[2].distance_in_meters", equalTo(3000))
            .body("payload.daily_stats[3].date", equalTo("2025-04-03"))
            .body("payload.daily_stats[3].distance_in_meters", equalTo(4000))
            .body("payload.daily_stats[4].date", equalTo("2025-04-04"))
            .body("payload.daily_stats[4].distance_in_meters", equalTo(5000))
            .body("payload.daily_stats[5].date", equalTo("2025-04-05"))
            .body("payload.daily_stats[5].distance_in_meters", equalTo(6000))
            .body("payload.daily_stats[6].date", equalTo("2025-04-06"))
            .body("payload.daily_stats[6].distance_in_meters", equalTo(14000));
    }

    @Test
    @Sql(scripts = "/sql/weekly_record_partial_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 주간_달리기_거리_조회_성공시_일부_날짜에_데이터가_없을때_0을_반환한다() {
        // given
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
            .body("payload.simple_stat", notNullValue())
            .body("payload.simple_stat.total_time_in_seconds", equalTo(18000))
            .body("payload.simple_stat.total_running_count", equalTo(5))
            .body("payload.simple_stat.total_distance_in_meters", equalTo(21000))
            .body("payload.daily_stats.size()", equalTo(5))
            .body("payload.daily_stats[0].date", equalTo("2025-03-31"))
            .body("payload.daily_stats[0].distance_in_meters", equalTo(1000))
            .body("payload.daily_stats[1].date", equalTo("2025-04-01"))
            .body("payload.daily_stats[1].distance_in_meters", equalTo(2000))
            .body("payload.daily_stats[2].date", equalTo("2025-04-04"))
            .body("payload.daily_stats[2].distance_in_meters", equalTo(5000))
            .body("payload.daily_stats[3].date", equalTo("2025-04-05"))
            .body("payload.daily_stats[3].distance_in_meters", equalTo(6000))
            .body("payload.daily_stats[4].date", equalTo("2025-04-06"))
            .body("payload.daily_stats[4].distance_in_meters", equalTo(7000));
    }

    @Test
    @WithMockUser(username = USER_UUID)
    @Sql(scripts = "/sql/weekly_record_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 월간_기록_통계_조회_성공시_정확한_정보를_반환한다() {
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
            .body("payload.simple_stat", notNullValue())
            .body("payload.simple_stat.total_time_in_seconds", equalTo(25200))
            .body("payload.simple_stat.total_running_count", equalTo(7))
            .body("payload.simple_stat.total_distance_in_meters", equalTo(34000))
            .body("payload.daily_stats.size()", equalTo(6))
            .body("payload.daily_stats[0].date", equalTo("2025-04-01"))
            .body("payload.daily_stats[0].distance_in_meters", equalTo(2000))
            .body("payload.daily_stats[1].date", equalTo("2025-04-02"))
            .body("payload.daily_stats[1].distance_in_meters", equalTo(3000))
            .body("payload.daily_stats[5].distance_in_meters", equalTo(14000));
    }

    @Test
    @WithMockUser(username = USER_UUID)
    @Sql(scripts = "/sql/weekly_record_partial_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 월간_기록_통계_조회_잘못된_요청_데이터() {
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
    @Sql(scripts = "/sql/weekly_record_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
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

    @Test
    @Sql(scripts = "/sql/weekly_record_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 사용자_기록_페이지네이션_조회() {

        given()
            .contentType(ContentType.JSON)
            .header("Authorization", token)
            .param("startDate", "2025-04-01")
            .param("endDate", "2025-04-30")
            .param("page", 0)
            .param("size", 5)
            .when()
            .get("/api/v1/records/me")
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .body("payload.pagination.total_pages", equalTo(2))
            .body("payload.pagination.per_page", equalTo(5))
            .body("payload.pagination.current_page", equalTo(0))
            .body("payload.pagination.total_items", equalTo(7))
            .body("payload.items.size()", equalTo(5));
    }

    @Test
    @Sql(scripts = "/sql/weekly_record_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 사용자_기록_페이지네이션_조회_결과_없음() {

        given()
            .contentType(ContentType.JSON)
            .header("Authorization", token)
            .param("startDate", "2010-01-01")
            .param("endDate", "2010-01-30")
            .param("size", 5)
            .when()
            .get("/api/v1/records/me")
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .body("payload.pagination.total_pages", equalTo(0))
            .body("payload.pagination.per_page", equalTo(5))
            .body("payload.pagination.current_page", equalTo(0))
            .body("payload.pagination.total_items", equalTo(0))
            .body("payload.items.size()", equalTo(0));
    }

    @Test
    @Sql(scripts = "/sql/weekly_record_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 사용자_기록_페이지네이션_조회_잘못된_요청() {

        given()
            .contentType(ContentType.JSON)
            .header("Authorization", token)
            .param("page", -1) // 잘못된 페이지 번호
            .param("size", 5)
            .when()
            .get("/api/v1/records/me")
            .then()
            .log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @Sql(scripts = "/sql/weekly_record_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 사용자_기록_업데이트() throws JsonProcessingException {
        RecordUpdateRequest request = new RecordUpdateRequest(
            "예시 제목",
            "오늘은 올림픽 공원을 달렸어요.",
            "https://example.com/image.jpg"
        );

        given()
            .contentType(ContentType.JSON)
            .header("Authorization", token)
            .body(objectMapper.writeValueAsString(request))
            .when()
            .patch("/api/v1/records/record-public-id-1")
            .then()
            .statusCode(HttpStatus.OK.value());

        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)
            .when()
            .get("/api/v1/records/{recordId}", "record-public-id-1")
            .then()
            .log().all()
            .statusCode(200)
            .body("payload.record_id", notNullValue())
            .body("payload.title", equalTo("예시 제목"))
            .body("payload.description", equalTo("오늘은 올림픽 공원을 달렸어요."))
            .body("payload.img_url", equalTo("https://example.com/image.jpg"));

    }
}

