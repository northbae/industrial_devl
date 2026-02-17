package ru.tigrbank.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@ToString
public class AnalyticsReport {
    private final LocalDate from;
    private final LocalDate to;
    private final BigDecimal totalIncome;
    private final BigDecimal totalExpense;
    private final BigDecimal netResult;
    private final List<CategorySummary> incomeByCategory;
    private final List<CategorySummary> expenseByCategory;
}
