import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Dashboard extends JFrame {

    ExpenseManager manager = new ExpenseManager();

    JLabel balanceLabel;
    JLabel incomeLabel;
    JLabel expenseLabel;

    JTable table;
    DefaultTableModel model;

    Color purple = new Color(102, 92, 255);
    Color background = new Color(245, 246, 252);

    public Dashboard() {

        manager.loadFromFile();

        setTitle("Personal Expense Tracker");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        getContentPane().setBackground(background);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(purple);
        header.setBorder(
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        );

        JLabel title = new JLabel("Personal Expense Tracker");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 25));

        JLabel subtitle = new JLabel("Manage your money smartly!");
        subtitle.setForeground(Color.WHITE);

        JPanel heading = new JPanel(new GridLayout(2, 1));
        heading.setOpaque(false);
        heading.add(title);
        heading.add(subtitle);

        header.add(heading, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);

        // Main Panel
        JPanel main = new JPanel(new BorderLayout(15, 20));
        main.setBackground(background);
        main.setBorder(
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        );

        // Summary Cards
        JPanel cards = new JPanel(new GridLayout(1, 3, 15, 15));
        cards.setBackground(background);

        balanceLabel = new JLabel("₹0.00");
        incomeLabel = new JLabel("₹0.00");
        expenseLabel = new JLabel("₹0.00");

        cards.add(createCard("Total Balance", balanceLabel));
        cards.add(createCard("Total Income", incomeLabel));
        cards.add(createCard("Total Expenses", expenseLabel));

        main.add(cards, BorderLayout.NORTH);

        // Transaction Table
        String[] columns = {
            "ID", "Type", "Category", "Amount", "Description"
        };

        model = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);

        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 14)
        );

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel historyPanel = new JPanel(new BorderLayout(10, 10));
        historyPanel.setBackground(background);

        JLabel historyTitle = new JLabel("Transaction History");
        historyTitle.setFont(
            new Font("Arial", Font.BOLD, 19)
        );

        historyPanel.add(historyTitle, BorderLayout.NORTH);
        historyPanel.add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttons = new JPanel(
            new FlowLayout(FlowLayout.LEFT, 10, 10)
        );

        buttons.setBackground(background);

        JButton addIncome = createButton("+ Add Income");
        JButton addExpense = createButton("+ Add Expense");
        JButton delete = createButton("Delete");
        JButton edit = createButton("Edit");
        
        addIncome.addActionListener(e -> addTransaction("Income"));

        addExpense.addActionListener(e -> addTransaction("Expense"));

        delete.addActionListener(e -> deleteTransaction());

        edit.addActionListener(e -> editTransaction());

        buttons.add(addIncome);
        buttons.add(addExpense);
        buttons.add(edit);
        buttons.add(delete);

        historyPanel.add(buttons, BorderLayout.SOUTH);

        main.add(historyPanel, BorderLayout.CENTER);

        add(main, BorderLayout.CENTER);

        refreshDashboard();

        setVisible(true);
    }

    // Create Summary Card
    private JPanel createCard(String title, JLabel value) {

        JPanel card = new JPanel(new GridLayout(2, 1, 5, 5));

        card.setBackground(Color.WHITE);

        card.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    new Color(225, 225, 235)
                ),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            )
        );

        JLabel heading = new JLabel(title);
        heading.setForeground(Color.GRAY);
        heading.setFont(new Font("Arial", Font.PLAIN, 14));

        value.setFont(new Font("Arial", Font.BOLD, 23));

        card.add(heading);
        card.add(value);

        return card;
    }

    // Create Buttons
    private JButton createButton(String text) {

        JButton button = new JButton(text);

        button.setBackground(purple);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(
            BorderFactory.createEmptyBorder(12, 18, 12, 18)
        );

        return button;
    }

    // Add Transaction
    private void addTransaction(String type) {

        JTextField category = new JTextField();
        JTextField amount = new JTextField();
        JTextField description = new JTextField();

        Object[] fields = {
            "Category:", category,
            "Amount:", amount,
            "Description:", description
        };

        int result = JOptionPane.showConfirmDialog(
            this,
            fields,
            "Add " + type,
            JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {

            try {

                String cat = category.getText().trim();
                String desc = description.getText().trim();

                double amt = Double.parseDouble(
                    amount.getText().trim()
                );

                if (cat.isEmpty() || desc.isEmpty() ||
                    !Double.isFinite(amt) || amt <= 0) {

                    JOptionPane.showMessageDialog(
                        this,
                        "Enter valid transaction details!"
                    );

                    return;
                }

                manager.addTransaction(
                    type, cat, amt, desc
                );

                refreshDashboard();

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid amount!"
                );
            }
        }
    }

    // Delete Transaction
    private void deleteTransaction() {

        int row = table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                this,
                "Please select a transaction!"
            );

            return;
        }

        int id = Integer.parseInt(
            model.getValueAt(row, 0).toString()
        );

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this transaction?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            manager.deleteTransaction(id);

            refreshDashboard();
        }
    }

    // Edit Transaction
    private void editTransaction() {

        int row = table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                this,
                "Please select a transaction!"
            );

            return;
        }

        int id = Integer.parseInt(
            model.getValueAt(row, 0).toString()
        );

        Transaction selected = null;

        for (Transaction t : manager.getTransactions()) {

            if (t.getId() == id) {
                selected = t;
                break;
            }
        }

        if (selected == null) {
            return;
        }

        JTextField category = new JTextField(
            selected.getCategory()
        );

        JTextField amount = new JTextField(
            String.valueOf(selected.getAmount())
        );

        JTextField description = new JTextField(
            selected.getDescription()
        );

        Object[] fields = {
            "Category:", category,
            "Amount:", amount,
            "Description:", description
        };

        int result = JOptionPane.showConfirmDialog(
            this,
            fields,
            "Edit Transaction",
            JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {

            try {

                String cat = category.getText().trim();
                String desc = description.getText().trim();

                double amt = Double.parseDouble(
                    amount.getText().trim()
                );

                if (cat.isEmpty() || desc.isEmpty() ||
                    !Double.isFinite(amt) || amt <= 0) {

                    JOptionPane.showMessageDialog(
                        this,
                        "Enter valid details!"
                    );

                    return;
                }

                manager.updateTransaction(
                    id, cat, amt, desc
                );

                refreshDashboard();

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                    this,
                    "Enter a valid amount!"
                );
            }
        }
    }

    // Refresh Dashboard
    private void refreshDashboard() {

        model.setRowCount(0);

        List<Transaction> transactions =
            manager.getTransactions();

        for (Transaction t : transactions) {

            model.addRow(new Object[] {
                t.getId(),
                t.getType(),
                t.getCategory(),
                "₹" + t.getAmount(),
                t.getDescription()
            });
        }

        balanceLabel.setText(
            String.format("₹%.2f", manager.getBalance())
        );

        incomeLabel.setText(
            String.format("₹%.2f", manager.getTotalIncome())
        );

        expenseLabel.setText(
            String.format("₹%.2f", manager.getTotalExpense())
        );
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new Dashboard());
    }
}