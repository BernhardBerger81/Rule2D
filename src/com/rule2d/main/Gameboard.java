package com.rule2d.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.Random;
import javax.swing.text.html.StyleSheet;

public class Gameboard {
	int x = 0;
	int y = 0;
	int xa = 1;
	int ya = 1;
	private Rule2D game;

	public Gameboard(Rule2D game) {
		this.game = game;
	}	

	public void generateMap(int MAPWIDTH, int MAPHEIGHT) throws Exception {		
		if(Rule2D.mapInitialisationCounter == 1) {
			
			// Load the terrain data into an array to reduce database connections
			DBConnector dbConnector = new DBConnector();
			String[] terrainColors = dbConnector.queryDatabaseReturnArray("SELECT terrainColor FROM Terrain");
			
			for(int longitude = 0; longitude < MAPWIDTH; longitude++) {
				for(int latitude = 0; latitude < MAPHEIGHT; latitude++) {					
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
	
	public void paintMap(Graphics2D g, int SCREENRESWIDTH, int SCREENRESHEIGHT, int BLOCKWIDTH, int BLOCKHEIGHT) throws Exception {
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
					
					g.setColor(color);
					g.fillRect(longitude*BLOCKWIDTH, latitude*BLOCKHEIGHT, BLOCKWIDTH, BLOCKHEIGHT);
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
	
	public void paintPlayerPosition(Graphics2D g, int intPlayerLongitude, int intPlayerLatitude, int BLOCKWIDTH, int BLOCKHEIGHT) {
		System.out.println("Inside paintPlayerPosition"); // Debugging
		g.setColor(Color.WHITE);
		// Make the oval a circle by setting equal x and y values.
		int ovalX = 20;
		int ovalY = 20;
		
		// The player's position is show as a white dot with a diameter of 20px.
		// The dot's coordinates are multiples of the BLOCKWIDTH and BLOCKHEIGHT.
		// The correctional factor "correctFactor" makes sure that the player position is in the middle of the field.
		// The correctional factor for width is half the BLOCKWIDTH plus half the oval's x value.
		// The correctional factor for height is half the BLOCKHEIGHT plus half the oval's y value.
		int correctFactorW = BLOCKWIDTH/2 + ovalX/2;
		int correctFactorH = BLOCKHEIGHT/2 + ovalY/2;
		
		g.fillOval((BLOCKWIDTH * intPlayerLongitude) - correctFactorW, (BLOCKHEIGHT * intPlayerLatitude) - correctFactorH, ovalX, ovalY);
	}
	
	public int randomTerrainType(int arrayLength) {
		Random rand = new Random();
		
		// Random number from 1 to number of entries in terrain table.
		int number = rand.nextInt(arrayLength); // TODO: Programmatically use number of terrain types in database
		
		return number;
	}
	
	public String getTerrainColor(int pkTerrain) throws Exception {
		DBConnector dbConnector = new DBConnector();
		String terrainColor = dbConnector.queryDataBaseReturnString("SELECT terrainColor FROM Terrain WHERE pkTerrain = " + pkTerrain);
				
		return terrainColor;
	}
}