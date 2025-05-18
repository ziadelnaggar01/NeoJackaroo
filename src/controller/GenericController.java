package controller;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;

public final class GenericController {

	// Private constructor to prevent instantiation
	private GenericController() {
	}

	// general-purpose version of switch scene, not tied to a mouse or keyboard event
	public static void switchScene(Stage stage, Parent root) {
		Scene currentScene = stage.getScene();
		if (currentScene != null)
			currentScene.setRoot(root);
		else
			stage.setScene(new Scene(root));
	}

	// keep this for MouseEvent-based transitions
	public static void switchScene(MouseEvent event, Parent root) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		switchScene(stage, root); // delegate to the new one
	}

	// keep this for KeyEvent-based transitions
	public static void switchScene(KeyEvent event, Parent root) {
	    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    switchScene(stage, root);
	}

	public static void buttonGlowON(ImageView image, Color color) {
		DropShadow shadow = new DropShadow();
		shadow.setColor(color);
		shadow.setRadius(20);
		image.setEffect(shadow);
	}

	public static void buttonGlowON(ImageView image, Color color, int radius) {
		DropShadow shadow = new DropShadow();
		shadow.setColor(color);
		shadow.setRadius(radius);
		image.setEffect(shadow);
	}

	public static void buttonGlowOFF(ImageView image) {
		image.setEffect(null);
	}
	
	
	public static String extractCardRank(String ID){
		String rank="";
		for (int i=0;i<ID.length();i++){
			char c=ID.charAt(i);
			if (c>='0'&&c<='9'){// assumption: numbers only used to represent rank 
				rank+=c;
			}
		}
		return rank;
	}
	
}
