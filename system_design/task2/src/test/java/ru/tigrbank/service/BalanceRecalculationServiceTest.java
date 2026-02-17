package ru.tigrbank.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tigrbank.domain.model.BankAccount;
import ru.tigrbank.domain.model.Operation;
import ru.tigrbank.domain.model.OperationType;
import ru.tigrbank.repository.BankAccountRepository;
import ru.tigrbank.repository.OperationRepository;
import ru.tigrbank.service.impl.BalanceRecalculationServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BalanceRecalculationService")
class BalanceRecalculationServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;
    @Mock
    private OperationRepository operationRepository;

    @InjectMocks
    private BalanceRecalculationServiceImpl recalculationService;

    private List<Operation> sampleOperations(UUID accountId) {
        UUID catId = UUID.randomUUID();
        return List.of(
                new Operation(OperationType.INCOME, accountId,
                        new BigDecimal("10000.00"), catId, LocalDate.now(), null),
                new Operation(OperationType.EXPENSE, accountId,
                        new BigDecimal("3000.00"), catId, LocalDate.now(), null)
        );
    }

    @Test
    @DisplayName("Нет расхождения - возвращает 0")
    void returnZero_onNoDiscrepancy() {
        UUID id = UUID.randomUUID();
        BankAccount account = new BankAccount(id, "Тест", new BigDecimal("7000.00"));

        when(bankAccountRepository.findById(id)).thenReturn(Optional.of(account));
        when(operationRepository.findByBankAccountId(id)).thenReturn(sampleOperations(id));

        assertEquals(new BigDecimal("0.00"), recalculationService.getDiscrepancy(id));
    }

    @Test
    @DisplayName("Есть расхождение - возвращает разницу")
    void returnDifference_onHasDiscrepancy() {
        UUID id = UUID.randomUUID();
        BankAccount account = new BankAccount(id, "Тест", new BigDecimal("9999.00"));

        when(bankAccountRepository.findById(id)).thenReturn(Optional.of(account));
        when(operationRepository.findByBankAccountId(id)).thenReturn(sampleOperations(id));

        assertEquals(new BigDecimal("2999.00"), recalculationService.getDiscrepancy(id));
    }

    @Test
    @DisplayName("Пересчет исправляет баланс")
    void fixesBalance_onRecalculation() {
        UUID id = UUID.randomUUID();
        BankAccount account = new BankAccount(id, "Тест", new BigDecimal("9999.00"));

        when(bankAccountRepository.findById(id)).thenReturn(Optional.of(account));
        when(operationRepository.findByBankAccountId(id)).thenReturn(sampleOperations(id));

        BigDecimal result = recalculationService.recalculateBalance(id);

        assertEquals(new BigDecimal("7000.00"), result);
        assertEquals(new BigDecimal("7000.00"), account.getBalance());
        verify(bankAccountRepository).save(account);
    }
}