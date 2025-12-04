package Lesson_2.Exercise;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;



public class Ex8TokensTest {
    @Test
    public void testRestAssured() throws InterruptedException {

        String url = "https://playground.learnqa.ru/ajax/api/longtime_job";

        JsonPath response = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .get(url)
                .jsonPath();

        String token = response.get("token");
        int seconds = response.get("seconds");
            System.out.println("1. Создана задача. " + "\n" + "Token: " + token +'\n' + "Seconds: " + seconds +'\n' );


        JsonPath checkResponse = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .queryParams("token", token)
                .get(url)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath();

        String status = checkResponse.get("status");
        System.out.println("2. Статус до завершения: " + status);
        assert status.equals("Job is NOT ready") : "Статус должен быть 'Job is NOT ready'";



        System.out.println("3. Время до завершения: " + seconds + " секунд");
        Thread.sleep((seconds + 2) * 1000L);


        JsonPath checkResponseAfter = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .queryParams("token", token)
                .get(url)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath();

        String statusAfter = checkResponseAfter.get("status");
        System.out.println("4. Статус после завершения: " + statusAfter +'\n' );
        assert statusAfter.equals("Job is ready") : "Статус должен быть 'Job is ready'";

        String result = checkResponseAfter.getString("result");
        System.out.println("Результат работы: " + result);
        assert result != null && !result.isEmpty() : "Поле result должно быть заполнено.";
    }
}
