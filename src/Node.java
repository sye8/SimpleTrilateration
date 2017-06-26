//Sifan, Ye
//Assumes an iBeacon Node

public class Node {
	
	//Node properties
	private int major;
   	private int minor;
	public double accuracy; //In meters
	
	public Node(int major, int minor, double accuracy){
		this.major = major;
		this.minor = minor;
		this.accuracy = accuracy;
	}
	
	public String getMajorStr(){
		return Integer.toString(this.major);
	}
	
	public String getMinorStr(){
		return Integer.toString(this.minor);
	}
	
	public String toString(){
		return "Major: " + major + " Minor: " + minor + " Est. Distance: " + accuracy;
	}
}
