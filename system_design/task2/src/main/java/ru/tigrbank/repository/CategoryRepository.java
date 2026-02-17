package ru.tigrbank.repository;

import ru.tigrbank.domain.model.Category;
import ru.tigrbank.domain.model.OperationType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository {
    void save(Category category);
    Optional<Category> findById(UUID id);
    List<Category> findAll();
    List<Category> findByType(OperationType type);
    void deleteById(UUID id);
}
