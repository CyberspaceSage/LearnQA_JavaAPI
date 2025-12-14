package Lesson_4.tests;

import Lesson_4.lib.ApiCoreRequests;
import Lesson_4.lib.Assertions;
import Lesson_4.lib.BaseTestCase;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

@Epic("User Management")
@Feature("User Data Retrieval")
@Owner("TestAutomationEngineer")
public class UserGetTest extends BaseTestCase {
    private Map<String, String> getCookiesMap(Response response) {
        return response.getCookies();
    }
    ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @DisplayName("Get user data without authorization")
    @Description("Verifies that requesting user data without authorization returns only public fields.")
    @Severity(SeverityLevel.NORMAL)
    public void testGetUserDataNotAuth(){
        Response responseUserData = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();

        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNotField(responseUserData, "firstName");
        Assertions.assertJsonHasNotField(responseUserData, "lastName");
        Assertions.assertJsonHasNotField(responseUserData, "email");
    }
    @Test
    @DisplayName("Get own user data when authenticated")
    @Description("Authenticate as a user and verify that all personal fields are accessible.")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetUserDetailsAuthAsSameUser(){
        Map<String,String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseUserData = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .header("x-csrf-token", header)
                .cookie("auth_sid", cookie)
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();

        String[] expectedFields = {"username", "firstName", "lastName", "email"};
        Assertions.assertJsonHasFields(responseUserData, expectedFields);

        Assertions.assertJsonHasField(responseUserData, "username");
//        Assertions.assertJsonHasField(responseUserData, "firstName");
//        Assertions.assertJsonHasField(responseUserData, "lastName");
//        Assertions.assertJsonHasField(responseUserData, "email");
    }
    @Test
    @DisplayName("Access other user's data with authentication")
    @Description("Authenticate as one user and attempt to get another user's data; only public fields should be available.")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetOtherUserDataAsAuth() {

        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseLogin = apiCoreRequests.loginUser(authData);
        String token = this.getHeader(responseLogin, "x-csrf-token");
        String cookie = this.getCookie(responseLogin, "auth_sid");
        Map<String, String> cookiesMap = this.getCookiesMap(responseLogin);

        Response responseUserData = apiCoreRequests.getUserDataById(1, cookiesMap, token);
        responseUserData.prettyPrint();

        Assertions.assertJsonHasKey(responseUserData, "username");
        Assertions.assertJsonHasNotField(responseUserData, "firstName");
        Assertions.assertJsonHasNotField(responseUserData, "lastName");
        Assertions.assertJsonHasNotField(responseUserData, "email");

    }
}