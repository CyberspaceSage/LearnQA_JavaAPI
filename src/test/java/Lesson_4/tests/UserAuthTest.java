package Lesson_4.tests;

import Lesson_4.lib.Assertions;
import Lesson_4.lib.BaseTestCase;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.HashMap;
import java.util.Map;

import Lesson_4.lib.ApiCoreRequests;
import org.junit.jupiter.api.DisplayName;

@Epic("Authorisation cases")
@Feature("Authorisation")
@Owner("TestAutomationEngineer") // Полезно знать, кто ответственный за данный тест.
public class UserAuthTest extends BaseTestCase {

    String cookie;
    String header;
    int userIdOnAuth;
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @BeforeEach
    @Description("Setup authentication data for each test") // Добавил сюда этот тег, чтобы поимать для чего данные подготавливаются
    @Step("Login user before each test") // Для быстрого понимания, какие данные в даном шаге вводим
    public void loginUser(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);


        this.cookie = this.getCookie(responseGetAuth,"auth_sid");
        this.header = this.getHeader(responseGetAuth,"x-csrf-token");
        this.userIdOnAuth = this.getIntFromJson(responseGetAuth, "user_id");
    }

    @Test
    @Description ("This test successfully authorize user by email and password")
    @DisplayName("Test positive auth user")
    @Severity(SeverityLevel.CRITICAL)  // Наобходимо знать уроверь критичности теста
    public void testAuthUser() {

        Response responseCheckAuth = apiCoreRequests
                .makeGetRequest(
                        "https://playground.learnqa.ru/api/user/auth",
                        this.header,
                        this.cookie
                );

        Assertions.asserJsonByName(responseCheckAuth, "user_id", this.userIdOnAuth);
    }
    @Description("This test checks authorisation status w/o sending auth cookie or token")
    @DisplayName("Test negative auth user")
    @Severity(SeverityLevel.NORMAL) // Наобходимо знать уроверь критичности теста
    @ParameterizedTest
    @ValueSource(strings = {"cookie", "headers"})
    public void testNegativeAuthUser(String condition){

        RequestSpecification spec = RestAssured
                .given()
                .relaxedHTTPSValidation(); // добавляем для обхода SSL-проверки
        spec.baseUri("https://playground.learnqa.ru/api/user/auth");

        if (condition.equals("cookie")) {
            Response responseForCheck = apiCoreRequests.makeGetRequestWithCookie(
                    "https://playground.learnqa.ru/api/user/auth",
                    this.cookie
            );
            Assertions.asserJsonByName(responseForCheck, "user_id",0);
        }else if (condition.equals("headers")){
            Response responseForCheck = apiCoreRequests.makeGetRequestWithToken(
                    "https://playground.learnqa.ru/api/user/auth",
                    this.header
            );
            Assertions.asserJsonByName(responseForCheck,"user_id",0);
        }else {
            throw new IllegalArgumentException("Condition value is not known: " + condition);
        }
    }
}