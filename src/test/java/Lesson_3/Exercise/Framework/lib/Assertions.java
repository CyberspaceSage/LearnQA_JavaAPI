package Lesson_3.Exercise.Framework.lib;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Assertions {
    public static void assertJsonEquals(Response response, String jsonPath, String expectedValue) {
        response.then().assertThat()
                .body("$", hasKey(jsonPath));
        String actualValue = response.jsonPath().getString(jsonPath);
        assertEquals(expectedValue, actualValue, String.format("Значение параметра '%s' не совпадает. Ожидается: '%s', Получено: '%s'\n", jsonPath, expectedValue, actualValue));
    }
}
