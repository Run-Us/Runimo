package org.runimo.runimo.runimo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class RunimoControllerTest {

    @LocalServerPort
    int port;

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

    @AfterEach
    void tearDown() {
        cleanUpUtil.cleanUpUserInfos();
    }


    @Test
    @Sql(scripts = "/sql/get_my_runimo_list_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 보유_러니모_목록_조회_성공() {
        String token = "Bearer " + jwtTokenFactory.generateAccessToken("test-user-uuid-1");


        given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)

                .when()
                .get("/api/v1/runimos/my")

                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())

                .body("code", equalTo("MSH2001"))
                .body("payload.my_runimos[0].id", equalTo(1))
                .body("payload.my_runimos[0].name", equalTo("토끼"))
                .body("payload.my_runimos[0].img_url", equalTo("http://dummy1"))
                .body("payload.my_runimos[0].code", equalTo("R-101"))
                .body("payload.my_runimos[0].egg_type", equalTo("MADANG"))
                .body("payload.my_runimos[0].description", equalTo("마당 토끼예여"));
    }

    @Test
    @Sql(scripts = "/sql/set_main_runimo_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 대표_러니모_설정_성공() {
        String token = "Bearer " + jwtTokenFactory.generateAccessToken("test-user-uuid-1");


        given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)

                .when()
                .patch("/api/v1/runimos/"+"1"+"/main")

                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())

                .body("code", equalTo("MSH2002"))
                .body("payload.main_runimo_id", equalTo(1));
    }
}