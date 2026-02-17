package ru.tigrbank.domain.exception;

public class LackFundsException extends DomainException {
    public LackFundsException(String message) {
        super(message);
    }
}
