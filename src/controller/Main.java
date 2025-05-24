package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import view.startMenu.Controller;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// 1) Load the splash screen FXML
		Parent splashRoot = FXMLLoader.load(getClass().getResource("/view/splashScene/Scene.fxml"));
		// 🔸 Attach scene to stage and display
		Controller.initalizeStage(primaryStage);
		
		// Set Cursor
		Scene scene = new Scene(splashRoot);
		Image pointerImage = new Image(getClass().getResource("/view/assets/Mouse Cursor.png").toExternalForm());
		scene.setCursor(new ImageCursor(pointerImage, 5, 2));
		
		// Show Scene
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		// Launch the application
		launch(args);
	}

}
