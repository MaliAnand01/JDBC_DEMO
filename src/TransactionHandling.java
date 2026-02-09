import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TransactionHandling {

    public  static void main(String[] args) throws SQLException{

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        try{
            Connection con = DatabaseConnectivity.getDBConnection();

            con.setAutoCommit(false);

            String debit_query = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
            String credit_query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";

            PreparedStatement debitPs = con.prepareStatement(debit_query);
            PreparedStatement creditPs = con.prepareStatement(credit_query);

            Scanner sc = new Scanner(System.in);
            System.out.println("Enter account number(101,102): ");
            int acc_num = sc.nextInt();

            System.out.println("Enter amount: ");
            double amount = sc.nextDouble();

            debitPs.setDouble(1, amount);
            debitPs.setInt(2, acc_num);

            creditPs.setDouble(1, amount);
            creditPs.setInt(2, 102);

            debitPs.executeUpdate();
            creditPs.executeUpdate();
            if (isSufficient(con, acc_num, amount)){
                con.commit();
                System.out.println("Transaction Successful!");
            }else {
                con.rollback();
                System.out.println("Transaction Failed!!");
            }

            debitPs.close();
            creditPs.close();
            con.close();
            sc.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    static boolean isSufficient(Connection con, int account_number, double amount) throws SQLException {
        try{
            String query = "SELECT balance FROM accounts WHERE account_number = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, account_number);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                double current_bal = rs.getDouble("balance");
                if(amount > current_bal){
                    return false;
                }else {
                    return true;
                }
            }
            rs.close();

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
