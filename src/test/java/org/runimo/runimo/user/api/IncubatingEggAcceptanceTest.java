package org.runimo.runimo.user.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.runimo.runimo.CleanUpUtil;
import org.runimo.runimo.auth.jwt.JwtTokenFactory;
import org.runimo.runimo.user.controller.UseLovePointRequest;
import org.runimo.runimo.user.controller.request.RegisterEggRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class IncubatingEggAcceptanceTest {

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
  @Sql(scripts = "/sql/incubating_egg_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  void 사용자의_부화중인_알_조회_성공() {
    String token = "Bearer " + jwtTokenFactory.generateAccessToken("test-user-uuid-1");

    given()
        .header("Authorization", token)
        .contentType(ContentType.JSON)
        .when()
        .get("/api/v1/users/eggs")
        .then()
        .log().all()
        .statusCode(200)
        .body("code", equalTo("USH2001"))
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
    String token = "Bearer " + jwtTokenFactory.generateAccessToken("test-user-uuid-1");
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
        .body("code", equalTo("USH2006"))
        .body("payload.current_love_point_amount", equalTo(0))
        .body("payload.required_love_point_amount", equalTo(100));
  }

  @Test
  @Sql(scripts = "/sql/incubating_egg_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  void 부화중인_알에_애정을_부여() throws JsonProcessingException {
    String token = "Bearer " + jwtTokenFactory.generateAccessToken("test-user-uuid-1");
    UseLovePointRequest request = new UseLovePointRequest(1L, 20L);
    given()
        .header("Authorization", token)
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(request))
        .when()
        .patch("/api/v1/users/eggs")
        .then()
        .log().all()
        .statusCode(200)
        .body("code", equalTo("USH2007"))
        .body("payload.current_love_point_amount", equalTo(70))
        .body("payload.required_love_point_amount", equalTo(100))
        .body("payload.egg_hatchable", equalTo(false));
  }
}
