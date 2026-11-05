
import java.util.*;
import java.io.*;

public class ExpenseManager {

    private ArrayList<Transaction> transactions =
            new ArrayList<>();

    private int nextId = 1;

    private final String FILE_NAME = "transactions.txt";

    // Add Transaction
    public void addTransaction(String type,
                               String category,
                               double amount,
                               String description) {

        if (amount <= 0) {
            System.out.println("Amount must be greater than zero!");
            return;
        }

        Transaction t = new Transaction(
                nextId++, type, category, amount, description
        );

        transactions.add(t);

        saveToFile();

        System.out.println("Transaction added successfully!");
    }

    // View Transactions
    public void viewTransactions() {

        if (transactions.isEmpty()) {
            System.out.println("No transactions found!");
            return;
        }

        for (Transaction t : transactions) {
            t.display();
        }
    }

    // Calculate Income
    public double getTotalIncome() {

        double total = 0;

        for (Transaction t : transactions) {

            if (t.getType().equalsIgnoreCase("Income")) {
                total += t.getAmount();
            }
        }

        return total;
    }

    // Calculate Expenses
    public double getTotalExpense() {

        double total = 0;

        for (Transaction t : transactions) {

            if (t.getType().equalsIgnoreCase("Expense")) {
                total += t.getAmount();
            }
        }

        return total;
    }

    // Calculate Balance
    public double getBalance() {

        return getTotalIncome() - getTotalExpense();
    }

    // Delete Transaction
    public void deleteTransaction(int id) {

        boolean removed = transactions.removeIf(
                t -> t.getId() == id
        );

        if (removed) {
            saveToFile();
            System.out.println("Transaction deleted!");
        } else {
            System.out.println("Transaction not found!");
        }
    }

    // Save Data
    public void saveToFile() {

        try (PrintWriter writer =
             new PrintWriter(new FileWriter(FILE_NAME))) {

            for (Transaction t : transactions) {

                writer.println(
                        t.getId() + "," +
                        t.getType() + "," +
                        t.getCategory() + "," +
                        t.getAmount() + "," +
                        t.getDescription()
                );
            }

        } catch (IOException e) {
            System.out.println("Error saving data!");
        }
    }

    // Load Data
    public void loadFromFile() {

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return;
        }

        try (Scanner reader = new Scanner(file)) {

            while (reader.hasNextLine()) {

                String line = reader.nextLine();

                String[] data = line.split(",", 5);

                if (data.length != 5) {
                    continue;
                }

                try {

                    int id = Integer.parseInt(data[0]);
                    String type = data[1];
                    String category = data[2];
                    double amount = Double.parseDouble(data[3]);
                    String description = data[4];

                    if (id <= 0 || amount <= 0 ||
                        (!type.equals("Income") &&
                         !type.equals("Expense"))) {
                        continue;
                    }

                    transactions.add(
                            new Transaction(
                                    id, type, category,
                                    amount, description
                            )
                    );

                    nextId = Math.max(nextId, id + 1);

                } catch (NumberFormatException e) {
                    System.out.println("Invalid record skipped!");
                }
            }

        } catch (IOException e) {
            System.out.println("Error loading data!");
        }
    }
}
