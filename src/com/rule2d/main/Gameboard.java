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
		// Load the terrain data into an array to reduce database connections
		DBConnector dbConnector = new DBConnector();
		String[] terrainColors = dbConnector.queryDatabaseReturnArray("SELECT terrainColor FROM Terrain");
		
		for (int longitude = 0; longitude < MAPWIDTH; longitude++) {
			for (int latitude = 0; latitude < MAPHEIGHT; latitude++) {
				String colorString = terrainColors[randomTerrainType(terrainColors.length)];
				
				Rule2D.mapCoordinatesTerrain[longitude][latitude] = colorString;
				// System.out.println(Rule2D.mapCoordinatesTerrain[longitude][latitude]); // Debugging
				
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
		
		Rule2D.windowStatus = "ShowMap";		
	}
	
	public void paintMap(Graphics2D g2d, int SCREENRESWIDTH, int SCREENRESHEIGHT, int BLOCKWIDTH, int BLOCKHEIGHT) throws Exception {		
		// Map painting using the array created during map creation
		// Paint the map field from the saved array  
		for (int longitude = 0; longitude < SCREENRESWIDTH/BLOCKWIDTH; longitude ++) {
			for (int latitude = 0; latitude < SCREENRESHEIGHT/BLOCKHEIGHT; latitude ++) {
				// Stylesheet allows stringToColor conversion of the color name loaded from the array
				StyleSheet stylesheet = new StyleSheet();
				String colorString = Rule2D.mapCoordinatesTerrain[longitude][latitude];
				Color color = stylesheet.stringToColor(colorString);
				
				g2d.setColor(color);
				g2d.fillRect(longitude*BLOCKWIDTH, latitude*BLOCKHEIGHT, BLOCKWIDTH, BLOCKHEIGHT);
			}
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
				int xN[] = new int[3];
				xN = getXCoordinates(angleN, intPlayerLongitude, BLOCKWIDTH);
				int yN[] = new int[3];
				yN = getYCoordinates(angleN, intPlayerLatitude, BLOCKHEIGHT);
				
				// Define the polygon with 3 points -> triangle
				Polygon polygonN = new Polygon(xN, yN, 3);				
				g2d.fillPolygon(polygonN);
				break;
			case "NE":
				// x: 11, 22, 15
				// y: 39, 35, 28
				double angleNE = 315.0;
				
				// Define the x and y coordinate values of the triangle end points
				int xNE[] = new int[3];
				xNE = getXCoordinates(angleNE, intPlayerLongitude, BLOCKWIDTH);
				int yNE[] = new int[3];
				yNE = getYCoordinates(angleNE, intPlayerLatitude, BLOCKHEIGHT);
				
				// Define the polygon with three points -> triangle
				Polygon polygonNE = new Polygon(xNE, yNE, 3);
				g2d.fillPolygon(polygonNE);
				break;
			case "E":
				// x: 5, 15, 15
				// y: 25, 20, 30
				double angleE = 270.0;
				
				// Define the x and y coordinate values of the triangle end points 
				int xE[] = new int[3];
				xE = getXCoordinates(angleE, intPlayerLongitude, BLOCKWIDTH);
				int yE[] = new int[3];
				yE = getYCoordinates(angleE, intPlayerLatitude, BLOCKHEIGHT);
				
				// Define the polygon with 3 points -> triangle
				Polygon polygonE = new Polygon(xE, yE, 3);
				g2d.fillPolygon(polygonE);
				break;
			case "SE":
				// x: 10, 15, 22
				// y: 10, 22, 15
				double angleSE = 225.0;
				
				// Define the x and y coordinate values of the triangle end points 
				int xSE[] = new int[3];
				xSE = getXCoordinates(angleSE, intPlayerLongitude, BLOCKWIDTH);
				int ySE[] = new int[3];
				ySE = getYCoordinates(angleSE, intPlayerLatitude, BLOCKHEIGHT);
				
				// Define the polygon with 3 points -> triangle
				Polygon polygonSE = new Polygon(xSE, ySE, 3);
				g2d.fillPolygon(polygonSE);
				break;
			case "S":
				// x: 25, 30, 20
				// y: 5, 15, 15
				double angleS = 180.0;
				
				// Define the x and y coordinate values of the triangle end points 
				int xS[] = new int[3];
				xS = getXCoordinates(angleS, intPlayerLongitude, BLOCKWIDTH);
				int yS[] = new int[3];
				yS = getYCoordinates(angleS, intPlayerLatitude, BLOCKHEIGHT);
								
				// Define the polygon with 3 points -> triangle
				Polygon polygonS = new Polygon(xS, yS, 3);
				g2d.fillPolygon(polygonS);
				break;
			case "SW":
				// x: 40, 28, 35
				// y: 10, 15, 22
				double angleSW = 135.0;
				
				// Define the x and y coordinate values of the triangle end points 
				int xSW[] = new int[3];
				xSW = getXCoordinates(angleSW, intPlayerLongitude, BLOCKWIDTH);
				int ySW[] = new int[3];
				ySW = getYCoordinates(angleSW, intPlayerLatitude, BLOCKHEIGHT);
								
				// Define the polygon with 3 points -> triangle
				Polygon polygonSW = new Polygon(xSW, ySW, 3);
				g2d.fillPolygon(polygonSW);
				break;
			case "W":
				// x: 45, 35, 35
				// y: 25, 20, 30
				double angleW = 90.0;
				
				// Define the x and y coordinate values of the triangle end points 
				int xW[] = new int[3];
				xW = getXCoordinates(angleW, intPlayerLongitude, BLOCKWIDTH);
				int yW[] = new int[3];
				yW = getYCoordinates(angleW, intPlayerLatitude, BLOCKHEIGHT);
				
				// Define the polygon with 3 points -> triangle
				Polygon polygonW = new Polygon(xW, yW, 3);
				g2d.fillPolygon(polygonW);
				break;
			case "NW":
				// x: 39, 28, 35
				// y: 39, 35, 28
				double angleNW = 45.0;
				
				// Define the x and y coordinate values of the triangle end points 
				int xNW[] = new int[3];
				xNW = getXCoordinates(angleNW, intPlayerLongitude, BLOCKWIDTH);
				int yNW[] = new int[3];
				yNW = getYCoordinates(angleNW, intPlayerLatitude, BLOCKHEIGHT);
				
				// Define the polygon with 3 points -> triangle
				Polygon polygonNW = new Polygon(xNW, yNW, 3);				
				g2d.fillPolygon(polygonNW);
				break;
		}
	}
	
	private int randomTerrainType(int arrayLength) {
		Random rand = new Random();
		
		// Random number from 1 to number of entries in terrain table.
		int number = rand.nextInt(arrayLength);
		
		return number;
	}
	
	private String getTerrainColor(int pkTerrain) throws Exception {
		DBConnector dbConnector = new DBConnector();
		String terrainColor = dbConnector.queryDataBaseReturnString("SELECT terrainColor FROM Terrain WHERE pkTerrain = " + pkTerrain);
				
		return terrainColor;
	}
	
	// Returns all three x coordinates
	private int[] getXCoordinates(double angle, int intPlayerLongitude, int BLOCKWIDTH) {
		double zoomFactor = Rule2D.mapZoomFactor;
		int cDIH = Rule2D.characterDirectionIndicatorHeight; // characterDirectionIndicatorHeight cDIH		
		int[] returnArray = new int[3];
		
		// The first calculation uses the original cDIH to obtain the x coordinate for the tip of the triangle
		// The second and third calculation use a fraction of the original cDIH to obtain the x coordinates for the sideward tips of the triangle
		for (int i = 0; i < 3; i++) {
			if (i == 0) {
				// Use the original angle
				int xCoordinate = intPlayerLongitude*BLOCKWIDTH - getXSubtrahend(angle, cDIH*zoomFactor, BLOCKWIDTH/2);
				returnArray[0] = xCoordinate;
			} else if (i == 1) {
				// Go 30 degrees to the left
				int xCoordinate = intPlayerLongitude*BLOCKWIDTH - getXSubtrahend(angle + 30, cDIH*0.55*zoomFactor, BLOCKWIDTH/2);
				returnArray[1] = xCoordinate;
			} else if (i == 2) {
				// Go 30 degrees to the right
				int xCoordinate = intPlayerLongitude*BLOCKWIDTH - getXSubtrahend(angle - 30, cDIH*0.55*zoomFactor, BLOCKWIDTH/2);
				returnArray[2] = xCoordinate;
			} else {
				System.out.println("Something went wrong!"); // Debugging
			}
		}
		
		return returnArray;
	}
	
	// Returns all three y coordinates
	private int[] getYCoordinates(double angle, int intPlayerLatitude, int BLOCKHEIGHT) {
		double zoomFactor = Rule2D.mapZoomFactor;
		int cDIH = Rule2D.characterDirectionIndicatorHeight; // characterDirectionIndicatorHeight cDIH		
		int[] returnArray = new int[3];
		
		// The first calculation uses the original cDIH to obtain the y coordinate for the tip of the triangle
		// The second and third calculation use a fraction of the original cDIH to obtain the y coordinates for the sideward tips of the triangle
		for (int i = 0; i < 3; i++) {
			if (i == 0) {
				// Use the original angle
				int yCoordinate = intPlayerLatitude*BLOCKHEIGHT - getYSubtrahend(angle, cDIH*zoomFactor, BLOCKHEIGHT/2);
				returnArray[0] = yCoordinate;
			} else if (i == 1) {
				// Go 30 degrees to the left
				int yCoordinate = intPlayerLatitude*BLOCKHEIGHT - getYSubtrahend(angle + 30, cDIH*0.55*zoomFactor, BLOCKHEIGHT/2);
				returnArray[1] = yCoordinate;
			} else if (i == 2) {
				// Go 30 degrees to the right
				int xCoordinate = intPlayerLatitude*BLOCKHEIGHT - getYSubtrahend(angle - 30, cDIH*0.55*zoomFactor, BLOCKHEIGHT/2);
				returnArray[2] = xCoordinate;
			} else {
				System.out.println("Something went wrong!"); // Debugging
			}
		}
		
		return returnArray;
	}
	
	private int getXSubtrahend(double angle, double distance, int xCoordinate) {
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
	
	private int getYSubtrahend(double angle, double distance, int yCoordinate) {
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
	
	// "Clear" the screen from any Graphics objects by painting a grey rectangle of size SCREENRESWIDTH * SCREENRESHEIGHT 
	public void clearScreen(Graphics2D g2d, int SCREENRESWIDTH, int SCREENRESHEIGHT) {
		g2d.setColor(new Color(220, 220, 220));
		g2d.fillRect(0, 0, SCREENRESWIDTH, SCREENRESHEIGHT);
	}
	
	// TODO: Put in own class "CharacterCreationScreen"
	public void createCharacter(Graphics2D g2d) {
		System.out.println("Inside createCharacter"); // Debugging
		g2d.drawString("This is the character creation screen", 200, 100);
	}
	
	// TODO: Put in own class "MonsterCreationScreen"
	public void createMonster(Graphics2D g2d) {
		System.out.println("Inside createMonster"); // Debugging
		g2d.drawString("This is the monster creation screen", 200, 100);
	}
}