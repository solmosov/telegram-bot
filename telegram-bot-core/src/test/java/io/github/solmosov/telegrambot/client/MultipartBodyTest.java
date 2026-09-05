package io.github.solmosov.telegrambot.client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.http.HttpRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

class MultipartBodyTest {

    @Test
    @DisplayName("Should return correct content type with boundary")
    void shouldReturnCorrectContentType() {
        String boundary = "----WebKitFormBoundary7MA4YWxkTrZu0gW";
        HttpRequest.BodyPublisher mockPublisher = mock(HttpRequest.BodyPublisher.class);

        MultipartBody multipartBody = new MultipartBody(boundary, mockPublisher);

        String expectedContentType = "multipart/form-data; boundary=" + boundary;
        assertEquals(expectedContentType, multipartBody.contentType(), "Content type string is invalid");
    }

    @Test
    @DisplayName("Should return the exact BodyPublisher provided in constructor")
    void shouldReturnBodyPublisher() {
        String boundary = "test-boundary";
        HttpRequest.BodyPublisher expectedPublisher = HttpRequest.BodyPublishers.ofString("test-data");

        MultipartBody multipartBody = new MultipartBody(boundary, expectedPublisher);

        assertNotNull(multipartBody.bodyPublisher(), "BodyPublisher should not be null");
        assertSame(expectedPublisher, multipartBody.bodyPublisher(), "Returned BodyPublisher reference does not match");
    }
}