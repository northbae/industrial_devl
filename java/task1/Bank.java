import java.util.ArrayList;
import java.util.Objects;

public class Bank {
    private long nextAccountNumber = 0;

    private final ArrayList<Customer> customers;
    private final ArrayList<Account> accounts;
    private final ArrayList<Transaction> transactions;

    public Bank() {
        this.customers = new ArrayList<>();
        this.accounts = new ArrayList<>();
        this.transactions = new ArrayList<>();
    }

    public Customer createCustomer(String fullName) {
        Customer customer = new Customer(fullName);
        customers.add(customer);
        return customer;
    }

    private String generateAccountNumber() {
        return String.format("ACC-%04d", ++nextAccountNumber);
    }

    public Account openDebitAccount(Customer owner, double initialBalance) {
        DebitAccount account = new DebitAccount(owner, generateAccountNumber(), initialBalance);
        accounts.add(account);
        return account;
    }

    public Account openCreditAccount(Customer owner, double initialBalance, double creditLimit) {
        CreditAccount account = new CreditAccount(owner, generateAccountNumber(), initialBalance, creditLimit);
        accounts.add(account);
        return account;
    }

    public Account findAccount(String accountNumber) {
        for (Account account : accounts) {
            if (Objects.equals(account.getAccountNumber(), accountNumber)) {
                return account;
            }
        }
        return null;
    }

    public boolean deposit(String accountNumber, double amount) {
        Account account = findAccount(accountNumber);
        if (account == null) {
            transactions.add(new Transaction(TransactionType.DEPOSIT, amount,
                            null, accountNumber, false, "Счет не найден"));
            return false;
        }
        if (amount <= 0) {
            transactions.add(new Transaction(TransactionType.DEPOSIT, amount,
                    null, accountNumber, false, "Сумма должна быть больше нуля"));
            return false;
        }
        boolean success = account.deposit(amount);
        if (success) {
            transactions.add(new Transaction(TransactionType.DEPOSIT, amount,
                    null, accountNumber, true, "ОК"));
        } else {
            transactions.add(new Transaction(TransactionType.DEPOSIT, amount,
                    null, accountNumber, false, "Ошибка пополнения"));
        }
        return success;
    }

    public boolean withdraw(String accountNumber, double amount) {
        Account account = findAccount(accountNumber);
        if (account == null) {
            transactions.add(new Transaction(TransactionType.WITHDRAW, amount,
                    accountNumber, null, false, "Счет не найден"));
            return false;
        }
        if (amount <= 0) {
            transactions.add(new Transaction(TransactionType.WITHDRAW, amount,
                    accountNumber, null, false, "Сумма должна быть больше нуля"));
            return false;
        }
        if (amount > account.getBalance()) {
            transactions.add(new Transaction(TransactionType.WITHDRAW, amount,
                    accountNumber, null, false, "Сумма не должна превышать текущий баланс"));
            return false;
        }
        boolean success = account.withdraw(amount);
        if (success) {
            transactions.add(new Transaction(TransactionType.WITHDRAW, amount,
                    accountNumber, null, true, "ОК"));
        }
        else {
            transactions.add(new Transaction(TransactionType.WITHDRAW, amount,
                    accountNumber, null, false, "Ошибка снятия"));
        }
        return success;
    }

    public boolean transfer(String from, String to, double amount) {
        Account accountFrom = findAccount(from);
        Account accountTo = findAccount(to);
        boolean success = accountFrom.transfer(accountTo, amount);
        if (success) {
            transactions.add(new Transaction(TransactionType.TRANSFER, amount,
                    from, to, true, "ОК"));
        }
        else {
            transactions.add(new Transaction(TransactionType.TRANSFER, amount,
                    from, to, false, "Ошибка пополнения"));
        }
        return success;
    }

    public Customer findCustomer(int customerId) {
        for (Customer customer : customers) {
            if (customer.getId() == customerId) {
                return customer;
            }
        }
        return null;
    }

    public void printCustomerAccounts(int customerId) {
        Customer customer = findCustomer(customerId);
        System.out.printf("Счета для клиента %d:%n", customerId);
        for (Account account : accounts) {
            if (account.getOwner() == customer) {
                System.out.printf("Счет %s, баланс %.3f%n", account.getAccountNumber(), account.getBalance());
            }
        }
    }

    public void printTransactions() {
        for (Transaction transaction : transactions) {
            System.out.printf("Тип: %s, сумма: %.3f, отправитель: %s, получатель: %s, " +
                    "время операции: %tF %tT, успех: %s, сообщение: %s%n",
                    transaction.getType(), transaction.getAmount(), transaction.getFromAccountNumber(),
                    transaction.getToAccountNumber(), transaction.getTimestamp(),
                    transaction.getTimestamp(), transaction.isSuccess(), transaction.getMessage());
        }
    }

    public void printReport() {
        long creditCount = accounts.stream().filter(CreditAccount.class::isInstance).count();
        System.out.printf("Кредитных счетов: %d%n", creditCount);
        long debitCount = accounts.stream().filter(DebitAccount.class::isInstance).count();
        System.out.printf("Дебетовых счетов: %d%n", debitCount);

        double creditSum = accounts.stream().filter(CreditAccount.class::isInstance).mapToDouble(Account::getBalance).sum();
        System.out.printf("Кредитные: %.3f%n", creditSum);
        double debitSum = accounts.stream().filter(DebitAccount.class::isInstance).mapToDouble(Account::getBalance).sum();
        System.out.printf("Дебетовые: %.3f%n", debitSum);

        long successCount = transactions.stream().filter(Transaction::isSuccess).count();
        System.out.printf("Успешных операций: %d%n", successCount);
        long failureCount = transactions.stream().filter(t -> !t.isSuccess()).count();
        System.out.printf("Не успешных операций: %d%n", failureCount);
    }
}
