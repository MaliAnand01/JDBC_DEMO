package BankingSystem;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class BankingApp {

    public static void main(String[] args) {
        try {
            DBConnection dbConnection = new MySqlConnection();
            try (Connection connection = dbConnection.getConnection();
                    Scanner scanner = new Scanner(System.in)) {

                User user = new User(connection, scanner);
                Accounts accounts = new Accounts(connection, scanner);
                AccountManager accountManager = new AccountManager(connection, scanner);

                String email;
                long account_number;

                while (true) {
                    System.out.println("*** WELCOME TO BANKING SYSTEM ***");
                    System.out.println();
                    System.out.println("1. Register");
                    System.out.println("2. Login");
                    System.out.println("3. Exit");
                    System.out.print("Enter your choice: ");

                    if (!scanner.hasNextInt()) {
                        System.out.println("Invalid input! Please enter a number.");
                        scanner.next(); // clear invalid input
                        continue;
                    }

                    int choice1 = scanner.nextInt();
                    switch (choice1) {
                        case 1:
                            user.register();
                            break;
                        case 2:
                            email = user.login();
                            if (email != null) {
                                System.out.println("User Logged In!");
                                if (!accounts.account_exist(email)) {
                                    System.out.println();
                                    System.out.println("1. Open a new Bank Account");
                                    System.out.println("2. Exit");
                                    System.out.print("Enter choice: ");
                                    if (scanner.nextInt() == 1) {
                                        account_number = accounts.open_account(email);
                                        System.out.println("Account Created Successfully");
                                        System.out.println("Your account number is: " + account_number);
                                    } else {
                                        break;
                                    }
                                }
                                account_number = accounts.getAccount_number(email);
                                int choice2 = 0;
                            while (choice2 != 6) {
                                System.out.println("\n1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Transaction History");
                                System.out.println("6. Log out");
                                System.out.print("Enter your choice: ");

                                if (!scanner.hasNextInt()) {
                                    System.out.println("Invalid input! Please enter a number.");
                                    scanner.next(); // clear
                                    continue;
                                }

                                choice2 = scanner.nextInt();
                                switch (choice2) {
                                    case 1:
                                        accountManager.debit_money(account_number);
                                        break;
                                    case 2:
                                        accountManager.credit_money(account_number);
                                        break;
                                    case 3:
                                        accountManager.transfer_money(account_number);
                                        break;
                                    case 4:
                                        accountManager.getBalance(account_number);
                                        break;
                                    case 5:
                                        accountManager.getTransactionHistory(account_number);
                                        break;
                                    case 6:
                                        break;
                                    default:
                                        System.out.println("Enter Valid Choice!");
                                        break;
                                }
                            }
                            } else {
                                System.out.println("Incorrect Email or Password!");
                            }
                            break;
                        case 3:
                            System.out.println("THANK YOU FOR USING BANKING SYSTEM!!!");
                            System.out.println("Exiting System!");
                            return;
                        default:
                            System.out.println("Enter Valid Choice");
                            break;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
