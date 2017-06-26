package sye8.utils;


public class Coordinate {

	public double x;
	public double y;
	
	public Coordinate(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public boolean isNull(){
		return Double.isNaN(this.x) || Double.isNaN(this.y);
	}
	
	public String toString(){
		return String.format("x: %.4f, y: %.4f",x,y);
	}
}
