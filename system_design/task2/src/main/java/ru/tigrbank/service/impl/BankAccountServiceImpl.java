package ru.tigrbank.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tigrbank.domain.exception.EntityNotFoundException;
import ru.tigrbank.domain.model.BankAccount;
import ru.tigrbank.repository.BankAccountRepository;
import ru.tigrbank.service.BankAccountService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    @Override
    public BankAccount createAccount(String name) {
        BankAccount account = new BankAccount(name);
        bankAccountRepository.save(account);
        return account;
    }

    @Override
    public BankAccount getAccount(UUID id) {
        return bankAccountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Счет не найден: " + id));
    }

    @Override
    public List<BankAccount> getAllAccounts() {
        return bankAccountRepository.findAll();
    }

    @Override
    public void renameAccount(UUID id, String newName) {
        BankAccount account = getAccount(id);
        account.rename(newName);
        bankAccountRepository.save(account);
    }

    @Override
    public void deleteAccount(UUID id) {
        getAccount(id);
        bankAccountRepository.deleteById(id);
    }
}
