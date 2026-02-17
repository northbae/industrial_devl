package ru.tigrbank.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tigrbank.domain.model.Category;
import ru.tigrbank.domain.model.Operation;
import ru.tigrbank.domain.model.OperationType;
import ru.tigrbank.dto.AnalyticsReport;
import ru.tigrbank.dto.CategorySummary;
import ru.tigrbank.repository.CategoryRepository;
import ru.tigrbank.repository.OperationRepository;
import ru.tigrbank.service.AnalyticsService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    private final OperationRepository operationRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public AnalyticsReport getReport(LocalDate from, LocalDate to) {
        List<Operation> operations = operationRepository.findByDateRange(from, to);
        return buildReport(operations, from, to);
    }

    @Override
    public AnalyticsReport getReportByAccount(UUID bankAccountId, LocalDate from, LocalDate to) {
        List<Operation> operations = operationRepository.findByBankAccountIdAndDateRange(bankAccountId, from, to);
        return buildReport(operations, from, to);
    }

    private AnalyticsReport buildReport(List<Operation> operations, LocalDate from, LocalDate to) {
        BigDecimal totalIncome = sumByType(operations, OperationType.INCOME);
        BigDecimal totalExpense = sumByType(operations, OperationType.EXPENSE);
        BigDecimal netResult = totalIncome.subtract(totalExpense).setScale(SCALE, ROUNDING);
        return AnalyticsReport.builder()
                .from(from)
                .to(to)
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .netResult(netResult)
                .incomeByCategory(groupByCategory(operations, OperationType.INCOME))
                .expenseByCategory(groupByCategory(operations, OperationType.EXPENSE))
                .build();
    }
    private BigDecimal sumByType(List<Operation> operations, OperationType type) {
        return operations.stream()
                .filter(op -> op.getType() == type).map(Operation::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(SCALE, ROUNDING);
    }

    private List<CategorySummary> groupByCategory(List<Operation> operations,
                                                  OperationType type) {
        Map<UUID, List<Operation>> grouped = operations.stream().filter(op -> op.getType() == type)
                .collect(Collectors.groupingBy(Operation::getCategoryId));
        List<CategorySummary> result = new ArrayList<>();
        for (Map.Entry<UUID, List<Operation>> entry : grouped.entrySet()) {
            String name = categoryRepository.findById(entry.getKey())
                    .map(Category::getName).orElse("Неизвестная категория");

            BigDecimal total = entry.getValue().stream()
                    .map(Operation::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(SCALE, ROUNDING);

            result.add(new CategorySummary(name, total, entry.getValue().size()));
        }
        result.sort((a, b) -> b.getTotal().compareTo(a.getTotal()));
        return result;
    }
}
