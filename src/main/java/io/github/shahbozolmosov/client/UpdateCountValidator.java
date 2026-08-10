package io.github.shahbozolmosov.client;

import io.github.shahbozolmosov.exception.TelegramClientException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.ObjectMapper;

public final class UpdateCountValidator {
    private final int maxUpdates;
    private final ObjectMapper objectMapper;

    public UpdateCountValidator(
            ObjectMapper objectMapper,
            int maxUpdates
    ) {
        this.objectMapper = objectMapper;
        this.maxUpdates = maxUpdates;
    }

    public void validate(String json) {
        JsonParser parser = objectMapper.createParser(json);

        while (parser.nextToken() != null) {

            if (parser.currentToken() == JsonToken.PROPERTY_NAME
                    && parser.currentName().equals("result")) {

                JsonToken token = parser.nextToken();

                if (token != JsonToken.START_ARRAY) {
                    return;
                }

                int count = 0;

                while (parser.nextToken() != JsonToken.END_ARRAY) {
                    count++;

                    if (count > maxUpdates) {
                        throw new TelegramClientException(
                                "Telegram API returned too many updates"
                        );
                    }

                    parser.skipChildren();
                }

                return;
            }
        }
    }
}
