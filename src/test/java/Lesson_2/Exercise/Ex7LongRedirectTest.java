package Lesson_2.Exercise;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class Ex7LongRedirectTest {
    @Test
    public void testRestAssured() {
        int statusCode;
        int count = 0;

        String url = "https://playground.learnqa.ru/api/long_redirect";
        while (true) {
            Response response = RestAssured
                    .given()
                    .relaxedHTTPSValidation()
                    .redirects()
                    .follow(false)
                    .get(url)
                    .andReturn();

            statusCode = response.getStatusCode();
            String locationHeaders = response.getHeader("Location");

            System.out.println("Статус: " + statusCode);

            if (statusCode == 200) {
                System.out.println("Колличество редиректов: " + count);
                break;
            }
            if (locationHeaders != null && !locationHeaders.isEmpty()){
            System.out.println("Редирект на адрес: " + locationHeaders);
                url = locationHeaders;
                count++;
        } else {
                System.out.println("Нет заголовка Location, цикл завершен");
                break;
            }
        }
    }
}
