package org.runimo.runimo.rewards.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.runimo.runimo.CleanUpUtil;
import org.runimo.runimo.auth.jwt.JwtTokenFactory;
import org.runimo.runimo.records.RecordFixtures;
import org.runimo.runimo.records.controller.request.RecordSaveRequest;
import org.runimo.runimo.rewards.controller.request.RewardClaimRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class RewardAcceptanceTest {

    private static final LocalDateTime pivotTime = LocalDateTime.of(2023, 10, 1, 10, 0);
    @LocalServerPort
    private int port;
    @Autowired
    private JwtTokenFactory jwtTokenFactory;
    @Autowired
    private CleanUpUtil cleanUpUtil;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach()
    void tearDown() {
        cleanUpUtil.cleanUpUserInfos();
    }


    @Test
    @Sql(scripts = "/sql/user_item_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 달리기_기록_저장_후_주간_첫번째_달리기보상_수령() throws JsonProcessingException {
        String header = "Bearer " + jwtTokenFactory.generateAccessToken("test-user-uuid-1");
        RecordSaveRequest request = RecordFixtures.createRecordSaveRequest();
        ValidatableResponse res = given()
            .header("Authorization", header)
            .body(objectMapper.writeValueAsString(request))
            .contentType(ContentType.JSON)
            .when()
            .post("/api/v1/records")
            .then()
            .log().ifValidationFails()
            .statusCode(HttpStatus.CREATED.value())
            .body("payload", notNullValue())
            .body("payload.saved_id", notNullValue());

        String recordId = res.extract().path("payload.saved_id");

        RewardClaimRequest rewardClaimRequest = new RewardClaimRequest(recordId);

        given()
            .header("Authorization", header)
            .body(objectMapper.writeValueAsString(rewardClaimRequest))
            .contentType(ContentType.JSON)
            .when()
            .post("/api/v1/rewards/runnings")
            .then()
            .log().all()
            .body("payload", notNullValue());
    }

    @Test
    @Sql(scripts = "/sql/user_item_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 달리기_보상_수령_후_재시도_시_예외() throws JsonProcessingException {
        String header = "Bearer " + jwtTokenFactory.generateAccessToken("test-user-uuid-1");
        RecordSaveRequest request = RecordFixtures.createRecordSaveRequest();
        ValidatableResponse res = given()
            .header("Authorization", header)
            .body(objectMapper.writeValueAsString(request))
            .contentType(ContentType.JSON)
            .when()
            .post("/api/v1/records")
            .then()
            .log().ifValidationFails()
            .statusCode(HttpStatus.CREATED.value())
            .body("payload", notNullValue())
            .body("payload.saved_id", notNullValue());

        String recordId = res.extract().path("payload.saved_id");

        RewardClaimRequest rewardClaimRequest = new RewardClaimRequest(recordId);

        given()
            .header("Authorization", header)
            .body(objectMapper.writeValueAsString(rewardClaimRequest))
            .contentType(ContentType.JSON)
            .when()
            .post("/api/v1/rewards/runnings")
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .body("payload", notNullValue());

        given()
            .header("Authorization", header)
            .body(objectMapper.writeValueAsString(rewardClaimRequest))
            .contentType(ContentType.JSON)
            .when()
            .post("/api/v1/rewards/runnings")
            .then()
            .log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @Sql(scripts = "/sql/user_item_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 첫번째_기록이_이번주_첫번째_달리기가_아니라서_알_미지급() throws JsonProcessingException {
        String header = "Bearer " + jwtTokenFactory.generateAccessToken("test-user-uuid-1");

        // 첫번째 기록 저장
        RecordSaveRequest firstRequest = new RecordSaveRequest(
            pivotTime,
            pivotTime.plusMinutes(20),
            1000L,
            1000L,
            null);
        ValidatableResponse firstRes = given()
            .header("Authorization", header)
            .body(objectMapper.writeValueAsString(firstRequest))
            .contentType(ContentType.JSON)
            .when()
            .post("/api/v1/records")
            .then()
            .log().ifValidationFails()
            .statusCode(HttpStatus.CREATED.value())
            .body("payload", notNullValue())
            .body("payload.saved_id", notNullValue());

        String firstRecordId = firstRes.extract().path("payload.saved_id");

        // 두번째 기록 저장 (startedAt이 더 빠르게)
        RecordSaveRequest secondRequest = new RecordSaveRequest(
            pivotTime.minusDays(1),
            pivotTime.minusDays(1).plusMinutes(20),
            1000L,
            1000L,
            null);
        given()
            .header("Authorization", header)
            .body(objectMapper.writeValueAsString(secondRequest))
            .contentType(ContentType.JSON)
            .when()
            .post("/api/v1/records")
            .then()
            .log().ifValidationFails()
            .statusCode(HttpStatus.CREATED.value())
            .body("payload", notNullValue())
            .body("payload.saved_id", notNullValue());

        // 첫번째 기록의 id로 보상 요청
        RewardClaimRequest rewardClaimRequest = new RewardClaimRequest(firstRecordId);

        given()
            .header("Authorization", header)
            .body(objectMapper.writeValueAsString(rewardClaimRequest))
            .contentType(ContentType.JSON)
            .when()
            .post("/api/v1/rewards/runnings")
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .body("payload.egg_type", nullValue())
            .body("payload.egg_code", equalTo("EMPTY"));
    }

    @Test
    @Sql(scripts = "/sql/user_item_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 달리기_기록_저장_후_애정_포인트_지급() throws JsonProcessingException {
        String header = "Bearer " + jwtTokenFactory.generateAccessToken("test-user-uuid-1");
        RecordSaveRequest request = RecordFixtures.createRecordSaveRequest();

        ValidatableResponse res = given()
            .header("Authorization", header)
            .body(objectMapper.writeValueAsString(request))
            .contentType(ContentType.JSON)
            .when()
            .post("/api/v1/records")
            .then()
            .log().ifValidationFails()
            .statusCode(HttpStatus.CREATED.value())
            .body("payload", notNullValue())
            .body("payload.saved_id", notNullValue());

        String recordId = res.extract().path("payload.saved_id");

        RewardClaimRequest rewardClaimRequest = new RewardClaimRequest(recordId);

        given()
            .header("Authorization", header)
            .body(objectMapper.writeValueAsString(rewardClaimRequest))
            .contentType(ContentType.JSON)
            .when()
            .post("/api/v1/rewards/runnings")
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .body("payload.love_point_amount", notNullValue())
            .body("payload.love_point_amount", greaterThan(0));
    }

    @Test
    @Sql(scripts = "/sql/user_item_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 기준거리_미달이면_애정을_미지급() throws JsonProcessingException {
        String header = "Bearer " + jwtTokenFactory.generateAccessToken("test-user-uuid-1");
        RecordSaveRequest request = new RecordSaveRequest(
            pivotTime,
            pivotTime.plusMinutes(20),
            900L,
            1000L,
            List.of()
        );
        ValidatableResponse res = given()
            .header("Authorization", header)
            .body(objectMapper.writeValueAsString(request))
            .contentType(ContentType.JSON)
            .when()
            .post("/api/v1/records")
            .then()
            .log().ifValidationFails()
            .statusCode(HttpStatus.CREATED.value())
            .body("payload", notNullValue())
            .body("payload.saved_id", notNullValue());

        String recordId = res.extract().path("payload.saved_id");

        RewardClaimRequest rewardClaimRequest = new RewardClaimRequest(recordId);

        given()
            .header("Authorization", header)
            .body(objectMapper.writeValueAsString(rewardClaimRequest))
            .contentType(ContentType.JSON)
            .when()
            .post("/api/v1/rewards/runnings")
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .body("payload.love_point_amount", notNullValue())
            .body("payload.love_point_amount", equalTo(0));
    }

}
