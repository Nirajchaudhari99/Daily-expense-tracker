import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class Dailyexpensetracker {

    // Class to store details of individual expenses
    static class Expense {
        private final double amount;
        private final String category;
        private final String description;
        private final LocalDate date;

        public Expense(double amount, String category, String description, LocalDate date) {
            this.amount = amount;
            this.category = category;
            this.description = description;
            this.date = date;
        }

        public double getAmount() {
            return amount;
        }

        public LocalDate getDate() {
            return date;
        }

        @Override
        public String toString() {
            return amount + "," + category + "," + description + "," + date;
        }

        public static Expense fromString(String line) {
            String[] parts = line.split(",");
            return new Expense(
                Double.parseDouble(parts[0]),
                parts[1],
                parts[2],
                LocalDate.parse(parts[3])
            );
        }
    }

    // Class to manage all expenses
    static class ExpenseManager {
        private final List<Expense> expenses = new ArrayList<>();
        private final String FILE_NAME = "expenses.txt";

        public ExpenseManager() {
            loadExpenses();
        }

        // Add a new expense
        public void addExpense(double amount, String category, String description) {
            Expense expense = new Expense(amount, category, description, LocalDate.now());
            expenses.add(expense);
            saveExpenses();
        }

        // View all expenses
        public void viewAllExpenses() {
            if (expenses.isEmpty()) {
                System.out.println("No expenses recorded.");
            } else {
                for (Expense expense : expenses) {
                    System.out.println(expense);
                }
            }
        }

        // View summary by time period
        public void viewSummary(String period) {
            LocalDate now = LocalDate.now();
            double total = 0;

            for (Expense expense : expenses) {
                boolean include = false;
                if (period.equalsIgnoreCase("day") && expense.getDate().isEqual(now)) {
                    include = true;
                } else if (period.equalsIgnoreCase("week") && expense.getDate().isAfter(now.minusDays(7))) {
                    include = true;
                } else if (period.equalsIgnoreCase("month") && expense.getDate().isAfter(now.minusMonths(1))) {
                    include = true;
                }

                if (include) {
                    total += expense.getAmount();
                }
            }

            System.out.printf("Total expenses for the %s: %.2f%n", period, total);
        }

        // Save expenses to a file
        private void saveExpenses() {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
                for (Expense expense : expenses) {
                    writer.write(expense.toString());
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error saving expenses: " + e.getMessage());
            }
        }

        // Load expenses from a file
        private void loadExpenses() {

            try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    expenses.add(Expense.fromString(line));
                }
            } catch (IOException e) {
                System.out.println("Error loading expenses: " + e.getMessage());
            }
        }
    }

    // Main application logic
    public static void main(String[] args) {
        ExpenseManager manager = new ExpenseManager();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nDaily Expense Tracker Menu:");
            System.out.println("1. Add Expense");
            System.out.println("2. View All Expenses");
            System.out.println("3. View Daily Summary");
            System.out.println("4. View Weekly Summary");
            System.out.println("5. View Monthly Summary");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter amount: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter category: ");
                    String category = scanner.nextLine();
                    System.out.print("Enter description: ");
                    String description = scanner.nextLine();
                    manager.addExpense(amount, category, description);
                    System.out.println("Expense added successfully!");
                }
                case 2 -> manager.viewAllExpenses();
                case 3 -> manager.viewSummary("day");
                case 4 -> manager.viewSummary("week");
                case 5 -> manager.viewSummary("month");
                case 6 -> {
                    System.out.println("Exiting Daily Expense Tracker. Goodbye!");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}


