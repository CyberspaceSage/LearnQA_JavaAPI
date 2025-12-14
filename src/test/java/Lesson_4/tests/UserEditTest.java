package Lesson_4.tests;

import Lesson_4.lib.ApiCoreRequests;
import Lesson_4.lib.DataGenerator;
import Lesson_4.lib.Assertions;
import Lesson_4.lib.BaseTestCase;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("User Management")
@Feature("User Editing")
@Owner("TestAutomationEngineer")
public class UserEditTest extends BaseTestCase {

    ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    @Test
    @DisplayName("Edit User Info: Change first name successfully")
    @Description("Generate and authenticate a user, then attempt to change the first name to a valid value and verify the change.")
    @Severity(SeverityLevel.CRITICAL)
    public void testEditJustCreatedTest(){
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();

        String userId = responseCreateAuth.getString("id");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        //EDIT
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
                .body(editData)
                .put("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        //GET

        Response responseUserData = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .header("x-csrf-token", this.getHeader(responseGetAuth,"x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth,"auth_sid"))
                .get("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        System.out.println(responseUserData.asString());

        Assertions.asserJsonByName(responseUserData, "firstName", newName);

    }

    @Test
    @DisplayName("Attempt to edit user without authentication")
    @Description("Generate a user and try to edit user data without authentication; verify that the operation is forbidden or fails.")
    @Severity(SeverityLevel.CRITICAL)
    public void testEditUserWithoutAuth() {
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();

        String userId = responseCreateAuth.getString("id");


        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", "John");

        Response response = apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                editData,
                null,
                null
        );

        Assertions.assertResponseCodeEquals(response, 400);
    }

    @Test
    @DisplayName("Attempt to edit another user's data")
    @Description("Create two users, login as the second, and try to edit the first user's data; only the correct user should be able to edit their own data.")
    @Severity(SeverityLevel.CRITICAL)
    public void testEditUserWithOtherUser() {
        // Создаем первого пользователя
        Map<String, String> userData1 = DataGenerator.getRegistrationData();

        JsonPath createResponse1 = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .body(userData1)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();

        String userId1 = createResponse1.getString("id");


        Map<String, String> userData2 = DataGenerator.getRegistrationData();
        JsonPath createResponse2 = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .body(userData2)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();

        String userId2 = createResponse2.getString("id");

        Map<String, String> authData2 = new HashMap<>();
        authData2.put("email", userData2.get("email"));
        authData2.put("password", userData2.get("password"));

        Response responseGetAuth2 = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .body(authData2)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        String token2 = this.getHeader(responseGetAuth2, "x-csrf-token");
        String cookie2 = this.getCookie(responseGetAuth2, "auth_sid");

        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", "John");

        Response response = apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api/user/" + userId1,
                editData,
                token2,
                cookie2
        );

        Assertions.assertResponseCodeEquals(response, 400);
    }

    @Test
    @DisplayName("Attempt to change email to invalid format")
    @Description("Generate and login as a user, then try to change email to an invalid format without '@'. Verify that validation fails.")
    @Severity(SeverityLevel.NORMAL)
    public void testChangeEmailWithoutAtSymbol() {

        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath createResponse = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();
        String userId = createResponse.getString("id");

        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response authResponse = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        String token = this.getHeader(authResponse, "x-csrf-token");
        String cookie = this.getCookie(authResponse, "auth_sid");

        Map<String, String> editData = new HashMap<>();
        editData.put("email", "invalidemail.com");

        Response response = apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                editData,
                token,
                cookie
        );

        Assertions.assertResponseCodeEquals(response, 400);
        Assertions.assertResponseTextContains(response, "Invalid email");
    }

    @Test
    @DisplayName("Edit first name to a single character")
    @Description("Create and login as a user, then attempt to set first name to a single character indicating minimum length validation.")
    @Severity(SeverityLevel.NORMAL)
    public void testEditFirstNameToOneSymbol() {
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath createResponse = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();

        String userId = createResponse.getString("id");

        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response authResponse = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        String token = this.getHeader(authResponse, "x-csrf-token");
        String cookie = this.getCookie(authResponse, "auth_sid");

        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", "A");

        Response response = apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                editData,
                token,
                cookie
        );

        Assertions.assertResponseCodeEquals(response, 400);
        Assertions.assertResponseTextContains(response, "The value for field `firstName` is too short");
    }
}

