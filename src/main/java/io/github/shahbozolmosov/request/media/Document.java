package io.github.shahbozolmosov.request.media;

import io.github.shahbozolmosov.model.InputFIle;

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

    public static SendDocumentUploadRequest.Builder file(byte[] file, String name, String type){
        var inputFile = new InputFIle(file, name, type);
        return SendDocumentUploadRequest.builder().document(inputFile);
    }
}
