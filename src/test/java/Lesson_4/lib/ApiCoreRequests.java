package Lesson_4.lib;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import java.util.Map;
import static io.restassured.RestAssured.given;

public class ApiCoreRequests {
    @Step("Make a Get-request with token and auth cookie")
    public Response makeGetRequest(String url, String token, String cookie) {
        return given()
                .relaxedHTTPSValidation()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make a Get-request with auth cookie only")
    public Response makeGetRequestWithCookie(String url, String cookie) {
        return given()
                .relaxedHTTPSValidation()
                .filter(new AllureRestAssured())
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make a Get-request with token only")
    public Response makeGetRequestWithToken(String url, String token) {
        return given()
                .relaxedHTTPSValidation()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .get(url)
                .andReturn();

    }

    @Step("Make a POST-request")
    public Response makePostRequest(String url, Map<String, String> authData) {
        return given()
                .relaxedHTTPSValidation()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .andReturn();
    }

    @Step("Creating a user with data: {userData}")
    public Response createUser(Map<String, String> userData) {
        String url = "https://playground.learnqa.ru/api/user/";
        return makePostRequest(url, userData);
    }
    @Step("Login user with data: {authData}")
    public Response loginUser(Map<String, String> authData) {
        String url = "https://playground.learnqa.ru/api/user/login";
        return makePostRequest(url, authData);
    }

    @Step("Get user data by id: {userId} with cookies and token")
    public Response getUserDataById(int userId, Map<String, String> cookies, String token) {
        String url = "https://playground.learnqa.ru/api/user/" + userId;
        return given()
                .relaxedHTTPSValidation()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookies(cookies)
                .get(url)
                .andReturn();
    }
}