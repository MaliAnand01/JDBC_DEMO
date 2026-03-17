package BankingSystem;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AccountManager {
    private final Connection connection;
    private final Scanner scanner;

    AccountManager(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void credit_money(long account_number) throws SQLException {
        scanner.nextLine();
        System.out.print("Enter Amount: ");
        BigDecimal amount = scanner.nextBigDecimal();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);
            if (account_number != 0) {
                String select_query = "SELECT * FROM Accounts WHERE account_number = ? and security_pin = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(select_query)) {
                    preparedStatement.setLong(1, account_number);
                    preparedStatement.setString(2, security_pin);
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            String credit_query = "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?";
                            try (PreparedStatement creditStatement = connection.prepareStatement(credit_query)) {
                                creditStatement.setBigDecimal(1, amount);
                                creditStatement.setLong(2, account_number);
                                int rowsAffected = creditStatement.executeUpdate();
                                if (rowsAffected > 0) {
                                    System.out.println("Rs." + amount + " credited Successfully");
                                    logTransaction(account_number, amount, "CREDIT");
                                    connection.commit();
                                    return;
                                } else {
                                    System.out.println("Transaction Failed!");
                                    connection.rollback();
                                }
                            }
                        } else {
                            System.out.println("Invalid Security Pin!");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error during credit: " + e.getMessage());
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void debit_money(long account_number) throws SQLException {
        scanner.nextLine();
        System.out.print("Enter Amount: ");
        BigDecimal amount = scanner.nextBigDecimal();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();
        try {
            connection.setAutoCommit(false);
            if (account_number != 0) {
                String select_query = "SELECT * FROM Accounts WHERE account_number = ? and security_pin = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(select_query)) {
                    preparedStatement.setLong(1, account_number);
                    preparedStatement.setString(2, security_pin);
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            BigDecimal current_balance = resultSet.getBigDecimal("balance");
                            if (amount.compareTo(current_balance) <= 0) {
                                String debit_query = "UPDATE Accounts SET balance = balance - ? WHERE account_number = ?";
                                try (PreparedStatement debitStatement = connection.prepareStatement(debit_query)) {
                                    debitStatement.setBigDecimal(1, amount);
                                    debitStatement.setLong(2, account_number);
                                    int rowsAffected = debitStatement.executeUpdate();
                                    if (rowsAffected > 0) {
                                        System.out.println("Rs." + amount + " debited Successfully");
                                        logTransaction(account_number, amount, "DEBIT");
                                        connection.commit();
                                        return;
                                    } else {
                                        System.out.println("Transaction Failed!");
                                        connection.rollback();
                                    }
                                }
                            } else {
                                System.out.println("Insufficient Balance!");
                            }
                        } else {
                            System.out.println("Invalid Pin!");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error during debit: " + e.getMessage());
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void transfer_money(long sender_account_number) throws SQLException {
        scanner.nextLine();
        System.out.print("Enter Receiver Account Number: ");
        long receiver_account_number = scanner.nextLong();
        System.out.print("Enter Amount: ");
        BigDecimal amount = scanner.nextBigDecimal();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();
        try {
            connection.setAutoCommit(false);
            if (sender_account_number != 0 && receiver_account_number != 0) {
                String select_query = "SELECT * FROM Accounts WHERE account_number = ? AND security_pin = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(select_query)) {
                    preparedStatement.setLong(1, sender_account_number);
                    preparedStatement.setString(2, security_pin);
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            BigDecimal current_balance = resultSet.getBigDecimal("balance");
                            if (amount.compareTo(current_balance) <= 0) {
                                String debit_query = "UPDATE Accounts SET balance = balance - ? WHERE account_number = ?";
                                String credit_query = "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?";
                                try (PreparedStatement debitStatement = connection.prepareStatement(debit_query);
                                     PreparedStatement creditStatement = connection.prepareStatement(credit_query)) {
                                    debitStatement.setBigDecimal(1, amount);
                                    debitStatement.setLong(2, sender_account_number);
                                    creditStatement.setBigDecimal(1, amount);
                                    creditStatement.setLong(2, receiver_account_number);
                                    int rowsAffected1 = debitStatement.executeUpdate();
                                    int rowsAffected2 = creditStatement.executeUpdate();
                                    if (rowsAffected1 > 0 && rowsAffected2 > 0) {
                                        System.out.println("Transaction Successful!");
                                        System.out.println("Rs." + amount + " Transferred Successfully");
                                        logTransaction(sender_account_number, amount, "TRANSFER (DEBIT)");
                                        logTransaction(receiver_account_number, amount, "TRANSFER (CREDIT)");
                                        connection.commit();
                                        return;
                                    } else {
                                        System.out.println("Transaction Failed");
                                        connection.rollback();
                                    }
                                }
                            } else {
                                System.out.println("Insufficient Balance!");
                            }
                        } else {
                            System.out.println("Invalid Security Pin!");
                        }
                    }
                }
            } else {
                System.out.println("Invalid account number");
            }
        } catch (SQLException e) {
            System.out.println("Error during transfer: " + e.getMessage());
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void getBalance(long account_number) {
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();
        try {
            String query = "SELECT balance FROM Accounts WHERE account_number = ? AND security_pin = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, security_pin);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        BigDecimal balance = resultSet.getBigDecimal("balance");
                        System.out.println("Balance: " + balance);
                    } else {
                        System.out.println("Invalid Pin!");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching balance: " + e.getMessage());
        }
    }

    private void logTransaction(long account_number, BigDecimal amount, String type) throws SQLException {
        String query = "INSERT INTO Transactions(account_number, amount, type) VALUES(?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, account_number);
            preparedStatement.setBigDecimal(2, amount);
            preparedStatement.setString(3, type);
            preparedStatement.executeUpdate();
        }
    }

    public void getTransactionHistory(long account_number) {
        String query = "SELECT amount, type, transaction_date FROM Transactions WHERE account_number = ? ORDER BY transaction_date DESC";
        List<Transaction> transactions = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, account_number);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    BigDecimal amount = resultSet.getBigDecimal("amount");
                    String type = resultSet.getBigDecimal("amount") == null ? "" : resultSet.getString("type");
                    Timestamp date = resultSet.getTimestamp("transaction_date");
                    transactions.add(new Transaction(amount, type, date));
                }
            }
            
            System.out.println("\n--- Transaction History ---");
            if (transactions.isEmpty()) {
                System.out.println("No transactions found.");
            } else {
                for (Transaction t : transactions) {
                    System.out.println(t);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching transactions: " + e.getMessage());
        }
    }
}