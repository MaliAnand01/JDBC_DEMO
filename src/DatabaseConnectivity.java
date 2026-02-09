import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public interface DatabaseConnectivity {
      String url = "jdbc:mysql://localhost:3306/jdbcDemo";
      String username = "root";
      String password = "@nand01SQL";

      static Connection getDBConnection() throws SQLException {
          return DriverManager.getConnection(url, username, password);
      }
}
