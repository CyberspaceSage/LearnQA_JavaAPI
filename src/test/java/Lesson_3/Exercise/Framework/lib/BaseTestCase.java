package Lesson_3.Exercise.Framework.lib;
import org.junit.jupiter.api.BeforeEach;
import java.util.HashMap;
import java.util.Map;

public class BaseTestCase {
    protected Map<String, UserAgentData> userAgentExpectedMap;

    @BeforeEach
    public void baseSetUp() {
        userAgentExpectedMap = new HashMap<>();
        userAgentExpectedMap.put(
                "Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F)",
                new UserAgentData("Android", "No", "Mobile")
        );
        userAgentExpectedMap.put(
                "Mozilla/5.0 (Linux; Android 8.0.0; SM-G960F)",
                new UserAgentData("Android", "Chrome", "Mobile")
        );
        userAgentExpectedMap.put(
                "Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X)",
                new UserAgentData("iOS", "Chrome", "Mobile")
        );
        userAgentExpectedMap.put(
                "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)",
                new UserAgentData("Unknown", "Unknown", "Unknown")
        );
    }
    public static class UserAgentData {
        public String device;
        public String browser;
        public String platform;

        public UserAgentData(String device, String browser, String platform) {
            this.device = device;
            this.browser = browser;
            this.platform = platform;
        }
    }
}