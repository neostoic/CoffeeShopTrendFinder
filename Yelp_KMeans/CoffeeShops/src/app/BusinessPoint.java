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
	public void addDataPoint(DataPoint dp){
		DataPoint[] temp = counts;
		counts = new DataPoint[temp.length+1];
		int ptr = 0;
		for (int i = 0; i < temp.length; i++){
			counts[i] = temp[i];
			ptr = i;
		}
		counts[ptr+1] = dp;
	}
	public boolean hasDataPoint(DataPoint dp){
		String word = dp.getWord();
		for (int i = 0; i < counts.length; i++){
			String currWord = counts[i].getWord();
			if (currWord.equals(word))
				return true;
		}
		return false;
	}
	public boolean hasWord(String word){
		for (int i = 0; i < counts.length; i++){
			String currWord = counts[i].getWord();
			if (currWord.equals(word))
				return true;
		}
		return false;
	}
	public String toString(){
		String bpStr = "| BusinessPoint Name: " + name;
//		bpStr += " DataPoints:";
//		for (int i = 0; i < counts.length; i++){
//			bpStr += " " + counts[i];
//		}
		return bpStr;
	}
}
