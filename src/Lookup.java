//Sifan, Ye (sye8)
//Jun. 19, 2017

import java.awt.Color;
import java.awt.Graphics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JFrame;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer.Optimum;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;

import com.google.gson.Gson;

import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;

import sye8.trilaterate.Coordinate;
import sye8.trilaterate.Node;

/**
 * Servlet implementation class Lookup
 */
@WebServlet("/Lookup")
public class Lookup extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	JFrame frame = new JFrame("Debug");
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Lookup() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		StringBuilder stringBuilder = new StringBuilder();
	    BufferedReader reader = request.getReader();
	    try {
	        String line;
	        while ((line = reader.readLine()) != null) {
	        	stringBuilder.append(line).append('\n');
	        }
	    } finally {
	        reader.close();
	    }
	    String json = stringBuilder.toString();
	    Gson gson = new Gson();
	    
	    Node[] nodes = gson.fromJson(json, Node[].class);
	    System.out.println(Arrays.toString(nodes));

		//Connect to DB
		Connection connection = (Connection) getServletContext().getAttribute("DBConnection");
		PreparedStatement[] preparedStates = new PreparedStatement[nodes.length];
		ResultSet[] resultSets = new ResultSet[nodes.length];
		double[][] positions = new double[nodes.length][2];
		double[] distances = new double[nodes.length];
		for(int i = 0; i < nodes.length; i++){
			preparedStates[i] = null;
			resultSets[i] = null;
			try{
				preparedStates[i] = connection.prepareStatement("select major, minor, x, y from Nodes where Major=? and Minor=? limit 1");
				preparedStates[i].setString(1, nodes[i].getMajorStr());
				preparedStates[i].setString(2, nodes[i].getMinorStr());
				resultSets[i] = preparedStates[i].executeQuery();
				if(resultSets[i] != null && resultSets[i].next()){
					positions[i][0] = resultSets[i].getDouble("x");
					positions[i][1] = resultSets[i].getDouble("y");
					distances[i] = nodes[i].accuracy;
				}			
			}catch(SQLException e){
				e.printStackTrace();
				continue;
			}
		}
			
		//Estimate User Location
		NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(positions, distances), new LevenbergMarquardtOptimizer());
		Optimum optimum = solver.solve();
		double[] centroid = optimum.getPoint().toArray();
		
		Coordinate loc = new Coordinate(centroid[0], centroid[1]);
		System.out.println(loc);
				
		//Debug
		frame.setVisible(true);
		frame.setSize(1200, 900);
		
		Graphics g = frame.getGraphics();
		g.setColor(Color.BLACK);
		for(int i = 0; i < nodes.length; i++){
			int x = (int)(positions[i][0]*100);
			int y = (int)(positions[i][1]*100);
			g.fillOval(x-3, y-3, 6, 6);
//			g.drawOval(x-d, y-d, d*2, d*2);
		}
		
		//Send String if loc is not null
		if(!loc.isNull()){
			g.setColor(Color.BLUE);
			g.fillOval((int)(loc.x*100) - 3, (int)(loc.y*100) - 3, 6, 6);
			PrintWriter out = response.getWriter();
			out.print(loc);
			out.close();
			out.flush();
		}					
		doGet(request, response);
	}

}
