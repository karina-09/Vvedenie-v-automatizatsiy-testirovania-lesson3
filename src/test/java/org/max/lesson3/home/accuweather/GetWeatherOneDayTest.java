package org.max.lesson3.home.accuweather;

import io.restassured.http.Cookie;
import io.restassured.http.Method;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.max.lesson3.seminar.accuweather.AccuweatherAbstractTest;
import org.max.lesson3.seminar.accuweather.weather.Weather;
import org.max.lesson3.seminar.spoonacular.ConvertAmountsDto;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.max.lesson3.home.accuweather.AccuweatherAbstractTest.getApiKey;
import static org.max.lesson3.home.accuweather.AccuweatherAbstractTest.getBaseUrl;

public class GetWeatherOneDayTest extends AccuweatherAbstractTest {

    @Test
    void getWeatherOneDay_shouldReturn() {

        Weather response = given()
                .queryParam("apikey", getApiKey())
                .when()
                .get(getBaseUrl()+"/forecasts/v1/daily/1day/294021")
                .then()
                .statusCode(200)
                .time(Matchers.lessThan(2000l))
                .extract()
                .response()
            //тут надо поправить классы Night.java и Day.java
                .body().as(Weather.class);

        Assertions.assertEquals(1,response.getDailyForecasts().size());
    }
}
