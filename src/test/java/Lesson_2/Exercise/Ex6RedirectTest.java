package Lesson_2.Exercise;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class Ex6RedirectTest {
    @Test
    public void testRestAssured(){

        Response response = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .redirects()
                .follow(false)
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();
        response.prettyPrint();

        String locationHeaders = response.getHeader("Location");
        System.out.println("Редирект на адрес: " + locationHeaders);
    }
}
