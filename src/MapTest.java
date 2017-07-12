import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import javax.swing.JFrame;

import sye8.utils.Coordinate2D;
import sye8.utils.ImageUtils;
import sye8.utils.Graph.Edge;
import sye8.utils.Graph.Graph;
import sye8.utils.Graph.GraphNode;

public class MapTest {

	public static void main(String[] args){
		
		JFrame frame = new JFrame("test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Map<String, GraphNode> vertices = new HashMap<String, GraphNode>();  
	    Map<String, Edge> roads = new HashMap<String, Edge>();
	    Map<String, List<String>> adjList = new HashMap<String, List<String>>();
	    
	    Graph roomMap;		    
	    
		frame.setSize(1400, 1000);
		frame.setVisible(true);
		
		Graphics g = frame.getGraphics();
		Image map = ImageUtils.loadImage("/Users/yesifan/Documents/workspace/Trilateration/Room.jpg");
		g.drawImage(map,0,0,null);
		
		try {
			Scanner in = new Scanner(new File("/Users/yesifan/Documents/workspace/Trilateration/room.txt"));
			while(in.hasNextLine()){
				if(in.next().equals("i")){
					String id = in.next();
					GraphNode tempVertex = new GraphNode(id, in.nextDouble(), in.nextDouble());
					vertices.put(id, tempVertex);
					adjList.put(id, new ArrayList<String>());
				}else{
					String id = in.next();
	    			String intersect1 = in.next();
	    			String intersect2 = in.next();
	    			adjList.get(intersect1).add(intersect2);
	    			adjList.get(intersect2).add(intersect1);
	    			roads.put(id, new Edge(id, vertices.get(intersect1), vertices.get(intersect2)));
				}
			}
			
			in.close();
		} catch (FileNotFoundException e) {
			System.out.println("Map text file not found");
		}	
		
		roomMap = new Graph(vertices, roads, adjList, null);
		
		//Draw Map
	   	Iterator<Entry<String, Edge>> it = roads.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String, Edge> pair = it.next();
			Edge e = ((Edge) pair.getValue());
			int y1 = (int)(e.v.y*100);
	    	int x1 = (int)(e.v.x*100);
	   		int y2 = (int)(e.w.y*100);
	   		int x2 = (int)(e.w.x*100);    	
	    	g.drawLine(x1, y1, x2, y2);    	
		}
		
		Scanner consoleIn = new Scanner(System.in);
		String directions = "YES";
		while(directions.equals("YES")){
			System.out.println("Enter start x: ");
			double x = Double.parseDouble(consoleIn.nextLine());
			System.out.println("Enter start y: ");
			double y = Double.parseDouble(consoleIn.nextLine());
			Coordinate2D selected = new Coordinate2D(x,y);
			
			GraphNode start = roomMap.findStartingPointWithCoordinate(selected);
			
			System.out.println("Enter end point: ");
			GraphNode end = vertices.get(consoleIn.nextLine());
			GraphNode path = end;
			roomMap.dijkstra(start);
			g.setColor(Color.BLUE);
			g.drawLine((int)(x*100), (int)(y*100), (int)(start.x*100), (int)(start.y*100));
			while(path.path != null){
				int pstx = (int)(path.x*100);
				int psty = (int)(path.y*100);
				int pedx = (int)(path.path.x*100);
				int pedy = (int)(path.path.y*100);
				//Iterate through the vertices
				path = path.path;
				g.drawLine(pstx, psty, pedx, pedy);
			}
			g.setColor(Color.GREEN);
			g.fillOval((int)(x*100)-3, (int)(y*100)-3, 6, 6);
			g.setColor(Color.RED);
			g.fillOval((int)(end.x*100)-3, (int)(end.y*100)-3, 6, 6);
			System.out.println("New Directions? (YES or NO)");
			directions = consoleIn.nextLine();
			g.drawImage(map,0,0,null);
			g.setColor(Color.BLACK);
			it = roads.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<String, Edge> pair = it.next();
				Edge e = ((Edge) pair.getValue());
				int y1 = (int)(e.v.y*100);
		    	int x1 = (int)(e.v.x*100);
		   		int y2 = (int)(e.w.y*100);
		   		int x2 = (int)(e.w.x*100);    	
		    	g.drawLine(x1, y1, x2, y2);    	
			}
			roomMap.removeTempNode();
		}	
		consoleIn.close();
		System.exit(0);
	}
	
}
