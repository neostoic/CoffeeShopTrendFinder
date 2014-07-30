package app;

import java.util.LinkedList;

public class Driver {
	public static void main(String[] args){
		MySQLDatabase mysql = new MySQLDatabase();
		LinkedList<DataPoint> res = mysql.query("select * from Wordcount where id = 4");
		for (int i = 0; i < res.size(); i++){
			System.out.println(res.toString());
		}
		mysql.close();
	}
}
