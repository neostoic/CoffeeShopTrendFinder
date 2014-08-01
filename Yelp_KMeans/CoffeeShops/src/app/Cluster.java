package app;

import java.util.LinkedList;

public class Cluster {
	private String name;
	private LinkedList<BusinessPoint> clusterList;
	public Cluster(String cName, BusinessPoint firstPoint){
		name = cName;
		clusterList = new LinkedList<BusinessPoint>();
		clusterList.add(firstPoint);
	}
	public void addPoint(BusinessPoint bp){
		clusterList.add(bp);
	}
}
