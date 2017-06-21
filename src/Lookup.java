//Sifan, Ye (sye8)
//Jun. 19, 2017

import java.awt.Color;
import java.awt.Graphics;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.swing.JFrame;

import sye8.trilaterate.Coordinate;
import sye8.trilaterate.Node;
import sye8.trilaterate.Trilaterate;

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
		
		//Get data from request
		String major1 = request.getParameter("major1");
		System.out.println(major1);
		String minor1 = request.getParameter("minor1");
		System.out.println(minor1);
		double d1 = Double.parseDouble(request.getParameter("d1"));
		System.out.println(d1);
		String major2 = request.getParameter("major2");
		System.out.println(major2);
		String minor2 = request.getParameter("minor2");
		System.out.println(minor2);
		double d2 = Double.parseDouble(request.getParameter("d2"));
		System.out.println(d2);
		String major3 = request.getParameter("major3");
		System.out.println(major3);
		String minor3 = request.getParameter("minor3");
		System.out.println(minor3);
		double d3 = Double.parseDouble(request.getParameter("d3"));
		System.out.println(d3);
		
		//Connect to DB
		Connection connection = (Connection) getServletContext().getAttribute("DBConnection");
		PreparedStatement prepedState1 = null;
		PreparedStatement prepedState2 = null;
		PreparedStatement prepedState3 = null;
		ResultSet resultSet1 = null;
		ResultSet resultSet2 = null;
		ResultSet resultSet3 = null;
		
		//Try to retrieve data with a query
		try{
			prepedState1 = connection.prepareStatement("select Major, Minor, X, Y from Nodes where Major=? and Minor=? limit 1");
			prepedState1.setString(1, major1);
			prepedState1.setString(2, minor1);
			resultSet1 = prepedState1.executeQuery();
			prepedState2 = connection.prepareStatement("select Major, Minor, X, Y from Nodes where Major=? and Minor=? limit 1");
			prepedState2.setString(1, major2);
			prepedState2.setString(2, minor2);
			resultSet2 = prepedState2.executeQuery();
			prepedState3 = connection.prepareStatement("select Major, Minor, X, Y from Nodes where Major=? and Minor=? limit 1");
			prepedState3.setString(1, major3);
			prepedState3.setString(2, minor3);
			resultSet3 = prepedState3.executeQuery();
			
			if(resultSet1 != null && resultSet1.next() && resultSet2 != null && resultSet2.next() && resultSet3 != null && resultSet3.next()){
				Node n1 = new Node(resultSet1.getInt("Major"), resultSet1.getInt("Minor"), resultSet1.getDouble("X"), resultSet1.getDouble("Y"), d1);
				Node n2 = new Node(resultSet2.getInt("Major"), resultSet2.getInt("Minor"), resultSet2.getDouble("X"), resultSet2.getDouble("Y"), d2);
				Node n3 = new Node(resultSet3.getInt("Major"), resultSet3.getInt("Minor"), resultSet3.getDouble("X"), resultSet3.getDouble("Y"), d3);
				
				//Estimate User Location
				Coordinate loc = Trilaterate.estCoordCorrect(n1, n2, n3);
				System.out.println(n1.coordToString());
				System.out.println(n2.coordToString());
				System.out.println(n3.coordToString());
				
				System.out.println(loc);
				System.out.println();
				
				//Debug
				frame.setVisible(true);
				frame.setSize(900, 900);
				
				Graphics g = frame.getGraphics();
				g.setColor(Color.BLACK);
				g.fillOval((int)(n1.coord.x*100) - 3, (int)(n1.coord.y*100) - 3, 6, 6);
				g.drawOval((int)(n1.coord.x*100) - (int)(d1*100), (int)(n1.coord.y*100) - (int)(d1*100), (int)(d1*2*100), (int)(d1*2*100));
				g.fillOval((int)(n2.coord.x*100) - 3, (int)(n2.coord.y*100) - 3, 6, 6);
				g.drawOval((int)(n2.coord.x*100) - (int)(d2*100), (int)(n2.coord.y*100) - (int)(d2*100), (int)(d2*2*100), (int)(d2*2*100));
				g.fillOval((int)(n3.coord.x*100) - 3, (int)(n3.coord.y*100) - 3, 6, 6);
				g.drawOval((int)(n3.coord.x*100) - (int)(d3*100), (int)(n3.coord.y*100) - (int)(d3*100), (int)(d3*2*100), (int)(d3*2*100));
				//frame.repaint();
				
				//Send String if loc is not null
				if(!loc.isNull()){
					g.setColor(Color.BLUE);
					g.fillOval((int)(loc.x*100) - 3, (int)(loc.y*100) - 3, 6, 6);
					PrintWriter out = response.getWriter();
					out.print(loc);
					out.close();
					out.flush();
				}			
			}
					
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		doGet(request, response);
	}

}
