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
@Feature("User Deletion")
@Owner("TestAutomationEngineer") // Полезно знать, кто ответственный за данный тест.
public class UserDeleteTest extends BaseTestCase {

    ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @DisplayName("Test that a user cannot delete himself via API")
    @Description("Verify that a user cannot delete their own account through the API, expecting a 400 response.")
    @Severity(SeverityLevel.NORMAL) // Наобходимо знать уроверь критичности теста
    public void testUserCannotDeleteSelf() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response loginResponse = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        String token = getHeader(loginResponse, "x-csrf-token");
        String cookie = getCookie(loginResponse, "auth_sid");

        int userId = loginResponse.jsonPath().getInt("user_id");


        Response deleteResponse = apiCoreRequests.makeDeleteRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                token,
                cookie
        );

        Assertions.assertResponseCodeEquals(deleteResponse, 400);
    }

    @Test
    @DisplayName("Test that a user can delete their own account successfully")
    @Description("Create a new user, authenticate, delete the account, and verify deletion. Expect status code 200 and 404 on subsequent get request.")
    @Severity(SeverityLevel.CRITICAL)
    public void testDeleteOwnUser() {
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

        String token = getHeader(authResponse, "x-csrf-token");
        String cookie = getCookie(authResponse, "auth_sid");

        Response deleteResponse = apiCoreRequests.makeDeleteRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                token,
                cookie
        );

        Assertions.assertResponseCodeEquals(deleteResponse, 200);

        Response getResponse = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .header("x-csrf-token", token)
                .cookie("auth_sid", cookie)
                .get("https://playground.learnqa.ru/api/user/" + userId);
        Assertions.assertResponseCodeEquals(getResponse, 404);
    }

    @Test
    @DisplayName("Test that a user cannot delete another user’s account")
    @Description("Create two users, login as the second, attempt to delete the first, expect failure with 400.")
    @Severity(SeverityLevel.CRITICAL)
    public void testDeleteUserWithOtherUser() {
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

        Response authResponse2 = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .body(authData2)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        String token2 = getHeader(authResponse2, "x-csrf-token");
        String cookie2 = getCookie(authResponse2, "auth_sid");

        Response response = apiCoreRequests.makeDeleteRequest(
                "https://playground.learnqa.ru/api/user/" + userId1,
                token2,
                cookie2
        );

        Assertions.assertResponseCodeEquals(response, 400);
    }
}

