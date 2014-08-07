package app;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class CoffeeShopTrendFinder {
	private LinkedList<BusinessPoint> businessPoints = new LinkedList<BusinessPoint>();
	private Cluster[] clusters;
	private Random rand = new Random();
	private String[] businesses = {
			"the-naked-lounge-coffee-house-sacramento-2",
			"the-naked-lounge-coffee-house-sacramento",
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
	private Scanner scan = new Scanner(System.in);
	private String dbPassword;
	private int k = 0;
	public CoffeeShopTrendFinder() {
		// Create MySQL db instance through JDBC
		
		System.out.print("Password: ");
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
		
		// Get the two points that are furthest from one another. And get the distance between them.
		Object[] bpts = getFurthestPoints(businessPoints);
		BusinessPoint furthestBP1 = (BusinessPoint)bpts[0];
		BusinessPoint furthestBP2 = (BusinessPoint)bpts[1];
		
		// Start k-means: find out k (how many clusters)
		System.out.print("How many clusters? k = ");
		while (k < businessPoints.size() && k == 0){
			k = scan.nextInt();
			if (k > businessPoints.size())
				System.out.println("Error: k is greater than the number of businessPoints available.");
		}
		
		// Create clusters
		clusters = new Cluster[k];
		LinkedList<BusinessPoint> bpsAvail = businessPoints; // TODO: try changing this to businssPoints.clone();
		bpsAvail.remove(furthestBP1);
		bpsAvail.remove(furthestBP2);
		clusters[0] = new Cluster("cluster 0", furthestBP1); // Make furthest clusters first.
		clusters[clusters.length-1] = new Cluster("cluster " + (clusters.length-1), furthestBP2);
		for (int i = 1; i < clusters.length-1; i++){
			clusters[i] = new Cluster("cluster "+i, getRandomBusinessPoint(bpsAvail)); // Gets random business points for the clusters that ARE NOT the furthest clusters.
		}
		
		// Perform algorithm
		performKMeans(k, clusters,bpsAvail, businessPoints);
		displayClusters();
	}
	
	public static void main(String[] args){
		CoffeeShopTrendFinder f = new CoffeeShopTrendFinder();
	}
	
	public void performKMeans(int kVal, Cluster[] kClusters, LinkedList<BusinessPoint> nonClusteredPts, LinkedList<BusinessPoint> allBPs){
		/**
		 * PHASE 1: Put non-clustered Business points into their closest clusters
		 */
		for (BusinessPoint bp: nonClusteredPts){
			int count = 0;
			double minDist = 0.0; // closest distance
			Cluster closestCluster = null;
			for (int i = 0; i < kClusters.length; i++){
				double currDist = getEucDist(bp.getCounts(), kClusters[i].getCentroid().getCounts());
				if (count == 0){
					minDist = currDist;
					closestCluster = kClusters[i];
					count++;
				}
				else if (currDist < minDist){
					minDist = currDist;
					closestCluster = kClusters[i];
				}
			} // end of < kClusters.length
			closestCluster.addPoint(bp);
		} // end of bp: nonClustered
		
		/**
		 * PHASE 2: Relocate business points.
		 */
		
		// Iterate loops for relocation.
		// TODO: all of phase 2 should use kclusters[i] and not clusters[i]
		int newDistCount = -1;
		int iterations = 0;
		while (newDistCount != 0 || iterations == 50){
			newDistCount = 0;
			for (BusinessPoint bp : allBPs){
				// newDistCount = 0; MOVING THIS BEFORE THE FOR LOOP
				for (int i = 0; i < clusters.length; i++){
					if (clusters[i].containsPoint(bp))
						continue;
					double newDist = getEucDist(bp.getCounts(),clusters[i].getCentroid().getCounts());
					Cluster currClust = getCorrespondingCluster(bp,clusters);
					double currDist = getEucDist(bp.getCounts(),currClust.getCentroid().getCounts());
					if (newDist < currDist){
						currClust.removePoint(bp);
						clusters[i].addPoint(bp);
						newDistCount++;
					}
				}
			}
			iterations++;
		}
	}
	
	public Cluster getCorrespondingCluster(BusinessPoint bp, Cluster[] clArr){
		for (int i = 0; i < clArr.length; i++){
			if (clArr[i].containsPoint(bp))
				return clArr[i];
		}
		return null;
	}
	
	public BusinessPoint getRandomBusinessPoint(LinkedList<BusinessPoint> bps){
		return bps.remove(rand.nextInt(bps.size()));
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
	
	public void displayClusters(){
		System.out.println("----Clusters----");
		for (int i = 0; i < clusters.length; i++){
			System.out.println(clusters[i].toString());
		} 
		System.out.println("----Clusters----");
	}
	
	public void testEucDist(double[] v1, double[] v2){
		if (v1.length != v2.length)
			return;
		double dist = 0.0;
        for (int i = 0; i < v1.length; i++){
        	dist += Math.pow(v1[i]-v2[i],2);
        }
        dist = Math.sqrt(dist);
        System.out.println("dist="+dist);
	}
	
	
	public void writeToFile(String content){
		Writer writer = null;
		try {
		    writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream("log.txt"), "utf-8"));
		    writer.write(content);
		} catch (IOException ex) {
		  // report
		} finally {
		   try {writer.close();} catch (Exception ex) {}
		}
	}
}
