package controller;

import java.io.IOException;

import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

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
	
//	public static void switchSceneWithFade(Stage stage, String fxmlPath) {
//	    try {
//	        Parent newRoot = FXMLLoader.load(getClass().getResource(fxmlPath));
//	        Scene currentScene = stage.getScene();
//
//	        // Set opacity to 0 and add the new root temporarily
//	        newRoot.setOpacity(0);
//
//	        // Swap the root and fade it in
//	        currentScene.setRoot(newRoot);
//
//	        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), newRoot);
//	        fadeIn.setFromValue(0);
//	        fadeIn.setToValue(1);
//	        fadeIn.play();
//
//	    } catch (IOException e) {
//	        e.printStackTrace();
//	    }
//	}
    
}
