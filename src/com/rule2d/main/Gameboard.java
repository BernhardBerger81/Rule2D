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
		double zoomFactor = Rule2D.mapZoomFactor;
		int cDIH = Rule2D.characterDirectionIndicatorHeight; // characterDirectionIndicatorHeight cDIH
		
		switch(characterDirection) {
			case "N":
				// x: 25, 30, 20
				// y: 45, 35, 35
				double angleN = 0.0;
				
				// Define the x and y coordinate values of the triangle end points
				int xN[] = {intPlayerLongitude*BLOCKWIDTH - getXSubtrahend(angleN, cDIH*zoomFactor, BLOCKWIDTH/2),
							intPlayerLongitude*BLOCKWIDTH - getXSubtrahend(angleN + 30, cDIH*0.55*zoomFactor, BLOCKWIDTH/2),
							intPlayerLongitude*BLOCKWIDTH - getXSubtrahend(angleN - 30, cDIH*0.55*zoomFactor, BLOCKWIDTH/2)};
				int yN[] = {intPlayerLatitude*BLOCKHEIGHT - getYSubtrahend(angleN, cDIH*zoomFactor, BLOCKWIDTH/2),
							intPlayerLatitude*BLOCKHEIGHT - getYSubtrahend(angleN + 30, cDIH*0.55*zoomFactor, BLOCKWIDTH/2),
							intPlayerLatitude*BLOCKHEIGHT - getYSubtrahend(angleN - 30, cDIH*0.55*zoomFactor, BLOCKWIDTH/2)};
				// Define the number of points in the polygon
				int nN = 3;
				
				// Define the polygon
				Polygon polygonN = new Polygon(xN, yN, nN);				
				g2d.fillPolygon(polygonN);
				break;
			case "NE":
				// x: 11, 22, 15
				// y: 39, 35, 28
				double angleNE = 315.0;
				
				// Define the x and y coordinate values of the triangle end points 
				int xNE[] = {intPlayerLongitude*BLOCKWIDTH - getXSubtrahend(angleNE, cDIH*zoomFactor, BLOCKWIDTH/2),
							 intPlayerLongitude*BLOCKWIDTH - getXSubtrahend(angleNE + 30, cDIH*0.55*zoomFactor, BLOCKWIDTH/2),
							 intPlayerLongitude*BLOCKWIDTH - getXSubtrahend(angleNE - 30, cDIH*0.55*zoomFactor, BLOCKWIDTH/2)};
				int yNE[] = {intPlayerLatitude*BLOCKHEIGHT - getYSubtrahend(angleNE, cDIH*zoomFactor, BLOCKWIDTH/2),
							 intPlayerLatitude*BLOCKHEIGHT - getYSubtrahend(angleNE + 30, cDIH*0.55*zoomFactor, BLOCKWIDTH/2),
							 intPlayerLatitude*BLOCKHEIGHT - getYSubtrahend(angleNE - 30, cDIH*0.55*zoomFactor, BLOCKWIDTH/2)};
				// Define the number of points in the polygon
				int nNE = 3;
				
				// Define the polygon
				Polygon polygonNE = new Polygon(xNE, yNE, nNE);				
				g2d.fillPolygon(polygonNE);
				break;
			case "E":
				// x: 5, 15, 15
				// y: 25, 20, 30
				double angleE = 270.0;
				
				// Define the x and y coordinate values of the triangle end points
				int xE[] = {intPlayerLongitude*BLOCKWIDTH - getXSubtrahend(angleE, cDIH*zoomFactor, BLOCKWIDTH/2),
							intPlayerLongitude*BLOCKWIDTH - getXSubtrahend(angleE + 30, cDIH*0.55*zoomFactor, BLOCKWIDTH/2),
							intPlayerLongitude*BLOCKWIDTH - getXSubtrahend(angleE - 30, cDIH*0.55*zoomFactor, BLOCKWIDTH/2)};
				int yE[] = {intPlayerLatitude*BLOCKHEIGHT - getYSubtrahend(angleE, cDIH*zoomFactor, BLOCKWIDTH/2),
							intPlayerLatitude*BLOCKHEIGHT - getYSubtrahend(angleE + 30, cDIH*0.55*zoomFactor, BLOCKWIDTH/2),
							intPlayerLatitude*BLOCKHEIGHT - getYSubtrahend(angleE - 30, cDIH*0.55*zoomFactor, BLOCKWIDTH/2)};
				int nE = 3;
				
				// Define the polygon
				Polygon polygonE = new Polygon(xE, yE, nE);
				g2d.fillPolygon(polygonE);
				break;
			case "SE":
				// x: 10, 15, 22
				// y: 10, 22, 15
				double angleSE = 225.0;
				
				// Define the x and y coordinate values of the triangle end points 
				int xSE[] = {intPlayerLongitude*BLOCKWIDTH - getXSubtrahend(angleSE, cDIH*zoomFactor, BLOCKWIDTH/2),
							 intPlayerLongitude*BLOCKWIDTH - getXSubtrahend(angleSE + 30, cDIH*0.55*zoomFactor, BLOCKWIDTH/2),
							 intPlayerLongitude*BLOCKWIDTH - getXSubtrahend(angleSE - 30, cDIH*0.55*zoomFactor, BLOCKWIDTH/2)};
				int ySE[] = {intPlayerLatitude*BLOCKHEIGHT - getYSubtrahend(angleSE, cDIH*zoomFactor, BLOCKWIDTH/2),
							 intPlayerLatitude*BLOCKHEIGHT - getYSubtrahend(angleSE + 30, cDIH*0.55*zoomFactor, BLOCKWIDTH/2),
							 intPlayerLatitude*BLOCKHEIGHT - getYSubtrahend(angleSE - 30, cDIH*0.55*zoomFactor, BLOCKWIDTH/2)};
				// Define the number of points in the polygon
				int nSE = 3;
				
				// Define the polygon
				Polygon polygonSE = new Polygon(xSE, ySE, nSE);				
				g2d.fillPolygon(polygonSE);
				break;
			case "S":
				// x: 25, 30, 20
				// y: 5, 15, 15
				double angleS = 180.0;
				
				// Define the x and y coordinate values of the triangle end points 
				int xS[] = {intPlayerLongitude*BLOCKWIDTH - getXSubtrahend(angleS, cDIH*zoomFactor, BLOCKWIDTH/2),
							intPlayerLongitude*BLOCKWIDTH - getXSubtrahend(angleS + 30, cDIH*0.55*zoomFactor, BLOCKWIDTH/2),
							intPlayerLongitude*BLOCKWIDTH - getXSubtrahend(angleS - 30, cDIH*0.55*zoomFactor, BLOCKWIDTH/2)};
				int yS[] = {intPlayerLatitude*BLOCKHEIGHT - getYSubtrahend(angleS, cDIH*zoomFactor, BLOCKWIDTH/2),
							intPlayerLatitude*BLOCKHEIGHT - getYSubtrahend(angleS + 30, cDIH*0.55*zoomFactor, BLOCKWIDTH/2),
							intPlayerLatitude*BLOCKHEIGHT - getYSubtrahend(angleS - 30, cDIH*0.55*zoomFactor, BLOCKWIDTH/2)};
				// Define the number of points in the polygon
				int nS = 3;
				
				// Define the polygon
				Polygon polygonS = new Polygon(xS, yS, nS);				
				g2d.fillPolygon(polygonS);
				break;
			case "SW":
				// x: 40, 28, 35
				// y: 10, 15, 22
				double angleSW = 135.0;
				
				// Define the x and y coordinate values of the triangle end points 
				int xSW[] = {intPlayerLongitude*BLOCKWIDTH - getXSubtrahend(angleSW, cDIH*zoomFactor, BLOCKWIDTH/2),
							 intPlayerLongitude*BLOCKWIDTH - getXSubtrahend(angleSW + 30, cDIH*0.55*zoomFactor, BLOCKWIDTH/2),
							 intPlayerLongitude*BLOCKWIDTH - getXSubtrahend(angleSW - 30, cDIH*0.55*zoomFactor, BLOCKWIDTH/2)};
				int ySW[] = {intPlayerLatitude*BLOCKHEIGHT - getYSubtrahend(angleSW, cDIH*zoomFactor, BLOCKWIDTH/2),
							 intPlayerLatitude*BLOCKHEIGHT - getYSubtrahend(angleSW + 30, cDIH*0.55*zoomFactor, BLOCKWIDTH/2),
							 intPlayerLatitude*BLOCKHEIGHT - getYSubtrahend(angleSW - 30, cDIH*0.55*zoomFactor, BLOCKWIDTH/2)};
				// Define the number of points in the polygon
				int nSW = 3;
				
				// Define the polygon
				Polygon polygonSW = new Polygon(xSW, ySW, nSW);				
				g2d.fillPolygon(polygonSW);
				break;
			case "W":
				// x: 45, 35, 35
				// y: 25, 20, 30
				double angleW = 90.0;
				
				// Define the x and y coordinate values of the triangle end points
				int xW[] = {intPlayerLongitude*BLOCKWIDTH - getXSubtrahend(angleW, cDIH*zoomFactor, BLOCKWIDTH/2),
							intPlayerLongitude*BLOCKWIDTH - getXSubtrahend(angleW + 30, cDIH*0.55*zoomFactor, BLOCKWIDTH/2),
							intPlayerLongitude*BLOCKWIDTH - getXSubtrahend(angleW - 30, cDIH*0.55*zoomFactor, BLOCKWIDTH/2)};
				int yW[] = {intPlayerLatitude*BLOCKHEIGHT - getYSubtrahend(angleW, cDIH*zoomFactor, BLOCKWIDTH/2),
							intPlayerLatitude*BLOCKHEIGHT - getYSubtrahend(angleW + 30, cDIH*0.55*zoomFactor, BLOCKWIDTH/2),
							intPlayerLatitude*BLOCKHEIGHT - getYSubtrahend(angleW - 30, cDIH*0.55*zoomFactor, BLOCKWIDTH/2)};
				int nW = 3;
				
				// Define the polygon
				Polygon polygonW = new Polygon(xW, yW, nW);
				g2d.fillPolygon(polygonW);
				break;
			case "NW":
				// x: 39, 28, 35
				// y: 39, 35, 28
				double angleNW = 45.0;
				
				// Define the x and y coordinate values of the triangle end points 
				int xNW[] = {intPlayerLongitude*BLOCKWIDTH - getXSubtrahend(angleNW, cDIH*zoomFactor, BLOCKWIDTH/2),
							 intPlayerLongitude*BLOCKWIDTH - getXSubtrahend(angleNW + 30, cDIH*0.55*zoomFactor, BLOCKWIDTH/2),
							 intPlayerLongitude*BLOCKWIDTH - getXSubtrahend(angleNW - 30, cDIH*0.55*zoomFactor, BLOCKWIDTH/2)};
				int yNW[] = {intPlayerLatitude*BLOCKHEIGHT - getYSubtrahend(angleNW, cDIH*zoomFactor, BLOCKWIDTH/2),
							 intPlayerLatitude*BLOCKHEIGHT - getYSubtrahend(angleNW + 30, cDIH*0.55*zoomFactor, BLOCKWIDTH/2),
							 intPlayerLatitude*BLOCKHEIGHT - getYSubtrahend(angleNW - 30, cDIH*0.55*zoomFactor, BLOCKWIDTH/2)};
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
	
	public int getXSubtrahend(double angle, double distance, int xCoordinate) {
		// Math as described here: https://classroom.synonym.com/coordinates-distances-angles-2732.html
		// Convert the angle from degrees to radians
		// Math.sin requires the angle in radians, see: https://www.tutorialspoint.com/java/lang/math_sin.htm
		double angleRadians = Math.toRadians(angle);
		double sinAngle = Math.sin(angleRadians);
		// Multiply angleRadians with the distance to the wanted coordinate
		double multiply = sinAngle * distance;
		// Round the value of multiply to the nearest int and add it to the xCoordinate value to obtain the wanted xCoordinate value
		int xSubtrahend = xCoordinate + (int) Math.round(multiply);
				
		return xSubtrahend;
	}
	
	public int getYSubtrahend(double angle, double distance, int yCoordinate) {
		// Math as described here: https://classroom.synonym.com/coordinates-distances-angles-2732.html
		// Convert the angle from degrees to radians
		// Math.cos requires the angle in radians, see: https://www.tutorialspoint.com/java/lang/math_cos.htm
		double angleRadians = Math.toRadians(angle);
		double sinAngle = Math.cos(angleRadians);
		// Multiply angleRadians with the distance to the wanted coordinate
		double multiply = sinAngle * distance;
		// Round the value of multiply to the nearest int and add it to the xCoordinate value to obtain the wanted xCoordinate value
		int ySubtrahend = yCoordinate + (int) Math.round(multiply);
				
		return ySubtrahend;
	}
}