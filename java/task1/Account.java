public class Account {
    private final String accountNumber;
    private double balance;
    private final Customer owner;

    public Account(Customer owner, String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
        this.owner = owner;
    }

    public boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        }
        balance += amount;
        return true;
    }

    public boolean withdraw(double amount) {
        if (amount <= 0) {
            return false;
        }
        if (amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }

    public boolean transfer(Account to, double amount) {
        if (this.withdraw(amount)) {
            to.deposit(amount);
            return true;
        }
        return false;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public double getBalance() {
        return this.balance;
    }

    public Customer getOwner() {
        return this.owner;
    }

    protected void setBalance(double balance) {
        this.balance = balance;
    }
}
