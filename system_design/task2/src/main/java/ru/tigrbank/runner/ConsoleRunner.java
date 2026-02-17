package ru.tigrbank.runner;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.tigrbank.domain.model.*;
import ru.tigrbank.dto.AnalyticsReport;
import ru.tigrbank.dto.CategorySummary;
import ru.tigrbank.service.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ConsoleRunner implements CommandLineRunner {

    private final BankAccountService bankAccountService;
    private final CategoryService categoryService;
    private final OperationService operationService;
    private final AnalyticsService analyticsService;
    private final BalanceRecalculationService balanceRecalculationService;
    private final DataExportService dataExportService;
    private final DataImportService dataImportService;
    private final TimingService timingService;

    static final String LINE = "=".repeat(50);
    static final String DASH = "-".repeat(30);

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println(LINE);
        System.out.println("Добро пожаловать в ТигрБанк, Модуль Учет финансов");
        System.out.println(LINE);

        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> createAccount(scanner);
                    case "2" -> listAccounts();
                    case "3" -> renameAccount(scanner);
                    case "4" -> deleteAccount(scanner);
                    case "5" -> createCategory(scanner);
                    case "6" -> listCategories();
                    case "7" -> renameCategory(scanner);
                    case "8" -> deleteCategory(scanner);
                    case "9" -> createOperation(scanner);
                    case "10" -> listOperations();
                    case "11" -> deleteOperation(scanner);
                    case "12" -> showAnalytics(scanner);
                    case "13" -> recalculateBalance(scanner);
                    case "14" -> checkDiscrepancy(scanner);
                    case "15" -> exportData(scanner);
                    case "16" -> importData(scanner);
                    case "0" -> running = false;
                    default -> System.out.println("Неизвестная команда.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            if (running) {
                System.out.println("\nНажмите Enter");
                scanner.nextLine();
            }
        }
    }

    private void printMenu() {
        System.out.println("\n--- МЕНЮ ---");
        System.out.println(" [Счета]");
        System.out.println(" 1 - Создать счет");
        System.out.println(" 2 - Список счетов");
        System.out.println(" 3 - Переименовать счет");
        System.out.println(" 4 - Удалить счет");
        System.out.println(" [Категории]");
        System.out.println(" 5 - Создать категорию");
        System.out.println(" 6 - Список категорий");
        System.out.println(" 7 - Переименовать категорию");
        System.out.println(" 8 - Удалить категорию");
        System.out.println(" [Операции]");
        System.out.println(" 9 - Создать операцию");
        System.out.println(" 10 - Список операций");
        System.out.println(" 11 - Удалить операцию");
        System.out.println(" [Аналитика]");
        System.out.println(" 12 - Отчет за период");
        System.out.println(" [Баланс]");
        System.out.println(" 13 - Пересчитать баланс");
        System.out.println(" 14 - Проверить расхождение");
        System.out.println(" [Экспорт / Импорт]");
        System.out.println(" 15 - Экспорт данных");
        System.out.println(" 16 - Импорт данных");
        System.out.println(" [Выход]");
        System.out.println(" 0 - Выход");
        System.out.print("Выбор: ");
    }

    private void createAccount(Scanner scanner) {
        System.out.print("Название счета: ");
        String name = scanner.nextLine().trim();
        BankAccount account = timingService.measureAndReturn("Создание счета",
                () -> bankAccountService.createAccount(name));
        System.out.println("Создан счет - " + account);
    }

    private void listAccounts() {
        List<BankAccount> accounts = bankAccountService.getAllAccounts();
        if (accounts.isEmpty()) {
            System.out.println("Счетов нет.");
            return;
        }
        System.out.println("--- Счета ---");
        for (BankAccount a : accounts) {
            System.out.printf("%s | %-20s | %s%n", a.getId(), a.getName(), a.getBalance().toPlainString());
        }
    }

    private void renameAccount(Scanner scanner) {
        listAccounts();
        System.out.print("ID счета: ");
        UUID id = UUID.fromString(scanner.nextLine().trim());
        System.out.print("Новое название: ");
        String newName = scanner.nextLine().trim();
        bankAccountService.renameAccount(id, newName);
        System.out.println("Счет переименован");
    }

    private void deleteAccount(Scanner scanner) {
        listAccounts();
        System.out.print("ID счета: ");
        UUID id = UUID.fromString(scanner.nextLine().trim());
        bankAccountService.deleteAccount(id);
        System.out.println("Счет дален");
    }

    private void createCategory(Scanner scanner) {
        System.out.print("Тип (INCOME / EXPENSE): ");
        OperationType type = OperationType.valueOf(scanner.nextLine().trim().toUpperCase());
        System.out.print("Название: ");
        String name = scanner.nextLine().trim();
        Category cat = timingService.measureAndReturn("Создание категории",
                () -> categoryService.createCategory(type, name));
        System.out.println("Создана категория -" + cat);
    }

    private void listCategories() {
        List<Category> categories = categoryService.getAllCategories();
        if (categories.isEmpty()) {
            System.out.println("Категорий нет.");
            return;
        }
        System.out.println("--- Категории ---");
        for (Category c : categories) {
            System.out.printf("%s | %-8s | %s%n", c.getId(), c.getType(), c.getName());
        }
    }

    private void renameCategory(Scanner scanner) {
        listCategories();
        System.out.print("ID категории: ");
        UUID id = UUID.fromString(scanner.nextLine().trim());
        System.out.print("Новое название: ");
        String newName = scanner.nextLine().trim();
        categoryService.renameCategory(id, newName);
        System.out.println("Категория переименована.");
    }

    private void deleteCategory(Scanner scanner) {
        listCategories();
        System.out.print("ID категории: ");
        UUID id = UUID.fromString(scanner.nextLine().trim());
        categoryService.deleteCategory(id);
        System.out.println("Категория уалена.");
    }

    private void createOperation(Scanner scanner) {
        System.out.print("Тип (INCOME / EXPENSE): ");
        OperationType type = OperationType.valueOf(scanner.nextLine().trim().toUpperCase());
        listAccounts();
        System.out.print("ID счета: ");
        UUID accountId = UUID.fromString(scanner.nextLine().trim());
        System.out.print("Сумма: ");
        BigDecimal amount = new BigDecimal(scanner.nextLine().trim());
        listCategories();
        System.out.print("ID категории: ");
        UUID categoryId = UUID.fromString(scanner.nextLine().trim());
        System.out.print("Дата (YYYY-MM-DD) [Enter = сегодня]: ");
        String dateStr = scanner.nextLine().trim();
        LocalDate date = dateStr.isEmpty() ? LocalDate.now() : LocalDate.parse(dateStr);
        System.out.print("Описание [Enter = пропустить]: ");
        String desc = scanner.nextLine().trim();
        String description = desc.isEmpty() ? null : desc;
        Operation op = timingService.measureAndReturn("Создание операции",
                () -> operationService.createOperation(
                        type, accountId, amount, categoryId, date, description));
        System.out.println("Проведена операция " + op);
        System.out.println("Баланс: " + bankAccountService.getAccount(accountId).getBalance().toPlainString());
    }

    private void listOperations() {
        List<Operation> operations = operationService.getAllOperations();
        if (operations.isEmpty()) {
            System.out.println("Операций нет.");
            return;
        }
        System.out.println("--- Операции ---");
        for (Operation o : operations) {
            String catName = categoryService.getCategory(o.getCategoryId()).getName();
            System.out.printf("%s | %-8s | %10s | %-15s | %s | %s%n",
                    o.getId(), o.getType(), o.getAmount().toPlainString(), catName, o.getDate(),
                    o.getDescription() != null ? o.getDescription() : "-");
        }
    }

    private void deleteOperation(Scanner scanner) {
        listOperations();
        System.out.print("ID операции: ");
        UUID id = UUID.fromString(scanner.nextLine().trim());
        operationService.deleteOperation(id);
        System.out.println("Операция отменена (баланс скорректирован).");
    }

    private void showAnalytics(Scanner scanner) {
        System.out.print("Дата начала (YYYY-MM-DD): ");
        LocalDate from = LocalDate.parse(scanner.nextLine().trim());
        System.out.print("Дата конца (YYYY-MM-DD): ");
        LocalDate to = LocalDate.parse(scanner.nextLine().trim());
        AnalyticsReport report = timingService.measureAndReturn("Аналитика", () -> analyticsService.getReport(from, to));
        printReport(report);
    }

    private void printReport(AnalyticsReport report) {
        System.out.println(LINE);
        System.out.printf("Период: %s - %s%n", report.getFrom(), report.getTo());
        System.out.printf("Доходы: %s%n", report.getTotalIncome().toPlainString());
        System.out.printf("Расходы: %s%n", report.getTotalExpense().toPlainString());
        System.out.printf("Итого: %s%n", report.getNetResult().toPlainString());
        System.out.println(LINE);
        if (!report.getIncomeByCategory().isEmpty()) {
            System.out.println("Доходы по категориям:");
            for (CategorySummary cs : report.getIncomeByCategory()) {
                System.out.printf("%-20s %10s (%d оп.)%n",
                        cs.getCategoryName(), cs.getTotal().toPlainString(), cs.getOperationCount());
            }
        }
        if (!report.getExpenseByCategory().isEmpty()) {
            System.out.println("Расходы по категориям:");
            for (CategorySummary cs : report.getExpenseByCategory()) {
                System.out.printf("%-20s %10s (%d оп.)%n",
                        cs.getCategoryName(), cs.getTotal().toPlainString(), cs.getOperationCount());
            }
        }
    }

    private void recalculateBalance(Scanner scanner) {
        listAccounts();
        System.out.print("ID счета: ");
        UUID id = UUID.fromString(scanner.nextLine().trim());
        BigDecimal newBalance = balanceRecalculationService.recalculateBalance(id);
        System.out.println("Баланс пересчитан: " + newBalance.toPlainString());
    }

    private void checkDiscrepancy(Scanner scanner) {
        listAccounts();
        System.out.print("ID счета: ");
        UUID id = UUID.fromString(scanner.nextLine().trim());
        BigDecimal discrepancy = balanceRecalculationService.getDiscrepancy(id);
        if (discrepancy.compareTo(BigDecimal.ZERO) == 0) {
            System.out.println("Расхождений нет.");
        } else {
            System.out.println("Расхождение: " + discrepancy.toPlainString());
        }
    }

    private void exportData(Scanner scanner) throws Exception {
        System.out.println("Формат: 1 - JSON, 2 - YAML, 3 - CSV");
        String fmt = scanner.nextLine().trim();
        switch (fmt) {
            case "1" -> {
                timingService.measure("Экспорт JSON", () -> {
                    try { dataExportService.exportToJson("export.json"); }
                    catch (Exception e) { throw new RuntimeException(e); }
                });
                System.out.println("export.json");
            }
            case "2" -> {
                timingService.measure("Экспорт YAML", () -> {
                    try { dataExportService.exportToYaml("export.yaml"); }
                    catch (Exception e) { throw new RuntimeException(e); }
                });
                System.out.println("export.yaml");
            }
            case "3" -> {
                timingService.measure("Экспорт CSV", () -> {
                    try { dataExportService.exportToCsv("export_csv"); }
                    catch (Exception e) { throw new RuntimeException(e); }
                });
                System.out.println("export_csv/");
            }
            default -> System.out.println("Неизвестный формат.");
        }
    }

    private void importData(Scanner scanner) throws Exception {
        System.out.println("Формат: 1 -JSON, 2 - YAML, 3 - CSV");
        String fmt = scanner.nextLine().trim();
        System.out.print("Путь: ");
        String path = scanner.nextLine().trim();
        switch (fmt) {
            case "1" -> {
                timingService.measure("Импорт JSON", () -> {
                    try { dataImportService.importFromJson(path); }
                    catch (Exception e) { throw new RuntimeException(e); }
                });
                System.out.println("Импорт завершен.");
            }
            case "2" -> {
                timingService.measure("Импорт YAML", () -> {
                    try { dataImportService.importFromYaml(path); }
                    catch (Exception e) { throw new RuntimeException(e); }
                });
                System.out.println("Импорт завершен.");
            }
            case "3" -> {
                timingService.measure("Импорт CSV", () -> {
                    try { dataImportService.importFromCsv(path); }
                    catch (Exception e) { throw new RuntimeException(e); }
                });
                System.out.println("Импорт завершен.");
            }
            default -> System.out.println("Неизвестный формат.");
        }
    }
}
