package com.rule2d.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.Arrays;
import java.util.Random;
import javax.swing.text.html.StyleSheet;

public class Gameboard {	
	private Rule2D game;

	public Gameboard(Rule2D game) {
		this.game = game;
	}	

	public void generateMap(int MAPWIDTH, int MAPHEIGHT) throws Exception {		
		if (Rule2D.mapInitialisationCounter == 1) {
			
			// Load the terrain data into an array to reduce database connections
			DBConnector dbConnector = new DBConnector();
			String[] terrainColors = dbConnector.queryDatabaseReturnArray("SELECT terrainColor FROM Terrain");
			
			for (int longitude = 0; longitude < MAPWIDTH; longitude++) {
				for (int latitude = 0; latitude < MAPHEIGHT; latitude++) {					
					String colorString = terrainColors[randomTerrainType(terrainColors.length)];
					
					Rule2D.mapCoordinatesTerrain[longitude][latitude] = colorString;
					
					// Old test code
					/*if(i % 2 == 0 && j % 2 == 0) {
						g.setColor(Color.RED);
						g.fillRect(i*BLOCKWIDTH, j*BLOCKHEIGHT, BLOCKWIDTH, BLOCKHEIGHT);
					} else if(i % 2 != 0 && j % 2 == 0) {
						g.setColor(Color.BLUE);
						g.fillRect(i*BLOCKWIDTH, j*BLOCKHEIGHT, BLOCKWIDTH, BLOCKHEIGHT);
					} else if(i % 2 == 0 && j % 2 != 0) {
						g.setColor(Color.BLUE);
						g.fillRect(i*BLOCKWIDTH, j*BLOCKHEIGHT, BLOCKWIDTH, BLOCKHEIGHT);
					} else if (i % 2 != 0 && j % 2 != 0) {
						g.setColor(Color.RED);
						g.fillRect(i*BLOCKWIDTH, j*BLOCKHEIGHT, BLOCKWIDTH, BLOCKHEIGHT);
					}	*/		
				}
			}
			
			///////////////////////////////////////////////////////////////////////////////////
			///// This prevents the map creation to be triggered twice by Rule2d.paint!!! /////
			///////////////////////////////////////////////////////////////////////////////////
			Rule2D.mapInitialisationCounter++;			
			
		}
	}
	
	public void paintMap(Graphics2D g2d, int SCREENRESWIDTH, int SCREENRESHEIGHT, int BLOCKWIDTH, int BLOCKHEIGHT) throws Exception {
		///////////////////////////////////////////////////////////////////////////////////
		///// This prevents the map creation to be triggered twice by Rule2d.paint!!! /////
		///////////////////////////////////////////////////////////////////////////////////
		
		// Random map generation and painting using the existing filed types in the database
		if (Rule2D.mapInitialisationCounter > 1) {
			// Paint the map field from the saved array  
			for (int longitude = 0; longitude < SCREENRESWIDTH/BLOCKWIDTH; longitude ++) {
				for (int latitude = 0; latitude < SCREENRESHEIGHT/BLOCKHEIGHT; latitude ++) {
					// Stylesheet allows stringToColor conversion of the color name loaded from the database
					StyleSheet stylesheet = new StyleSheet();
					String colorString = Rule2D.mapCoordinatesTerrain[longitude][latitude];
					Color color = stylesheet.stringToColor(colorString);
					
					g2d.setColor(color);
					g2d.fillRect(longitude*BLOCKWIDTH, latitude*BLOCKHEIGHT, BLOCKWIDTH, BLOCKHEIGHT);
				}
			}
		}		
		
		else if (Rule2D.mapInitialisationCounter < 1) {
			///////////////////////////////////////////////////////////////////////////////////
			///// This prevents the map creation to be triggered twice by Rule2d.paint!!! /////
			///////////////////////////////////////////////////////////////////////////////////
			Rule2D.mapInitialisationCounter++;
		}		
	}
	
	public void paintPlayerPosition(Graphics2D g2d, int intPlayerLongitude, int intPlayerLatitude, int BLOCKWIDTH, int BLOCKHEIGHT) {
		g2d.setColor(Color.WHITE);
		// Make the oval a circle by setting equal x and y values.
		int ovalX = 20;
		int ovalY = 20;
		
		// The player's position is show as a white dot with a diameter of 20px.
		// The dot's coordinates are multiples of the BLOCKWIDTH and BLOCKHEIGHT.
		// The correctional factor "correctFactor" makes sure that the player position is in the middle of the block.
		// The correctional factor for width is half the BLOCKWIDTH plus half the oval's x value.
		// The correctional factor for height is half the BLOCKHEIGHT plus half the oval's y value.
		int correctFactorW = BLOCKWIDTH/2 + ovalX/2;
		int correctFactorH = BLOCKHEIGHT/2 + ovalY/2;
		
		g2d.fillOval((BLOCKWIDTH * intPlayerLongitude) - correctFactorW, (BLOCKHEIGHT * intPlayerLatitude) - correctFactorH, ovalX, ovalY);
		
		// Paint the player direction indicator
		paintPlayerDirection(g2d, intPlayerLongitude, intPlayerLatitude, BLOCKWIDTH, BLOCKHEIGHT);		
	}
	
	public void paintPlayerDirection(Graphics2D g2d, int intPlayerLongitude, int intPlayerLatitude, int BLOCKWIDTH, int BLOCKHEIGHT) {
		// Set the color to deep pink for the moment. TODO: Find a better color for the player direction indicator.
		g2d.setColor(new Color(255, 20, 147));
		
		// The player direction indicator shows north for the time being...
		String characterDirection = Rule2D.characterDirection;
		
		switch(characterDirection) {
			case "N":
				// Define the x and y coordinate values of the triangle end points 
				int xN[] = {intPlayerLongitude*BLOCKWIDTH - 25, intPlayerLongitude*BLOCKWIDTH - 30, intPlayerLongitude*BLOCKWIDTH - 20};
				int yN[] = {intPlayerLatitude*BLOCKHEIGHT - 45, intPlayerLatitude*BLOCKHEIGHT - 35, intPlayerLatitude*BLOCKHEIGHT - 35};
				// Define the number of points in the polygon
				int nN = 3;
				
				// Define the polygon
				Polygon polygonN = new Polygon(xN, yN, nN);				
				g2d.fillPolygon(polygonN);
				break;
			case "NE":
				// Define the x and y coordinate values of the triangle end points 
				int xNE[] = {intPlayerLongitude*BLOCKWIDTH - 10, intPlayerLongitude*BLOCKWIDTH - 22, intPlayerLongitude*BLOCKWIDTH - 15};
				int yNE[] = {intPlayerLatitude*BLOCKHEIGHT - 40, intPlayerLatitude*BLOCKHEIGHT - 35, intPlayerLatitude*BLOCKHEIGHT - 28};
				// Define the number of points in the polygon
				int nNE = 3;
				
				// Define the polygon
				Polygon polygonNE = new Polygon(xNE, yNE, nNE);				
				g2d.fillPolygon(polygonNE);
				break;
			case "E":
				// Define the x and y coordinate values of the triangle end points
				int xE[] = {intPlayerLongitude*BLOCKWIDTH - 5, intPlayerLongitude*BLOCKWIDTH - 15, intPlayerLongitude*BLOCKWIDTH - 15};
				int yE[] = {intPlayerLatitude*BLOCKHEIGHT - 25, intPlayerLatitude*BLOCKHEIGHT -20, intPlayerLatitude*BLOCKHEIGHT - 30};
				int nE = 3;
				
				// Define the polygon
				Polygon polygonE = new Polygon(xE, yE, nE);
				g2d.fillPolygon(polygonE);
				break;
			case "SE":
				// Define the x and y coordinate values of the triangle end points 
				int xSE[] = {intPlayerLongitude*BLOCKWIDTH - 10, intPlayerLongitude*BLOCKWIDTH - 15, intPlayerLongitude*BLOCKWIDTH - 23};
				int ySE[] = {intPlayerLatitude*BLOCKHEIGHT - 10, intPlayerLatitude*BLOCKHEIGHT - 21, intPlayerLatitude*BLOCKHEIGHT - 12};
				// Define the number of points in the polygon
				int nSE = 3;
				
				// Define the polygon
				Polygon polygonSE = new Polygon(xSE, ySE, nSE);				
				g2d.fillPolygon(polygonSE);
				break;
			case "S":
				// Define the x and y coordinate values of the triangle end points 
				int xS[] = {intPlayerLongitude*BLOCKWIDTH - 25, intPlayerLongitude*BLOCKWIDTH - 30, intPlayerLongitude*BLOCKWIDTH - 20};
				int yS[] = {intPlayerLatitude*BLOCKHEIGHT - 5, intPlayerLatitude*BLOCKHEIGHT - 15, intPlayerLatitude*BLOCKHEIGHT - 15};
				// Define the number of points in the polygon
				int nS = 3;
				
				// Define the polygon
				Polygon polygonS = new Polygon(xS, yS, nS);				
				g2d.fillPolygon(polygonS);
				break;
			case "SW":
				// Define the x and y coordinate values of the triangle end points 
				int xSW[] = {intPlayerLongitude*BLOCKWIDTH - 40, intPlayerLongitude*BLOCKWIDTH - 28, intPlayerLongitude*BLOCKWIDTH - 35};
				int ySW[] = {intPlayerLatitude*BLOCKHEIGHT - 10, intPlayerLatitude*BLOCKHEIGHT - 15, intPlayerLatitude*BLOCKHEIGHT - 22};
				// Define the number of points in the polygon
				int nSW = 3;
				
				// Define the polygon
				Polygon polygonSW = new Polygon(xSW, ySW, nSW);				
				g2d.fillPolygon(polygonSW);
				break;
			case "W":
				// Define the x and y coordinate values of the triangle end points
				int xW[] = {intPlayerLongitude*BLOCKWIDTH - 45, intPlayerLongitude*BLOCKWIDTH - 35, intPlayerLongitude*BLOCKWIDTH - 35};
				int yW[] = {intPlayerLatitude*BLOCKHEIGHT - 25, intPlayerLatitude*BLOCKHEIGHT -20, intPlayerLatitude*BLOCKHEIGHT - 30};
				int nW = 3;
				
				// Define the polygon
				Polygon polygonW = new Polygon(xW, yW, nW);
				g2d.fillPolygon(polygonW);
				break;
			case "NW":
				// Define the x and y coordinate values of the triangle end points 
				int xNW[] = {intPlayerLongitude*BLOCKWIDTH - 40, intPlayerLongitude*BLOCKWIDTH - 28, intPlayerLongitude*BLOCKWIDTH - 35};
				int yNW[] = {intPlayerLatitude*BLOCKHEIGHT - 40, intPlayerLatitude*BLOCKHEIGHT - 35, intPlayerLatitude*BLOCKHEIGHT - 28};
				// Define the number of points in the polygon
				int nNW = 3;
				
				// Define the polygon
				Polygon polygonNW = new Polygon(xNW, yNW, nNW);				
				g2d.fillPolygon(polygonNW);
				break;
		}
	}
	
	public int randomTerrainType(int arrayLength) {
		Random rand = new Random();
		
		// Random number from 1 to number of entries in terrain table.
		int number = rand.nextInt(arrayLength);
		
		return number;
	}
	
	public String getTerrainColor(int pkTerrain) throws Exception {
		DBConnector dbConnector = new DBConnector();
		String terrainColor = dbConnector.queryDataBaseReturnString("SELECT terrainColor FROM Terrain WHERE pkTerrain = " + pkTerrain);
				
		return terrainColor;
	}
	
	public void repaint(Graphics2D g2d) {
		
	}
}