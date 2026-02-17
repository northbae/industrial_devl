package ru.tigrbank.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.tigrbank.domain.exception.LackFundsException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BankAccount")
class BankAccountTest {

    @Test
    @DisplayName("Новый счёт создаётся с нулевым балансом")
    void AccountHasZeroBalance_onCreateAccount() {
        BankAccount account = new BankAccount("Основной");

        assertNotNull(account.getId());
        assertEquals("Основной", account.getName());
        assertEquals(new BigDecimal("0.00"), account.getBalance());
    }

    @Test
    @DisplayName("Deposit увеличивает баланс, withdraw уменьшает")
    void depositAndWithdraw() {
        BankAccount account = new BankAccount("Тест");

        account.deposit(new BigDecimal("1000.00"));
        assertEquals(new BigDecimal("1000.00"), account.getBalance());

        account.withdraw(new BigDecimal("300.00"));
        assertEquals(new BigDecimal("700.00"), account.getBalance());
    }

    @Test
    @DisplayName("При сумме снятия больше баланса выбрасывается ошибка, баланс не меняется")
    void lackFoundsExceptionThrows_onWithdrawMoreThanBalance() {
        BankAccount account = new BankAccount("Тест");
        account.deposit(new BigDecimal("100.00"));

        assertThrows(LackFundsException.class, () -> account.withdraw(new BigDecimal("200.00")));
        assertEquals(new BigDecimal("100.00"), account.getBalance());
    }
}