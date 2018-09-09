package com.rule2d.main;

import java.awt.event.MouseEvent;

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
		if (mouseEvent.getX() >= 0 && mouseEvent.getX() <= 200) {
			// Button "Create Map" clicked
			if (mouseEvent.getY() >= 0 && mouseEvent.getY() <= 100) {
				Rule2D.windowStatus = "CreateMap";
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
}
