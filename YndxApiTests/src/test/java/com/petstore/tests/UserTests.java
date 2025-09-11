package com.petstore.tests;

import com.petstore.api.UserApi;
import com.petstore.model.User;
import com.petstore.testdata.TestDataGenerator;
import com.petstore.utils.TestCleanup;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTests {

    private UserApi userApi;
    private User testUser;
    private String username;

    @BeforeEach
    public void setUp() {
        userApi = new UserApi();
        testUser = TestDataGenerator.generateUser();
        username = testUser.getUsername();
    }

    @AfterEach
    public void tearDown() {
        TestCleanup.deleteUserIfExists(username);
    }        // Очищаем тестовые данные

    @Test
    public void testCreateUser_ShouldReturnSuccess() {
        Response response = userApi.createUser(testUser);

        assertThat(response.statusCode(), equalTo(200));

        assertThat(response.jsonPath().getInt("code"), equalTo(200));
        assertThat(response.jsonPath().getString("type"), equalTo("unknown"));
        assertThat(response.jsonPath().getString("message"), containsString(testUser.getId().toString()));
    }

    @Test
    public void testGetUserByUsername_ShouldReturnUser() {
        userApi.createUser(testUser);

        Response response = userApi.getUserByName(username);

        assertThat(response.statusCode(), equalTo(200));

        User retrievedUser = response.as(User.class);
        assertThat(retrievedUser.getUsername(), equalTo(username));
        assertThat(retrievedUser.getFirstName(), equalTo(testUser.getFirstName()));
        assertThat(retrievedUser.getEmail(), equalTo(testUser.getEmail()));
    }

    @Test
    public void testGetUserByUsername_NonExistentUser_ShouldReturnNotFound() {
        Response response = userApi.getUserByName("nonexistentuser12345");

        assertThat(response.statusCode(), equalTo(404));
    }

    @Test
    public void testUpdateUser_ShouldUpdateSuccessfully() {
        userApi.createUser(testUser);

        testUser.setFirstName("UpdatedFirstName");
        testUser.setLastName("UpdatedLastName");
        testUser.setEmail("updated@example.com");

        Response response = userApi.updateUser(username, testUser);

        assertThat(response.statusCode(), equalTo(200));

        // Проверяем что данные обновились
        Response getResponse = userApi.getUserByName(username);
        User updatedUser = getResponse.as(User.class);

        assertThat(updatedUser.getFirstName(), equalTo("UpdatedFirstName"));
        assertThat(updatedUser.getLastName(), equalTo("UpdatedLastName"));
        assertThat(updatedUser.getEmail(), equalTo("updated@example.com"));
    }

    @Test
    public void testDeleteUser_ShouldDeleteSuccessfully() {
        userApi.createUser(testUser);

        Response deleteResponse = userApi.deleteUser(username);

        assertThat(deleteResponse.statusCode(), equalTo(200));

        // Проверяем что пользователь удален
        Response getResponse = userApi.getUserByName(username);
        assertThat(getResponse.statusCode(), equalTo(404));
    }

    @Test
    public void testUserLogin_ShouldReturnSuccess() {
        userApi.createUser(testUser);
        Response response = userApi.loginUser(testUser.getUsername(), testUser.getPassword());
        assertThat(response.statusCode(), equalTo(200));
        assertThat(response.jsonPath().getString("message"), containsString("logged in user session"));
    }

    @Test
    public void testUserLogout_ShouldReturnSuccess() {
        Response response = userApi.logoutUser();
        assertThat(response.statusCode(), equalTo(200));
        assertThat(response.jsonPath().getString("message"), containsString("ok"));
    }

    @Test
    public void testCreateUser_WithInvalidData_ShouldReturnError() {
        User invalidUser = TestDataGenerator.generateInvalidUser();
        Response response = userApi.createUser(invalidUser);
        assertThat(response.statusCode(), anyOf(equalTo(400), equalTo(500)));
    }
}