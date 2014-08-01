package app;

import java.util.LinkedList;
import java.util.Scanner;

public class CoffeeShopTrendFinder {
	private LinkedList<BusinessPoint> businessPoints = new LinkedList<BusinessPoint>();
	private String[] businesses = {
			"Tupelo-coffee-house-and-roasting-sacramento",
			"the-naked-lounge-coffee-house-sacramento-2",
			"The-naked-lounge-coffee-house-sacramento",
			"temple-coffee-roasters-sacramento-3",
			"temple-coffee-roasters-sacramento-2",
			"temple-coffee-roasters-sacramento",
			"shine-sacramento",
			"pachamama-coffee-co-op-sacramento",
			"old-soul-sacramento-5",
			"old-soul-at-the-weatherstone-sacramento-3",
			"old-soul-at-40-acres-sacramento-2",
			"maestro-coffeehouse-sacramento",
			"insight-coffee-roasters-sacramento",
			"fluid-espresso-bar-sacramento",
			"dutch-bros-coffee-sacramento",
			"coffee-works-sacramento",
			"coffee-garden-sacramento",
			"chocolate-fish-coffee-sacramento",
			"chocolate-fish-coffee-roasters-sacramento",
			"a-perfect-cup-sacramento-2"
	};
	private String dbPassword;
	public CoffeeShopTrendFinder() {
		// Create MySQL db instance through JDBC
		
		Scanner scan = new Scanner(System.in);
		dbPassword = scan.nextLine();
		
		MySQLDatabase sql = new MySQLDatabase(dbPassword);
		
		// Populate all BusinessPoints
		for (int i = 0; i < businesses.length; i++){
			LinkedList<DataPoint> results = sql.query("SELECT * FROM Wordcount WHERE business='"+businesses[i]+"'");
			DataPoint[] wordCounts = new DataPoint[results.size()];
			for (int j = 0; j < wordCounts.length; j++)
				wordCounts[j] = results.get(j);
			
			businessPoints.add(new BusinessPoint(businesses[i],wordCounts));
		}
		
		// Close sql instance.
		sql.close();
		
		equalizeVectorLengths(businessPoints);
		getBPLengths(businessPoints);
		
		// Get the two points that are furthest from one another. And get the distance between them.
		Object[] bpts = getFurthestPoints(businessPoints);
		System.out.println(((BusinessPoint)bpts[0]).getName());
		System.out.println(((BusinessPoint)bpts[1]).getName());
		System.out.println("distance: " + bpts[2]);
		
	}

	public static void main(String[] args){
		CoffeeShopTrendFinder f = new CoffeeShopTrendFinder();
	}

	public Object[] getFurthestPoints(LinkedList<BusinessPoint> allPoints){
		LinkedList<Distance> distancesList = new LinkedList<Distance>();
		for (int i = 0; i < allPoints.size(); i++){
			for (int j = 0; j < allPoints.size(); j++){
				if (allPoints.get(i) == allPoints.get(j))
					continue;
				if (!distExists(allPoints.get(i),allPoints.get(j),distancesList)){
					distancesList.add(new Distance(allPoints.get(i), allPoints.get(j), getEucDist(allPoints.get(i).getCounts(),allPoints.get(j).getCounts())));
				}
			}
		}
		// find max distance
		Object[] maxPoints = new Object[3];
		maxPoints[2] = (double)0.0;
		for (Distance d: distancesList){
			if (d.getLength() > (double)maxPoints[2]){
				maxPoints[0] = d.getPoint1();
				maxPoints[1] = d.getPoint2();
				maxPoints[2] = d.getLength();
			}
		}
		
		return maxPoints;
	}
	
	public boolean distExists(BusinessPoint pt1, BusinessPoint pt2, LinkedList<Distance> distances){
//		boolean exists = false;
		for (Distance distance : distances){
			if ((distance.getPoint1().equals(pt1) && distance.getPoint2().equals(pt2)) || 
						(distance.getPoint1().equals(pt2) && distance.getPoint2().equals(pt1)))
			{
				return true;
			}
		}
		return false;
	}
	
	public double[] getWordCountsFromDataPoints(DataPoint[] dpArr){
		double[] countArr = new double[dpArr.length];
		for (int i = 0; i < countArr.length; i++)
			countArr[i] = dpArr[i].getCount();
		return countArr;
	}
	
	public double getEucDist(DataPoint[] dp1, DataPoint[] dp2){
		double[] v1 = getWordCountsFromDataPoints(dp1);
		double[] v2 = getWordCountsFromDataPoints(dp2);
		if (v1.length != v2.length)
			return 0.0;
		double dist = 0.0;
        for (int i = 0; i < v1.length; i++){
        	dist += Math.pow(v1[i]-v2[i],2);
        }
        dist = Math.sqrt(dist);
        return dist;
	}
	
	public int[] map(String[] strArr){
		int[] intArr = new int[strArr.length];
		for (int i = 0; i < strArr.length; i++)
			intArr[i] = Integer.parseInt(strArr[i]);
		return intArr;
	}
	
	// makes all vectors same length
	public void equalizeVectorLengths(LinkedList<BusinessPoint> allPoints){
		// get all words
		MySQLDatabase sql = new MySQLDatabase(dbPassword);
		LinkedList<String> allWords = sql.getWords();
		for (String currWord : allWords){
			for (BusinessPoint currBusPt: allPoints){
				if (!currBusPt.hasWord(currWord)){
					currBusPt.addDataPoint(new DataPoint(999,currWord,0,currBusPt.getName()));
				}
			}
		}
	}
	
	public void getBPLengths(LinkedList<BusinessPoint> allPoints){
		for (BusinessPoint bp : allPoints){
			System.out.println(bp.getName() + ": " + bp.getCounts().length + " words.");
		}
	}
	
	
}
