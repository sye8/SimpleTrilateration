package sye8.trilaterate;

//Implemented by Sifan Ye
//Jun.13, 2017
//Trilaterate Algorithms
//findCoord and estCoordCorrect formula credit: TRIANGLE AND CENTROID LOCALIZATION ALGORITHM BASED ON DISTANCE COMPENSATION by Ye Shang, Zhigang Liu, Jinkuan Wang and Xianda Xiao
//Weighted coordinate estimate formula credit: The wireless sensor network (WSN) triangle centroid localization algorithm based on RSSI by Chuan Wei ZHANG ,Xin ZHAO

public class Trilaterate {
	
	//Estimate User location with distance correction
	public static Coordinate estCoordCorrect(Node n1, Node n2, Node n3){
		if(intersect2(n1, n2) && intersect2(n2, n3) && intersect2(n3, n1)){
			System.out.println("Three circles intersect");
			return estimateCoord(n1, n2, n3);
		}else if(intersect2(n1, n2)){
			System.out.println("Two circles intersect");
			double d1 = dBetweenCircles(n1, n3) - n1.accuracy;
			double d2 = dBetweenCircles(n2, n3) - n2.accuracy;
			Node nCorr = new Node(n3.major, n3.minor, n3.coord.x, n3.coord.y, Math.max(d1, d2));
			return estimateCoord(n1, n2, nCorr);
		}else if(intersect2(n1, n3)){
			System.out.println("Two circles intersect");
			double d1 = dBetweenCircles(n1, n2) - n1.accuracy;
			double d2 = dBetweenCircles(n2, n3) - n3.accuracy;
			Node nCorr = new Node(n2.major, n2.minor, n2.coord.x, n2.coord.y, Math.max(d1, d2));
			return estimateCoord(n1, nCorr, n3);
		}else if(intersect2(n2, n3)){
			System.out.println("Two circles intersect");
			double d1 = dBetweenCircles(n1, n2) - n2.accuracy;
			double d2 = dBetweenCircles(n1, n3) - n3.accuracy;
			Node nCorr = new Node(n1.major, n1.minor, n1.coord.x, n1.coord.y, Math.max(d1, d2));
			return estimateCoord(nCorr, n2, n3);
		}else{
			System.out.println("No circle intersect");
			return new Coordinate(Double.NaN, Double.NaN);
		}
	}
	
	//Check if two circles intersect each other
	private static boolean intersect2(Node n1, Node n2){
		double d2 = Math.pow(dBetweenCircles(n1, n2),2);
		return (((Math.pow((n1.accuracy - n2.accuracy),2)) <= d2) && (d2 <= Math.pow((n1.accuracy + n2.accuracy),2)));
	}
	
	//Distance between two circles
	private static double dBetweenCircles(Node n1, Node n2){
		return Math.sqrt((Math.pow((n1.coord.x - n2.coord.x), 2) + Math.pow((n1.coord.y - n2.coord.y), 2)));
	}
	
	//Returns an estimate of user coordinate
	private static Coordinate estimateCoord(Node n1, Node n2, Node n3){	
		Coordinate e = findCoord(n1.coord.x, n1.coord.y, n2.coord.x, n2.coord.y, n3.coord.x, n3.coord.y, n1.accuracy, n2.accuracy, n3.accuracy);
		Coordinate f = findCoord(n2.coord.x, n2.coord.y, n3.coord.x, n3.coord.y, n1.coord.x, n1.coord.y, n2.accuracy, n3.accuracy, n1.accuracy);
		Coordinate g = findCoord(n3.coord.x, n3.coord.y, n1.coord.x, n1.coord.y, n2.coord.x, n2.coord.y, n3.accuracy, n1.accuracy, n2.accuracy);
		//Weighted coordinate estimate
		double weight = (1/(n2.accuracy+n3.accuracy) + 1/(n1.accuracy+n3.accuracy) + 1/(n1.accuracy+n2.accuracy));
		double retX = (e.x/(n2.accuracy+n3.accuracy) + f.x/(n1.accuracy+n3.accuracy) + g.x/(n1.accuracy+n2.accuracy))/weight;
		double retY = (e.y/(n2.accuracy+n3.accuracy) + f.y/(n1.accuracy+n3.accuracy) + g.y/(n1.accuracy+n2.accuracy))/weight;
		return new Coordinate (retX, retY);
	}
	
	//Used to find the vertices of the triangle
	private static Coordinate findCoord(double ax, double ay, double bx, double by, double cx, double cy, double ar, double br, double cr){
		double p = cx - bx;
		double q = by - cy;
		double t = br*br - cr*cr + cx*cx - bx*bx + cy*cy - by*by;
		double s = (4*p*p*by*by + t*t - 4*p*t*bx + 4*p*p*bx*bx - 4*p*p*br*br);
		if(p == 0){
			double ey = (br*br-cr*cr-cy*cy+by*by)/(2*q);
			double ex1 = (Math.sqrt(br*br-Math.pow(ey-by, 2))+bx);
			double ex2 = (bx-Math.sqrt(br*br-Math.pow(ey-by, 2)));
			if(Math.sqrt(Math.pow((ex1 - ax), 2)+Math.pow((ey-ay), 2)) <= ar){
				return new Coordinate(ex1,ey);
			}else{
				return new Coordinate(ex2,ey);
			}	
		}
		if(q == 0){
			double ex = (br*br-cr*cr-bx*bx+cx*cx)/(2*p);
			double ey1 = Math.sqrt(br*br-Math.pow((ex-bx),2))+by;
			double ey2 = by-Math.sqrt(br*br-Math.pow((ex-bx),2));
			if(Math.sqrt(Math.pow((ex - ax), 2)+Math.pow((ey1-ay), 2)) <= ar){
				return new Coordinate(ex,ey1);
			}else{
				return new Coordinate(ex,ey2);
			}
		}
		double ey1 = (1/(p*p+q*q))*(p*q*bx + by*p*p - 0.5*q*t+(0.5*Math.sqrt(Math.pow((q*t-2*by*p*p-2*p*q*bx),2) - s*(p*p+q*q))));
		double ey2 = (1/(p*p+q*q))*(p*q*bx + by*p*p - 0.5*q*t-(0.5*Math.sqrt(Math.pow((q*t-2*by*p*p-2*p*q*bx),2) - s*(p*p+q*q))));
		double ex1 = ((1/(2*p))*(2*ey1*q+br*br-cr*cr+cx*cx-bx*bx+cy*cy-by*by));
		double ex2 = ((1/(2*p))*(2*ey2*q+br*br-cr*cr+cx*cx-bx*bx+cy*cy-by*by));
		if(Math.sqrt(Math.pow((ex1 - ax), 2)+Math.pow((ey1-ay), 2)) <= ar){
			return new Coordinate(ex1,ey1);
		}else{
			return new Coordinate(ex2,ey2);
		}
	}
}
