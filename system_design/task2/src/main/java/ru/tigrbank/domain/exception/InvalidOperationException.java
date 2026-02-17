package ru.tigrbank.domain.exception;

public class InvalidOperationException extends DomainException {
    public InvalidOperationException(String message) {
        super(message);
    }
}
