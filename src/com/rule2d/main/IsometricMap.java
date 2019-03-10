package com.rule2d.main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.Random;

import javax.swing.text.html.StyleSheet;

public class IsometricMap {
	private Rule2D game;

	public IsometricMap (Rule2D game) {
		this.game = game;
	}
	
	public void generateIsometricMap(int MAPWIDTH, int MAPHEIGHT) throws Exception {
		// Load the terrain data into an array to reduce database connections
		DBConnector dbConnector = new DBConnector();
		String[] terrainColors = dbConnector.queryDatabaseReturnArray("SELECT terrainColor FROM Terrain");
		
		for (int longitude = 0; longitude < MAPWIDTH; longitude++) {
			for (int latitude = 0; latitude < MAPHEIGHT; latitude++) {
				String colorString = terrainColors[randomTerrainType(terrainColors.length)];
				
				Rule2D.mapCoordinatesTerrain[longitude][latitude] = colorString;
				// System.out.println(Rule2D.mapCoordinatesTerrain[longitude][latitude]); // Debugging		
			}
		}
		
		Rule2D.windowStatus = "ShowIsometricMap";
		Rule2D.frame.repaint();		
	}
	
	public void preparePaintIsometricMap(Graphics2D g2d, int intPlayerLongitude, int intPlayerLatitude, int ISOMAPDISPLAYWIDTH, int ISOMAPDISPLAYHEIGHT, 
			int BLOCKWIDTH,	int BLOCKHEIGHT) {
		// longitudeStart is the start coordinate of painting longitude map values on the screen
		int longitudeStart = intPlayerLongitude - Rule2D.ISOMAPDISPLAYWIDTHBLOCKS/2;
		// latitudeStart is the start coordinate of painting latitude map values on the screen
		int latitudeStart = intPlayerLatitude - Rule2D.ISOMAPDISPLAYHEIGHTBLOCKS/2;
		int longitudeHolder = longitudeStart;
		int latitudeHolder = latitudeStart;
		
		paintIsometricMap(g2d, longitudeStart, latitudeStart);
	}
	
	public void paintIsometricMap(Graphics2D g2d, int longitudeStart, int latitudeStart) {
		for (int latitudeCounter = 0; latitudeCounter < Rule2D.ISOMAPDISPLAYHEIGHTBLOCKS; latitudeCounter++) {
			for (int longitudeCounter = 0; longitudeCounter < Rule2D.ISOMAPDISPLAYWIDTHBLOCKS; longitudeCounter++) {
				paintIsometricField(g2d, longitudeStart, latitudeStart, Rule2D.BLOCKWIDTH, Rule2D.BLOCKHEIGHT);
				
				longitudeStart++;
			}
			
			latitudeStart++;
			
			longitudeStart = 0;
		}
	}
	
	public void paintIsometricField(Graphics2D g2d, int x, int y, int width, int height) {
		// System.out.println("longitudeStart: " + x); // Debugging
		// System.out.println("latitudeStart: " + y); // Debugging
		
		// Calculate the coordinates of the tip of the isometric field. All other coordinates derive from the tip coordinates
		// Check if y is even or odd. Odd y values mean that x has to go right half the width of the isometric field
		int xTopTipCoordinate;
		if (y % 2 == 0) {
			xTopTipCoordinate = x * width + width/2;
			// System.out.println("if case xTopTipCoordinate: " + xTopTipCoordinate); // Debugging
		} else {
			xTopTipCoordinate = (x + 1) * width;
			// System.out.println("else case xTopTipCoordinate: " + xTopTipCoordinate); // Debugging
		}
		
		// Check if y is even or odd. Odd y values mean that y has to go down half the height of the isometric field		
		int yTopTipCoordinate;
		if (y % 2 == 0) {
			yTopTipCoordinate = y * height + 200;
			if (y > 1) {
				yTopTipCoordinate = yTopTipCoordinate - y/2 * height; 
			}
			// System.out.println("if case yTopTipCoordinate: y = " + y + ", " + yTopTipCoordinate); // Debugging
		} else {
			yTopTipCoordinate = y * height - height/2 + 200;
			if (y > 1) {
				yTopTipCoordinate = yTopTipCoordinate - y/2 * height; 
			}
			// System.out.println("else case yTopTipCoordinate: y = " + y + ", " + yTopTipCoordinate); // Debugging
		}
		
		int xRightTipCoordinate = xTopTipCoordinate + width/2;
		int yRightTipCoordinate = yTopTipCoordinate + height/2;
		
		int xBottomTipCoordinate = xTopTipCoordinate;
		// int yBottomTipCoordinate = (y + 1) * Rule2D.BLOCKHEIGHT - Rule2D.BLOCKHEIGHT/2 + 200;
		int yBottomTipCoordinate = yTopTipCoordinate + width;
		
		int xLeftTipCoordinate = xTopTipCoordinate - width/2;
		int yLeftTipCoordinate = yRightTipCoordinate;
		
		int xIsometricField[] = {xTopTipCoordinate, xRightTipCoordinate, xBottomTipCoordinate, xLeftTipCoordinate};
		int yIsometricField[] = {yTopTipCoordinate, yRightTipCoordinate, yBottomTipCoordinate, yLeftTipCoordinate};
		
		Polygon isometricField = new Polygon(xIsometricField, yIsometricField, xIsometricField.length);
		
		StyleSheet stylesheet = new StyleSheet();
		
		String colorString = Rule2D.mapCoordinatesTerrain[x][y];
		Color color = stylesheet.stringToColor(colorString);
		g2d.setColor(color);
		
		g2d.fillPolygon(isometricField);
		
		g2d.setStroke(new BasicStroke(2));
		g2d.setColor(Color.PINK);
		g2d.drawPolygon(isometricField);
		
		g2d.setColor(Color.RED);
		
		// Print the field coordinates for debugging
		// Check if y is even or odd. Odd y values mean that x has to go right the width of the isometric field
		// Check if y is even or odd. Odd y values mean that y has to go down half the height of the isometric field		
		/*if (y % 2 == 0) {
			// System.out.println("x*width: " + x*width + ", " + "y*height: " + y*height); // Debugging
			g2d.drawString(x + ", " + y, x*width + 15, y*height + 230); // Debugging
			if (y > 1) {
				g2d.drawString(x + ", " + y, x*width + 15, y/2*height + 230); // Debugging
			}
		} else {
			g2d.drawString(x + ", " + y, x*width + width/2 + 15, y*height - height/2 + 230); // Debugging
			if (y > 1) {
				g2d.drawString(x + ", " + y, x*width + width/2 + 15, (y + 1)/2*height - height/2 + 230); // Debugging
			}
		}*/
	}
	
	public void paintPlayerPosition(Graphics2D g2d, int intPlayerLongitude, int intPlayerLatitude, int BLOCKWIDTH, int BLOCKHEIGHT,
			int ISOMAPDISPLAYWIDTH, int ISOMAPDISPLAYHEIGHT, int MAPHEIGHT) {
		
		System.out.println("Inside isometric paintPlayerPosition"); // Debugging
		
		// Set the color to WHITE
		g2d.setColor(Color.WHITE);
		// The player's position is shown as a white dot with a diameter of 20px.
		// Make the oval a circle by setting equal x and y values.
		int ovalX = 20;
		int ovalY = 20;
		
		// The dot's coordinates are multiples of the BLOCKWIDTH and BLOCKHEIGHT.
		// The correctional factor "correctFactor" makes sure that the player position is in the middle of the block.
		// The correctional factor for width is half the BLOCKWIDTH plus half the oval's x value.
		// The correctional factor for height is half the BLOCKHEIGHT plus half the oval's y value.
		int correctFactorW = ovalX - BLOCKWIDTH/8 + 1;
		int correctFactorH = ovalY/2;
		
		int xOvalPosition = BLOCKWIDTH * ISOMAPDISPLAYWIDTH/BLOCKWIDTH/2 + correctFactorW;
		int yOvalPosition = (BLOCKHEIGHT * (ISOMAPDISPLAYHEIGHT/BLOCKHEIGHT/4 + 1) + 200) - correctFactorH;
		
		g2d.fillOval(xOvalPosition, yOvalPosition, ovalX, ovalY);
		
		// Debugging: Make a PINK dot for measuring if the dot is on the correct position
		g2d.setColor(Color.PINK); // Debugging
		g2d.fillOval(xOvalPosition + 10, yOvalPosition + 10, 1, 1); // Debugging
		
		/*g2d.fillOval((BLOCKWIDTH * ISOMAPDISPLAYWIDTH/BLOCKWIDTH/2) - correctFactorW + BLOCKWIDTH/2,
				 (BLOCKHEIGHT * (ISOMAPDISPLAYHEIGHT/BLOCKHEIGHT/2 + 1) + 200) - correctFactorH,
				 ovalX, ovalY);*/
	}
	
	private int randomTerrainType(int arrayLength) {
		Random rand = new Random();
		
		// Random number from 1 to number of entries in terrain table.
		int number = rand.nextInt(arrayLength);
		
		return number;
	}
}
