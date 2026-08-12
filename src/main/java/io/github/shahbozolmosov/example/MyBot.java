package io.github.shahbozolmosov.example;

import io.github.shahbozolmosov.annotation.BotHandler;
import io.github.shahbozolmosov.annotation.Command;
import io.github.shahbozolmosov.annotation.Message;
import io.github.shahbozolmosov.context.BotContext;
import io.github.shahbozolmosov.keyboard.inline.InlineKeyboard;
import io.github.shahbozolmosov.keyboard.reply.ReplyKeyboard;
import io.github.shahbozolmosov.media.Document;
import io.github.shahbozolmosov.model.InputFIle;
import io.github.shahbozolmosov.request.media.SendDocumentUploadRequest;

import java.io.IOException;
import java.io.InputStream;

import static io.github.shahbozolmosov.keyboard.reply.ReplyKeyboard.button;

@BotHandler
public class MyBot {

    private static final int SELECT_USERS = 1;
    private static final int SELECT_PREMIUM_USERS = 2;
//    private static final byte[] MOCK_PDF =;

    @Command("/start")
    public void start(BotContext context) {
        String html = """
                <b>Welcome, %s! 👋</b>
                
                I'm glad to see you here.
                Choose an option below to get started.
                """
                .formatted(context.message().from().firstName());

        var keyboard = ReplyKeyboard.of(
                button("📦 My Orders"),
                button("📦 My Orders Uploadable")
        );

        context.message().sendHtml(html, keyboard);
    }

    @Message("📦 My Orders")
    public void myOrders(BotContext context) {

        var document = Document
                .url("https://leman.com/wp-content/uploads/2024/03/placeholder-pdf.pdf")
                .caption("Hello caption")
                .protectContent(true);

        var keyboard = InlineKeyboard.of(
                InlineKeyboard.button("Button", "button1")
        );

        context.message().sendDocument(document, keyboard);
    }

    @Message("📦 My Orders Uploadable")
    public void myOrdersUploadablePdf(BotContext context) {

        var html = """
                <b> Hello </b>
                
                - item 1
                - item 2
                - item 3
                """;

        var document = Document
                .file(getMockPdfFile(), "orders.pdf")
                //.file(getMockPdfFile(), "orders.pdf", "application/pdf")
                .html(html);

        context.message().sendDocument(document);
    }

    private byte[] getMockPdfFile() {
        byte[] pdfBytes;
        try (InputStream is = getClass().getResourceAsStream("/files/orders.pdf")) {
            if (is == null) {
                throw new IllegalStateException("PDF is not found: /files/orders.pdf");
            }
            pdfBytes = is.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return pdfBytes;
    }
}
