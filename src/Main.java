import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class Main {
    private static final String url = "jdbc:mysql://localhost:3306/jdbcDemo";
    private static final String username = "root";
    private static final String password = "@nand01SQL";

    public static void main(String[] args){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
//            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        try {
            Connection con = DriverManager.getConnection(url, username, password);
            Statement statement = con.createStatement();

//            String query = "select * from students";
            String query = String.format("INSERT INTO students (name, age, marks) VALUES ('%s', %d, %f)", "Kaushik", 22, 97.4);
//            String query = String.format("UPDATE students SET marks = %f WHERE id = %d", 89.5, 2);
//            String query  = "DELETE FROM students WHERE id = 2;";
//            ResultSet rs = statement.executeQuery(query);
                int rows = statement.executeUpdate(query);
//            while (rs.next()){
//                int id = rs.getInt("id");
//                String name = rs.getString("name");
//                int age = rs.getInt("age");
//                double marks = rs.getDouble("marks");
//                System.out.println("ID: "+id);
//                System.out.println("Name: "+name);
//                System.out.println("Age: "+age);
//                System.out.println("Marks: "+marks);
//            }

            if(rows > 0){
//                System.out.println("Data inserted successfully! affected rows "+rows);
                System.out.println("Data updated successfully! affected rows "+rows);
            }else {
//                System.out.println("Data not inserted");
                System.out.println("Data not updated");
            }

//            rs.close();
            statement.close();
            con.close();
        }catch (SQLException e) {
            //throw new RuntimeException(e);
            System.out.println(e.getMessage());
        }
    }
}
