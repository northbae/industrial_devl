package ru.tigrbank.repository.impl;

import org.springframework.stereotype.Repository;
import ru.tigrbank.domain.model.Category;
import ru.tigrbank.domain.model.OperationType;
import ru.tigrbank.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

    private final Map<UUID, Category> store = new ConcurrentHashMap<>();

    @Override
    public void save(Category category) {
        store.put(category.getId(), category);
    }

    @Override
    public Optional<Category> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Category> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<Category> findByType(OperationType type) {
        return store.values().stream().filter(c -> c.getType() == type).collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        store.remove(id);
    }
}