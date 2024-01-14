package io.agistep.event.exception;

public class GamjaComponentCreationException extends RuntimeException {

    public GamjaComponentCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public GamjaComponentCreationException(String message) {
        this(message, null);
    }

}
