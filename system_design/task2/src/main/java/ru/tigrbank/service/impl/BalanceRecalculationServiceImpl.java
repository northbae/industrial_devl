package ru.tigrbank.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tigrbank.domain.exception.EntityNotFoundException;
import ru.tigrbank.domain.model.BankAccount;
import ru.tigrbank.domain.model.Operation;
import ru.tigrbank.domain.model.OperationType;
import ru.tigrbank.repository.BankAccountRepository;
import ru.tigrbank.repository.OperationRepository;
import ru.tigrbank.service.BalanceRecalculationService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BalanceRecalculationServiceImpl implements BalanceRecalculationService {

    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    private final BankAccountRepository bankAccountRepository;
    private final OperationRepository operationRepository;

    @Override
    public BigDecimal recalculateBalance(UUID bankAccountId) {
        BankAccount account = findAccountOrThrow(bankAccountId);
        BigDecimal calculated = calculateFromOperations(bankAccountId);
        account.forceSetBalance(calculated);
        bankAccountRepository.save(account);
        return calculated;
    }

    @Override
    public BigDecimal getDiscrepancy(UUID bankAccountId) {
        BankAccount account = findAccountOrThrow(bankAccountId);
        BigDecimal calculated = calculateFromOperations(bankAccountId);
        return account.getBalance().subtract(calculated).setScale(SCALE, ROUNDING);
    }

    private BankAccount findAccountOrThrow(UUID id) {
        return bankAccountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Счет не найден: " + id));
    }

    private BigDecimal calculateFromOperations(UUID bankAccountId) {
        List<Operation> operations = operationRepository.findByBankAccountId(bankAccountId);
        BigDecimal balance = BigDecimal.ZERO;
        for (Operation op : operations) {
            if (op.getType() == OperationType.INCOME) {
                balance = balance.add(op.getAmount());
            } else {
                balance = balance.subtract(op.getAmount());
            }
        }
        return balance.setScale(SCALE, ROUNDING);
    }
}
