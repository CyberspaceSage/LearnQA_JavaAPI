package Lesson_2;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class ServerCodesTest {
    @Test
    public void testRestAssured(){

        Response response = RestAssured
                .given()
                .relaxedHTTPSValidation()  // добавляем для обхода SSL-проверки
                .redirects()
                .follow(true)
                .when()
                .get("https://playground.learnqa.ru/api/get_303")
                .andReturn();

        int statusCode = response.getStatusCode();
        System.out.println(statusCode);
    }
}
