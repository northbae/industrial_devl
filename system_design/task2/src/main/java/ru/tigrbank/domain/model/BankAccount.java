package ru.tigrbank.domain.model;

import lombok.Getter;
import ru.tigrbank.domain.exception.LackFundsException;
import ru.tigrbank.domain.exception.InvalidOperationException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Getter
public class BankAccount {

    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    private final UUID id;
    private String name;
    private BigDecimal balance;

    public BankAccount(String name) {
        validateName(name);
        this.id = UUID.randomUUID();
        this.name = name.trim();
        this.balance = BigDecimal.ZERO.setScale(SCALE, ROUNDING);
    }

    public BankAccount(UUID id, String name, BigDecimal balance) {
        if (id == null) throw new InvalidOperationException("ID счета не может быть null");
        validateName(name);
        if (balance == null) throw new InvalidOperationException("Баланс не может быть null");
        this.id = id;
        this.name = name.trim();
        this.balance = balance.setScale(SCALE, ROUNDING);
    }

    public void rename(String newName) {
        validateName(newName);
        this.name = newName.trim();
    }

    public void deposit(BigDecimal amount) {
        validatePositiveAmount(amount);
        this.balance = this.balance.add(amount).setScale(SCALE, ROUNDING);
    }

    public void withdraw(BigDecimal amount) {
        validatePositiveAmount(amount);
        BigDecimal newBalance = this.balance.subtract(amount).setScale(SCALE, ROUNDING);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new LackFundsException("Недостаточно средств на счете «" + name + "». " +
                    "Баланс: " + balance.toPlainString() + ", списание: " + amount.toPlainString());
        }
        this.balance = newBalance;
    }

    public void forceSetBalance(BigDecimal newBalance) {
        if (newBalance == null) throw new InvalidOperationException("Баланс не может быть null");
        this.balance = newBalance.setScale(SCALE, ROUNDING);
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidOperationException("Название счета не может быть пустым");
        }
    }

    private void validatePositiveAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidOperationException("Сумма должна быть положительной");
        }
    }

    @Override
    public String toString() {
        return String.format("BankAccount{id=%s, name='%s', balance=%s}", id, name, balance.toPlainString());
    }
}
