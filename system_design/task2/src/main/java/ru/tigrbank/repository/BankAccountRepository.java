package ru.tigrbank.repository;

import ru.tigrbank.domain.model.BankAccount;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BankAccountRepository {
    void save(BankAccount account);
    Optional<BankAccount> findById(UUID id);
    List<BankAccount> findAll();
    void deleteById(UUID id);
}
