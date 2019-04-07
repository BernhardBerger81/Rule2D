package com.rule2d.main;

import java.awt.Graphics2D;

public class ScreenControl {
	public void doScreenControl(Graphics2D g2d, String windowStatus, Gameboard gameboard, IsometricMap isometricMap, int MAPWIDTH, int MAPHEIGHT, 
			int MAPDISPLAYWIDTH, int MAPDISPLAYHEIGHT, int ISOMAPDISPLAYWIDTH, int ISOMAPDISPLAYHEIGHT, int BLOCKWIDTH, int BLOCKHEIGHT,
			int ISOBLOCKWIDTH, int ISOBLOCKHEIGHT, int intPlayerLongitude, int intPlayerLatitude, int screenWidth, int screenHeight) {
		switch(windowStatus) {
			case "IntroScreen":
				// Load the intro screen
				IntroScreen.paintIntroScreen(g2d);
				break;
			case "CreateMap":
				// Generate the map. Fill array mapCoordinatesTerrain
				try {
					gameboard.generateMap(MAPWIDTH, MAPHEIGHT);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case "ShowSquareBasedMap":
				// Paint the SQUARE BASED map on the screen.
				try {
					gameboard.preparePaintMap(g2d, intPlayerLongitude, intPlayerLatitude, MAPDISPLAYWIDTH, MAPDISPLAYHEIGHT, BLOCKWIDTH, BLOCKHEIGHT);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// Paint the player position on the map
				gameboard.paintPlayerPosition(g2d, intPlayerLongitude, intPlayerLatitude, BLOCKWIDTH, BLOCKHEIGHT, 
						MAPDISPLAYWIDTH, MAPDISPLAYHEIGHT, MAPHEIGHT);
				break;
			case "CreateCharacter":
				// Show the character creation screen
				gameboard.createCharacter(g2d);
				break;
			case "CreateMonster":
				// Show the monster creation screen
				gameboard.createMonster(g2d);
				break;
			case "CreateIsometricMap":
				// Generate the map. Fill array mapCoordinatesTerrain	
				try {
					isometricMap.generateIsometricMap(MAPWIDTH, MAPHEIGHT);
					// isometricMap.preparePaintIsometricMap(g2d, intPlayerLongitude, intPlayerLatitude, MAPDISPLAYWIDTH, MAPDISPLAYHEIGHT, BLOCKWIDTH, BLOCKHEIGHT);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case "ShowIsometricMap":
				// Paint the ISOMETRIC map on the screen
				
				int currentZoomFactor = Rule2D.mapZoomFactor;
				
				int isoBlockWidth = calculateIsoBlockWidth(currentZoomFactor, ISOBLOCKWIDTH);
				int isoBlockHeight = calculateIsoBlockHeight(currentZoomFactor, ISOBLOCKHEIGHT);
				
				int isoMapDisplayWidthBlocks = calculateIsoMapDisplayWidthBlocks(currentZoomFactor, screenWidth, isoBlockWidth);
				int isoMapDisplayHeightBlocks = calculateIsoMapDisplayHeightBlocks(currentZoomFactor, screenHeight, isoBlockHeight);
				
				try {
					isometricMap.preparePaintIsometricMap(g2d, intPlayerLongitude, intPlayerLatitude, isoBlockWidth, isoBlockHeight, 
							isoMapDisplayWidthBlocks, isoMapDisplayHeightBlocks);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// Paint the player position on the map
				isometricMap.paintPlayerPosition(g2d, intPlayerLongitude, intPlayerLatitude, ISOBLOCKWIDTH, ISOBLOCKHEIGHT, 
						ISOMAPDISPLAYWIDTH, ISOMAPDISPLAYHEIGHT, MAPHEIGHT);
		}
	}
	
	private int calculateIsoBlockWidth(int currentZoomFactor, int ISOBLOCKWIDTH) {
		int isoBlockWidth = ISOBLOCKWIDTH + currentZoomFactor * 20;
		
		return isoBlockWidth;
	}
	
	private int calculateIsoBlockHeight(int currentZoomFactor, int ISOBLOCKHEIGHT) {
		int isoBlockHeight = ISOBLOCKHEIGHT + currentZoomFactor * 20;
		
		return isoBlockHeight;
	}
	
	private int calculateIsoMapDisplayWidthBlocks(int currentZoomFactor, int screenWidth, int isoBlockWidth) {
		// How many blocks can we fit width-wise?
		int totalBlocks = screenWidth/isoBlockWidth;
		
		// We want blocks to fill 2/3 of the screen width-wise
		Rule2D.ISOMAPDISPLAYWIDTHBLOCKS = totalBlocks * 2/3;
		return totalBlocks * 2/3;
	}
	
	private int calculateIsoMapDisplayHeightBlocks(int currentZoomFactor, int screenHeight, int isoBlockHeight) {
		// How many blocks can we fit height-wise after subtracting the space for the top menu?
		int totalBlocks = screenHeight - 200;
		totalBlocks = totalBlocks/(isoBlockHeight/3);
		// System.out.println("totalBlocks: " + totalBlocks); // Debugging
		
		Rule2D.ISOMAPDISPLAYHEIGHTBLOCKS = totalBlocks;
		
		return totalBlocks;
	}
}