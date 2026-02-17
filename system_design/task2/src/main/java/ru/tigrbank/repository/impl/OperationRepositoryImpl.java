package ru.tigrbank.repository.impl;

import org.springframework.stereotype.Repository;
import ru.tigrbank.domain.model.Operation;
import ru.tigrbank.domain.model.OperationType;
import ru.tigrbank.repository.OperationRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class OperationRepositoryImpl implements OperationRepository {

    private final Map<UUID, Operation> store = new ConcurrentHashMap<>();

    @Override
    public void save(Operation operation) {
        store.put(operation.getId(), operation);
    }

    @Override
    public Optional<Operation> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Operation> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<Operation> findByBankAccountId(UUID bankAccountId) {
        return store.values().stream()
                .filter(op -> op.getBankAccountId().equals(bankAccountId)).collect(Collectors.toList());
    }

    @Override
    public List<Operation> findByType(OperationType type) {
        return store.values().stream()
                .filter(op -> op.getType() == type).collect(Collectors.toList());
    }

    @Override
    public List<Operation> findByDateRange(LocalDate from, LocalDate to) {
        return store.values().stream()
                .filter(op -> !op.getDate().isBefore(from) && !op.getDate().isAfter(to))
                .collect(Collectors.toList());
    }

    @Override
    public List<Operation> findByBankAccountIdAndDateRange(UUID bankAccountId,
                                                           LocalDate from, LocalDate to) {
        return store.values().stream()
                .filter(op -> op.getBankAccountId().equals(bankAccountId))
                .filter(op -> !op.getDate().isBefore(from) && !op.getDate().isAfter(to))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        store.remove(id);
    }
}
