package Lesson_3.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JunitTest {
    @Test
    public void testFor200() {
        Response response = RestAssured
                .given()
                .relaxedHTTPSValidation() // добавляем для обхода SSL-проверки
                .get("https://playground.learnqa.ru/api/map")
                .andReturn();
        assertEquals(200, response.statusCode(), "Unexpected status code");

        //  assertTrue ((response.statusCode()) == 200, "Unexpected status code");
    }

    @Test
    public void testFor404() {
        Response response = RestAssured
                .given()
                .relaxedHTTPSValidation() // добавляем для обхода SSL-проверки
                .get("https://playground.learnqa.ru/api/map2")
                .andReturn();
        assertEquals(404, response.statusCode(), "Unexpected status code");
    }
}