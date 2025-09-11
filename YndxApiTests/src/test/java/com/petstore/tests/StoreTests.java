package com.petstore.tests;

import com.petstore.api.StoreApi;
import com.petstore.model.Order;
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

public class StoreTests {

    private StoreApi storeApi;
    private Pet testPet;
    private Order testOrder;
    private Long orderId;

    @BeforeEach
    public void setUp() {
        storeApi = new StoreApi();

        // Создаем питомца для заказа
        testPet = TestDataGenerator.generatePet();
        testOrder = TestDataGenerator.generateOrder(testPet.getId());
        orderId = testOrder.getId();
        new com.petstore.api.PetApi().createPet(testPet);
    }

    @AfterEach
    public void tearDown() {
        // Очищаем тестовые данные
        TestCleanup.deleteOrderIfExists(orderId);
        TestCleanup.deletePetIfExists(testPet.getId());
    }

    @Test
    public void testGetInventory_ShouldReturnInventory() {
        Response response = storeApi.getInventory();

        assertThat(response.statusCode(), equalTo(200));

        // Проверяем что inventory возвращается как map
        // Явно указываем типы для getMap()
        java.util.Map<String, Integer> inventory = response.jsonPath().getMap("");
        assertThat(inventory, is(notNullValue()));

        // Проверяем наличие ожидаемых статусов
        assertThat(inventory.keySet(), hasItems("available", "pending", "sold"));

        assertThat(inventory.get("available"), greaterThanOrEqualTo(0));
        assertThat(inventory.get("pending"), greaterThanOrEqualTo(0));
        assertThat(inventory.get("sold"), greaterThanOrEqualTo(0));
    }

    @Test
    public void testCreateOrder_ShouldReturnSuccess() {
        Response response = storeApi.createOrder(testOrder);

        assertThat(response.statusCode(), equalTo(200));

        Order createdOrder = response.as(Order.class);
        assertThat(createdOrder.getId(), equalTo(orderId));
        assertThat(createdOrder.getPetId(), equalTo(testOrder.getPetId()));
        assertThat(createdOrder.getQuantity(), equalTo(testOrder.getQuantity()));
        assertThat(createdOrder.getStatus(), equalTo(testOrder.getStatus()));
    }

    @Test
    public void testGetOrderById_ShouldReturnOrder() {
        storeApi.createOrder(testOrder);

        Response response = storeApi.getOrderById(orderId);

        assertThat(response.statusCode(), equalTo(200));

        Order retrievedOrder = response.as(Order.class);
        assertThat(retrievedOrder.getId(), equalTo(orderId));
        assertThat(retrievedOrder.getPetId(), equalTo(testOrder.getPetId()));
        assertThat(retrievedOrder.getStatus(), equalTo(testOrder.getStatus()));
    }

    @Test
    public void testDeleteOrder_ShouldDeleteSuccessfully() {
        storeApi.createOrder(testOrder);

        Response deleteResponse = storeApi.deleteOrder(orderId);

        assertThat(deleteResponse.statusCode(), equalTo(200));

        Response getResponse = storeApi.getOrderById(orderId);
        assertThat(getResponse.statusCode(), equalTo(404));
    }
}