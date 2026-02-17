package ru.tigrbank.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tigrbank.domain.exception.EntityNotFoundException;
import ru.tigrbank.domain.model.BankAccount;
import ru.tigrbank.domain.model.Operation;
import ru.tigrbank.domain.model.OperationType;
import ru.tigrbank.repository.BankAccountRepository;
import ru.tigrbank.repository.CategoryRepository;
import ru.tigrbank.repository.OperationRepository;
import ru.tigrbank.service.OperationService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OperationServiceImpl implements OperationService {

    private final OperationRepository operationRepository;
    private final BankAccountRepository bankAccountRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Operation createOperation(OperationType type,
                                     UUID bankAccountId,
                                     BigDecimal amount,
                                     UUID categoryId,
                                     LocalDate date,
                                     String description) {
        BankAccount account = bankAccountRepository.findById(bankAccountId)
                .orElseThrow(() -> new EntityNotFoundException("Счет не найден: " + bankAccountId));
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Категория не найдена: " + categoryId));
        if (type == OperationType.INCOME) {
            account.deposit(amount);
        } else {
            account.withdraw(amount);
        }
        Operation operation = new Operation(type, bankAccountId, amount, categoryId, date, description);
        bankAccountRepository.save(account);
        operationRepository.save(operation);
        return operation;
    }

    @Override
    public Operation getOperation(UUID id) {
        return operationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Операция не найдена: " + id));
    }

    @Override
    public List<Operation> getAllOperations() {
        return operationRepository.findAll();
    }

    @Override
    public List<Operation> getOperationsByAccount(UUID bankAccountId) {
        return operationRepository.findByBankAccountId(bankAccountId);
    }

    @Override
    public List<Operation> getOperationsByDateRange(LocalDate from, LocalDate to) {
        return operationRepository.findByDateRange(from, to);
    }

    @Override
    public void deleteOperation(UUID id) {
        Operation op = getOperation(id);
        BankAccount account = bankAccountRepository.findById(op.getBankAccountId())
                .orElseThrow(() -> new EntityNotFoundException("Счет не найден: " + op.getBankAccountId()));
        if (op.getType() == OperationType.INCOME) {
            account.forceSetBalance(account.getBalance().subtract(op.getAmount()));
        } else {
            account.forceSetBalance(account.getBalance().add(op.getAmount()));
        }
        bankAccountRepository.save(account);
        operationRepository.deleteById(id);
    }
}
