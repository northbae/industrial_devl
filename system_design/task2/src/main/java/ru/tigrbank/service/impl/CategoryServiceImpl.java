package ru.tigrbank.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tigrbank.domain.exception.EntityNotFoundException;
import ru.tigrbank.domain.model.Category;
import ru.tigrbank.domain.model.OperationType;
import ru.tigrbank.repository.CategoryRepository;
import ru.tigrbank.service.CategoryService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category createCategory(OperationType type, String name) {
        Category category = new Category(type, name);
        categoryRepository.save(category);
        return category;
    }

    @Override
    public Category getCategory(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Категория не найдена: " + id));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> getCategoriesByType(OperationType type) {
        return categoryRepository.findByType(type);
    }

    @Override
    public void renameCategory(UUID id, String newName) {
        Category category = getCategory(id);
        category.rename(newName);
        categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(UUID id) {
        getCategory(id);
        categoryRepository.deleteById(id);
    }
}
