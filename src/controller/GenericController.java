package controller;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;

public final class GenericController {
	
    // Private constructor to prevent instantiation
    private GenericController() {}
    
	public static void switchScene(MouseEvent event, Parent root) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene currentScene = stage.getScene();
	    if (currentScene != null) 
	    	 currentScene.setRoot(root);
	    else
	    {
	        // Fallback if no scene exists (initial load)
	        currentScene = new Scene(root);
	        stage.setScene(currentScene);
	    } 
	}
	
	public static void buttonGlowON(ImageView image, Color color)
	{
		DropShadow shadow = new DropShadow();
		shadow.setColor(color);
		shadow.setRadius(20);
		image.setEffect(shadow);
	}
	
	public static void buttonGlowON(ImageView image, Color color, int radius)
	{
		DropShadow shadow = new DropShadow();
		shadow.setColor(Color.RED);
		shadow.setRadius(radius);
		image.setEffect(shadow);
	}
	
	public static void buttonGlowOFF(ImageView image)
	{
		image.setEffect(null);
	}   
}
