package com.petstore.utils;

import com.petstore.api.PetApi;
import com.petstore.api.UserApi;
import com.petstore.api.StoreApi;
import io.restassured.response.Response;

public class TestCleanup {

    private static final PetApi petApi = new PetApi();
    private static final StoreApi storeApi = new StoreApi();

    public static void deletePetIfExists(Long petId) {
        try {
            Response response = petApi.getPetById(petId);
            if (response.statusCode() == 200) {
                petApi.deletePet(petId);
            }
        } catch (Exception e) {
            // Игнорируем ошибки при очистке
            System.out.println("Cleanup warning: " + e.getMessage());
        }
    }

    public static void deleteOrderIfExists(Long orderId) {
        try {
            Response response = storeApi.getOrderById(orderId);
            if (response.statusCode() == 200) {
                storeApi.deleteOrder(orderId);
            }
        } catch (Exception e) {
            // Игнорируем ошибки при очистке
            System.out.println("Cleanup warning: " + e.getMessage());
        }
    }

    private static final UserApi userApi = new UserApi();

    public static void deleteUserIfExists(String username) {
        try {
            Response response = userApi.getUserByName(username);
            if (response.statusCode() == 200) {
                userApi.deleteUser(username);
            }
        } catch (Exception e) {
            System.out.println("Cleanup warning: " + e.getMessage());
        }
    }
}