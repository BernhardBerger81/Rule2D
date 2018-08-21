package com.rule2d.main;

import java.awt.Color;
import java.awt.Graphics2D;
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

	public void generateMap(int SCREENRESWIDTH, int SCREENRESHEIGHT, int BLOCKWIDTH, int BLOCKHEIGHT) throws Exception {
		if(Rule2D.mapInitialisationCounter == 1) {
			for(int longitude = 0; longitude < SCREENRESWIDTH/BLOCKWIDTH; longitude++) {
				for(int latitude = 0; latitude < SCREENRESHEIGHT/BLOCKHEIGHT; latitude++) {
					// Stylesheet allows stringToColor conversion of the color name loaded from the database
					StyleSheet stylesheet = new StyleSheet();
					String colorString = getTerrainColor(randomTerrainType());				
					
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
	
	public void paint(Graphics2D g, int SCREENRESWIDTH, int SCREENRESHEIGHT, int BLOCKWIDTH, int BLOCKHEIGHT) throws Exception {
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
	
	public int randomTerrainType() {
		Random rand = new Random();
		
		// Random number from 1 to number of entries in terrain table.
		int number = rand.nextInt(6) + 1; // TODO: Programmatically use number of terrain types in database
		
		return number;
	}
	
	public String getTerrainColor(int pkTerrain) throws Exception {
		DBConnector dbConnector = new DBConnector();
		String terrainColor = dbConnector.queryDataBaseReturnString("SELECT terrainColor FROM Terrain WHERE pkTerrain = " + pkTerrain);
				
		return terrainColor;
	}
}