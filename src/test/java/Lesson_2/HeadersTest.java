package Lesson_2;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;


public class HeadersTest {

    @Test
    public void testRestAssured(){

//        Map<String, String> headers = new HashMap<>();
//        headers.put("myHeader1", "myValue1");
//        headers.put("myHeader2", "myValue2");

        Response response = RestAssured
                .given()
                .relaxedHTTPSValidation()  // добавляем для обхода SSL-проверки
                .redirects()
                .follow(false)
                .when()
//                .get("https://playground.learnqa.ru/api/show_all_headers")
                .get("https://playground.learnqa.ru/api/get_303")
                .andReturn();

        response.prettyPrint();

//        Headers responseHeaders = response.getHeaders();
        String locationHeaders = response.getHeader("Location");
//        System.out.println(responseHeaders);
        System.out.println(locationHeaders);
    }
}
