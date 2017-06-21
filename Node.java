//Sifan, Ye
//Assumes an iBeacon Node

package sye8.trilaterate;

public class Node {
	
	//Node properties
	public int major;
	public int minor;
	public double accuracy; //In meters
	
	//Coordinates
	public Coordinate coord;
	
	public Node(int major, int minor, double x, double y, double accuracy){
		this.major = major;
		this.minor = minor;
		this.accuracy = accuracy;
		this.coord = new Coordinate(x,y);
	}
	
	public String toString(){
		return "Major: " + major + "Minor: " + minor + "Est. Distance: " + accuracy + "Coordinate: " + coord;
	}
	
	public String coordToString(){
		return "x: " + coord.x + " y: " + coord.y; 
	}
	
}
