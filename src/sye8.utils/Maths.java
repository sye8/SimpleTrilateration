package sye8.utils;

//Implemented by Sifan Ye
//Jun.13, 2017
//Math Helper Functions
//findCoord formula credit: TRIANGLE AND CENTROID LOCALIZATION ALGORITHM BASED ON DISTANCE COMPENSATION by Ye Shang, Zhigang Liu, Jinkuan Wang and Xianda Xiao

public class Maths {
	
	//Check if two circles intersect each other
	public static boolean intersect(double[][] positions, double[] distances){
		double x1 = positions[0][0];
		double y1 = positions[0][1];
		double x2 = positions[1][0];
		double y2 = positions[1][1];
		double d1 = distances[0];
		double d2 = distances[1];
		double d = Math.sqrt((Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2)));
		double dSq = d*d;
		return (((Math.pow((d1-d2),2)) <= dSq) && (dSq <= Math.pow((d1+d2),2)));
	}
	
	//Returns the intersecting points of two circles
	public static Coordinate[] findCoord(double[][] positions, double[] distances){
		double ax = positions[0][0];
		double ay = positions[0][1];
		double bx = positions[1][0];
		double by = positions[1][1];
		double ar = distances[0];
		double br = distances[1];
		double p = ax - bx;
		double q = by - ay;
		double t = br*br - ar*ar + ax*ax - bx*bx + ay*ay - by*by;
		double s = (4*p*p*by*by + t*t - 4*p*t*bx + 4*p*p*bx*bx - 4*p*p*br*br);
		if(p == 0){
			double ey = (br*br-ar*ar-ay*ay+by*by)/(2*q);
			double ex1 = (Math.sqrt(br*br-Math.pow(ey-by, 2))+bx);
			double ex2 = (bx-Math.sqrt(br*br-Math.pow(ey-by, 2)));
			Coordinate[] retArr = new Coordinate[2];
			retArr[0] = new Coordinate(ex1, ey);
			retArr[1] = new Coordinate(ex2, ey);
			return retArr;	
		}
		if(q == 0){
			double ex = (br*br-ar*ar-bx*bx+ax*ax)/(2*p);
			double ey1 = Math.sqrt(br*br-Math.pow((ex-bx),2))+by;
			double ey2 = by-Math.sqrt(br*br-Math.pow((ex-bx),2));
			Coordinate[] retArr = new Coordinate[2];
			retArr[0] = new Coordinate(ex, ey1);
			retArr[1] = new Coordinate(ex, ey2);
			return retArr;	
		}
		double ey1 = (1/(p*p+q*q))*(p*q*bx + by*p*p - 0.5*q*t+(0.5*Math.sqrt(Math.pow((q*t-2*by*p*p-2*p*q*bx),2) - s*(p*p+q*q))));
		double ey2 = (1/(p*p+q*q))*(p*q*bx + by*p*p - 0.5*q*t-(0.5*Math.sqrt(Math.pow((q*t-2*by*p*p-2*p*q*bx),2) - s*(p*p+q*q))));
		double ex1 = ((1/(2*p))*(2*ey1*q+br*br-ar*ar+ax*ax-bx*bx+ay*ay-by*by));
		double ex2 = ((1/(2*p))*(2*ey2*q+br*br-ar*ar+ax*ax-bx*bx+ay*ay-by*by));
		Coordinate[] retArr = new Coordinate[2];
		retArr[0] = new Coordinate(ex1, ey1);
		retArr[1] = new Coordinate(ex2, ey2);
		return retArr;	
	}
}
