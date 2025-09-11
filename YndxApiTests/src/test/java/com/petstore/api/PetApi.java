package com.petstore.api;

import com.petstore.model.Pet;
import com.petstore.utils.Config;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class PetApi {

    public Response createPet(Pet pet) {
        return given(Config.getRequestSpecification())
                .body(pet)
                .when()
                .post("/pet")
                .then()
                .log().all()
                .extract()
                .response();
    }

    public Response getPetById(Long petId) {
        return given(Config.getRequestSpecification())
                .when()
                .get("/pet/{petId}", petId)
                .then()
                .log().all()
                .extract()
                .response();
    }

    public Response updatePet(Pet pet) {
        return given(Config.getRequestSpecification())
                .body(pet)
                .when()
                .put("/pet");
    }

    public Response deletePet(Long petId) {
        return given(Config.getRequestSpecification())
                .when()
                .delete("/pet/{petId}", petId)
                .then()
                .log().all()
                .extract()
                .response();
    }

    public Response findPetsByStatus(String status) {
        return given(Config.getRequestSpecification())
                .queryParam("status", status)
                .when()
                .get("/pet/findByStatus")
                .then()
                .log().all()
                .extract()
                .response();
    }
}