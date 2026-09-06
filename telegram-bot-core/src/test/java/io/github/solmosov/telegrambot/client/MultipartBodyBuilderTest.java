package io.github.solmosov.telegrambot.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.solmosov.telegrambot.model.InputFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import tools.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Flow;

import static org.junit.jupiter.api.Assertions.*;

class MultipartBodyBuilderTest {

    private MultipartBodyBuilder builder;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        builder = new MultipartBodyBuilder(objectMapper);
    }

    @Test
    @DisplayName("Should build multipart body with text and scalar fields correctly")
    void shouldBuildMultipartWithTextFields() throws Exception {
        TestTextRequest request = new TestTextRequest("123456", "Hello World", 99, Status.ACTIVE, "secret_token");

        MultipartBody multipartBody = builder.build(request);

        assertNotNull(multipartBody, "MultipartBody must not be null");
        assertTrue(multipartBody.contentType().startsWith("multipart/form-data; boundary="));

        String bodyString = readBodyPublisher(multipartBody.bodyPublisher());

        assertTrue(bodyString.contains("name=\"chat_id\"\r\n\r\n123456"));
        assertTrue(bodyString.contains("name=\"text\"\r\n\r\nHello World"));
        assertTrue(bodyString.contains("name=\"count\"\r\n\r\n99"));
        assertTrue(bodyString.contains("name=\"status\"\r\n\r\nACTIVE"));
        assertTrue(!bodyString.contains("ignored_field"));
        assertTrue(!bodyString.contains("secret_token"));
    }

    @Test
    @DisplayName("Should build multipart body containing file stream and text payload")
    void shouldBuildMultipartWithFileAndTextPayload(@TempDir Path tempDir) throws Exception {
        Path tempFile = tempDir.resolve("sample.png");
        String fileContent = "fake-image-bytes-content";
        Files.writeString(tempFile, fileContent);

        InputFile inputFile = new InputFile(tempFile, "sample.png");
        TestFileRequest request = new TestFileRequest("777888", inputFile);

        MultipartBody multipartBody = builder.build(request);
        String bodyString = readBodyPublisher(multipartBody.bodyPublisher());

        assertTrue(bodyString.contains("name=\"chat_id\"\r\n\r\n777888"));
        assertTrue(bodyString.contains("name=\"photo\"; filename=\"sample.png\""));
        assertTrue(bodyString.contains("Content-Type: image/png"));
        assertTrue(bodyString.contains(fileContent));
    }

    @Test
    @DisplayName("Should serialize complex objects into JSON text fields")
    void shouldSerializeComplexObjectsToJson() throws Exception {
        Payload payload = new Payload("user_admin", List.of("READ", "WRITE"));
        TestComplexRequest request = new TestComplexRequest("1001", payload);

        MultipartBody multipartBody = builder.build(request);
        String bodyString = readBodyPublisher(multipartBody.bodyPublisher());

        assertTrue(bodyString.contains("name=\"chat_id\"\r\n\r\n1001"));
        assertTrue(bodyString.contains("name=\"payload\""));
        assertTrue(bodyString.contains("{\"username\":\"user_admin\",\"permissions\":[\"READ\",\"WRITE\"]}"));
    }

    @Test
    @DisplayName("Should throw RuntimeException when opening stream for a non-existent file")
    void shouldThrowExceptionWhenFileDoesNotExist() {
        Path nonexistentPath = Path.of("nonexistent_test_file.txt");
        InputFile inputFile = new InputFile(nonexistentPath, "missing.txt");
        TestFileRequest request = new TestFileRequest("123", inputFile);

        assertThrows(RuntimeException.class, () -> builder.build(request));
    }

    private String readBodyPublisher(HttpRequest.BodyPublisher publisher) throws Exception {
        SyncSubscriber subscriber = new SyncSubscriber();
        publisher.subscribe(subscriber);
        return subscriber.getResponseBody();
    }

    private enum Status {
        ACTIVE
    }

    private static class BaseRequest {
        @JsonProperty("chat_id")
        private final String chatId;

        public BaseRequest(String chatId) {
            this.chatId = chatId;
        }
    }

    private static class TestTextRequest extends BaseRequest {
        private final String text;
        private final int count;
        private final Status status;

        @JsonIgnore
        private final String ignoredField;

        public TestTextRequest(String chatId, String text, int count, Status status, String ignoredField) {
            super(chatId);
            this.text = text;
            this.count = count;
            this.status = status;
            this.ignoredField = ignoredField;
        }
    }

    private static class TestFileRequest extends BaseRequest {
        private final InputFile photo;

        public TestFileRequest(String chatId, InputFile photo) {
            super(chatId);
            this.photo = photo;
        }
    }

    private record Payload(String username, List<String> permissions) {}

    private static class TestComplexRequest extends BaseRequest {
        private final Payload payload;

        public TestComplexRequest(String chatId, Payload payload) {
            super(chatId);
            this.payload = payload;
        }
    }

    private static class SyncSubscriber implements Flow.Subscriber<java.nio.ByteBuffer> {
        private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            subscription.request(Long.MAX_VALUE);
        }

        @Override
        public void onNext(java.nio.ByteBuffer item) {
            byte[] bytes = new byte[item.remaining()];
            item.get(bytes);
            outputStream.write(bytes, 0, bytes.length);
        }

        @Override
        public void onError(Throwable throwable) {
            throw new RuntimeException("Failed to read BodyPublisher content in subscriber", throwable);
        }

        @Override
        public void onComplete() {
        }

        public String getResponseBody() {
            return outputStream.toString(StandardCharsets.UTF_8);
        }
    }
}