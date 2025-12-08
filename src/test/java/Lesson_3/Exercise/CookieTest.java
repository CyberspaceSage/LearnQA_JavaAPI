package Lesson_3.Exercise;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CookieTest {

    @Test
    public void testCookie() {

        Response response = RestAssured
                .given()
                .relaxedHTTPSValidation() // добавляем для обхода SSL-проверки
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();

        Map<String, String> cookies = response.getCookies();
        System.out.println("Cookies со значением: " + cookies);

        String answerCookieName = "HomeWork";
        assertTrue(cookies.containsKey(answerCookieName), "Ответ содержит cookie с именем " + answerCookieName);
        String expectedCookieValue = "hw_value";
        assertEquals(expectedCookieValue, cookies.get(answerCookieName), "Значение cookie " + answerCookieName + " соответствует ожидаемому");
    }
}
