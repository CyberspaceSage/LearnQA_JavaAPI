package Lesson_4.tests;

import Lesson_4.lib.ApiCoreRequests;
import Lesson_4.lib.DataGenerator;
import Lesson_4.lib.Assertions;
import Lesson_4.lib.BaseTestCase;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.HashMap;
import java.util.Map;


public class UserRegisterTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    @Test
    @DisplayName("Test creating a user with an existing email")
    @Description("This test verifies that the system does not allow creating a user with an email that already exists.")
    public void testCreateUserWithExistingEmail() {
        String email = "vinkotov@example.com";
        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);
        Response responseCreateAuth = apiCoreRequests.createUser(userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");
    }

    @Test
    @DisplayName("Test successful user creation")
    @Description("This test checks that a user can be successfully created with valid data.")
    public void testCreateUserSuccessfully() {
        Map<String, String> userData = DataGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequests.createUser(userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertJsonHasKey(responseCreateAuth, "id");
    }

    @Test
    @DisplayName("Test creating a user with invalid email format")
    @Description("This test verifies that creation fails when email format is invalid.")
    public void testCreateUserWithInvalidEmail() {
        Map<String, String> userData = DataGenerator.getRegistrationData();

        String email = "invalidemail.com";
        userData.put("email", email);
        Response responseCreateAuth = apiCoreRequests.createUser(userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Invalid email format");
    }

    @ParameterizedTest
    @DisplayName("Test creating a user with missing parameters")
    @Description("This test verifies that creating a user fails when required parameters are missing.")
    @ValueSource(strings = {"lastName"})
    public void testCreateUserWithMissingParameter(String missingParam) {
        Map<String, String> userData = DataGenerator.getRegistrationData();
        userData.remove(missingParam);
        Response responseCreateAuth = apiCoreRequests.createUser(userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The following required params are missed: lastName");
    }

    @Test
    @DisplayName("Test creating a user with a one-character first name")
    @Description("This test verifies that creating a user with a too-short first name fails.")
    public void testCreateUserWithOneCharName() {
        Map<String, String> userData = DataGenerator.getRegistrationData();
        String shortName = "A";
        userData.put("firstName", shortName);
        Response responseCreateAuth = apiCoreRequests.createUser(userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'firstName' field is too short");
    }

    @Test
    @DisplayName("Test creating a user with an excessively long name")
    @Description("This test verifies that creating a user with a too-long first name fails.")
    public void testCreateUserWithLongName() {
        Map<String, String> userData = DataGenerator.getRegistrationData();
        String longName = "a".repeat(251);
        userData.put("firstName", longName);
        Response responseCreateAuth = apiCoreRequests.createUser(userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextContains(responseCreateAuth, "field is too long", "firstName");
    }
}