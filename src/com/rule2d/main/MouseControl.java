package com.rule2d.main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class MouseControl {
	public void mousePressed(MouseEvent mouseEvent) {
		switch(Rule2D.windowStatus) {
			case "IntroScreen":
				windowStatusIntroScreen(mouseEvent);
				break;
			case "CreateMap":
				windowStatusCreateMapScreen(mouseEvent);
				break;
			case "CreateCharacter":
				windowStatusCreateCharacterScreen(mouseEvent);
				break;
			case "CreateMonster":
				windowStatusCreateMonsterScreen(mouseEvent);
				break;
		}
	}
	
	public void windowStatusIntroScreen(MouseEvent mouseEvent) {
		// Button "Create Map" clicked
		if (mouseEvent.getX() >= 0 && mouseEvent.getX() <= 200) {
			if (mouseEvent.getY() >= 0 && mouseEvent.getY() <= 100) {
				Rule2D.windowStatus = "CreateMap";
				Rule2D.frame.repaint();
			}
			// Button "Create isometric map" clicked	
			if(mouseEvent.getY() >= 150 && mouseEvent.getY() <= 250) {
				Rule2D.windowStatus = "CreateIsometricMap";
				Rule2D.frame.repaint();
			}
		// Button "Create Character" clicked
		} else if (mouseEvent.getX() >= 250 && mouseEvent.getX() <= 450) {
			if (mouseEvent.getY() >= 0 && mouseEvent.getY() <= 100) {
				Rule2D.windowStatus = "CreateCharacter";
				Rule2D.frame.repaint();
			}
		// Button "Create monster" clicked
		} else if (mouseEvent.getX() >= 500 && mouseEvent.getX() <= 700) {
			if (mouseEvent.getY() >= 0 && mouseEvent.getY() <= 100) {
				Rule2D.windowStatus = "CreateMonster";
				Rule2D.frame.repaint();
			}
		}
	}
	
	public void windowStatusCreateMapScreen(MouseEvent mouseEvent) {
		
	}
	
	public void windowStatusCreateCharacterScreen(MouseEvent mouseEvent) {
		
	}
	
	public void windowStatusCreateMonsterScreen(MouseEvent mouseEvent) {
		
	}
	
	public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
		int mapZoomFactor = Rule2D.mapZoomFactor;
		
		if (mouseWheelEvent.getWheelRotation() > 0) {
			// It is possible to zoom out twice, not more often			
			if (mapZoomFactor > -2) {
				Rule2D.mapZoomFactor = mapZoomFactor - 1;
				System.out.println("Mouse wheel down, zooming out!"); // Debugging
				
				Rule2D.frame.repaint();
			}			
		} else {
			// It is possible to zoom in to mapZoomFactor = 0, not closer
			if (mapZoomFactor < 0) {
				Rule2D.mapZoomFactor = mapZoomFactor + 1;
				System.out.println("Mouse wheel up, zooming in!"); // Debugging
				
				Rule2D.frame.repaint();
			}
		}
	}
}
