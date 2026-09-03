package io.github.solmosov.telegrambot.keyboard.reply.button;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestUsersTest {

    @Test
    void builder_shouldSetRequestId() {
        RequestUsers requestUsers = RequestUsers.builder(123)
                .build();

        assertEquals(123, requestUsers.getRequestId());
    }

    @Test
    void builder_shouldApplyAllOptions() {
        RequestUsers requestUsers = RequestUsers.builder(123)
                .userIsBot()
                .userIsPremium()
                .requestName()
                .requestUsername()
                .requestPhoto()
                .maxQuantity(5)
                .build();

        assertEquals(123, requestUsers.getRequestId());
        assertTrue(requestUsers.getUserIsBot());
        assertTrue(requestUsers.getUserIsPremium());
        assertTrue(requestUsers.getRequestName());
        assertTrue(requestUsers.getRequestUsername());
        assertTrue(requestUsers.getRequestPhoto());
        assertEquals(5, requestUsers.getMaxQuantity());
    }

    @Test
    void builder_withoutOptions_shouldKeepOptionalFieldsNull() {
        RequestUsers requestUsers = RequestUsers.builder(123)
                .build();

        assertEquals(123, requestUsers.getRequestId());
        assertNull(requestUsers.getUserIsBot());
        assertNull(requestUsers.getUserIsPremium());
        assertNull(requestUsers.getRequestName());
        assertNull(requestUsers.getRequestUsername());
        assertNull(requestUsers.getRequestPhoto());
        assertNull(requestUsers.getMaxQuantity());
    }

    @Test
    void getRequestId_shouldSetValue() {
        RequestUsers requestUsers = RequestUsers.builder(123)
                .build();

        assertEquals(123, requestUsers.getRequestId());
    }

    @Test
    void getUserIsBot_shouldSetValue() {
        RequestUsers requestUsers = RequestUsers.builder(123)
                .userIsBot()
                .build();

        assertTrue(requestUsers.getUserIsBot());
    }

    @Test
    void getUserIsBot_shouldAcceptNull() {
        RequestUsers requestUsers = RequestUsers.builder(123)
                .build();

        assertNull(requestUsers.getUserIsBot());
    }

    @Test
    void getUserIsPremium_shouldSetValue() {
        RequestUsers requestUsers = RequestUsers.builder(123)
                .userIsPremium()
                .build();

        assertTrue(requestUsers.getUserIsPremium());
    }

    @Test
    void getUserIsPremium_shouldAcceptNull() {
        RequestUsers requestUsers = RequestUsers.builder(123)
                .build();

        assertNull(requestUsers.getUserIsPremium());
    }

    @Test
    void getMaxQuantity_shouldSetValue() {
        RequestUsers requestUsers = RequestUsers.builder(123)
                .maxQuantity(10)
                .build();

        assertEquals(10, requestUsers.getMaxQuantity());
    }

    @Test
    void getMaxQuantity_shouldAcceptNull() {
        RequestUsers requestUsers = RequestUsers.builder(123)
                .maxQuantity(null)
                .build();

        assertNull(requestUsers.getMaxQuantity());
    }

    @Test
    void getRequestName_shouldSetValue() {
        RequestUsers requestUsers = RequestUsers.builder(123)
                .requestName()
                .build();

        assertTrue(requestUsers.getRequestName());
    }

    @Test
    void getRequestName_shouldAcceptNull() {
        RequestUsers requestUsers = RequestUsers.builder(123)
                .build();

        assertNull(requestUsers.getRequestName());
    }

    @Test
    void getRequestUsername_shouldSetValue() {
        RequestUsers requestUsers = RequestUsers.builder(123)
                .requestUsername()
                .build();

        assertTrue(requestUsers.getRequestUsername());
    }

    @Test
    void getRequestUsername_shouldAcceptNull() {
        RequestUsers requestUsers = RequestUsers.builder(123)
                .build();

        assertNull(requestUsers.getRequestUsername());
    }

    @Test
    void getRequestPhoto_shouldSetValue() {
        RequestUsers requestUsers = RequestUsers.builder(123)
                .requestPhoto()
                .build();

        assertTrue(requestUsers.getRequestPhoto());
    }

    @Test
    void getRequestPhoto_shouldAcceptNull() {
        RequestUsers requestUsers = RequestUsers.builder(123)
                .build();

        assertNull(requestUsers.getRequestPhoto());
    }
}