import java.sql.*;

public class PreparedStatementDemo {

    private static final String url = "jdbc:mysql://localhost:3306/jdbcDemo";
    private static final String username = "root";
    private static final String password = "@nand01SQL";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
//            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        try{
            Connection con = DriverManager.getConnection(url, username, password);

//            String query = "INSERT INTO students(name, age, marks) VALUES(?, ?, ?)";
            String query = "SELECT marks FROM students WHERE id = ?";

            PreparedStatement ps = con.prepareStatement(query);

            // for insert statement
//            ps.setString(1, "Rakesh");
//            ps.setInt(2, 20);
//            ps.setDouble(3, 80.5);

            //for select statement
//            ps.setInt(1, 3);

            int rows = ps.executeUpdate();
            if(rows > 0){
                System.out.println("Data updated successfully! affected rows "+rows);
            }else {
                System.out.println("Data not updated");
            }

//            ResultSet rs = ps.executeQuery();
//            if (rs.next()){
//                double marks = rs.getDouble("marks");
//                System.out.println("Marks: "+ marks);
//            }else{
//                System.out.println("Marks not found!!");
//            }
//            rs.close();

            ps.close();
            con.close();
        }catch (SQLException e) {
            //throw new RuntimeException(e);
            System.out.println(e.getMessage());
        }
    }
}
