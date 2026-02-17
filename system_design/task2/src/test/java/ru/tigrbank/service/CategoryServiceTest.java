package ru.tigrbank.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tigrbank.domain.exception.EntityNotFoundException;
import ru.tigrbank.domain.model.Category;
import ru.tigrbank.domain.model.OperationType;
import ru.tigrbank.repository.CategoryRepository;
import ru.tigrbank.service.impl.CategoryServiceImpl;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CategoryService")
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Создание категории - сохраняет и возвращает")
    void createCategory() {
        Category result = categoryService.createCategory(OperationType.EXPENSE, "Кафе");

        assertEquals("Кафе", result.getName());
        assertEquals(OperationType.EXPENSE, result.getType());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    @DisplayName("Переименование существующей категории")
    void renameCategory() {
        UUID id = UUID.randomUUID();
        Category category = new Category(id, OperationType.INCOME, "Старое");
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        categoryService.renameCategory(id, "Новое");

        assertEquals("Новое", category.getName());
        verify(categoryRepository).save(category);
    }

    @Test
    @DisplayName("Получение несуществующей —-ошибка")
    void entityNotFoundException_onNotExistingCategory() {
        when(categoryRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoryService.getCategory(UUID.randomUUID()));
    }
}