package app;

public class DataPoint{
	 private int id;
	 private String word;
	 private int count;
	 private String business;
	 public DataPoint(int i, String w, int c, String b){
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
	 public int getCount(){
		 return count;
	 }
	 public String getBusiness(){
		 return business;
	 }
	 public String toString(){
		 return id + ","  + word + "," + count + "," + business;
	 }
}