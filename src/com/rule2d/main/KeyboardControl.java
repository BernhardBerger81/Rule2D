package com.rule2d.main;

import java.awt.event.KeyEvent;

public class KeyboardControl {
	public void keyPressed(KeyEvent keyEvent) {
		// Depending on which key was pressed, we hand over the handling of the KeyEvent
		
		// Movement related key presses when Numlock is active
		if (keyEvent.getKeyCode() == KeyEvent.VK_NUMPAD1 || keyEvent.getKeyCode() == KeyEvent.VK_NUMPAD2 ||
				keyEvent.getKeyCode() == KeyEvent.VK_NUMPAD3 || keyEvent.getKeyCode() == KeyEvent.VK_NUMPAD4 ||
				keyEvent.getKeyCode() == KeyEvent.VK_NUMPAD5 || keyEvent.getKeyCode() == KeyEvent.VK_NUMPAD6 ||
				keyEvent.getKeyCode() == KeyEvent.VK_NUMPAD7 || keyEvent.getKeyCode() == KeyEvent.VK_NUMPAD8 || 
				keyEvent.getKeyCode() == KeyEvent.VK_NUMPAD9) {
			movementControl(keyEvent);
		}		
	}
	
	public void movementControl(KeyEvent e) {
		////////////////////////////////////
		///// Turn left by 180 degrees /////
		////////////////////////////////////
		if (e.getKeyCode() == KeyEvent.VK_NUMPAD1) {
			// Turning left by 180 degrees means "walking left" by 4 entries in the String[] movementDirections.
			// If the "walking left" goal would be out of bounds of the String[], start again at the right (String[7]) and "walk" the remaining steps to the left.
			// Find the index "oldIndex" of the current characterDirection
			int currentIndex = -1;
			int newIndex = 0;
			
			currentIndex = getCurrentIndex();
			
			// "Walk left" through the array for 4 steps. If we reach the end of the array, we start at String[7] and continue "walking left" the remaining steps.
			// If we "walk left" without passing the end of the array.
			if ((currentIndex - 4) >= 0) {
				newIndex = currentIndex - 4;
			// If we "walk left" and pass the end of the array. 
			} else {
				// How many steps from currentIndex to zero
				newIndex = Rule2D.movementDirections.length - (4 - currentIndex);
			}
			
			// Set the new characterDirection
			Rule2D.characterDirection = Rule2D.movementDirections[newIndex];
			
			// After changing the player direction, repaint the map to show the correct player direction indicator
			Rule2D.frame.repaint();
		}
		
		/////////////////////////////////////////////////////////////////////
		///// Move backwards from the direction the character is facing /////
		/////////////////////////////////////////////////////////////////////
		if (e.getKeyCode() == KeyEvent.VK_NUMPAD2) {
			// System.out.println(e.getKeyCode()); // Debugging			
			
			// Determine the direction the player is looking into
			// Calculate changes to intPLayerLongitude and intPlayerLatiude accordingly
			switch(Rule2D.characterDirection) {
				/////////////////////////////////////////////
				///// Move backwards while facing North /////
				/////////////////////////////////////////////
				case "N":
					if (Rule2D.intPlayerLatitude + 1 > Rule2D.MAPHEIGHT) {
						// Walking over the edge of the map, we arrive at the other side of the globe via the pole.
						if (Rule2D.intPlayerLongitude - Rule2D.MAPWIDTH/2 < 0) {
							Rule2D.intPlayerLongitude = Rule2D.MAPWIDTH - (Rule2D.MAPWIDTH/2 - Rule2D.intPlayerLongitude);
						} else {
							Rule2D.intPlayerLongitude = Rule2D.intPlayerLongitude - Rule2D.MAPWIDTH/2;
							// Make sure that intPlayerLongitude doesn't become zero (0).
							if (Rule2D.intPlayerLongitude <= 0) {
								Rule2D.intPlayerLongitude = Rule2D.MAPWIDTH;
							}
						}
						// Change the character direction to its opposite
						Rule2D.characterDirection = "S";
					} else {
						// Elsewhere on the map, we just move.
						Rule2D.intPlayerLongitude = Rule2D.intPlayerLongitude;
						Rule2D.intPlayerLatitude  = Rule2D.intPlayerLatitude + 1;
					}
										
					Rule2D.frame.repaint();
					System.out.println("Moving backwards while looking N"); // Debugging					
					break;
				//////////////////////////////////////////////////
				///// Move backwards while facing North East /////
				//////////////////////////////////////////////////
				case "NE":					
					if (Rule2D.intPlayerLatitude + 1 > Rule2D.MAPHEIGHT) {
						// Walking over the edge of the map, we arrive at the other side of the globe via the pole.
						if (Rule2D.intPlayerLongitude - Rule2D.MAPWIDTH/2 < 0) {
							Rule2D.intPlayerLongitude = Rule2D.MAPWIDTH - (Rule2D.MAPWIDTH/2 - Rule2D.intPlayerLongitude);							
						} else {
							Rule2D.intPlayerLongitude = Rule2D.intPlayerLongitude - Rule2D.MAPWIDTH/2;
							// Make sure that intPlayerLongitude doesn't become zero (0).
							if (Rule2D.intPlayerLongitude <= 0) {
								Rule2D.intPlayerLongitude = Rule2D.MAPWIDTH;
							}
						}
						// Change the character direction to its opposite
						Rule2D.characterDirection = "SW";
					} else {
						// Check if we pass the Western edge of the map
						if (Rule2D.intPlayerLongitude - 1 <= 0) {
							Rule2D.intPlayerLongitude = Rule2D.MAPWIDTH;
							Rule2D.intPlayerLatitude = Rule2D.intPlayerLatitude + 1;
						} else {
							// Elsewhere on the map, we just move.
							Rule2D.intPlayerLongitude = Rule2D.intPlayerLongitude - 1;
							Rule2D.intPlayerLatitude  = Rule2D.intPlayerLatitude + 1;
						}
						
					}
					
					Rule2D.frame.repaint();
					System.out.println("Moving backwards while looking NE"); // Debugging
					break;
				////////////////////////////////////////////
				///// Move backwards while facing East /////
				////////////////////////////////////////////
				case "E":
					if (Rule2D.intPlayerLongitude - 1 == 0) {
						Rule2D.intPlayerLongitude = Rule2D.MAPWIDTH;
					} else {
						Rule2D.intPlayerLongitude = Rule2D.intPlayerLongitude - 1;
					}
					
					Rule2D.intPlayerLatitude  = Rule2D.intPlayerLatitude;
					
					Rule2D.frame.repaint();
					System.out.println("Moving backwards while looking E"); // Debugging
					break;
				//////////////////////////////////////////////////
				///// Move backwards while facing South East /////
				//////////////////////////////////////////////////
				case "SE":
					if (Rule2D.intPlayerLatitude - 1 <= 0) {
						// Walking over the edge of the map, we arrive on the other side of the globe via the pole
						if (Rule2D.intPlayerLongitude - Rule2D.MAPWIDTH/2 < 0) {
							Rule2D.intPlayerLongitude = Rule2D.MAPWIDTH - (Rule2D.MAPWIDTH/2 - Rule2D.intPlayerLongitude);
						} else {
							Rule2D.intPlayerLongitude = Rule2D.intPlayerLongitude - Rule2D.MAPWIDTH/2;
							// Make sure that intPlayerLongitude doesn't become zero (0).
							if (Rule2D.intPlayerLongitude <= 0) {
								Rule2D.intPlayerLongitude = Rule2D.MAPWIDTH;
							}
						}
						// Change the character direction to its opposite
						Rule2D.characterDirection = "NW";
					} else {
						// Elsewhere on the map, we just move.
						Rule2D.intPlayerLongitude = Rule2D.intPlayerLongitude - 1;
						Rule2D.intPlayerLatitude  = Rule2D.intPlayerLatitude - 1;
					}
					
					Rule2D.frame.repaint();
					System.out.println("Moving backwards while looking SE"); // Debugging
					break;
				/////////////////////////////////////////////
				///// Move backwards while facing South /////
				/////////////////////////////////////////////
				case "S":
					if (Rule2D.intPlayerLatitude - 1 <= 0) {
						// Walking over the edge of the map, we arrive at the other side of the globe via the pole.
						if (Rule2D.intPlayerLongitude - Rule2D.MAPWIDTH/2 < 0) {
							Rule2D.intPlayerLongitude = Rule2D.MAPWIDTH - (Rule2D.MAPWIDTH/2 - Rule2D.intPlayerLongitude);
						} else {
							Rule2D.intPlayerLongitude = Rule2D.intPlayerLongitude - Rule2D.MAPWIDTH/2;
							// Make sure that intPlayerLongitude doesn't become zero (0).
							if (Rule2D.intPlayerLongitude <= 0) {
								Rule2D.intPlayerLongitude = Rule2D.MAPWIDTH;
							}
						}
						// Change the character direction to its opposite
						Rule2D.characterDirection = "S";
					} else {
						// Elsewhere on the map, we just move.
						Rule2D.intPlayerLongitude = Rule2D.intPlayerLongitude;
						Rule2D.intPlayerLatitude  = Rule2D.intPlayerLatitude - 1;
					}
										
					Rule2D.frame.repaint();
					System.out.println("Moving backwards while looking S"); // Debugging
					break;
				//////////////////////////////////////////////////
				///// Move backwards while facing South West /////
				//////////////////////////////////////////////////
				case "SW":
					if (Rule2D.intPlayerLatitude - 1 <= 0) {
						// Walking over the edge of the map, we arrive on the other side of the globe via the pole
						if (Rule2D.intPlayerLongitude - Rule2D.MAPWIDTH/2 < 0) {
							Rule2D.intPlayerLongitude = Rule2D.MAPWIDTH - (Rule2D.MAPWIDTH/2 - Rule2D.intPlayerLongitude);
						} else {
							Rule2D.intPlayerLongitude = Rule2D.intPlayerLongitude - Rule2D.MAPWIDTH/2;
							// Make sure that intPlayerLongitude doesn't become zero (0).
							if (Rule2D.intPlayerLongitude <= 0) {
								Rule2D.intPlayerLongitude = Rule2D.MAPWIDTH;
							}
						}
						// Change the character direction to its opposite
						Rule2D.characterDirection = "NE";
					} else {
						// Check if we pass the Eastern edge of the map
						if (Rule2D.intPlayerLongitude + 1 > Rule2D.MAPWIDTH) {
							Rule2D.intPlayerLongitude = 1;
							Rule2D.intPlayerLatitude = Rule2D.intPlayerLatitude - 1;
						} else {
							// Elsewhere on the map, we just move.
							Rule2D.intPlayerLongitude = Rule2D.intPlayerLongitude + 1;
							Rule2D.intPlayerLatitude  = Rule2D.intPlayerLatitude - 1;
						}						
					}					
					
					Rule2D.frame.repaint();
					System.out.println("Moving backwards while looking SW"); // Debugging
					break;
				////////////////////////////////////////////
				///// Move backwards while facing West /////
				////////////////////////////////////////////
				case "W":
					if (Rule2D.intPlayerLongitude + 1 > Rule2D.MAPWIDTH) {
						Rule2D.intPlayerLongitude = 1;
					} else {
						Rule2D.intPlayerLongitude = Rule2D.intPlayerLongitude + 1;
					}
					
					Rule2D.intPlayerLatitude  = Rule2D.intPlayerLatitude;
					
					Rule2D.frame.repaint();
					System.out.println("Moving backwards while looking W"); // Debugging
					break;					
				//////////////////////////////////////////////////
				///// Move backwards while facing North West /////
				//////////////////////////////////////////////////
				case "NW":
					if (Rule2D.intPlayerLatitude + 1 > Rule2D.MAPHEIGHT) {
						// Walking over the edge of the map, we arrive at the other side of the globe via the pole.
						if (Rule2D.intPlayerLongitude - Rule2D.MAPWIDTH/2 < 0) {
							Rule2D.intPlayerLongitude = Rule2D.MAPWIDTH - (Rule2D.MAPWIDTH/2 - Rule2D.intPlayerLongitude);							
						} else {
							Rule2D.intPlayerLongitude = Rule2D.intPlayerLongitude - Rule2D.MAPWIDTH/2;
							// Make sure that intPlayerLongitude doesn't become zero (0)
							if (Rule2D.intPlayerLongitude <= 0) {
								Rule2D.intPlayerLongitude = Rule2D.MAPWIDTH;
							}
						}
						// Change the character direction to its opposite
						Rule2D.characterDirection = "SE";
					} else {
						// Check if we pass the Eastern Edge of the map
						if (Rule2D.intPlayerLongitude + 1 > Rule2D.MAPWIDTH) {
							Rule2D.intPlayerLongitude = 1;
							Rule2D.intPlayerLatitude  = Rule2D.intPlayerLatitude + 1;
						} else {
							// Elsewhere on the map, we just move.
							Rule2D.intPlayerLongitude = Rule2D.intPlayerLongitude + 1;
							Rule2D.intPlayerLatitude  = Rule2D.intPlayerLatitude + 1;
						}
						
					}					
					
					Rule2D.frame.repaint();
					System.out.println("Moving backwards while looking NW"); // Debugging
					break;
			}
		}
		
		/////////////////////////////////////
		///// Turn right by 180 degrees /////
		/////////////////////////////////////
		if (e.getKeyCode() == KeyEvent.VK_NUMPAD3) {
			// Turning right by 180 degrees means "walking right" by 4 entries in the String[] movementDirections.
			// If the "walking right" goal would be out of bounds of the String[], start again at the left (String[0]) and "walk" the remaining steps to the right.
			// Find the index "oldIndex" of the current characterDirection
			int currentIndex = -1;
			int newIndex = 0;
			
			currentIndex = getCurrentIndex();
			
			// "Walk right" through the array for 4 steps. If we reach the end of the array, we start at 0 and continue "walking right" the remaining steps.
			// If we "walk right" without passing the end of the array.
			if ((currentIndex + 4) < Rule2D.movementDirections.length) {
				newIndex = currentIndex + 4;
			// If we "walk right" and pass the end of the array. 
			} else {
				newIndex = 4 - (Rule2D.movementDirections.length - currentIndex);
			}
			
			// Set the new characterDirection
			Rule2D.characterDirection = Rule2D.movementDirections[newIndex];
			
			// After changing the player direction, repaint the map to show the correct player direction indicator
			Rule2D.frame.repaint();
		}
		
		///////////////////////////////////////
		///// Turn 45 degrees to the left /////
		///////////////////////////////////////
		if (e.getKeyCode() == KeyEvent.VK_NUMPAD4) {		
			// Turning left by 45 degrees means "walking left" by 1 entry in the String[] movementDirections.
			// If the "walking left" goal would be out of bounds of the String[], start again at the right (String[7]) and "walk" the remaining steps to the left.
			// Find the index "oldIndex" of the current characterDirection
			int currentIndex = -1;
			int newIndex = 0;
			
			currentIndex = getCurrentIndex();
			
			// "Walk left" through the array for 1 step. If we reach the end of the array, we start at String[7] and continue "walking left" the remaining steps.
			// If we "walk left" without passing the end of the array.
			if ((currentIndex - 1) >= 0) {
				newIndex = currentIndex - 1;
			// If we "walk left" and pass the end of the array. 
			} else {
				// How many steps from currentIndex to zero
				newIndex = Rule2D.movementDirections.length - (1 - currentIndex);
			}
			
			// Set the new characterDirection
			Rule2D.characterDirection = Rule2D.movementDirections[newIndex];
			
			// After changing the player direction, repaint the map to show the correct player direction indicator
			Rule2D.frame.repaint();
		}
		
		///////////////////////////////////////////////////////////////////////////////
		///// Pass a tick of time. TODO: Pressing keypad 5 pauses the game a tick. /////
		///////////////////////////////////////////////////////////////////////////////
		if (e.getKeyCode() == KeyEvent.VK_NUMPAD5) {			
			// System.out.println(e.getKeyCode()); // Debugging
			System.out.println("Pausing a tic. No functionality yet!!!!!!!"); // Debugging
		}
		
		////////////////////////////////////////
		///// Turn 45 degrees to the right /////
		////////////////////////////////////////
		if (e.getKeyCode() == KeyEvent.VK_NUMPAD6) {
			// Turning right by 45 degrees means "walking right" by 1 entry in the String[] movementDirections.
			// If the "walking right" goal would be out of bounds of the String[], start again at the left (String[0]) and "walk" the remaining steps to the right.
			// Find the index "oldIndex" of the current characterDirection
			int currentIndex = -1;
			int newIndex = 0;
			
			currentIndex = getCurrentIndex();
			
			// "Walk right" through the array for 1 step. If we reach the end of the array, we start at 0 and continue "walking right" the remaining steps.
			// If we "walk right" without passing the end of the array.
			if ((currentIndex + 1) < Rule2D.movementDirections.length) {
				newIndex = currentIndex + 1;
			// If we "walk right" and pass the end of the array. 
			} else {
				newIndex = 1 - (Rule2D.movementDirections.length - currentIndex);
			}
			
			// Set the new characterDirection
			Rule2D.characterDirection = Rule2D.movementDirections[newIndex];
			
			// After changing the player direction, repaint the map to show the correct player direction indicator
			Rule2D.frame.repaint();
		}
		
		///////////////////////////////////////
		///// Turn 90 degrees to the left /////
		///////////////////////////////////////
		if (e.getKeyCode() == KeyEvent.VK_NUMPAD7) {
			// Turning left by 90 degrees means "walking left" by 2 entries in the String[] movementDirections.
			// If the "walking left" goal would be out of bounds of the String[], start again at the right (String[7]) and "walk" the remaining steps to the left.
			// Find the index "oldIndex" of the current characterDirection
			int currentIndex = -1;
			int newIndex = 0;
			
			currentIndex = getCurrentIndex();
			
			// "Walk left" through the array for 2 steps. If we reach the end of the array, we start at String[7] and continue "walking left" the remaining steps.
			// If we "walk left" without passing the end of the array.
			if ((currentIndex - 2) >= 0) {
				newIndex = currentIndex - 2;
			// If we "walk left" and pass the end of the array. 
			} else {
				// How many steps from currentIndex to zero
				newIndex = Rule2D.movementDirections.length - (2 - currentIndex);
			}
			
			// Set the new characterDirection
			Rule2D.characterDirection = Rule2D.movementDirections[newIndex];
			
			// After changing the player direction, repaint the map to show the correct player direction indicator
			Rule2D.frame.repaint();
		}
		
		/////////////////////////////////////////////////////////////////
		///// Move forward in the direction the character is facing /////
		/////////////////////////////////////////////////////////////////
		if (e.getKeyCode() == KeyEvent.VK_NUMPAD8) {			
			// Determine the direction the player is looking into
			// Calculate changes to intPLayerLongitude and intPlayerLatiude accordingly
			switch(Rule2D.characterDirection) {
				/////////////////////////////////////////////
				///// Move forward while facing North /////
				/////////////////////////////////////////////
				case "N":
					if (Rule2D.intPlayerLatitude - 1 <= 0) {
						// Walking over the edge of the map, we arrive at the other side of the globe via the pole.
						if (Rule2D.intPlayerLongitude - Rule2D.MAPWIDTH/2 < 0) {
							Rule2D.intPlayerLongitude = Rule2D.MAPWIDTH - (Rule2D.MAPWIDTH/2 - Rule2D.intPlayerLongitude);
						} else {
							Rule2D.intPlayerLongitude = Rule2D.intPlayerLongitude - Rule2D.MAPWIDTH/2;
							// Make sure that intPlayerLongitude doesn't become zero (0).
							if (Rule2D.intPlayerLongitude <= 0) {
								Rule2D.intPlayerLongitude = Rule2D.MAPWIDTH;
							}
						}
						// Change the character direction to its opposite
						Rule2D.characterDirection = "S";
					} else {
						// Elsewhere on the map, we just move.
						Rule2D.intPlayerLongitude = Rule2D.intPlayerLongitude;
						Rule2D.intPlayerLatitude  = Rule2D.intPlayerLatitude - 1;
					}
										
					Rule2D.frame.repaint();
					System.out.println("Moving forward while looking N"); // Debugging					
					break;
				//////////////////////////////////////////////////
				///// Move forward while facing North East /////
				//////////////////////////////////////////////////
				case "NE":					
					if (Rule2D.intPlayerLatitude - 1 <= 0) {
						// Walking over the edge of the map, we arrive at the other side of the globe via the pole.
						if (Rule2D.intPlayerLongitude - Rule2D.MAPWIDTH/2 < 0) {
							Rule2D.intPlayerLongitude = Rule2D.MAPWIDTH - (Rule2D.MAPWIDTH/2 - Rule2D.intPlayerLongitude);							
						} else {
							Rule2D.intPlayerLongitude = Rule2D.intPlayerLongitude - Rule2D.MAPWIDTH/2;
							// Make sure that intPlayerLongitude doesn't become zero (0).
							if (Rule2D.intPlayerLongitude <= 0) {
								Rule2D.intPlayerLongitude = Rule2D.MAPWIDTH;
							}
						}
						// Change the character direction to its opposite
						Rule2D.characterDirection = "SW";
					} else {
						// Check if we pass the Eastern edge of the map
						if (Rule2D.intPlayerLongitude + 1 > Rule2D.MAPWIDTH) {
							Rule2D.intPlayerLongitude = 1;
							Rule2D.intPlayerLatitude = Rule2D.intPlayerLatitude - 1;
						} else {
							// Elsewhere on the map, we just move.
							Rule2D.intPlayerLongitude = Rule2D.intPlayerLongitude + 1;
							Rule2D.intPlayerLatitude = Rule2D.intPlayerLatitude - 1;
						}						
					}
					
					Rule2D.frame.repaint();
					System.out.println("Moving forward while looking NE"); // Debugging
					break;
				////////////////////////////////////////////
				///// Move forwards while facing East /////
				////////////////////////////////////////////
				case "E":
					if (Rule2D.intPlayerLongitude + 1 > Rule2D.MAPWIDTH) {
						Rule2D.intPlayerLongitude = 1;
					} else {
						Rule2D.intPlayerLongitude = Rule2D.intPlayerLongitude + 1;
					}
					
					Rule2D.intPlayerLatitude  = Rule2D.intPlayerLatitude;
					
					Rule2D.frame.repaint();
					System.out.println("Moving forwards while looking E"); // Debugging
					break;
				////////////////////////////////////////////////
				///// Move forward while facing South East /////
				////////////////////////////////////////////////
				case "SE":
					if (Rule2D.intPlayerLatitude + 1 > Rule2D.MAPHEIGHT) {
						// Walking over the edge of the map, we arrive on the other side of the globe via the pole
						if (Rule2D.intPlayerLongitude - Rule2D.MAPWIDTH/2 < 0) {
							Rule2D.intPlayerLongitude = Rule2D.MAPWIDTH - (Rule2D.MAPWIDTH/2 - Rule2D.intPlayerLongitude);
						} else {
							Rule2D.intPlayerLongitude = Rule2D.intPlayerLongitude - Rule2D.MAPWIDTH/2;
							// Make sure that intPlayerLongitude doesn't become zero (0).
							if (Rule2D.intPlayerLongitude <= 0) {
								Rule2D.intPlayerLongitude = Rule2D.MAPWIDTH;
							}
						}
						// Change the character direction to its opposite
						Rule2D.characterDirection = "NW";
					} else {
						// Check if we pass the Estern edge of the map
						if (Rule2D.intPlayerLongitude + 1 > Rule2D.MAPWIDTH) {
							Rule2D.intPlayerLongitude = 1;
							Rule2D.intPlayerLatitude  = Rule2D.intPlayerLatitude + 1;
						} else {
							// Elsewhere on the map, we just move.
							Rule2D.intPlayerLongitude = Rule2D.intPlayerLongitude + 1;
							Rule2D.intPlayerLatitude  = Rule2D.intPlayerLatitude + 1;
						}
					}
					
					Rule2D.frame.repaint();
					System.out.println("Moving forward while looking SE"); // Debugging
					break;
				/////////////////////////////////////////////
				///// Move forward while facing South /////
				/////////////////////////////////////////////
				case "S":
					if (Rule2D.intPlayerLatitude + 1 > Rule2D.MAPHEIGHT) {
						// Walking over the edge of the map, we arrive at the other side of the globe via the pole.
						if (Rule2D.intPlayerLongitude - Rule2D.MAPWIDTH/2 < 0) {
							Rule2D.intPlayerLongitude = Rule2D.MAPWIDTH - (Rule2D.MAPWIDTH/2 - Rule2D.intPlayerLongitude);
						} else {
							Rule2D.intPlayerLongitude = Rule2D.intPlayerLongitude - Rule2D.MAPWIDTH/2;
							// Make sure that intPlayerLongitude doesn't become zero (0).
							if (Rule2D.intPlayerLongitude <= 0) {
								Rule2D.intPlayerLongitude = Rule2D.MAPWIDTH;
							}
						}
						// Change the character direction to its opposite
						Rule2D.characterDirection = "N";
					} else {
						// Elsewhere on the map, we just move.
						Rule2D.intPlayerLongitude = Rule2D.intPlayerLongitude;
						Rule2D.intPlayerLatitude  = Rule2D.intPlayerLatitude + 1;
					}
					
					Rule2D.frame.repaint();
					System.out.println("Moving forward while looking S"); // Debugging
					break;
				//////////////////////////////////////////////////
				///// Move forward while facing South West /////
				//////////////////////////////////////////////////
				case "SW":
					if (Rule2D.intPlayerLatitude + 1 > Rule2D.MAPHEIGHT) {
						// Walking over the edge of the map, we arrive on the other side of the globe via the pole
						if (Rule2D.intPlayerLongitude - Rule2D.MAPWIDTH/2 < 0) {
							Rule2D.intPlayerLongitude = Rule2D.MAPWIDTH - (Rule2D.MAPWIDTH/2 - Rule2D.intPlayerLongitude);
						} else {
							Rule2D.intPlayerLongitude = Rule2D.intPlayerLongitude - Rule2D.MAPWIDTH/2;
							// Make sure that intPlayerLongitude doesn't become zero (0).
							if (Rule2D.intPlayerLongitude <= 0) {
								Rule2D.intPlayerLongitude = Rule2D.MAPWIDTH;
							}
						}
						// Change the character direction to its opposite
						Rule2D.characterDirection = "NE";
					} else {
						// Check if we pass the Western edge of the map
						if (Rule2D.intPlayerLongitude - 1 <= 0) {
							Rule2D.intPlayerLongitude  = Rule2D.MAPWIDTH;
							Rule2D.intPlayerLatitude  = Rule2D.intPlayerLatitude + 1;
						} else {
							// Elsewhere on the map, we just move.
							Rule2D.intPlayerLongitude = Rule2D.intPlayerLongitude - 1;
							Rule2D.intPlayerLatitude  = Rule2D.intPlayerLatitude + 1;
						}						
					}					
					
					Rule2D.frame.repaint();
					System.out.println("Moving backwards while looking SW"); // Debugging
					break;
				////////////////////////////////////////////
				///// Move forward while facing West /////
				////////////////////////////////////////////
				case "W":					
					if (Rule2D.intPlayerLongitude - 1 == 0) {
						Rule2D.intPlayerLongitude = Rule2D.MAPWIDTH;
					} else {
						Rule2D.intPlayerLongitude = Rule2D.intPlayerLongitude - 1;
					}
					
					Rule2D.intPlayerLatitude  = Rule2D.intPlayerLatitude;					
					
					Rule2D.frame.repaint();
					System.out.println("Moving forward while looking W"); // Debugging
					break;					
				//////////////////////////////////////////////////
				///// Move forward while facing North West /////
				//////////////////////////////////////////////////
				case "NW":
					if (Rule2D.intPlayerLatitude - 1 <= 0) {
						// Walking over the edge of the map, we arrive at the other side of the globe via the pole.
						if (Rule2D.intPlayerLongitude - Rule2D.MAPWIDTH/2 < 0) {
							Rule2D.intPlayerLongitude = Rule2D.MAPWIDTH - (Rule2D.MAPWIDTH/2 - Rule2D.intPlayerLongitude);							
						} else {
							Rule2D.intPlayerLongitude = Rule2D.intPlayerLongitude - Rule2D.MAPWIDTH/2;
							// Make sure that intPlayerLongitude doesn't become zero (0)
							if (Rule2D.intPlayerLongitude <= 0) {
								Rule2D.intPlayerLongitude = Rule2D.MAPWIDTH;
							}
						}
						// Change the character direction to its opposite
						Rule2D.characterDirection = "SE";
					} else {
						// Check if we pass the Western edge of the map
						if (Rule2D.intPlayerLongitude -1 <= 0) {
							Rule2D.intPlayerLongitude = Rule2D.MAPWIDTH;
							Rule2D.intPlayerLatitude  = Rule2D.intPlayerLatitude - 1;
						} else {
							// Elsewhere on the map, we just move.
							Rule2D.intPlayerLongitude = Rule2D.intPlayerLongitude - 1;
							Rule2D.intPlayerLatitude  = Rule2D.intPlayerLatitude - 1;
						}
					}
					
					Rule2D.frame.repaint();
					System.out.println("Moving forward while looking NW"); // Debugging
					break;
			}
		}
		
		////////////////////////////////////////
		///// Turn 90 degrees to the right /////
		////////////////////////////////////////
		if (e.getKeyCode() == KeyEvent.VK_NUMPAD9) {
			// Turning right by 90 degrees means "walking right" by 2 entries in the String[] movementDirections.
			// If the "walking right" goal would be out of bounds of the String[], start again at the left (String[0]) and "walk" the remaining steps to the right.
			// Find the index "oldIndex" of the current characterDirection
			int currentIndex = -1;
			int newIndex = 0;
			
			currentIndex = getCurrentIndex();
			
			// "Walk right" through the array for 2 steps. If we reach the end of the array, we start at 0 and continue "walking right" the remaining steps.
			// If we "walk right" without passing the end of the array.
			if ((currentIndex + 2) < Rule2D.movementDirections.length) {
				newIndex = currentIndex + 2;
			// If we "walk right" and pass the end of the array. 
			} else {
				newIndex = 2 - (Rule2D.movementDirections.length - currentIndex);
			}
			
			// Set the new characterDirection
			Rule2D.characterDirection = Rule2D.movementDirections[newIndex];
			
			// After changing the player direction, repaint the map to show the correct player direction indicator
			Rule2D.frame.repaint();
		}
	}
	
	private int getCurrentIndex() {
		int currentIndex = -1;
		
		for (int i = 0; i < Rule2D.movementDirections.length; i++) {
			if (Rule2D.movementDirections[i].equals(Rule2D.characterDirection)) {
				currentIndex = i;
				break;
			}
		}
		
		return currentIndex;
	}
}
