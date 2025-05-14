package generic;

import java.io.IOException;

import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GenericController {
	public void switchSceneWithFade(Stage stage, String fxmlPath) {
	    try {
	        Parent newRoot = FXMLLoader.load(getClass().getResource(fxmlPath));
	        Scene currentScene = stage.getScene();

	        // Set opacity to 0 and add the new root temporarily
	        newRoot.setOpacity(0);

	        // Swap the root and fade it in
	        currentScene.setRoot(newRoot);

	        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), newRoot);
	        fadeIn.setFromValue(0);
	        fadeIn.setToValue(1);
	        fadeIn.play();

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
}
