package com.rule2d.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
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
	public final static int MAPWIDTH = 16; // Setting
	public final static int MAPHEIGHT = 12; // Setting
	public final static int BLOCKWIDTH  = 50; // Setting
	public final static int BLOCKHEIGHT = 50; // Setting
	public final static String startingCharacterDirection = "N"; // Setting
	public final static int characterDirectionIndicatorHeight = 20; // Setting
	public final static double startingMapZoomFactor = 1;
	
	// Not settings
	public static int mapInitialisationCounter = 0; // Not a setting
	public static boolean databaseAvailable = false; // Not a setting 
	public static String[][] mapCoordinatesTerrain = new String[MAPWIDTH][MAPHEIGHT]; // [longitude coordinate][latitude coordinate] => pkTerrain // Not a setting
	public static int intPlayerLongitude = 5; // Not a setting
	public static int intPlayerLatitude = 5; // Not a setting
	public static String characterDirection = startingCharacterDirection; // Not a setting
	public final static String[] movementDirections = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"}; // Not a setting -> That's debatable!!!
	public static double mapZoomFactor = startingMapZoomFactor; // Not a setting

	Gameboard gameboard = new Gameboard(this);
	// Create and set up the window		
	public static JFrame frame = new JFrame("Rule2D");
	
	
	public Rule2D() {
		addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				// https://stackoverflow.com/questions/7071757/keylistener-keypressed-versus-keytyped
				// keyTyped - when the unicode character represented by this key is sent by the keyboard to system input.
				// Note that keyTyped will only work for something that can be printed...
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// Hand over KeyEvent handling to KeyboardControl.keyPressed 
				KeyboardControl keyboardControl = new KeyboardControl();
				keyboardControl.keyPressed(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
			}
		
		});
		setFocusable(true);
	}

	public static void init() {		
		// Stuff that needs to be done once when the application starts.
		// Make sure that Numlock is on
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		// Get the locking state of the Numlock key. If it is not "on", turn it on
		if (!toolkit.getLockingKeyState(KeyEvent.VK_NUM_LOCK)) {
			toolkit.setLockingKeyState(KeyEvent.VK_NUM_LOCK, true);
		}
	}	
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		try {
			// First, generate the map. Fill array mapCoordinatesTerrain.
			// Only generate map once: When mapInitialisationCounter == 0
			// TODO: Add button to generate map.
			if (mapInitialisationCounter == 1) {
				gameboard.generateMap(MAPWIDTH, MAPHEIGHT);
			}
			
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
	
	private static void createAndShowGUI() {
		Rule2D game = new Rule2D();
		
		frame.add(game);
		// Set the width and height of the frame to accommodate the strange fact that the blocks don't fit into the frame...  
		frame.setSize(SCREENRESWIDTH + 25, SCREENRESHEIGHT + 50);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void repaint(JFrame frame) {
		frame.repaint();
	}
	
/*	public void gameOver() {
		JOptionPane.showMessageDialog(this, "Game Over", "GameOver", JOptionPane.YES_NO_OPTION);
		System.exit(ABORT);
	}*/

	public static void main(String[] args) throws InterruptedException, Exception {		
		// Check if the database is available
		DBConnector dbConnector = new DBConnector();
		dbConnector.pingDatabase(); // Ping the database
		if (databaseAvailable == false) {
			System.out.println("The MySQL database is offline. Exiting program.");
			System.exit(ABORT);
		}
		
		// Initialise stuff
		init();
		
		// Start the GUI
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