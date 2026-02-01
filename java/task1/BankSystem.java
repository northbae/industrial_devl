import java.util.Scanner;

public class BankSystem {
    private static Bank bank = new Bank();

    private static Scanner scanner = new Scanner(System.in);

    private static final String customerNotFound = "Клиент не найден";
    private static final String accountNotFound = "Счет не найден";

    public static void main(String[] args) {
        boolean operations = true;
        while (operations) {
            printMenu();
            int choice = readInt("Выберите пункт: ");

            switch (choice) {
                case 1 -> createCustomer();
                case 2 -> openDebitAccount();
                case 3 -> openCreditAccount();
                case 4 -> deposit();
                case 5 -> withdraw();
                case 6 -> transfer();
                case 7 -> showCustomerAccounts();
                case 8 -> showTransactions();
                case 9 -> showBankReport();
                case 10 -> {
                    operations = false;
                    System.out.println("Операции закончены");
                }
                default -> System.out.println("Неверный пункт меню");
            }
            System.out.println();
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("-".repeat(40));
        System.out.println("1. Создать клиента");
        System.out.println("2. Открыть дебетовый счёт");
        System.out.println("3. Открыть кредитный счёт");
        System.out.println("4. Пополнить");
        System.out.println("5. Снять");
        System.out.println("6. Перевести");
        System.out.println("7. Показать счета клиента");
        System.out.println("8. Показать транзакции");
        System.out.println("9. Отчёт банка");
        System.out.println("10. Выход");
        System.out.println("-".repeat(40));
    }

    private static void createCustomer() {
        String fullName = readString("Введите ФИО: ");
        Customer customer = bank.createCustomer(fullName);
        System.out.printf("Клиент создан, id: %d%n", customer.getId());
    }

    private static void openDebitAccount() {
        int customerId = readInt("Введите id клиента: ");
        Customer customer = bank.findCustomer(customerId);
        if (customer == null) {
            System.out.println(customerNotFound);
            return;
        }
        Account account;
        double balance = readDouble("Введите начальный баланс: ");
        account = bank.openDebitAccount(customer, balance);
        System.out.printf("Счёт открыт, номер: %s%n", account.getAccountNumber());
    }

    private static void openCreditAccount() {
        int customerId = readInt("Введите id клиента: ");
        Customer customer = bank.findCustomer(customerId);
        if (customer == null) {
            System.out.println(customerNotFound);
            return;
        }
        double balance = readDouble("Введите начальный баланс: ");
        double limit = readDouble("Введите кредитный лимит: ");

        Account account = bank.openCreditAccount(customer, balance, limit);
        System.out.printf("Счёт открыт, номер: %s%n", account.getAccountNumber());
    }

    private static void deposit() {
        String accountNumber = readString("Введите номер счёта: ");
        Account account = bank.findAccount(accountNumber);
        if (account == null) {
            System.out.println(accountNotFound);
            return;
        }
        double amount = readDouble("Введите сумму пополнения: ");
        boolean success = bank.deposit(accountNumber, amount);
        if (success) {
            System.out.printf("Успешно, новый баланс: %.2f%n", account.getBalance());
        }
        else {
            System.out.println("Ошибка пополнения");
        }
    }

    private static void withdraw() {
        String accountNumber = readString("Введите номер счёта: ");
        Account account = bank.findAccount(accountNumber);
        if (account != null) {
            System.out.printf("Текущий баланс: %.2f%n", account.getBalance());
        }
        else {
            System.out.println(accountNotFound);
            return;
        }
        double amount = readDouble("Введите сумму снятия: ");
        boolean success = bank.withdraw(accountNumber, amount);
        if (success) {
            System.out.printf("Успешно, новый баланс: %.2f%n", account.getBalance());
        }
        else {
            System.out.println("Ошибка снятия");
        }
    }

    private static void transfer() {
        String fromNumber = readString("Введите номер счёта отправителя: ");
        String toNumber = readString("Введите номер счёта получателя: ");
        Account fromAccount = bank.findAccount(fromNumber);
        if (fromAccount != null) {
            System.out.printf("Баланс отправителя: %.2f%n", fromAccount.getBalance());
        }
        else {
            System.out.println(accountNotFound);
            return;
        }
        Account toAccount = bank.findAccount(toNumber);
        if (toAccount == null) {
            System.out.println(accountNotFound);
            return;
        }
        double amount = readDouble("Введите сумму перевода: ");
        boolean success = bank.transfer(fromNumber, toNumber, amount);
        if (success) {
            System.out.println("Перевод выполнен успешно");
            System.out.printf("Баланс отправителя: %.2f%n", fromAccount.getBalance());
            System.out.printf("Баланс получателя: %.2f%n", toAccount.getBalance());
        }
        else {
            System.out.println("Ошибка перевода");
        }
    }

    private static void showCustomerAccounts() {
        int customerId = readInt("Введите id клиента: ");
        Customer customer = bank.findCustomer(customerId);
        if (customer == null) {
            System.out.println(customerNotFound);
            return;
        }
        bank.printCustomerAccounts(customerId);
    }

    private static void showTransactions() {
        bank.printTransactions();
    }

    private static void showBankReport() {
        bank.printReport();
    }

    private static String readString(String input) {
        System.out.print(input);
        return scanner.nextLine().trim();
    }

    private static int readInt(String input) {
        System.out.print(input);
        return Integer.parseInt(scanner.nextLine().trim());
    }

    private static double readDouble(String input) {
        System.out.print(input);
        return Double.parseDouble(scanner.nextLine().trim());
    }
}