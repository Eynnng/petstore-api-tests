package com.petstore.utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class Config {
    private static final String BASE_URL = "https://petstore.swagger.io/v2";

    public static RequestSpecification getRequestSpecification() {
        return RestAssured.given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .log().all();
    }


    public static String getBaseUrl() {
        return BASE_URL;
    }
}