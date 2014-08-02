package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Scanner;
public class MySQLDatabase {
 // JDBC driver name and database URL
 private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
 private static final String DB_URL = "jdbc:mysql://host214.hostmonster.com/dalisayd_wc";

 //  Database credentials
 private static final String USER = "dalisayd_david";
 private String PASS = "";

 private Connection conn = null;
 
 // To take in password
 private Scanner scan = new Scanner(System.in);

 public MySQLDatabase(){
	 conn = connect();
	 System.out.println("Success");
 }
 
 public MySQLDatabase(String pswd){
	 conn = connect(pswd);
	 System.out.println("Success");
 }
 
 public Connection connect() {
	 Connection connection = null;
	 try{
	    Class.forName("com.mysql.jdbc.Driver");
	   
	    System.out.print("Password: ");
	    PASS = scan.nextLine();
	    System.out.println("Connecting to database...");
	    connection = DriverManager.getConnection(DB_URL,USER,PASS);
	
	 }catch(SQLException se){
	    //Handle errors for JDBC
	    se.printStackTrace();
	 }catch(Exception e){
	    //Handle errors for Class.forName
	    e.printStackTrace();
	 }
	 return connection;
 }//end connect()
 
 public Connection connect(String pswd) {
	 Connection connection = null;
	 try{
	    Class.forName("com.mysql.jdbc.Driver");
	   
	    PASS = pswd;
	    System.out.println("Connecting to database...");
	    connection = DriverManager.getConnection(DB_URL,USER,PASS);
	
	 }catch(SQLException se){
	    //Handle errors for JDBC
	    se.printStackTrace();
	 }catch(Exception e){
	    //Handle errors for Class.forName
	    e.printStackTrace();
	 }
	 return connection;
 }//end connect()

 
 public LinkedList<DataPoint> query(String queryStmt){
	 Statement stmt = null;
	 System.out.println("Creating statement...");
	 LinkedList<DataPoint> result = new LinkedList<DataPoint>();
	 try{
	    stmt = conn.createStatement();
	    ResultSet rs = stmt.executeQuery(queryStmt);

	    while(rs.next()){ 
	       int id  = rs.getInt("id");
	       String word = rs.getString("word");
	       double count = rs.getInt("count");
	       String business = rs.getString("business");

	       //Display values
//	       System.out.print("ID: " + id);
//	       System.out.print(", Word: " + word);
//	       System.out.print(", count: " + count);
//	       System.out.println(", business: " + business);
	       result.add(new DataPoint(id,word,count,business));
	    }
	    rs.close();
	    stmt.close();
	 }catch(Exception e){
		 e.printStackTrace();
	 }
	 return result;
 }
 
 public LinkedList<String> getWords(){
	 Statement stmt = null;
	 System.out.println("Creating statement...");
	 LinkedList<String> allWords = new LinkedList<String>();
	 try{
	    stmt = conn.createStatement();
	    ResultSet rs = stmt.executeQuery("SELECT word FROM Wordcount");
	    while(rs.next()){ 
	       String word = rs.getString("word");
	       allWords.add(word);
	    }
	    rs.close();
	    stmt.close();
	 }catch(Exception e){
		 e.printStackTrace();
	 }
	 return allWords;
 }
 
 public LinkedList<DataPoint> getAllDataPoints(){
	 Statement stmt = null;
	 System.out.println("Creating statement...");
	 LinkedList<DataPoint> allDataPoints = new LinkedList<DataPoint>();
	 try{
	    stmt = conn.createStatement();
	    ResultSet rs = stmt.executeQuery("SELECT * FROM Wordcount");
	    while(rs.next()){ 
	       int id  = rs.getInt("id");
	       String word = rs.getString("word");
	       int count = rs.getInt("count");
	       String business = rs.getString("business");
	       allDataPoints.add(new DataPoint(id,word,count,business));
	    }
	    rs.close();
	    stmt.close();
	 }catch(Exception e){
		 e.printStackTrace();
	 }
	 return allDataPoints;
 }
 
 public void close(){
	 try{
		 conn.close();
	 } catch(Exception e){
		 e.printStackTrace();
	 }
 }
}//end MySQLDatabase

