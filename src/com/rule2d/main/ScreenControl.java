package com.rule2d.main;

import java.awt.Graphics2D;

public class ScreenControl {
	public void doScreenControl(Graphics2D g2d, String windowStatus, Gameboard gameboard, IsometricMap isometricMap, int MAPWIDTH, int MAPHEIGHT, int MAPDISPLAYWIDTH,
			int MAPDISPLAYHEIGHT, int BLOCKWIDTH, int BLOCKHEIGHT, int intPlayerLongitude, int intPlayerLatitude) {
		switch(windowStatus) {
			case "IntroScreen":
				// Load the intro screen
				IntroScreen.paintIntroScreen(g2d);
				break;
			case "CreateMap":
				// Generate the map. Fill array mapCoordinatesTerrain.
				try {
					gameboard.generateMap(MAPWIDTH, MAPHEIGHT);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case "ShowMap":
				// Paint the map on the screen.
				try {
					gameboard.preparePaintMap(g2d, intPlayerLongitude, intPlayerLatitude, MAPDISPLAYWIDTH, MAPDISPLAYHEIGHT, BLOCKWIDTH, BLOCKHEIGHT);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// Paint the player position on the map.
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
				// Paint the ISOMETRIC map on the screen
				try {
					isometricMap.preparePaintIsometricMap(g2d, intPlayerLongitude, intPlayerLatitude, MAPDISPLAYWIDTH, MAPDISPLAYHEIGHT, BLOCKWIDTH, BLOCKHEIGHT);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
		}		
	}
}