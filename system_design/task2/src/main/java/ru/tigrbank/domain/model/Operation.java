package ru.tigrbank.domain.model;

import lombok.Getter;
import ru.tigrbank.domain.exception.InvalidOperationException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;

@Getter
public class Operation {

    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    private final UUID id;
    private final OperationType type;
    private final UUID bankAccountId;
    private final BigDecimal amount;
    private final UUID categoryId;
    private LocalDate date;
    private String description;

    public Operation(OperationType type, UUID bankAccountId, BigDecimal amount, UUID categoryId, LocalDate date,
                     String description) {
        validate(type, bankAccountId, amount, categoryId, date);
        this.id = UUID.randomUUID();
        this.type = type;
        this.bankAccountId = bankAccountId;
        this.amount = amount.setScale(SCALE, ROUNDING);
        this.categoryId = categoryId;
        this.date = date;
        this.description = description;
    }

    public Operation(UUID id, OperationType type, UUID bankAccountId, BigDecimal amount, UUID categoryId,
                     LocalDate date, String description) {
        if (id == null) throw new InvalidOperationException("ID операции не может быть null");
        validate(type, bankAccountId, amount, categoryId, date);
        this.id = id;
        this.type = type;
        this.bankAccountId = bankAccountId;
        this.amount = amount.setScale(SCALE, ROUNDING);
        this.categoryId = categoryId;
        this.date = date;
        this.description = description;
    }

    public void updateDescription(String newDescription) {
        this.description = newDescription;
    }

    public void updateDate(LocalDate newDate) {
        if (newDate == null) throw new InvalidOperationException("Дата не может быть null");
        this.date = newDate;
    }

    private void validate(OperationType type, UUID bankAccountId,
                          BigDecimal amount, UUID categoryId, LocalDate date) {
        if (type == null)
            throw new InvalidOperationException("Тип операции не может быть null");
        if (bankAccountId == null)
            throw new InvalidOperationException("ID счета не может быть null");
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new InvalidOperationException("Сумма операции должна быть положительной");
        if (categoryId == null)
            throw new InvalidOperationException("ID категории не может быть null");
        if (date == null)
            throw new InvalidOperationException("Дата операции не может быть null");
    }

    @Override
    public String toString() {
        return String.format("Operation{id=%s, type=%s, account=%s, amount=%s, category=%s, date=%s, desc='%s'}",
                id, type, bankAccountId, amount.toPlainString(), categoryId, date, description);
    }
}
