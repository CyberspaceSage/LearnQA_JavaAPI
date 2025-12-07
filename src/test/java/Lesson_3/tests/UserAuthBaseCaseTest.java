package Lesson_3.tests;

import Lesson_3.lib.BaseTestCase;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import Lesson_3.lib.Assertions;
import java.util.HashMap;
import java.util.Map;


public class UserAuthBaseCaseTest extends BaseTestCase {

    String cookie;
    String header;
    int userIdOnAuth;

    @BeforeEach
    public void loginUser(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = RestAssured
                .given()
                .relaxedHTTPSValidation() // добавляем для обхода SSL-проверки
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
        System.out.println(authData);

        this.cookie = this.getCookie(responseGetAuth,"auth_sid");
        this.header = this.getHeader(responseGetAuth,"x-csrf-token");
        this.userIdOnAuth = this.getIntFromJson(responseGetAuth, "user_id");
    }

    @Test
    public void testAuthUser() {

        Response responseCheckAuth = RestAssured
                .given()
                .relaxedHTTPSValidation() // добавляем для обхода SSL-проверки
                .header("x-csrf-token", this.header)
                .cookie("auth_sid", this.cookie)
                .get("https://playground.learnqa.ru/api/user/auth")
                .andReturn();
        responseCheckAuth.prettyPrint();

        Assertions.asserJsonByName(responseCheckAuth, "user_id", this.userIdOnAuth);
    }
    @ParameterizedTest
    @ValueSource(strings = {"cookie", "headers"})
    public void testNegativeAuthUser(String condition){

        RequestSpecification spec = RestAssured
                .given()
                .relaxedHTTPSValidation(); // добавляем для обхода SSL-проверки
        spec.baseUri("https://playground.learnqa.ru/api/user/auth");

        if (condition.equals("cookie")) {
            spec.cookie("auth_sid", this.cookie);
        }else if(condition.equals("headers")){
            spec.header("x-csrf-token", this.header);
        }else {
            throw new IllegalArgumentException("Condition value is known: " + condition );
        }
        Response responseForCheck = spec.get().andReturn();
        Assertions.asserJsonByName(responseForCheck, "user_id", 0);
    }
}