package io.agistep.aggregator;

public class IllegalAggregateIdException extends RuntimeException {
    public IllegalAggregateIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalAggregateIdException(String message) {
        this(message, null);
    }
}
