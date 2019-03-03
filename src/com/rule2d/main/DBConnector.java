package com.rule2d.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
// import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DBConnector {
	private static Connection connection = null;
	private static Statement statement = null;
	// private static PreparedStatement preparedStatement = null;
	private static ResultSet resultSet = null;	
	
	public String queryDataBaseReturnString(String query) throws Exception {
		String returnString = "";		
		
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			// Setup the connection with the DB
			connection = DriverManager.getConnection("jdbc:mysql://localhost/rule2d?user=root&password=");		
			
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
			
			// Statements allow to issue SQL queries to the DB
			// statement = connection.createStatement();
			// resultSet = statement.executeQuery("SELECT * FROM Terrain");
			
			// Prepared statements can use variables and are more efficient
			// preparedStatement = connection.prepareStatement("INSERT INTO rule2d.Terrain VALUES (default, ?)");
			// preparedStatement.setString(1, "Pudding");
			// preparedStatement.executeUpdate();			
			
			while (resultSet.next()) {
				returnString = resultSet.getString("terrainColor");
				// System.out.println(returnString); // Debugging
			}

		} catch (SQLException e) {
			// Handle any errors
		    System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
		} finally {
			close();
		}
		
		return returnString;
	}
	
	public String[] queryDatabaseReturnArray(String query) throws Exception {
		// We create an ArrayList of type String. An ArrayList can be expanded as opposed to an array.
		// Later, we will convert to ArrayList to an array.
		List<String> resultList = new ArrayList<String>();
		
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			// Setup the connection with the DB
			connection = DriverManager.getConnection("jdbc:mysql://localhost/rule2d?user=root&password=");
			
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
			
			while (resultSet.next()) {
				String string = resultSet.getString("TerrainColor");
				resultList.add(string);
			}
			
		} catch (SQLException e) {
			// Handle any errors
		    System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
		} finally {
			close();
		}
		
		// Convert the ArrayList resultList to an array.
		String[] resultString = resultList.toArray(new String[resultList.size()]);
		
		return resultString;
	}
	
	public void pingDatabase() throws Exception {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			// Setup the connection with the DB
			connection = DriverManager.getConnection("jdbc:mysql://localhost/rule2d?user=root&password=");
			
			Rule2D.databaseAvailable = true;
			
		} catch (SQLException e) {
			// Handle any errors
			System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
		    
		    Rule2D.databaseAvailable = false;
		}
	}

	private static void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}
			
			if (statement != null) {
				statement.close();
			}
			
			if (connection != null) {
				connection.close();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
