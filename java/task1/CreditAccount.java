public class CreditAccount extends Account{
    private double creditLimit;

    public CreditAccount(Customer owner, String accountNumber, double initialBalance, double creditLimit) {
        super(owner, accountNumber, initialBalance);
        this.creditLimit = creditLimit;
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0)
            return false;
        if (amount > getBalance() + creditLimit)
            return false;
        setBalance(getBalance() - amount);
        return true;
    }

    public double getCreditLimit(){
        return this.creditLimit;
    }
}
