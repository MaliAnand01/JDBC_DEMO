package BankingSystem;

import java.sql.Connection;
import java.sql.SQLException;


//Interface for database connectivity. 
public interface DBConnection {
    Connection getConnection() throws SQLException;
}
