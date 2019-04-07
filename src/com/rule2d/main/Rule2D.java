package com.rule2d.main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Rule2D extends JPanel {
	// Settings. TODO: Move to settings file.
	public final static int STARTINGSQUAREBLOCKWIDTH  = 50; // Setting
	public final static int STARTINGSQUAREBLOCKHEIGHT = 50; // Setting
	public final static int STARTINGISOBLOCKWIDTH = 80; // Setting
	public final static int STARTINGISOBLOCKHEIGHT = 80; // Setting
	public final static int STARTINGMAPDISPLAYWIDTHBLOCKS = 17; // Setting
	public final static int STARTINGMAPDISPLAYHEIGHTBLOCKS = 13; // Setting
	public final static int STARTINGISOMAPDISPLAYWIDTHBLOCKS = 17; // Setting
	public final static int STARTINGISOMAPDISPLAYHEIGHTBLOCKS = 26; // Setting // We can paint double the blocks on an isometric map
	// public final static int SCREENRESWIDTH  = 1200; // Setting
	// public final static int SCREENRESHEIGHT = 900; // Setting
	public final static int STARTINGSQUAREMAPDISPLAYWIDTH = STARTINGMAPDISPLAYWIDTHBLOCKS * STARTINGSQUAREBLOCKWIDTH; // Setting
	public final static int STARTINGSQUAREMAPDISPLAYHEIGHT = STARTINGMAPDISPLAYHEIGHTBLOCKS * STARTINGSQUAREBLOCKHEIGHT; // Setting
	public final static int STARTINGISOMAPDISPLAYWIDTH = STARTINGISOMAPDISPLAYWIDTHBLOCKS * STARTINGISOBLOCKWIDTH; // Setting
	public final static int STARTINGISOMAPDISPLAYHEIGHT = STARTINGISOMAPDISPLAYHEIGHTBLOCKS * STARTINGISOBLOCKHEIGHT; // Setting
	public final static int MAPWIDTH = 80; // Setting // Has to be even number!!!
	public final static int MAPHEIGHT = 80; // Setting // Has to be even number!!!
	public final static int MAXELEVATION = 1; // Setting

	public final static int characterDirectionIndicatorHeight = 20; // Setting
	public final static int startingMapZoomFactor = 0; // Setting
	
	// Not settings
	public static int SQUAREBLOCKWIDTH = STARTINGSQUAREBLOCKWIDTH; // Not a setting
	public static int SQUAREBLOCKHEIGHT = STARTINGSQUAREBLOCKHEIGHT; // Not a setting
	public static int ISOBLOCKWIDTH = STARTINGISOBLOCKWIDTH; // Not a setting
	public static int ISOBLOCKHEIGHT = STARTINGISOBLOCKHEIGHT; // Not a setting
	public static int MAPDISPLAYWIDTHBLOCKS = STARTINGMAPDISPLAYWIDTHBLOCKS; // Not a setting
	public static int MAPDISPLAYHEIGHTBLOCKS = STARTINGMAPDISPLAYHEIGHTBLOCKS; // Not a setting
	public static int ISOMAPDISPLAYWIDTHBLOCKS = STARTINGISOMAPDISPLAYWIDTHBLOCKS; // Not a setting
	public static int ISOMAPDISPLAYHEIGHTBLOCKS = STARTINGISOMAPDISPLAYHEIGHTBLOCKS; // Not a setting
	public static int SQUAREMAPDISPLAYWIDTH = STARTINGSQUAREMAPDISPLAYWIDTH; // Not a setting
	public static int SQUAREMAPDISPLAYHEIGHT = STARTINGSQUAREMAPDISPLAYHEIGHT; // Not a setting
	public static int ISOMAPDISPLAYWIDTH = STARTINGISOMAPDISPLAYWIDTH; // Not a setting
	public static int ISOMAPDISPLAYHEIGHT = STARTINGISOMAPDISPLAYHEIGHT; // Not a setting
	
	
	public static int mapInitialisationCounter = 0; // Not a setting
	public static boolean databaseAvailable = false; // Not a setting 
	public static String[][] mapCoordinatesTerrain = new String[MAPWIDTH][MAPHEIGHT]; // [longitude coordinate][latitude coordinate] => pkTerrain // Not a setting
	public static int[][] mapCoordinatesElevation = new int[MAPWIDTH][MAPHEIGHT]; // [longitude coordinate][latitude coordinate] => elevation // Not a setting
	public static int intPlayerLongitude = 8; // Not a setting // TODO: Randomize player starting position
	public static int intPlayerLatitude = 13; // Not a setting // TODO: Randomize player starting position
	public final static String[] movementDirections = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"}; // Not a setting -> That's debatable!!!
	// public static String characterDirection = randomCharacterStartingDirection(); // Not a setting
	// For testing purposes, set the starting characterDirection to a fixed value
	public static String characterDirection = "S"; // Not a setting // Debugging
	public static int mapZoomFactor = startingMapZoomFactor; // Not a setting
	public static String windowStatus = "IntroScreen";

	Gameboard gameboard = new Gameboard(this);
	IsometricMap isometricMap = new IsometricMap(this);
	// Create and set up the window		
	public static JFrame frame = new JFrame("Rule2D");
	
	
	public Rule2D() {
		addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent keyEvent) {
				// https://stackoverflow.com/questions/7071757/keylistener-keypressed-versus-keytyped
				// keyTyped - when the Unicode character represented by this key is sent by the keyboard to system input.
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
		
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
				MouseControl mouseControl = new MouseControl();
				mouseControl.mouseWheelMoved(mouseWheelEvent);
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
		
		// What's the screen size on a multi-monitor device?
		// https://stackoverflow.com/questions/3680221/how-can-i-get-screen-resolution-in-java
		GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int screenWidth = graphicsDevice.getDisplayMode().getWidth();
		int screenHeight = graphicsDevice.getDisplayMode().getHeight();
		System.out.println("Screen size: " + screenWidth + "x" + screenHeight); // Debugging
		
		try {
			// Load the intro screen.
			ScreenControl screenControl = new ScreenControl();
			screenControl.doScreenControl(g2d, windowStatus, gameboard, isometricMap, MAPWIDTH, MAPHEIGHT, SQUAREMAPDISPLAYWIDTH, SQUAREMAPDISPLAYHEIGHT,
					ISOMAPDISPLAYWIDTH, ISOMAPDISPLAYHEIGHT, SQUAREBLOCKWIDTH, SQUAREBLOCKHEIGHT, ISOBLOCKWIDTH, ISOBLOCKHEIGHT,
					intPlayerLongitude, intPlayerLatitude, screenWidth, screenHeight);				
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
		
		// Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		// int screenWidth = (int) screensize.getWidth();
		// int screenHeight = (int) screensize.getHeight();
		// System.out.println("Screen size: " + screenWidth + "x" + screenHeight); // Debugging
		// frame.setSize(screenWidth, screenHeight); // Don't use frame.setSize, it doesn't set the window to fullscreen correctly!!
		
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setUndecorated(false); // "true" hides the title bar		
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