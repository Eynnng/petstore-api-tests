package com.petstore.api;

import com.petstore.model.User;
import com.petstore.utils.Config;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserApi {

    public Response createUser(User user) {
        return given(Config.getRequestSpecification())
                .body(user)
                .when()
                .post("/user")
                .then()
                .log().all()
                .extract()
                .response();
    }

    public Response getUserByName(String username) {
        return given(Config.getRequestSpecification())
                .when()
                .get("/user/{username}", username)
                .then()
                .log().all()
                .extract()
                .response();
    }

    public Response updateUser(String username, User user) {
        return given(Config.getRequestSpecification())
                .body(user)
                .when()
                .put("/user/{username}", username)
                .then()
                .log().all()
                .extract()
                .response();
    }

    public Response deleteUser(String username) {
        return given(Config.getRequestSpecification())
                .when()
                .delete("/user/{username}", username)
                .then()
                .log().all()
                .extract()
                .response();
    }

    public Response loginUser(String username, String password) {
        return given(Config.getRequestSpecification())
                .queryParam("username", username)
                .queryParam("password", password)
                .when()
                .get("/user/login")
                .then()
                .log().all()
                .extract()
                .response();
    }

    public Response logoutUser() {
        return given(Config.getRequestSpecification())
                .when()
                .get("/user/logout")
                .then()
                .log().all()
                .extract()
                .response();
    }
}