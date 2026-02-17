package ru.tigrbank.service;

import ru.tigrbank.domain.model.BankAccount;

import java.util.List;
import java.util.UUID;

public interface BankAccountService {
    BankAccount createAccount(String name);
    BankAccount getAccount(UUID id);
    List<BankAccount> getAllAccounts();
    void renameAccount(UUID id, String newName);
    void deleteAccount(UUID id);
}
