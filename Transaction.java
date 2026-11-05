
public class Transaction {

    private int id;
    private String type;
    private String category;
    private double amount;
    private String description;

    public Transaction(int id, String type,
                       String category,
                       double amount,
                       String description) {

        this.id = id;
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public void display() {

        System.out.println("-------------------------");

        System.out.println("ID: " + id);
        System.out.println("Type: " + type);
        System.out.println("Category: " + category);
        System.out.println("Amount: ₹" + amount);
        System.out.println("Description: " + description);
    }
}

