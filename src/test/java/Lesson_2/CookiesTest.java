package Lesson_2;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;


import java.util.HashMap;
import java.util.Map;

public class CookiesTest {
    @Test
    public void testRestAssured(){
        Map<String, String> data = new HashMap<>();
        data.put("login", "secret_login");
        data.put("password", "secret_pass");

        Response responseForGet = RestAssured
                .given()
                .relaxedHTTPSValidation()  // добавляем для обхода SSL-проверки
                .body(data)
                .when()
                .post("https://playground.learnqa.ru/api/get_auth_cookie")
                .andReturn();

        String responseCookie = responseForGet.getCookie("auth_cookie");

        Map<String, String> cookies = new HashMap<>();
        if (responseCookie !=null){
            cookies.put("auth_cookie", responseCookie);
        }

        Response responseForCheck = RestAssured
                .given()
                .relaxedHTTPSValidation()  // добавляем для обхода SSL-проверки
                .body(data)
                .cookies(cookies)
                .when()
                .post("https://playground.learnqa.ru/api/check_auth_cookie")
                .andReturn();

        responseForCheck.print();



//        System.out.println("\nPretty text:");
//        response.prettyPrint();
//
//        System.out.println("\nHeaders:");
//        Headers responseHeaders = response.getHeaders();
//        System.out.println(responseHeaders);

//        System.out.println("\nCookies:");
//        Map<String, String> responseCookies = response.getCookies();
//        String responseCookies = response.getCookie("auth_cookie");
//        System.out.println(responseCookies);
    }
}
