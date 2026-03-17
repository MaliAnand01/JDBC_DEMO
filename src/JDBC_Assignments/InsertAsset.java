package JDBC_Assignments;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class InsertAsset {
    public static void main(String[] args) {
        // Database credentials
        String url = "jdbc:mysql://localhost:3306/assets_db";
        String user = "root";
        String password = "@nand01SQL";

        // 1. Get user input
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Asset Name: ");
        String assetName = scanner.nextLine();

        System.out.print("Enter Purchase Date (YYYY-MM-DD): ");
        String purchaseDate = scanner.nextLine();

        System.out.print("Enter Cost: ");
        double cost = scanner.nextDouble();

        // 2. SQL Insert Query with Placeholders (?)
        String sql = "INSERT INTO assets (assetName, purchaseDate, cost) VALUES (?, ?, ?)";

        // 3. Establish connection and execute
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {


            pstmt.setString(1, assetName);
            pstmt.setString(2, purchaseDate);
            pstmt.setDouble(3, cost);

            // Execute the insert
            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Asset added successfully");
            }

        } catch (SQLException e) {
            System.out.println("Error inserting asset!");
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
