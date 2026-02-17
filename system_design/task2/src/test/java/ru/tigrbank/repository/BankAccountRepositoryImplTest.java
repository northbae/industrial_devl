package ru.tigrbank.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.tigrbank.domain.model.BankAccount;
import ru.tigrbank.repository.impl.BankAccountRepositoryImpl;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BankAccountRepositoryImpl")
class BankAccountRepositoryImplTest{

    private BankAccountRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new BankAccountRepositoryImpl();
    }

    @Test
    @DisplayName("Сохранение и поиск по ID")
    void saveAndFind() {
        BankAccount account = new BankAccount("Тест");
        repository.save(account);

        assertTrue(repository.findById(account.getId()).isPresent());
        assertEquals("Тест", repository.findById(account.getId()).get().getName());
    }

    @Test
    @DisplayName("Поиск несуществующего - пустой Optional")
    void returnEmpty_onNonExistent() {
        assertTrue(repository.findById(UUID.randomUUID()).isEmpty());
    }

    @Test
    @DisplayName("Удаление работает")
    void delete() {
        BankAccount account = new BankAccount("Тест");
        repository.save(account);
        repository.deleteById(account.getId());

        assertTrue(repository.findById(account.getId()).isEmpty());
        assertEquals(0, repository.findAll().size());
    }
}