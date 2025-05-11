package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

public class Main extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("NeoJackaroo");
		primaryStage.setFullScreen(true);
		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/view/assets/Game Icon.png")));
		
		primaryStage.setFullScreenExitHint("");
		primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		
		Parent root = FXMLLoader.load(getClass().getResource("/view/startMenu.fxml"));
	    // 🔸 Create scene with the FXML root
	    Scene scene = new Scene(root);
	    // 🔸 Attach scene to stage and display
	    primaryStage.setScene(scene);
	    primaryStage.show();
	}
	
	public static void main(String[] args) {
        // Launch the application
        launch(args);
    }

}
