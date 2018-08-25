package com.rule2d.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Rule2D extends JPanel {
	// Settings. TODO: Move to settings file.
	public final static int SCREENRESWIDTH  = 800; // Setting
	public final static int SCREENRESHEIGHT = 600; // Setting
	public final static int MAPWIDTH = 20; // Setting
	public final static int MAPHEIGHT = 20; // Setting
	public final static int BLOCKWIDTH  = 50; // Setting
	public final static int BLOCKHEIGHT = 50; // Setting	
	
	// Not settings
	public static int mapInitialisationCounter = 0; // Not a setting
	public static boolean databaseAvailable = false; // Not a setting 
	public static String[][] mapCoordinatesTerrain = new String[MAPWIDTH][MAPHEIGHT]; // [longitude coordinate][latitude coordinate] => pkTerrain // Not a setting

	Gameboard gameboard = new Gameboard(this);
	
	public Rule2D() {
		addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		
		});
		setFocusable(true);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		try {
			// First, generate the map. Fill array mapCoordinatesTerrain.
			// TODO: Add button to generate map.
			gameboard.generateMap(MAPWIDTH, MAPHEIGHT);
			// Second, paint the map on the screen.
			gameboard.paint(g2d, SCREENRESWIDTH, SCREENRESHEIGHT, BLOCKWIDTH, BLOCKHEIGHT);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void createAndShowGUI() {
		// Create and set up the window
		// Idea from tutorial at https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/TopLevelDemoProject/src/components/TopLevelDemo.java
		JFrame frame = new JFrame("Rule2D");		
		
		// Create the menu bar. Make it have a green background
		// TODO: Format menu bar. It's length value does not correspond with what is displayed on screen. 
		JMenuBar greenMenuBar = new JMenuBar();
		greenMenuBar.setOpaque(true);
		greenMenuBar.setBackground(new Color(248, 213, 131));
		greenMenuBar.setPreferredSize(new Dimension(200, 50)); // (length, height)
		
		// Set the menu bar to the content pane
		frame.setJMenuBar(greenMenuBar);
		
		Rule2D game = new Rule2D();
		
		frame.add(game);
		frame.setSize(SCREENRESWIDTH, SCREENRESHEIGHT);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
/*	public void gameOver() {
		JOptionPane.showMessageDialog(this, "Game Over", "GameOver", JOptionPane.YES_NO_OPTION);
		System.exit(ABORT);
	}*/

	public static void main(String[] args) throws InterruptedException, Exception {		
		// First, check if the database is available
		DBConnector dbConnector = new DBConnector();
		dbConnector.pingDatabase(); // Ping the database
		if (databaseAvailable == false) {
			System.out.println("The MySQL database is offline. Exiting program.");
			System.exit(ABORT);
		}
		
		// Second, start the GUI
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
		
		while(true) {
			// game.repaint();
			// Thread.sleep(10);
		}
	}

}