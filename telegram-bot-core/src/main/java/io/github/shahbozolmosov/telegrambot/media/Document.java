package io.github.shahbozolmosov.telegrambot.media;

import io.github.shahbozolmosov.telegrambot.model.InputFIle;
import io.github.shahbozolmosov.telegrambot.request.message.caption.SendDocumentRequest;
import io.github.shahbozolmosov.telegrambot.request.message.caption.SendDocumentUploadRequest;

public final class Document {

    private Document() {
    }

    public static SendDocumentRequest.Builder url(String url) {
        return SendDocumentRequest.builder().document(url);
    }

    public static SendDocumentUploadRequest.Builder file(byte[] file, String name){
        var inputFile = new InputFIle(file, name);
        return SendDocumentUploadRequest.builder().document(inputFile);
    }

    public static SendDocumentUploadRequest.Builder file(byte[] file, String name, String mimeType){
        var inputFile = new InputFIle(file, name, mimeType);
        return SendDocumentUploadRequest.builder().document(inputFile);
    }
}
