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
	public CoffeeShopTrendFinder() {
//		Scanner scan = new Scanner(System.in);
//		int k = scan.nextInt();
		MySQLDatabase sql = new MySQLDatabase();
		for (int i = 0; i < businesses.length; i++){
			LinkedList<DataPoint> results = sql.query("SELECT * FROM Wordcount WHERE business='"+businesses[i]+"'");
			for (DataPoint dp : results)
				System.out.println(dp.toString());
		}
	}

	public static void main(String[] args){
		CoffeeShopTrendFinder f = new CoffeeShopTrendFinder();
	}

	public BusinessPoint[] getFurthestPoints(LinkedList<BusinessPoint> allPoints){
		LinkedList<Distance> distancesList = new LinkedList<Distance>();
		for (int i = 0; i < allPoints.size(); i++){
			for (int j = 0; j < allPoints.size(); j++){
				if (allPoints.get(i) == allPoints.get(j))
					continue;
				if (!distExists(allPoints.get(i),allPoints.get(j),distancesList))
					distancesList.add(new Distance(allPoints.get(i), allPoints.get(j), getEucDist(allPoints.get(i).getCounts(),allPoints.get(j).getCounts())));
			}
		}
		for (Distance d : distancesList){
			System.out.println(d.toString());
		}
		return new BusinessPoint[]{};
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
	
	
}
