package io.github.solmosov.telegrambot.client;

import io.github.solmosov.telegrambot.exception.client.TelegramClientException;
import org.junit.jupiter.api.Test;

import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LimitedBodyHandlerTest {

    @Test
    void shouldCreateSubscriberWhenContentLengthIsWithinLimit() {
        LimitedBodyHandler handler = new LimitedBodyHandler(1024);

        HttpResponse.ResponseInfo responseInfo = responseInfo(512);

        HttpResponse.BodySubscriber<byte[]> subscriber =
                handler.apply(responseInfo);

        assertInstanceOf(LimitedBodySubscriber.class, subscriber);
    }

    @Test
    void shouldNotThrowWhenContentLengthEqualsLimit() {
        LimitedBodyHandler handler = new LimitedBodyHandler(512);

        assertDoesNotThrow(
                () -> handler.apply(responseInfo(512))
        );
    }

    @Test
    void shouldThrowWhenContentLengthExceedsLimit() {
        LimitedBodyHandler handler = new LimitedBodyHandler(512);

        TelegramClientException exception = assertThrows(
                TelegramClientException.class,
                () -> handler.apply(responseInfo(513))
        );

        assertEquals(
                "Telegram API response is too large",
                exception.getMessage()
        );
    }

    @Test
    void shouldCreateSubscriberWhenContentLengthIsUnknown() {
        LimitedBodyHandler handler = new LimitedBodyHandler(512);

        HttpResponse.ResponseInfo responseInfo =
                mock(HttpResponse.ResponseInfo.class);

        when(responseInfo.headers()).thenReturn(
                HttpHeaders.of(
                        Map.of(),
                        (name, value) -> true
                )
        );

        assertDoesNotThrow(
                () -> handler.apply(responseInfo)
        );
    }

    private static HttpResponse.ResponseInfo responseInfo(
            long contentLength
    ) {
        HttpResponse.ResponseInfo responseInfo =
                mock(HttpResponse.ResponseInfo.class);

        when(responseInfo.headers()).thenReturn(
                HttpHeaders.of(
                        Map.of(
                                "Content-Length",
                                List.of(String.valueOf(contentLength))
                        ),
                        (name, value) -> true
                )
        );

        return responseInfo;
    }
}