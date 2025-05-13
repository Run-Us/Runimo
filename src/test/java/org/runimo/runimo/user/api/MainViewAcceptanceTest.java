package org.runimo.runimo.user.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.runimo.runimo.TestConsts.TEST_USER_UUID;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.runimo.runimo.CleanUpUtil;
import org.runimo.runimo.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class MainViewAcceptanceTest {

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
    @Sql(scripts = "/sql/main_view_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 메인화면_조회_성공시_정확한_정보를_반환한다() {
        // when & then
        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)
            .when()
            .get("/api/v1/main")
            .then()
            .log().ifValidationFails()
            .statusCode(200)
            .body("code", equalTo("MAIN_PAGE_DATA_FETCHED"))
            .body("payload.main_runimo_stat_nullable.name", equalTo("토끼"))
            .body("payload.main_runimo_stat_nullable.image_url", equalTo("http://dummy1"))
            .body("payload.main_runimo_stat_nullable.total_running_count", equalTo(2))
            .body("payload.main_runimo_stat_nullable.total_distance_in_meters", equalTo(3456))
            .body("payload.user_info.love_point", equalTo(100))
            .body("payload.user_info.total_egg_count", equalTo(3));
    }

    @Test
    @Sql(scripts = "/sql/main_view_data.sql")
    void 메인화면_조회시_대표_러니모가_없으면_null_로반환() {
        // when & then
        String token = tokenUtils.createTokenByUserPublicId("test-user-uuid-2");
        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)
            .when()
            .get("/api/v1/main")
            .then()
            .log().ifValidationFails()
            .statusCode(200)
            .body("code", equalTo("MAIN_PAGE_DATA_FETCHED"))
            .body("payload.main_runimo_stat_nullable", equalTo(null))
            .body("payload.user_info.love_point", equalTo(23))
            .body("payload.user_info.total_egg_count", equalTo(7));
    }
}
