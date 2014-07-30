package app;

public class Distance {
	private BusinessPoint point1, point2;
	private double length;
	public Distance(BusinessPoint pt1, BusinessPoint pt2, double len){
		point1 = pt1;
		point2 = pt2;
		length = len;
	}
	public void setPoints(BusinessPoint pt1, BusinessPoint pt2){
		point1 = pt1;
		point2 = pt2;
	}
	public void setPoint1(BusinessPoint pt1){
		point1 = pt1;
	}
	public void setPoint2(BusinessPoint pt2){
		point2 = pt2;
	}
	public BusinessPoint[] getPoints(){
		return new BusinessPoint[]{point1,point2};
	}
	public BusinessPoint getPoint1(){
		return point1;
	}
	public BusinessPoint getPoint2(){
		return point2;
	}
	public double getLength(){
		return length;
	}
	public String toString(){
		String distStr = "point1: " + point1.toString() + "\n";
		distStr += "point2: " + point2.toString() + "\n";
		distStr += "length: " + length;
		return distStr;
	}
}
