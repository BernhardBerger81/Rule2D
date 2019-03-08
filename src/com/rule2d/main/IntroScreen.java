package com.rule2d.main;

import java.awt.Color;
import java.awt.Graphics2D;

public class IntroScreen {
	public static void paintIntroScreen(Graphics2D g2d) {
		///////////////////////////////////
		///// Button for map creation /////
		///////////////////////////////////
		// Button color
		Color buttonBackgroundColor = new Color(220, 220, 220); // Gray
		// Button start coordinates
		int xStartButton = 0;
		int yStartButton = 0;
		// Button dimensions
		int buttonWidth = 200;
		int buttonHeight = 100;		
		// Text color
		Color textColor = new Color(255, 0, 0); // Red
		// Button text
		String buttonText = "Create a map";
		
		// Now, create the button
		drawButton(g2d, buttonText, buttonBackgroundColor, textColor, xStartButton, yStartButton, buttonWidth, buttonHeight);		
		
		/////////////////////////////////////////
		///// Button for character creation /////
		/////////////////////////////////////////
		// Button color
		buttonBackgroundColor = new Color(220, 220, 220); // Grey
		// Button start coordinates
		xStartButton = 250;
		yStartButton = 0;		
		// Button dimensions
		buttonWidth = 200;
		buttonHeight = 100;		
		// Text color
		textColor = new Color(255, 0, 0); // Red
		// Button text
		buttonText = "Create a character";
		
		// Now, create the button
		drawButton(g2d, buttonText, buttonBackgroundColor, textColor, xStartButton, yStartButton, buttonWidth, buttonHeight);
		
		///////////////////////////////////////
		///// Button for monster creation /////
		///////////////////////////////////////
		// Button color
		buttonBackgroundColor = new Color(220, 220, 220); // Grey
		// Button start coordinates
		xStartButton = 500;
		yStartButton = 0;
		// Button dimensions
		buttonWidth = 200;
		buttonHeight = 100;
		// Text color
		textColor = new Color(225, 0, 0); // Red
		// Button text
		buttonText = "Create a monster";
		
		// Now, create the button
		drawButton(g2d, buttonText, buttonBackgroundColor, textColor, xStartButton, yStartButton, buttonWidth, buttonHeight);
		
		//////////////////////////////////////////////////
		///// TEMP button for isometric map creation /////
		//////////////////////////////////////////////////
		// Button color
		buttonBackgroundColor = new Color(220, 220, 220); // Grey
		// Button start coordinates
		xStartButton = 0;
		yStartButton = 150;
		// Button dimensions
		buttonWidth = 200;
		buttonHeight = 100;
		// Text color
		textColor = new Color(225, 0, 0); // Red
		// Button text
		buttonText = "Create isometric map";
		
		// Now, create the button
		drawButton(g2d, buttonText, buttonBackgroundColor, textColor, xStartButton, yStartButton, buttonWidth, buttonHeight);
	}
	
	public static void drawButton(Graphics2D g2d, String buttonText, Color buttonBackgroundColor, Color textColor, int xStartButton, int yStartButton,
			int buttonWidth, int buttonHeight) {
		// Prepare the starting x coordinate of the button text
		int textWidth = g2d.getFontMetrics().stringWidth(buttonText);
		int buttonTextStartXCoord = xStartButton + buttonWidth/2 - textWidth/2;
		
		// Prepare the starting y coordinate of the button text
		int buttonTextStartYCoord = yStartButton + buttonHeight/2;
		
		// Set the button background color
		g2d.setColor(buttonBackgroundColor);		
		
		// Draw the rectangular button with rounded edges
		g2d.fillRoundRect(xStartButton, yStartButton, buttonWidth, buttonHeight, 10, 10);
		
		// Draw a black border around the button
		g2d.setColor(Color.BLACK);
		g2d.drawRoundRect(xStartButton, yStartButton, buttonWidth, buttonHeight, 10, 10);
				
		// Set the text color
		g2d.setColor(textColor);
		
		// Draw the button text
		g2d.drawString(buttonText, buttonTextStartXCoord, buttonTextStartYCoord);
	}
	
	
	/*public static void addComponentsToPane(Container pane, Gameboard gameboard, int MAPWIDTH, int MAPHEIGHT, Graphics2D g2d, 
			int SCREENRESWIDTH, int SCREENRESHEIGHT, int BLOCKWIDTH, int BLOCKHEIGHT, int intPlayerLongitude, int intPlayerLatitude,
			int mapInitialisationCounter) {
		pane.setLayout(new FlowLayout());
		
		createNewMapButton(pane, gameboard, MAPWIDTH, MAPHEIGHT, g2d,
				SCREENRESWIDTH, SCREENRESHEIGHT, BLOCKWIDTH, BLOCKHEIGHT, intPlayerLongitude, intPlayerLatitude,
				mapInitialisationCounter);
		createNewCharacterButton(pane);
		createNewMonsterButton(pane);				
	}*/
	
	/*private static void createNewMapButton(Container pane, Gameboard gameboard, int MAPWIDTH, int MAPHEIGHT, Graphics2D g2d, 
			int SCREENRESWIDTH, int SCREENRESHEIGHT, int BLOCKWIDTH, int BLOCKHEIGHT, int intPlayerLongitude, int intPlayerLatitude,
			int mapInitialisationCounter) {
		JButton newMapButton = new JButton("Create new map");
		// newMapButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		// newMapButton.setAlignmentY(Component.TOP_ALIGNMENT);
		newMapButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println("Create new map"); // Debugging	
				try {
					System.out.println("Inside try clause in createNewMapButton"); // Debugging					
					// First, generate the map. Fill array mapCoordinatesTerrain.
					// Only generate map once: When mapInitialisationCounter == 0
					Rule2D.mapInitialisationCounter = 1;
					gameboard.generateMap(MAPWIDTH, MAPHEIGHT);
					
					pane.removeAll();
					Rule2D.frame.repaint();
					// pane.update(g2d);
					// Second, paint the map on the screen.
					gameboard.paintMap(g2d, SCREENRESWIDTH, SCREENRESHEIGHT, BLOCKWIDTH, BLOCKHEIGHT);
					
					// Third, paint the player position on the map.
					if (Rule2D.mapInitialisationCounter > 1) {
						gameboard.paintPlayerPosition(g2d, intPlayerLongitude, intPlayerLatitude, BLOCKWIDTH, BLOCKHEIGHT);
					}					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		pane.add(newMapButton);
	}*/
	
	/*private static void createNewCharacterButton(Container pane) {
		JButton newCharacterButton = new JButton("Create new character");
		// newCharacterButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		// newCharacterButton.setAlignmentY(Component.TOP_ALIGNMENT);
		newCharacterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Create new character"); // Debugging
			}
		});
		
		pane.add(newCharacterButton);
	}*/
	
	/*private static void createNewMonsterButton(Container pane) {
		JButton newMonsterButton = new JButton("Create new monster");
		// newMonsterButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		// newMonsterButton.setAlignmentY(Component.TOP_ALIGNMENT);
		newMonsterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Create new monster"); // Debugging
			}
		});
		
		pane.add(newMonsterButton);
	}*/
}
