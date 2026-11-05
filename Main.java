
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        ExpenseManager manager = new ExpenseManager();

        manager.loadFromFile();

        int choice = 0;

        do {

            System.out.println("\n===== EXPENSE TRACKER =====");

            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. View Transactions");
            System.out.println("4. View Total Income");
            System.out.println("5. View Total Expenses");
            System.out.println("6. View Balance");
            System.out.println("7. Delete Transaction");
            System.out.println("8. Exit");

            System.out.print("\nEnter your choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine());

            } catch (NumberFormatException e) {
                System.out.println("Enter a valid choice!");
                continue;
            }

            switch (choice) {

                case 1:
                case 2:

                    String type = (choice == 1)
                            ? "Income" : "Expense";

                    System.out.print("Enter Category: ");
                    String category = sc.nextLine();

                    System.out.print("Enter Amount: ");

                    double amount;

                    try {
                        amount = Double.parseDouble(sc.nextLine());

                    } catch (NumberFormatException e) {
                        System.out.println("Invalid amount!");
                        break;
                    }

                    System.out.print("Enter Description: ");
                    String description = sc.nextLine();

                    manager.addTransaction(
                            type, category, amount, description
                    );

                    break;

                case 3:

                    manager.viewTransactions();
                    break;

                case 4:

                    System.out.println(
                            "Total Income: ₹" +
                            manager.getTotalIncome()
                    );

                    break;

                case 5:

                    System.out.println(
                            "Total Expenses: ₹" +
                            manager.getTotalExpense()
                    );

                    break;

                case 6:

                    System.out.println(
                            "Available Balance: ₹" +
                            manager.getBalance()
                    );

                    break;

                case 7:

                    System.out.print("Enter Transaction ID: ");

                    try {
                        int id = Integer.parseInt(sc.nextLine());
                        manager.deleteTransaction(id);

                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID!");
                    }

                    break;

                case 8:

                    System.out.println("Thank you for using Expense Tracker!");
                    break;

                default:

                    System.out.println("Invalid choice!");
            }

        } while (choice != 8);

        sc.close();
    }
}

