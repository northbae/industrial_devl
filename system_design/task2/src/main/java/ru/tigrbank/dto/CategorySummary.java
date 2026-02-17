package ru.tigrbank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@ToString
public class CategorySummary {
    private final String categoryName;
    private final BigDecimal total;
    private final int operationCount;
}