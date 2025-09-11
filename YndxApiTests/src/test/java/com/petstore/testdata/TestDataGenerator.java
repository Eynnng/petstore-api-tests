package com.petstore.testdata;

import com.petstore.model.Category;
import com.petstore.model.Pet;
import com.petstore.model.Tag;
import com.petstore.model.Order;
import com.petstore.model.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TestDataGenerator {

    private static final String[] PET_NAMES = {"Buddy", "Max", "Charlie", "Bella", "Luna", "Lucy", "Bailey", "Stella"};
    private static final String[] CATEGORY_NAMES = {"Dogs", "Cats", "Birds", "Fish", "Reptiles"};
    private static final String[] TAG_NAMES = {"friendly", "active", "playful", "calm", "energetic", "loyal"};
    private static final String[] FIRST_NAMES = {"John", "Jane", "Bob", "Alice", "Charlie", "Eva"};
    private static final String[] LAST_NAMES = {"Smith", "Johnson", "Brown", "Davis", "Wilson", "Taylor"};

    public static User generateUser() {
        Long userId = ThreadLocalRandom.current().nextLong(1000, 10000);
        String username = "user" + userId;

        return new User(
                userId,
                username,
                FIRST_NAMES[ThreadLocalRandom.current().nextInt(FIRST_NAMES.length)],
                LAST_NAMES[ThreadLocalRandom.current().nextInt(LAST_NAMES.length)],
                username + "@example.com",
                "password123",
                "+1234567890",
                ThreadLocalRandom.current().nextInt(0, 2)
        );
    }

    public static User generateInvalidUser() {
        return new User(null, null, null, null, null, null, null, null);
    }
    public static Pet generatePet() {
        Long petId = ThreadLocalRandom.current().nextLong(1000, 10000);

        Category category = new Category(
                ThreadLocalRandom.current().nextLong(1, 6),
                CATEGORY_NAMES[ThreadLocalRandom.current().nextInt(CATEGORY_NAMES.length)]
        );

        List<String> photoUrls = Arrays.asList(
                "https://example.com/photo1.jpg",
                "https://example.com/photo2.jpg"
        );

        // Генерируем 1-3 случайных тега
        int tagCount = ThreadLocalRandom.current().nextInt(1, 4);
        Tag[] tags = new Tag[tagCount];
        for (int i = 0; i < tagCount; i++) {
            tags[i] = new Tag(
                    (long) (i + 1),
                    TAG_NAMES[ThreadLocalRandom.current().nextInt(TAG_NAMES.length)]
            );
        }

        String[] statuses = {"available", "pending", "sold"};
        String status = statuses[ThreadLocalRandom.current().nextInt(statuses.length)];

        return new Pet(
                petId,
                category,
                PET_NAMES[ThreadLocalRandom.current().nextInt(PET_NAMES.length)],
                photoUrls,
                Arrays.asList(tags),
                status
        );
    }

    public static Order generateOrder(Long petId) {
        Long orderId = ThreadLocalRandom.current().nextLong(1000, 10000);

        String[] statuses = {"placed", "approved", "delivered"};
        String status = statuses[ThreadLocalRandom.current().nextInt(statuses.length)];

        // Строка с датой в формате, который ожидает API
        String shipDate = "2024-01-15T10:30:00.000Z";

        return new Order(
                orderId,
                petId,
                ThreadLocalRandom.current().nextInt(1, 10),
                shipDate,
                status,
                false
        );
    }

    public static Pet generateInvalidPet() {
        // Питомец с невалидными данными (без обязательных полей)
        return new Pet(null, null, null, null, null, null);
    }

    public static Long generateNonExistentId() {
        return ThreadLocalRandom.current().nextLong(100000, 1000000);
    }

    // Генератор для разных статусов питомцев
    public static Pet generatePetWithStatus(String status) {
        Pet pet = generatePet();
        pet.setStatus(status);
        return pet;
    }
}