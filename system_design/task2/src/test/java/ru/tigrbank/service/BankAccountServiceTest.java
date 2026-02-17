package ru.tigrbank.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tigrbank.domain.exception.EntityNotFoundException;
import ru.tigrbank.domain.model.BankAccount;
import ru.tigrbank.repository.BankAccountRepository;
import ru.tigrbank.service.impl.BankAccountServiceImpl;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BankAccountService")
class BankAccountServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @InjectMocks
    private BankAccountServiceImpl bankAccountService;

    @Test
    @DisplayName("Создание - сохраняет и возвращает счёт с нулевым балансом")
    void createAccount() {
        BankAccount result = bankAccountService.createAccount("Основной");

        assertEquals("Основной", result.getName());
        assertEquals(new BigDecimal("0.00"), result.getBalance());
        verify(bankAccountRepository).save(any(BankAccount.class));
    }

    @Test
    @DisplayName("Получение существующего счёта")
    void getExistingAccount() {
        UUID id = UUID.randomUUID();
        BankAccount account = new BankAccount(id, "Тест", BigDecimal.ZERO);
        when(bankAccountRepository.findById(id)).thenReturn(Optional.of(account));

        BankAccount result = bankAccountService.getAccount(id);

        assertEquals("Тест", result.getName());
    }

    @Test
    @DisplayName("Получение несуществующего - ошибка")
    void entityNotFoundExceptionThrows_onNotExistingAccount() {
        UUID id = UUID.randomUUID();
        when(bankAccountRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bankAccountService.getAccount(id));
    }
}