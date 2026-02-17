package ru.tigrbank.domain.model;

import lombok.Getter;
import ru.tigrbank.domain.exception.InvalidOperationException;

import java.util.UUID;

@Getter
public class Category {

    private final UUID id;
    private OperationType type;
    private String name;

    public Category(OperationType type, String name) {
        validateType(type);
        validateName(name);
        this.id = UUID.randomUUID();
        this.type = type;
        this.name = name.trim();
    }

    public Category(UUID id, OperationType type, String name) {
        if (id == null) throw new InvalidOperationException("ID категории не может быть null");
        validateType(type);
        validateName(name);
        this.id = id;
        this.type = type;
        this.name = name.trim();
    }

    public void rename(String newName) {
        validateName(newName);
        this.name = newName.trim();
    }

    public void changeType(OperationType newType) {
        validateType(newType);
        this.type = newType;
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidOperationException("Название категории не может быть пустым");
        }
    }

    private void validateType(OperationType type) {
        if (type == null) {
            throw new InvalidOperationException("Тип категории не может быть null");
        }
    }

    @Override
    public String toString() {
        return String.format("Category{id=%s, type=%s, name='%s'}", id, type, name);
    }
}
