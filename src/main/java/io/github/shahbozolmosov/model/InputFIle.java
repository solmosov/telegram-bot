package io.github.shahbozolmosov.model;

public final class InputFIle {

    private final byte[] data;
    private final String fileName;
    private final String mimeType;

    public InputFIle(byte[] data, String fileName) {
        this(data, fileName, "application/octet-stream");
    }

    public InputFIle(byte[] data, String fileName, String mimeType) {
        this.data = data;
        this.fileName = fileName;
        this.mimeType = mimeType;
    }

    public byte[] getData() {
        return data;
    }

    public String getFileName() {
        return fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

}
