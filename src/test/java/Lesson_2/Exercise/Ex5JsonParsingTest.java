package Lesson_2.Exercise;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

public class Ex5JsonParsingTest {

    @Test

    public void testRestAssured(){

        JsonPath response = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();

        String message = response.get("messages[1].message");
        System.out.println("Второе сообщение: " + message);
        }
    }
