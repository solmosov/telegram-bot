package io.github.solmosov.telegrambot.messaging.builder;

import io.github.solmosov.telegrambot.client.TelegramClient;
import io.github.solmosov.telegrambot.keyboard.ReplyMarkup;
import io.github.solmosov.telegrambot.keyboard.inline.InlineKeyboardMarkup;
import io.github.solmosov.telegrambot.model.Message;
import io.github.solmosov.telegrambot.model.TelegramResponse;
import io.github.solmosov.telegrambot.request.message.message_action.EditMessageReplyMarkupRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EditMessageReplyMarkupBuilder extends AbstractMessageBuilder<Message> {

    private static final Logger log = LoggerFactory.getLogger(EditMessageReplyMarkupBuilder.class);
    private Long chatId;
    private final long messageId;
    private final ReplyMarkup replyMarkup;

    public EditMessageReplyMarkupBuilder(
            TelegramClient client,
            long messageId
    ) {
        this(client, null, null, messageId);
    }

    public EditMessageReplyMarkupBuilder(
            TelegramClient client,
            Long updateId,
            Long defaultChatId,
            long messageId
    ) {
        super(client, updateId);

        this.chatId = defaultChatId;
        this.messageId = messageId;

        this.replyMarkup = InlineKeyboardMarkup.remove();
    }

    public EditMessageReplyMarkupBuilder(
            TelegramClient client,
            long messageId,
            ReplyMarkup replyMarkup
    ) {
        this(client, null, null, messageId, replyMarkup);
    }

    public EditMessageReplyMarkupBuilder(
            TelegramClient client,
            Long updateId,
            Long defaultChatId,
            long messageId,
            ReplyMarkup replyMarkup
    ) {
        super(client, updateId);

        this.chatId = defaultChatId;
        this.messageId = messageId;
        this.replyMarkup = replyMarkup;
    }

    public EditMessageReplyMarkupBuilder toChat(long chatId) {
        this.chatId = chatId;
        return this;
    }

    @Override
    public TelegramResponse<Message> send() {
        EditMessageReplyMarkupRequest request = new EditMessageReplyMarkupRequest(
                chatId,
                messageId,
                replyMarkup
        );

        log.debug("Sending edit message reply markup to updateId: {} chatId: {} messageId: {}", getUpdateId(), chatId, messageId);

        return client.editMessageReplyMarkup(request);
    }
}
