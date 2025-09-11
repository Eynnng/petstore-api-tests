package com.petstore.tests;

import com.petstore.api.PetApi;
import com.petstore.model.Pet;
import com.petstore.testdata.TestDataGenerator;
import com.petstore.utils.TestCleanup;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PetTests {

    private PetApi petApi;
    private Pet testPet;
    private Long petId;

    @BeforeEach
    public void setUp() {
        petApi = new PetApi();
        testPet = TestDataGenerator.generatePet();
        petId = testPet.getId();
    }

    @AfterEach
    public void tearDown() {
        // Очищаем тестовые данные после каждого теста
        TestCleanup.deletePetIfExists(petId);
    }

    @Test
    public void testCreatePet_ShouldReturnSuccess() {
        Response response = petApi.createPet(testPet);

        assertThat(response.statusCode(), equalTo(200));

        Pet createdPet = response.as(Pet.class);
        assertThat(createdPet.getId(), equalTo(petId));
        assertThat(createdPet.getName(), equalTo(testPet.getName()));
        assertThat(createdPet.getStatus(), equalTo(testPet.getStatus()));
        assertThat(createdPet.getCategory().getName(), equalTo(testPet.getCategory().getName()));
    }

    @Test
    public void testGetPetById_ShouldReturnPet() {
        petApi.createPet(testPet);

        Response response = petApi.getPetById(petId);

        assertThat(response.statusCode(), equalTo(200));

        Pet retrievedPet = response.as(Pet.class);
        assertThat(retrievedPet.getId(), equalTo(petId));
        assertThat(retrievedPet.getName(), equalTo(testPet.getName()));
        assertThat(retrievedPet.getCategory().getId(), equalTo(testPet.getCategory().getId()));
        assertThat(retrievedPet.getTags(), hasSize(testPet.getTags().size()));
    }

    @Test
    public void testGetPetById_NonExistentPet_ShouldReturnNotFound() {
        Long nonExistentId = TestDataGenerator.generateNonExistentId();

        Response response = petApi.getPetById(nonExistentId);

        assertThat(response.statusCode(), equalTo(404));

        assertThat(response.jsonPath().getInt("code"), equalTo(1));
        assertThat(response.jsonPath().getString("type"), equalTo("error"));
        assertThat(response.jsonPath().getString("message"), containsString("Pet not found"));
    }

    @Test
    public void testDeletePet_ShouldDeleteSuccessfully() {
        petApi.createPet(testPet);

        Response deleteResponse = petApi.deletePet(petId);

        assertThat(deleteResponse.statusCode(), equalTo(200));

        // Проверяем что питомец удален
        Response getResponse = petApi.getPetById(petId);
        assertThat(getResponse.statusCode(), equalTo(404));
    }

}