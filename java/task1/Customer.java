public class Customer {
    private static int idCounter = 0;

    private final int id;
    private final String fullName;

    public Customer(String fullName) {
        this.id = ++idCounter;
        this.fullName = fullName;
    }

    public int getId() {
        return this.id;
    }

    public String getFullName() {
        return this.fullName;
    }
}
