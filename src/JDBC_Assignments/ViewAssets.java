package JDBC_Assignments;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class ViewAssets {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/assets_db";
        String user = "root";
        String password = "@nand01SQL";

        String sql = "SELECT id, assetName, purchaseDate, cost FROM assets";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Print Table Header
            System.out.println("ID | Asset Name | Purchase Date | Cost");
            System.out.println("---------------------------------------");

            // Iterate through the result set
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("assetName");
                String date = rs.getString("purchaseDate");
                double cost = rs.getDouble("cost");

                // Print Table Row
                System.out.println(id + " | " + name + " | " + date + " | " + cost);
            }
            System.out.println("---------------------------------------");

        } catch (SQLException e) {
            System.out.println("Error retrieving data!");
            e.printStackTrace();
        }
    }
}