package ru.tigrbank.repository.impl;

import org.springframework.stereotype.Repository;
import ru.tigrbank.domain.model.BankAccount;
import ru.tigrbank.repository.BankAccountRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class BankAccountRepositoryImpl implements BankAccountRepository {

    private final Map<UUID, BankAccount> store = new ConcurrentHashMap<>();

    @Override
    public void save(BankAccount account) {
        store.put(account.getId(), account);
    }

    @Override
    public Optional<BankAccount> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<BankAccount> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void deleteById(UUID id) {
        store.remove(id);
    }
}