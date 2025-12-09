package Lesson_3.Exercise.Framework.tests;

import Lesson_3.Exercise.Framework.lib.Assertions;
import Lesson_3.Exercise.Framework.lib.BaseTestCase;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class UserAgentTest extends BaseTestCase {

    @ParameterizedTest
    @ValueSource(strings = {
            "Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F)",
            "Mozilla/5.0 (Linux; Android 8.0.0; SM-G960F)",
            "Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X)",
            "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)"
    })
    public void testUserAgent(String userAgent) {
        UserAgentData expected = userAgentExpectedMap.get(userAgent);
        if (expected == null) {
            throw new RuntimeException("Не найдено ожидаемых данных для User-Agent: " + userAgent);
        }

        Response response = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .header("User-Agent", userAgent)
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .andReturn();

        Assertions.assertJsonEquals(response, "device", expected.device);
        Assertions.assertJsonEquals(response, "browser", expected.browser);
        Assertions.assertJsonEquals(response, "platform", expected.platform);

    }
}