package org.runimo.runimo.user.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.runimo.runimo.TestConsts.TEST_USER_UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.runimo.runimo.CleanUpUtil;
import org.runimo.runimo.TokenUtils;
import org.runimo.runimo.user.controller.request.RegisterEggRequest;
import org.runimo.runimo.user.controller.request.UseLovePointRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class IncubatingEggAcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    private CleanUpUtil cleanUpUtil;
    @Autowired
    private ObjectMapper objectMapper;

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
    @Sql(scripts = "/sql/incubating_egg_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 사용자의_부화중인_알_조회_성공() {

        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)
            .when()
            .get("/api/v1/users/eggs/incubators")
            .then()
            .log().all()
            .statusCode(200)
            .body("code", equalTo("MY_INCUBATING_EGG_FETCHED"))
            .body("payload.incubating_eggs.size()", greaterThan(0))
            .body("payload.incubating_eggs[0].name", equalTo("마당알"))
            .body("payload.incubating_eggs[0].id", equalTo(1))
            .body("payload.incubating_eggs[0].hatch_required_point_amount", equalTo(100))
            .body("payload.incubating_eggs[0].current_love_point_amount", equalTo(50))
            .body("payload.incubating_eggs[0].hatchable", equalTo(false));
    }

    @Test
    @Sql(scripts = "/sql/incubating_egg_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 사용자의_알을_부화중으로_변경() throws JsonProcessingException {
        RegisterEggRequest request = new RegisterEggRequest(1L);

        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(request))
            .when()
            .post("/api/v1/users/eggs")
            .then()
            .log().all()
            .statusCode(HttpStatus.CREATED.value())
            .body("code", equalTo("REGISTER_EGG_SUCCESS"))
            .body("payload.current_love_point_amount", equalTo(0))
            .body("payload.required_love_point_amount", equalTo(100));
    }

    @Test
    @Sql(scripts = "/sql/incubating_egg_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 부화중인_알에_애정을_부여() throws JsonProcessingException {
        Long incubatingEggId = 1L;
        UseLovePointRequest request = new UseLovePointRequest(20L);
        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(request))
            .when()
            .patch("/api/v1/users/eggs/{incubatingEggId}", incubatingEggId)
            .then()
            .log().all()
            .statusCode(200)
            .body("code", equalTo("USE_LOVE_POINT_SUCCESS"))
            .body("payload.current_love_point_amount", equalTo(70))
            .body("payload.required_love_point_amount", equalTo(100))
            .body("payload.egg_hatchable", equalTo(false));
    }

    @Test
    @Sql(scripts = "/sql/incubating_egg_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 알에_애정을_부여하면_사용자의_보유_애정이_감소한다() throws JsonProcessingException {
        // given
        Long incubatingEggId = 1L;

        // 사용자의 초기 애정 포인트 조회
        Integer initialLovePoint = given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)
            .when()
            .get("/api/v1/main")
            .then()
            .statusCode(200)
            .extract()
            .path("payload.user_info.love_point");

        // when
        // 20포인트의 애정을 부여
        Long useLovePointAmount = 20L;
        UseLovePointRequest request = new UseLovePointRequest(useLovePointAmount);

        // 알에 애정 부여 요청
        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(request))
            .when()
            .patch("/api/v1/users/eggs/{incubatingEggId}", incubatingEggId)
            .then()
            .log().all()
            .statusCode(200)
            .body("payload.current_love_point_amount", equalTo(70));

        // then
        // 사용자의 애정 포인트가 감소했는지 확인
        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)
            .when()
            .get("/api/v1/main")
            .then()
            .log().all()
            .statusCode(200)
            .body("payload.user_info.love_point",
                equalTo(initialLovePoint - useLovePointAmount.intValue()));
    }


    @Test
    @Sql(scripts = "/sql/incubating_egg_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 보유한_애정보다_더_많은_애정부여_요청_시_예외() throws JsonProcessingException {
        // given
        Long incubatingEggId = 1L;

        // 사용자의 초기 애정 포인트 조회
        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)
            .when()
            .get("/api/v1/main")
            .then()
            .statusCode(200)
            .extract()
            .path("payload.love_point");

        // when
        // 20포인트의 애정을 부여
        Long useLovePointAmount = 25L;
        UseLovePointRequest request = new UseLovePointRequest(useLovePointAmount);

        // 알에 애정 부여 요청
        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(request))
            .when()
            .patch("/api/v1/users/eggs/{incubatingEggId}", incubatingEggId)
            .then()
            .log().all()
            .statusCode(400);
    }

    @Test
    @Sql(scripts = "/sql/incubating_egg_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 애정부여_예외_시_애정보유량_유지() throws JsonProcessingException {
        // given
        Long incubatingEggId = 1L;

        // 사용자의 초기 애정 포인트 조회
        Integer initialLovePoint = given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)
            .when()
            .get("/api/v1/main")
            .then()
            .statusCode(200)
            .extract()
            .path("payload.love_point");

        // when
        // 20포인트의 애정을 부여
        Long useLovePointAmount = 25L;
        UseLovePointRequest request = new UseLovePointRequest(useLovePointAmount);

        // 알에 애정 부여 요청
        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(request))
            .when()
            .patch("/api/v1/users/eggs/{incubatingEggId}", incubatingEggId)
            .then()
            .log().all()
            .statusCode(400);

        // then
        // 사용자의 애정 포인트가 감소했는지 확인
        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)
            .when()
            .get("/api/v1/main")
            .then()
            .log().all()
            .statusCode(200)
            .body("payload.love_point", equalTo(initialLovePoint));
    }

    @Test
    @Sql(scripts = "/sql/user_item_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 알_등록_후_조회() throws JsonProcessingException {
        Integer eggId =
            given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)

                .when()
                .get("/api/v1/users/eggs")

                .then()
                .log().ifError()
                .statusCode(200)
                .body("payload.items", notNullValue())
                .extract()
                .path("payload.items[0].item_id");

        RegisterEggRequest request = new RegisterEggRequest((long) eggId);

        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(request))

            .when()
            .post("/api/v1/users/eggs")

            .then()
            .statusCode(201)
            .log().ifError()
            .body("payload.incubating_egg_id", notNullValue())
            .body("payload.current_love_point_amount", equalTo(0));

        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)

            .when()
            .get("/api/v1/users/eggs/incubators")
            .then()
            .log().all()
            .statusCode(200)
            .body("payload.incubating_eggs", notNullValue())
            .body("payload.incubating_eggs[0].name", notNullValue());


    }
}
