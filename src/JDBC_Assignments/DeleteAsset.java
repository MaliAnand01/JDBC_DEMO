package JDBC_Assignments;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class DeleteAsset {
    public static void main(String[] args) {
        // Database credentials
        String url = "jdbc:mysql://localhost:3306/assets_db";
        String user = "root";
        String password = "@nand01SQL";

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the Asset ID to delete: ");
        int idToDelete = scanner.nextInt();

        // SQL Delete Query
        String sql = "DELETE FROM assets WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Bind the ID to the placeholder
            pstmt.setInt(1, idToDelete);

            // Execute the deletion
            int rowsDeleted = pstmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Asset deleted successfully");
            } else {
                System.out.println("No asset found with ID: " + idToDelete);
            }

        } catch (SQLException e) {
            System.out.println("Error during deletion operation!");
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}