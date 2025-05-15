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
	    primaryStage.show();
	}
	
	public static void main(String[] args) {
        // Launch the application
        launch(args);
    }

}
