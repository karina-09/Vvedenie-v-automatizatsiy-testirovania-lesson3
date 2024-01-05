package org.max.lesson3.home.accuweather;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.max.lesson3.home.accuweather.location.Location;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;


public class GetLocationTest extends AccuweatherAbstractTest {

    @Test
    void getLocation_search_returnMinsk() {

        List<Location> response = given()
                .queryParam("apikey", getApiKey())
                .queryParam("q", "Minsk")
                .when()
                .get(getBaseUrl() + "/locations/v1/cities/search")
                .then()
                .statusCode(200)
                .time(Matchers.lessThan(2000L))
                .extract()
                .body().jsonPath().getList(".", Location.class);

        //тут возвращается результат из 2х вариантов, а не 24
        Assertions.assertEquals(24, response.size());
        Assertions.assertEquals("Minsk", response.get(0).getEnglishName());


    }
    @Test
    void getVerifyingResponseData(){

        JsonPath response = given()
                .when()
                .request(Method.GET,getBaseUrl()+"/locations/{version}/regions?" +
                        "apikey={apiKey}", "v1", getApiKey())
                .body()
                .jsonPath();

        assertThat(response.get("[0].ID"), equalTo("AFR"));
        assertThat(response.get("[1].ID"), equalTo("ANT"));
        assertThat(response.get("[2].ID"), equalTo("ARC"));


        given().response().expect()
                .body("[0].LocalizedName", equalTo("Africa"))
                .body("[0].EnglishName", equalTo("Africa"))
                .when()
                .request(Method.GET,getBaseUrl()+"/locations/{version}/regions?" +
                        "apikey={apiKey}", "v1", getApiKey())
                .then()
                .assertThat()
                //.cookie("cookieName", "cookieValue")
                .statusCode(200)
                .statusLine("HTTP/1.1 200 OK")
                .header("Connection", "keep-alive")
                .contentType(ContentType.JSON)
                .time(lessThan(2000L));

    }
    @Test
    void getSpecifyingRequestDataTest() {
        given()
                .when()
                .request(Method.GET,getBaseUrl()+"/locations/{version}/regions?" +
                        "apikey={apiKey}", "v1", getApiKey())
                .then()
                .statusCode(200);

        given()
                .queryParam("apikey", getApiKey())
                .pathParam("version", "v1")
                .when()
                .request(Method.GET,getBaseUrl()+"/locations/{version}/regions")
                .then()
                .statusCode(200);

        given().cookie("username","max")
                .cookie( new Cookie
                        .Builder("some_cookie", "some_value")
                        .setSecured(true)
                        .setComment("some comment")
                        .build())
                .when()
                .get(getBaseUrl()+"/locations/v1/regions?" +
                        "apikey=" +getApiKey())
                .then()
                .statusCode(200);

        given().headers("username","max")
                .when()
                .get(getBaseUrl()+"/locations/v1/regions?" +
                        "apikey=" +getApiKey())
                .then()
                .statusCode(200);
    }
}
