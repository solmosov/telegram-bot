package io.github.shahbozolmosov.media;

import io.github.shahbozolmosov.request.media.SendDocumentRequest;
import io.github.shahbozolmosov.request.media.SendMediaRequest;

public final class Document {

    private Document() {
    }

    public static SendDocumentRequest.Builder url(String url){
        return SendDocumentRequest.builder().document(url);
    }
}
