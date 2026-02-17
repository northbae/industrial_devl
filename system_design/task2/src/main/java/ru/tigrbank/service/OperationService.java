package ru.tigrbank.service;

import ru.tigrbank.domain.model.Operation;
import ru.tigrbank.domain.model.OperationType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface OperationService {
    Operation createOperation(OperationType type,
                              UUID bankAccountId,
                              BigDecimal amount,
                              UUID categoryId,
                              LocalDate date,
                              String description);

    Operation getOperation(UUID id);
    List<Operation> getAllOperations();
    List<Operation> getOperationsByAccount(UUID bankAccountId);
    List<Operation> getOperationsByDateRange(LocalDate from, LocalDate to);
    void deleteOperation(UUID id);
}
