package ru.tigrbank.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tigrbank.domain.model.Category;
import ru.tigrbank.domain.model.Operation;
import ru.tigrbank.domain.model.OperationType;
import ru.tigrbank.dto.AnalyticsReport;
import ru.tigrbank.repository.CategoryRepository;
import ru.tigrbank.repository.OperationRepository;
import ru.tigrbank.service.impl.AnalyticsServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("AnalyticsService")
class AnalyticsServiceTest {

    @Mock
    private OperationRepository operationRepository;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private AnalyticsServiceImpl analyticsService;

    private final LocalDate from = LocalDate.of(2024, 1, 1);
    private final LocalDate to = LocalDate.of(2024, 12, 31);

    @Test
    @DisplayName("Пустой отчёт - проверка нулевых значений")
    void emptyReport() {
        when(operationRepository.findByDateRange(from, to)).thenReturn(List.of());

        AnalyticsReport report = analyticsService.getReport(from, to);

        assertEquals(new BigDecimal("0.00"), report.getTotalIncome());
        assertEquals(new BigDecimal("0.00"), report.getTotalExpense());
        assertEquals(new BigDecimal("0.00"), report.getNetResult());
    }

    @Test
    @DisplayName("Корректный подсчет доходов, расходов и разницы")
    void calculatesCorrectly() {
        UUID accountId = UUID.randomUUID();
        UUID salaryId = UUID.randomUUID();
        UUID cafeId = UUID.randomUUID();

        when(operationRepository.findByDateRange(from, to)).thenReturn(List.of(
                new Operation(OperationType.INCOME, accountId,
                        new BigDecimal("100000.00"), salaryId, from.plusDays(1), null),
                new Operation(OperationType.EXPENSE, accountId,
                        new BigDecimal("3000.00"), cafeId, from.plusDays(5), null)
        ));
        when(categoryRepository.findById(salaryId))
                .thenReturn(Optional.of(new Category(salaryId, OperationType.INCOME, "Зарплата")));
        when(categoryRepository.findById(cafeId))
                .thenReturn(Optional.of(new Category(cafeId, OperationType.EXPENSE, "Кафе")));

        AnalyticsReport report = analyticsService.getReport(from, to);

        assertEquals(new BigDecimal("100000.00"), report.getTotalIncome());
        assertEquals(new BigDecimal("3000.00"), report.getTotalExpense());
        assertEquals(new BigDecimal("97000.00"), report.getNetResult());
    }

    @Test
    @DisplayName("Группировка по категориям")
    void groupsByCategory() {
        UUID accountId = UUID.randomUUID();
        UUID cafeId = UUID.randomUUID();

        when(operationRepository.findByDateRange(from, to)).thenReturn(List.of(
                new Operation(OperationType.EXPENSE, accountId,
                        new BigDecimal("1000.00"), cafeId, from.plusDays(1), null),
                new Operation(OperationType.EXPENSE, accountId,
                        new BigDecimal("2000.00"), cafeId, from.plusDays(2), null)
        ));
        when(categoryRepository.findById(cafeId))
                .thenReturn(Optional.of(new Category(cafeId, OperationType.EXPENSE, "Кафе")));

        AnalyticsReport report = analyticsService.getReport(from, to);

        assertEquals(1, report.getExpenseByCategory().size());
        assertEquals("Кафе", report.getExpenseByCategory().get(0).getCategoryName());
        assertEquals(new BigDecimal("3000.00"), report.getExpenseByCategory().get(0).getTotal());
        assertEquals(2, report.getExpenseByCategory().get(0).getOperationCount());
    }
}