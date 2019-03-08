package com.rule2d.main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class IsometricMap {
	private Rule2D game;

	public IsometricMap (Rule2D game) {
		this.game = game;
	}
	
	public void generateIsometricMap(int MAPWIDTH, int MAPHEIGHT) throws Exception {
		
	}
	
	public void preparePaintIsometricMap(Graphics2D g2d, int intPlayerLongitude, int intPlayerLatitude, int MAPDISPLAYWIDTH, int MAPDISPLAYHEIGHT, 
			int BLOCKWIDTH,	int BLOCKHEIGHT) {
		paintIsometricMap(g2d, intPlayerLongitude, intPlayerLatitude);
	}
	
	public void paintIsometricMap(Graphics2D g2d, int intPlayerLongitude, int intPlayerLatitude) {
		for (int x = 0; x < Rule2D.ISOMAPDISPLAYWIDTHBLOCKS; x++) {
			for (int y = 0; y < Rule2D.ISOMAPDISPLAYHEIGHTBLOCKS; y++) {
				paintIsometricField(g2d, x, y, Rule2D.BLOCKWIDTH, Rule2D.BLOCKHEIGHT);
			}
		}
		
	}
	
	public void paintIsometricField(Graphics2D g2d, int x, int y, int width, int height) {
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
		
		// Alternate colors for better visibility
		if (y % 2 != 0) {
			g2d.setColor(Color.GREEN);
		} else {
			g2d.setColor(Color.BLUE);
		}		
		
		g2d.fillPolygon(isometricField);
		
		g2d.setStroke(new BasicStroke(2));
		g2d.setColor(Color.PINK);
		g2d.drawPolygon(isometricField);
		
		g2d.setColor(Color.RED);
		
		// Print the field coordinates for debugging
		// Check if y is even or odd. Odd y values mean that x has to go down right the width of the isometric field
		// Check if y is even or odd. Odd y values mean that y has to go down half the height of the isometric field		
		if (y % 2 == 0) {
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
		}
	}
}


