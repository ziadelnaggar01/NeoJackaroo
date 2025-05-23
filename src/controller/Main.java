package controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.startMenu.Controller;

public class Main extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		Scene scene = new Scene(SceneConfig.getInstance().getStartScene());
		// 🔸 Attach scene to stage and display
		Controller.initalizeStage(primaryStage);
		primaryStage.setScene(scene);

		MusicManager.getInstance().playMusic("/view/assets/audio/Digital Voyage - Twin Musicom.mp3"); 
		SoundManager.getInstance().preloadSound("button_click", "/view/assets/audio/button click.mp3");
		SoundManager.getInstance().preloadSound("Marble_Selection", "/view/assets/audio/Marble Selection.mp3");
		primaryStage.show();
	}

	public static void main(String[] args) {
		// Launch the application
		launch(args);
	}

}
