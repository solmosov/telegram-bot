package io.github.shahbozolmosov.exception.authorization;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException() {
        super("Access denied");
    }

    public AccessDeniedException(String message){
        super(message);
    }

    public AccessDeniedException(String message, Throwable cause){
        super(message, cause);
    }
}
