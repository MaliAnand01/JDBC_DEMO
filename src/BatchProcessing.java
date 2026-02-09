import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class BatchProcessing {

   public static void main(String[] args) throws SQLException {

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
    }catch (ClassNotFoundException e){
//            e.printStackTrace();
        System.out.println(e.getMessage());
    }

    try{
        Connection con = DatabaseConnectivity.getDBConnection();
        String query = "INSERT INTO students(name, age, marks) VALUES(?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(query);
        Scanner sc = new Scanner(System.in);

        while (true){
            System.out.print("Enter name: ");
            String name = sc.next();
            System.out.print("Enter age: ");
            int age = sc.nextInt();
            System.out.print("Enter marks: ");
            double marks = sc.nextDouble();
            System.out.println("Enter more data?(Y/N): ");
            String choice = sc.next();

            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setDouble(3,marks);
            ps.addBatch();

            if (choice.equalsIgnoreCase("N")){
                break;
            }
        }

        int[] arr = ps.executeBatch();
        for (int i : arr) {
            if (i == 0) {
                System.out.println("Query: " +i+ " not executed!!");
            }
        }


    }catch (SQLException e){
        System.out.println(e.getMessage());
    }

   }
}
