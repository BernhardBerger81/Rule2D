package com.rule2d.main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Rule2D extends JPanel {
	// Settings. TODO: Move to settings file.
	public final static int BLOCKWIDTH  = 50; // Setting
	public final static int BLOCKHEIGHT = 50; // Setting
	public final static int MAPDISPLAYWIDTHBLOCKS = 17; // Setting
	public final static int MAPDISPLAYHEIGHTBLOCKS = 13; // Setting
	public final static int SCREENRESWIDTH  = 1200; // Setting
	public final static int SCREENRESHEIGHT = 1000; // Setting
	public final static int MAPDISPLAYWIDTH = MAPDISPLAYWIDTHBLOCKS * BLOCKWIDTH; // Setting
	public final static int MAPDISPLAYHEIGHT = MAPDISPLAYHEIGHTBLOCKS * BLOCKHEIGHT; // Setting
	public final static int MAPWIDTH = 20; // Setting // Has to be even number!!!
	public final static int MAPHEIGHT = 16; // Setting // Has to be even number!!!

	public final static int characterDirectionIndicatorHeight = 20; // Setting
	public final static double startingMapZoomFactor = 1; // Setting
	
	// Not settings
	public static int mapInitialisationCounter = 0; // Not a setting
	public static boolean databaseAvailable = false; // Not a setting 
	public static String[][] mapCoordinatesTerrain = new String[MAPWIDTH][MAPHEIGHT]; // [longitude coordinate][latitude coordinate] => pkTerrain // Not a setting
	public static int intPlayerLongitude = 9; // Not a setting
	public static int intPlayerLatitude = 7; // Not a setting
	public final static String[] movementDirections = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"}; // Not a setting -> That's debatable!!!
	public static String characterDirection = randomCharacterStartingDirection(); // Not a setting	
	public static double mapZoomFactor = startingMapZoomFactor; // Not a setting
	public static String windowStatus = "IntroScreen";

	Gameboard gameboard = new Gameboard(this);
	// Create and set up the window		
	public static JFrame frame = new JFrame("Rule2D");
	
	
	public Rule2D() {
		addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent keyEvent) {
				// https://stackoverflow.com/questions/7071757/keylistener-keypressed-versus-keytyped
				// keyTyped - when the unicode character represented by this key is sent by the keyboard to system input.
				// Note that keyTyped will only work for something that can be printed...
			}

			@Override
			public void keyPressed(KeyEvent keyEvent) {
				// Hand over KeyEvent handling to KeyboardControl.keyPressed 
				KeyboardControl keyboardControl = new KeyboardControl();
				keyboardControl.keyPressed(keyEvent);
			}

			@Override
			public void keyReleased(KeyEvent keyEvent) {
				// TODO Auto-generated method stub
			}
		
		});
		setFocusable(true);
		
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent mouseEvent) {
				MouseControl mouseControl = new MouseControl();
				mouseControl.mousePressed(mouseEvent);
			}
		});
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
			// Load the intro screen.
			ScreenControl screenControl = new ScreenControl();
			screenControl.doScreenControl(g2d, windowStatus, gameboard, MAPWIDTH, MAPHEIGHT, MAPDISPLAYWIDTH, MAPDISPLAYHEIGHT,
					BLOCKWIDTH, BLOCKHEIGHT, intPlayerLongitude, intPlayerLatitude);				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static String randomCharacterStartingDirection() {
		Random rand = new Random();
		
		// Random number from 1 to number of entries in array movementDirections.
		int number = rand.nextInt(movementDirections.length);
		
		String returnString = movementDirections[number];
		
		return returnString;
	}
	
	private static void createAndShowGUI() {
		Rule2D game = new Rule2D();
		
		frame.add(game);
		// Adjust the width and height of the frame to accommodate the strange fact that the blocks don't fit into the frame...  
		frame.setSize(SCREENRESWIDTH + 25, SCREENRESHEIGHT + 50); // TODO: Investigate why width and height need to be adjusted to fit the frame
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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