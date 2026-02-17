package ru.tigrbank.service;

import ru.tigrbank.domain.model.Category;
import ru.tigrbank.domain.model.OperationType;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    Category createCategory(OperationType type, String name);
    Category getCategory(UUID id);
    List<Category> getAllCategories();
    List<Category> getCategoriesByType(OperationType type);
    void renameCategory(UUID id, String newName);
    void deleteCategory(UUID id);
}
