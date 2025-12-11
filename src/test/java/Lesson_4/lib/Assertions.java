package Lesson_4.lib;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Assertions {
    public static void asserJsonByName(Response response, String name, int expectedValue) {
        response.then().assertThat().body("$", hasKey(name));

        int value = response.jsonPath().getInt(name);
        assertEquals(expectedValue, value, "JSON value is not equal to expected value");
    }

    public static void asserJsonByName(Response response, String name, String expectedValue) {
        response.then().assertThat().body("$", hasKey(name));

        String value = response.jsonPath().getString(name);
        assertEquals(expectedValue, value, "JSON value is not equal to expected value");
    }

    public static void assertResponseTextEquals(Response response, String expectedAnswer) {
        assertEquals(
                expectedAnswer,
                response.asString(),
                "response text is not as expected"
        );
    }
    public static void assertResponseCodeEquals(Response response, int expectedStatusCode) {
        assertEquals(
                expectedStatusCode,
                response.statusCode(),
                "response status code is not as expected"
        );
    }

    public static void assertJsonHasField(Response response, String unexpectedFieldName) {
        response.then().assertThat().body("$", hasKey(unexpectedFieldName));
    }

    public static void assertJsonHasFields(Response response, String[] expectedFieldNames) {
        for (String expectedFieldName : expectedFieldNames) {
            Assertions.assertJsonHasField(response, expectedFieldName);
        }
    }

    public static void assertJsonHasNotField(Response response, String unexpectedFieldName) {
        response.then().assertThat().body("$", not(hasKey(unexpectedFieldName)));
    }

    public static void assertJsonHasKey(Response response, String expectedFieldName) {
        response.then().assertThat().body("$", hasKey(expectedFieldName));
    }

    public static void assertResponseTextContains(Response response, String... expectedFragments) {
        String responseText = response.asString();
        for (String fragment : expectedFragments) {
            assertTrue(responseText.contains(fragment),
                    "The response does not contain the expected fragment: '" + fragment + "'. Response text:\n" + responseText);
        }
    }
}