package com.petstore.api;

import com.petstore.model.Order;
import com.petstore.utils.Config;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class StoreApi {

    public Response getInventory() {
        return given(Config.getRequestSpecification())
                .when()
                .get("/store/inventory")
                .then()
                .log().all()
                .extract()
                .response();
    }

    public Response createOrder(Order order) {
        return given(Config.getRequestSpecification())
                .body(order)
                .when()
                .post("/store/order")
                .then()
                .log().all()
                .extract()
                .response();
    }

    public Response getOrderById(Long orderId) {
        return given(Config.getRequestSpecification())
                .when()
                .get("/store/order/{orderId}", orderId)
                .then()
                .log().all()
                .extract()
                .response();
    }

    public Response deleteOrder(Long orderId) {
        return given(Config.getRequestSpecification())
                .when()
                .delete("/store/order/{orderId}", orderId)
                .then()
                .log().all()
                .extract()
                .response();
    }
}