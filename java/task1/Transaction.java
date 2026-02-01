import java.time.LocalDateTime;

public class Transaction {
    private final TransactionType type;
    private double amount;
    private final String fromAccountNumber;
    private final String toAccountNumber;
    private final LocalDateTime timestamp;
    private final boolean success;
    private final String message;

    public Transaction(TransactionType type,
                       double amount,
                       String fromAccountNumber,
                       String toAccountNumber,
                       boolean success,
                       String message) {
        this.type = type;
        this.amount = amount;
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.timestamp = LocalDateTime.now();
        this.success = success;
        this.message = message;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getFromAccountNumber() {
        return fromAccountNumber;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
