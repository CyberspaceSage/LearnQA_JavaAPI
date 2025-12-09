package Lesson_3.Exercise;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;



public class UserAgentTest {
    private Map<String, String[]> userAgentExpectedMap;

    @BeforeEach
    public void setup() {
        this.userAgentExpectedMap = new HashMap<>();
        this.userAgentExpectedMap.put(
                "Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F)",
                new String[]{"Android", "No", "Mobile"}
        );
        this.userAgentExpectedMap.put(
                "Mozilla/5.0 (Linux; Android 8.0.0; SM-G960F)",
                new String[]{"Android", "Chrome", "Mobile"}
        );
        this.userAgentExpectedMap.put(
                "Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X)",
                new String[]{"iOS", "Chrome", "Mobile"}
        );
        this.userAgentExpectedMap.put(
                "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)",
                new String[]{"Unknown", "Unknown", "Unknown"}
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F)",
            "Mozilla/5.0 (Linux; Android 8.0.0; SM-G960F)",
            "Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X)",
            "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)"
    })
    public void testUserAgent(String userAgent) {
        String[] expected = this.userAgentExpectedMap.get(userAgent);
        if (expected == null) {
            throw new RuntimeException("Не найдено ожидаемых данных для User-Agent: " + userAgent);
        }
        String expectedDevice = expected[0];
        String expectedBrowser = expected[1];
        String expectedPlatform = expected[2];

        Response response = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .header("User-Agent", userAgent)
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .andReturn();


        JsonPath json = response.jsonPath();

        String actualDevice = json.getString("device");
        String actualBrowser = json.getString("browser");
        String actualPlatform = json.getString("platform");

        assertEquals(expectedDevice, actualDevice, String.format("device — ожидается: %s, получено: %s\n", expectedDevice, actualDevice));
        assertEquals(expectedBrowser, actualBrowser, String.format("browser — ожидается: %s, получено: %s\n", expectedBrowser, actualBrowser));
        assertEquals(expectedPlatform, actualPlatform, String.format("platform — ожидается: %s, получено: %s\n", expectedPlatform, actualPlatform));
    }
}
