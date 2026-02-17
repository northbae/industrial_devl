package ru.tigrbank.repository;

import ru.tigrbank.domain.model.Operation;
import ru.tigrbank.domain.model.OperationType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OperationRepository {
    void save(Operation operation);
    Optional<Operation> findById(UUID id);
    List<Operation> findAll();
    List<Operation> findByBankAccountId(UUID bankAccountId);
    List<Operation> findByType(OperationType type);
    List<Operation> findByDateRange(LocalDate from, LocalDate to);
    List<Operation> findByBankAccountIdAndDateRange(UUID bankAccountId, LocalDate from, LocalDate to);
    void deleteById(UUID id);
}
