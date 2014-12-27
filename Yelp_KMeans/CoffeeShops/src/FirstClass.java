//STEP 1. Import required packages
import java.sql.*;
import java.util.Scanner;

public class FirstClass {
   // JDBC driver name and database URL
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   static final String DB_URL = "jdbc:mysql://host214.hostmonster.com/dalisayd_wc";
   
   //  Database credentials
   static final String USER = "dalisayd_david";
   private String PASS = "";
   
   private Scanner scan = new Scanner(System.in);
   public static void main(String[] args) {
   System.out.print("Database pswd:");
   PASS = scan.nextLine();
   Connection conn = null;
   Statement stmt = null;
   try{
      //STEP 2: Register JDBC driver
      Class.forName("com.mysql.jdbc.Driver");

      //STEP 3: Open a connection
      System.out.println("Connecting to database...");
      conn = DriverManager.getConnection(DB_URL,USER,PASS);

      //STEP 4: Execute a query
      System.out.println("Creating statement...");
      stmt = conn.createStatement();
      String sql;
      sql = "SELECT * FROM Wordcount LIMIT 5";
      ResultSet rs = stmt.executeQuery(sql);

      //STEP 5: Extract data from result set
      while(rs.next()){
         //Retrieve by column name
         int id  = rs.getInt("id");
         String word = rs.getString("word");
         int count = rs.getInt("count");
         String business = rs.getString("business");

         //Display values
         System.out.print("ID: " + id);
         System.out.print(", Word: " + word);
         System.out.print(", count: " + count);
         System.out.println(", business: " + business);
      }
      //STEP 6: Clean-up environment
      rs.close();
      stmt.close();
      conn.close();
   }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
      }// nothing we can do
      try{
         if(conn!=null)
            conn.close();
      }catch(SQLException se){
         se.printStackTrace();
      }//end finally try
   }//end try
   System.out.println("Goodbye!");
}//end main
}//end FirstExample
