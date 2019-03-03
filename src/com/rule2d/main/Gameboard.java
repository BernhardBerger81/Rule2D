package com.rule2d.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
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
		Rule2D.frame.repaint();
	}
	
	public void preparePaintMap(Graphics2D g2d, int intPlayerLongitude, int intPlayerLatitude, int MAPDISPLAYWIDTH, int MAPDISPLAYHEIGHT, 
			int BLOCKWIDTH,	int BLOCKHEIGHT) throws Exception {
		// longitudeStart is the start coordinate of painting longitude map values on the screen
		int longitudeStart = intPlayerLongitude - Rule2D.MAPDISPLAYWIDTHBLOCKS/2;
		// latitudeStart is the start coordinate of painting latitude map values on the screen
		int latitudeStart = intPlayerLatitude - Rule2D.MAPDISPLAYHEIGHTBLOCKS/2;
		int longitudeHolder = longitudeStart;
		int latitudeHolder = latitudeStart;
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///// Paint the whole map if no "border" is crossed, neither to the West or the East:                            /////
		///// Paint the whole map in one go if longitudeStart >= 0 AND longitudeStart + MAPDISPLAYWIDTHBLOCKS < MAPWIDTH /////
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		if (longitudeStart >= 0 && longitudeStart + Rule2D.MAPDISPLAYWIDTHBLOCKS < Rule2D.MAPWIDTH) {			
			// Special case: In the North of the map
			// The map has to stay still if latitudeStart is < MAPDISPLAYHEIGHT/2
			if (intPlayerLatitude < Rule2D.MAPDISPLAYHEIGHTBLOCKS/2) {
				latitudeStart = 0;
			}
			
			// Special case: In the South of the map
			// The map has to stay still if less than half a screen of blocks are left in latitude
			else if (intPlayerLatitude + Rule2D.MAPDISPLAYHEIGHTBLOCKS/2 >= Rule2D.MAPHEIGHT) {
				int arrayLength = MAPDISPLAYHEIGHT/BLOCKHEIGHT/2 + 1;
				int intArray[] = new int[arrayLength];
				for (int i = 0; i < arrayLength; i++) {
					intArray[i] = arrayLength - i;
				}
				
				// Set the latitudeStart for painting the map so that the map is drawn in it's full height even if 
				// the player position indicator is near the edge of the map
				if (intArray[Rule2D.MAPHEIGHT - intPlayerLatitude] >= 1) {
					latitudeStart = latitudeStart - intArray[Rule2D.MAPHEIGHT - intPlayerLatitude];
					latitudeHolder = latitudeStart;
				}
			} 
			// Normal case: In the middle of the map
			else {
				// Nothing needs to happen here
			}
			
			// We have determined the values of longitudeStart and latitudeStart. Now it's time to paint the map
			// Paint left-to-right (longitude) and top-to-bottom (latitude) in preparation for isometric display
			for (int latitudeCounter = 0; latitudeCounter < Rule2D.MAPDISPLAYHEIGHTBLOCKS; latitudeCounter++) {
				for (int longitudeCounter = 0; longitudeCounter < Rule2D.MAPDISPLAYWIDTHBLOCKS; longitudeCounter++) {
					paintMap(g2d, longitudeStart, latitudeStart, longitudeCounter, latitudeCounter, BLOCKWIDTH, BLOCKHEIGHT);
					
					longitudeStart++;
				}
				latitudeStart++;
				
				longitudeStart = longitudeHolder;
			}
		} else if (longitudeStart <= -1) {
			///////////////////////////////////////////////////////////////////////////
			///// Paint the map in two parts if the border is crossed to the West /////
			///// Paint the map in two parts if longitudeStart <= -1              /////
			///////////////////////////////////////////////////////////////////////////
			
			// Special case: In the North of the map
			if (intPlayerLatitude < Rule2D.MAPDISPLAYHEIGHTBLOCKS/2) {
				// System.out.println("Special case North, else if part"); // Debugging
				latitudeStart = 0;
			}
			
			// Special case: In the South of the map
			// The map has to stay still if less than half a screen of blocks are left in latitude
			else if (intPlayerLatitude + Rule2D.MAPDISPLAYHEIGHTBLOCKS/2 >= Rule2D.MAPHEIGHT) {
				int arrayLength = MAPDISPLAYHEIGHT/BLOCKHEIGHT/2 + 1;
				int intArray[] = new int[arrayLength];
				for (int i = 0; i < arrayLength; i++) {
					intArray[i] = arrayLength - i;
				}
				
				// Set the latitudeStart for painting the map so that the map is drawn in it's full height even if 
				// the player position indicator is near the edge of the map
				if (intArray[Rule2D.MAPHEIGHT - intPlayerLatitude] >= 1) {
					latitudeStart = latitudeStart - intArray[Rule2D.MAPHEIGHT - intPlayerLatitude];
					latitudeHolder = latitudeStart;
				}
			} else {
				// Nothing needs to happen here
			}
			
			// Normal case: In the middle of the map
			// Paint the Western half of the map			
			longitudeStart = longitudeStart + Rule2D.MAPWIDTH;
			
			for (int latitudeCounter = 0; latitudeCounter < Rule2D.MAPDISPLAYHEIGHTBLOCKS; latitudeCounter++) {
				for (int longitudeCounter = 0; longitudeCounter < Math.abs(longitudeHolder); longitudeCounter++) {
					paintMap(g2d, longitudeStart, latitudeStart, longitudeCounter, latitudeCounter, BLOCKWIDTH, BLOCKHEIGHT);
					
					longitudeStart++;
				}
				latitudeStart++;
				
				longitudeStart = longitudeHolder + Rule2D.MAPWIDTH;
			}
			
			// Paint the Eastern half of the map
			// Because the value of latitudeStart was already used for painting the first part of the map,
			// we need to consider the special circumstances again
			longitudeStart = 0;
			
			// Special case: In the North of the map
			if (intPlayerLatitude < Rule2D.MAPDISPLAYHEIGHTBLOCKS/2) {
				// System.out.println("Special case North, else if part"); // Debugging
				latitudeStart = 0;
			}
			
			// Special case: In the South of the map
			// The map has to stay still if less than half a screen of blocks are left in latitude
			else if (intPlayerLatitude + Rule2D.MAPDISPLAYHEIGHTBLOCKS/2 >= Rule2D.MAPHEIGHT) {
				// Reset the latitude because we already used it to paint the Eastern part of the map
				latitudeStart = latitudeHolder;
			} else {
				// Reset the latitude because we already used it to paint the Eastern part of the map
				latitudeStart = latitudeHolder;
			}
			
			for (int latitudeCounter = 0; latitudeCounter < Rule2D.MAPDISPLAYHEIGHTBLOCKS; latitudeCounter++) {
				for (int longitudeCounter = Math.abs(longitudeHolder); longitudeCounter < Rule2D.MAPDISPLAYWIDTHBLOCKS; longitudeCounter++) {
					paintMap(g2d, longitudeStart, latitudeStart, longitudeCounter, latitudeCounter, BLOCKWIDTH, BLOCKHEIGHT);

					longitudeStart++;
				}
				latitudeStart++;
				
				longitudeStart = 0;
			}
		} else {
			///////////////////////////////////////////////////////////////////////////
			///// Paint the map in two parts if the border is crossed to the East /////
			///////////////////////////////////////////////////////////////////////////
			
			// Special case: In the North of the map
			if (intPlayerLatitude < Rule2D.MAPDISPLAYHEIGHTBLOCKS/2) {
				latitudeStart = 0;
			}
			
			// Special case: In the South of the map
			// The map has to stay still if less than half a screen of blocks are left in latitude
			if (intPlayerLatitude + Rule2D.MAPDISPLAYHEIGHTBLOCKS/2 >= Rule2D.MAPHEIGHT) {
				int arrayLength = MAPDISPLAYHEIGHT/BLOCKHEIGHT/2 + 1;
				int intArray[] = new int[arrayLength];
				for (int i = 0; i < arrayLength; i++) {
					intArray[i] = arrayLength - i;
				}
				
				// Set the latitudeStart for painting the map so that the map is drawn in it's full height even if 
				// the player position indicator is near the edge of the map
				if (intArray[Rule2D.MAPHEIGHT - intPlayerLatitude] >= 1) {
					latitudeStart = latitudeStart - intArray[Rule2D.MAPHEIGHT - intPlayerLatitude];
					latitudeHolder = latitudeStart;
				}
			}
			
			// Normal case: In the middle of the map
			// Paint the Western half of the map
			for (int latitudeCounter = 0; latitudeCounter < Rule2D.MAPDISPLAYHEIGHTBLOCKS; latitudeCounter++) {
				for (int longitudeCounter = 0; longitudeStart < Rule2D.MAPWIDTH; longitudeCounter++) {
					paintMap(g2d, longitudeStart, latitudeStart, longitudeCounter, latitudeCounter, BLOCKWIDTH, BLOCKHEIGHT);

					longitudeStart++;
				}
				latitudeStart++;
				
				longitudeStart = longitudeHolder;
			}
			
			// Paint the Eastern half of the map
			// Because the value of latitudeStart was already used for painting the first part of the map,
			// we need to consider the special circumstances again
			longitudeStart = 0;

			// Special case: In the North of the map
			if (intPlayerLatitude < Rule2D.MAPDISPLAYHEIGHTBLOCKS/2) {
				latitudeStart = 0;
			}
			
			// Special case: In the South of the map
			// The map has to stay still if less than half a screen of blocks are left in latitude
			else if (intPlayerLatitude + Rule2D.MAPDISPLAYHEIGHTBLOCKS/2 >= Rule2D.MAPHEIGHT) {
				// Reset the latitude because we already used it to paint the Eastern part of the map
				latitudeStart = latitudeHolder;
			} else {
				// Reset the latitude because we already used it to paint the Eastern part of the map
				latitudeStart = latitudeHolder;
			}			
			
			for (int latitudeCounter = 0; latitudeCounter < Rule2D.MAPDISPLAYHEIGHTBLOCKS; latitudeCounter++) {
				for (int longitudeCounter = Rule2D.MAPWIDTH - longitudeHolder; longitudeCounter < Rule2D.MAPDISPLAYWIDTHBLOCKS; longitudeCounter++) {
					paintMap(g2d, longitudeStart, latitudeStart, longitudeCounter, latitudeCounter, BLOCKWIDTH, BLOCKHEIGHT);

					longitudeStart++;
				}
				latitudeStart++;
				
				longitudeStart = 0;
			}
		}
	}
	
	public void paintMap(Graphics2D g2d, int longitudeStart, int latitudeStart, int longitudeCounter, int latitudeCounter, 
			int BLOCKWIDTH, int BLOCKHEIGHT) {
		// Stylesheet allows stringToColor conversion of the color name loaded from the array
		StyleSheet stylesheet = new StyleSheet();
		
		String colorString = Rule2D.mapCoordinatesTerrain[longitudeStart][latitudeStart];
		Color color = stylesheet.stringToColor(colorString);
		g2d.setColor(color);					

		g2d.fillRect(longitudeCounter*BLOCKWIDTH, latitudeCounter*BLOCKHEIGHT + 200, BLOCKWIDTH, BLOCKHEIGHT);
		g2d.setColor(Color.PINK); // Debugging
		g2d.drawString(longitudeStart + ", " + latitudeStart, longitudeCounter*BLOCKWIDTH + 10, latitudeCounter*BLOCKHEIGHT + 230); // Debugging
	}
	
	public void paintPlayerPosition(Graphics2D g2d, int intPlayerLongitude, int intPlayerLatitude, int BLOCKWIDTH, int BLOCKHEIGHT,
			int MAPDISPLAYWIDTH, int MAPDISPLAYHEIGHT, int MAPHEIGHT) {
		g2d.setColor(Color.WHITE);
		// Make the oval a circle by setting equal x and y values.
		int ovalX = 20;
		int ovalY = 20;
		
		// The player's position is shown as a white dot with a diameter of 20px.
		// The dot's coordinates are multiples of the BLOCKWIDTH and BLOCKHEIGHT.
		// The correctional factor "correctFactor" makes sure that the player position is in the middle of the block.
		// The correctional factor for width is half the BLOCKWIDTH plus half the oval's x value.
		// The correctional factor for height is half the BLOCKHEIGHT plus half the oval's y value.
		int correctFactorW = BLOCKWIDTH/2 + ovalX/2;
		int correctFactorH = BLOCKHEIGHT/2 + ovalY/2;
		
		// The player position moves in the North half of the map if intPlayerLatitude <= MAPDISPLAYHEIGHT/BLOCKHEIGHT/2
		if (intPlayerLatitude < MAPDISPLAYHEIGHT/BLOCKHEIGHT/2) {
			// Special case multiplication by zero (0)
			if (intPlayerLatitude == 0) {
				g2d.fillOval((BLOCKWIDTH * MAPDISPLAYWIDTH/BLOCKWIDTH/2) - correctFactorW + BLOCKWIDTH/2,
						 (BLOCKHEIGHT + 200) - correctFactorH,
						 ovalX, ovalY);
			} else {
				g2d.fillOval((BLOCKWIDTH * MAPDISPLAYWIDTH/BLOCKWIDTH/2) - correctFactorW + BLOCKWIDTH/2,
					 (BLOCKHEIGHT * (intPlayerLatitude + 1) + 200) - correctFactorH,
					 ovalX, ovalY);
			}			
		}
		// The player position moves in the South half of the map if intPlayerLatitude > MAPHEIGHT - MAPDISPLAYHEIGHT/BLOCKHEIGHT/2
		else if (intPlayerLatitude > MAPHEIGHT - MAPDISPLAYHEIGHT/BLOCKHEIGHT/2) {
			int arrayLength = MAPDISPLAYHEIGHT/BLOCKHEIGHT/2;
			int intArray[] = new int[arrayLength];
			for (int i = 0; i < arrayLength; i++) {
				intArray[i] = arrayLength - i;
			}
			
			// System.out.println(intArray[MAPHEIGHT - intPlayerLatitude]); // Debugging
			
			g2d.fillOval((BLOCKWIDTH * MAPDISPLAYWIDTH/BLOCKWIDTH/2) - correctFactorW + BLOCKWIDTH/2,
						 (BLOCKHEIGHT * (MAPDISPLAYHEIGHT/BLOCKHEIGHT/2 + intArray[MAPHEIGHT - intPlayerLatitude] + 1) + 200) - correctFactorH,
						 ovalX, ovalY);			
		}
		// The player position stays in the middle of the map in all other cases
		else {
			g2d.fillOval((BLOCKWIDTH * MAPDISPLAYWIDTH/BLOCKWIDTH/2) - correctFactorW + BLOCKWIDTH/2,
					 (BLOCKHEIGHT * (MAPDISPLAYHEIGHT/BLOCKHEIGHT/2 + 1) + 200) - correctFactorH,
					 ovalX, ovalY);
		}
		
		// Paint the player direction indicator
		paintPlayerDirection(g2d, intPlayerLongitude, intPlayerLatitude, BLOCKWIDTH, BLOCKHEIGHT);
	}
	
	public void paintPlayerDirection(Graphics2D g2d, int intPlayerLongitude, int intPlayerLatitude, int BLOCKWIDTH, int BLOCKHEIGHT) {
		// Set the color to deep pink for the moment. TODO: Find a better color for the player direction indicator.
		g2d.setColor(new Color(255, 20, 147));

		String characterDirection = Rule2D.characterDirection;
		
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
				int yCoordinate = intPlayerLatitude*BLOCKHEIGHT - getYSubtrahend(angle, cDIH*zoomFactor, BLOCKHEIGHT/2) + 200;
				returnArray[0] = yCoordinate;
			} else if (i == 1) {
				// Go 30 degrees to the left
				int yCoordinate = intPlayerLatitude*BLOCKHEIGHT - getYSubtrahend(angle + 30, cDIH*0.55*zoomFactor, BLOCKHEIGHT/2) + 200;
				returnArray[1] = yCoordinate;
			} else if (i == 2) {
				// Go 30 degrees to the right
				int xCoordinate = intPlayerLatitude*BLOCKHEIGHT - getYSubtrahend(angle - 30, cDIH*0.55*zoomFactor, BLOCKHEIGHT/2) + 200;
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
		// Round the value of multiply to the nearest int and add it to the xCoordinate value to obtain the wanted xSubtrahend value
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
		// Round the value of multiply to the nearest int and add it to the xCoordinate value to obtain the wanted ySubtrahend value
		int ySubtrahend = yCoordinate + (int) Math.round(multiply);
				
		return ySubtrahend;
	}
	
	// "Clear" the screen from any Graphics objects by painting a grey rectangle of size SCREENRESWIDTH * SCREENRESHEIGHT
	// TODO: Do we need this method???
	public void clearScreen(Graphics2D g2d, int SCREENRESWIDTH, int SCREENRESHEIGHT) {
		g2d.setColor(new Color(220, 220, 220));
		g2d.fillRect(0, 0, SCREENRESWIDTH, SCREENRESHEIGHT);
	}
	
	// TODO: Put in own class "CharacterCreationScreen"
	public void createCharacter(Graphics2D g2d) {
		g2d.drawString("This is the character creation screen", 200, 100);
	}
	
	// TODO: Put in own class "MonsterCreationScreen"
	public void createMonster(Graphics2D g2d) {
		g2d.drawString("This is the monster creation screen", 200, 100);
	}
}