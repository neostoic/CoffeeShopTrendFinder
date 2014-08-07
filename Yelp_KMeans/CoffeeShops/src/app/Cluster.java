package app;

import java.io.PrintWriter;
import java.util.LinkedList;

public class Cluster {
	private String name;
	private LinkedList<BusinessPoint> clusterList;
	public Cluster(String cName, BusinessPoint firstPoint){
		name = cName;
		clusterList = new LinkedList<BusinessPoint>();
		clusterList.add(firstPoint);
	}
	public String getName(){
		return name;
	}
	public void addPoint(BusinessPoint bp){
		clusterList.add(bp);
	}
	public void removePoint(BusinessPoint bp){
		clusterList.remove(bp);
	}
	public String toString(){
		String clustStr = "";
		clustStr += name;
		clustStr += "(";
		for (BusinessPoint bp : clusterList)
			clustStr += bp.getName() + ",";
		clustStr += ")";
		clustStr += "\n\t";
		for (int i = 0; i < getCentroid().getCounts().length; i++){
			clustStr += getCentroid().getCounts()[i];
		}
		return clustStr;
	}
	public BusinessPoint getCentroid(){
		int wordCountLength = clusterList.get(0).getCounts().length;
		DataPoint[] centroidPoints = new DataPoint[wordCountLength]; // final centroid point array
		LinkedList<DataPoint> centroidPtList = new LinkedList<DataPoint>();
		for (int i = 0; i < wordCountLength; i++){
			int currWordCount = 0;
			String currWordStr = "";
			for (BusinessPoint bp: clusterList){
				// TODO: clusterList only has 1 cluster!!!! BAD
				currWordCount += bp.getCounts()[i].getCount();
				currWordStr = bp.getCounts()[i].getWord();
			}
			// TODO: currWordAvg is doing 1/10336 find out why currWordCount is not summing
			double currWordAvg = (double)currWordCount/(double)wordCountLength;
			centroidPtList.add(new DataPoint(888,currWordStr,currWordAvg,name+" centroid")); // Use 888 to indicate centroid.
		}
		for (int i = 0; i < centroidPtList.size(); i++){
			centroidPoints[i] = centroidPtList.get(i);
		}
		BusinessPoint centroid = new BusinessPoint(name + " centroid",centroidPoints);
		return centroid;
	}

}
