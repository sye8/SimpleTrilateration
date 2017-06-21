
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener{
	
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		ServletContext ctx = servletContextEvent.getServletContext();
		
		//initialize DB Connection at startup
    	String dbURL = ctx.getInitParameter("dbURL");
    	String user = ctx.getInitParameter("dbUser");
    	String password = ctx.getInitParameter("dbPassword");
    	
    	try{
    		DBConnector connector = new DBConnector(dbURL, user, password);
    		ctx.setAttribute("DBConnection", connector.getConnection());
			System.out.println("DB Connection initialized.");
    	}catch(ClassNotFoundException e){
    		e.printStackTrace();
    	}catch(SQLException e){
    		e.printStackTrace();
    	}
	}
    	
	//When context is destroyed
	public void contextDestroyed(ServletContextEvent servletContextEvent){
		Connection connect = (Connection) servletContextEvent.getServletContext().getAttribute("DBConnection");
		try {
			connect.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
