package app;

public class DataPoint{
	 private int id;
	 private String word;
	 private double count;
	 private String business;
	 public DataPoint(int i, String w, double c, String b){
		 id = i;
		 word = w;
		 count = c;
		 business = b;
	 }
	 public int getId(){
		 return id;
	 }
	 public String getWord(){
		 return word;
	 }
	 public double getCount(){
		 return count;
	 }
	 public String getBusiness(){
		 return business;
	 }
	 public String toString(){
		 return id + ","  + word + "," + count + "," + business;
	 }
}