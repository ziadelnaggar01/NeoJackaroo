package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.startMenu.Controller;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// 1) Load the splash screen FXML
		Parent splashRoot  = FXMLLoader.load(getClass().getResource("/view/splashScene/Scene.fxml"));
		// 🔸 Attach scene to stage and display
		Controller.initalizeStage(primaryStage);
		primaryStage.setScene(new Scene(splashRoot));
		primaryStage.show();

		SoundManager.getInstance().preloadSound("button_click", "/view/assets/audio/button click.mp3");
		SoundManager.getInstance().preloadSound("Marble_Selection", "/view/assets/audio/Marble Selection.mp3");
		SoundManager.getInstance().preloadSound("Card_Selection", "/view/assets/audio/Card Selection.mp3");
		SoundManager.getInstance().preloadSound("errorSoundEffect", "/view/assests/sound/errorSoundEffect.mp3");
		SoundManager.getInstance().preloadSound("skipTurnSound", "/view/assests/sound/skipTurnSound.mp3");
	}

	public static void main(String[] args) {
		// Launch the application
		launch(args);
	}

}
