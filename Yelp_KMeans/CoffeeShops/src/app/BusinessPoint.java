package app;

public class BusinessPoint {
	private String name;
	private DataPoint[] counts;
	public BusinessPoint(String n, DataPoint[] cnts){
		name = n;
		counts = cnts;
	}
	public DataPoint[] getCounts(){
		return counts;
	}
	public String getName(){
		return name;
	}
	public String toString(){
		String bpStr = "BusinessPoint Name: " + name;
		bpStr += "DataPoints:";
		for (int i = 0; i < counts.length; i++){
			bpStr += " " + counts[i];
		}
		return bpStr;
	}
}
