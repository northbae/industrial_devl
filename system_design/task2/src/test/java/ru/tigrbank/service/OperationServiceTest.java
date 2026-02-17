package ru.tigrbank.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tigrbank.domain.exception.LackFundsException;
import ru.tigrbank.domain.model.*;
import ru.tigrbank.repository.BankAccountRepository;
import ru.tigrbank.repository.CategoryRepository;
import ru.tigrbank.repository.OperationRepository;
import ru.tigrbank.service.impl.OperationServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OperationService")
class OperationServiceTest {

    @Mock
    private OperationRepository operationRepository;
    @Mock
    private BankAccountRepository bankAccountRepository;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private OperationServiceImpl operationService;

    @Test
    @DisplayName("Доход увеличивает баланс счёта")
    void increasedBalance_onIncome() {
        BankAccount account = new BankAccount("Тест");
        Category category = new Category(OperationType.INCOME, "Зарплата");

        when(bankAccountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        operationService.createOperation(OperationType.INCOME, account.getId(),
                new BigDecimal("5000.00"), category.getId(), LocalDate.now(), "Зарплата");

        assertEquals(new BigDecimal("5000.00"), account.getBalance());
        verify(bankAccountRepository).save(account);
        verify(operationRepository).save(any(Operation.class));
    }

    @Test
    @DisplayName("Расход больше баланса - ошибка, ничего не сохраняется")
    void LackFundsExceptionThrows_onExpenseMoreThanBalance() {
        BankAccount account = new BankAccount("Тест");
        account.deposit(new BigDecimal("100.00"));
        Category category = new Category(OperationType.EXPENSE, "Кафе");

        when(bankAccountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        assertThrows(LackFundsException.class, () -> operationService.createOperation(OperationType.EXPENSE, account.getId(),
                        new BigDecimal("99999.00"), category.getId(), LocalDate.now(), null));

        assertEquals(new BigDecimal("100.00"), account.getBalance());
        verify(bankAccountRepository, never()).save(any());
        verify(operationRepository, never()).save(any());
    }

    @Test
    @DisplayName("Удаление расхода возвращает деньги на счёт")
    void restoredBalance_onDeleteExpense() {
        BankAccount account = new BankAccount("Тест");
        account.deposit(new BigDecimal("7000.00"));
        Category category = new Category(OperationType.EXPENSE, "Кафе");

        Operation expense = new Operation(OperationType.EXPENSE, account.getId(),
                new BigDecimal("2000.00"), category.getId(), LocalDate.now(), "Обед");

        when(operationRepository.findById(expense.getId())).thenReturn(Optional.of(expense));
        when(bankAccountRepository.findById(account.getId())).thenReturn(Optional.of(account));

        operationService.deleteOperation(expense.getId());

        assertEquals(new BigDecimal("9000.00"), account.getBalance());
        verify(operationRepository).deleteById(expense.getId());
    }
}