package app;

public class BusinessPoint {
	private String name;
	private DataPoint[] rating;
	public BusinessPoint(String n, DataPoint[] words){
		name = n;
		rating = words;
	}
	public DataPoint[] getRatings(){
		return rating;
	}
	public String getName(){
		return name;
	}
}
