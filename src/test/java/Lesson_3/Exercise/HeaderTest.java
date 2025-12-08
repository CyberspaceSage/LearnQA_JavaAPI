package Lesson_3.Exercise;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;


import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HeaderTest {
    @Test
    public void testHeader() {

        Response response = RestAssured
                .given()
                .relaxedHTTPSValidation() // добавляем для обхода SSL-проверки
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();

        Headers headers = response.getHeaders();
        Map<String, String> headersMap = new HashMap<>();
        for (Header header : headers) {
            headersMap.put(header.getName(), header.getValue());
            System.out.println(header.getName() + ": " + header.getValue());
        }
        String headerToCheck = "x-secret-homework-header";
        String expectedValue = "Some secret value";

        assertTrue(headersMap.containsKey(headerToCheck), "Header " + headerToCheck + " должен присутствовать в ответе");
        String actualValue = headersMap.get(headerToCheck);
        assertEquals(expectedValue, actualValue, "Значение header " + headerToCheck + " совпадает с ожидаемым");

    }
}


