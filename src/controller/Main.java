package controller;

import engine.Game;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.startMenu.Controller;

public class Main extends Application{
	@Override
	public void start(Stage primaryStage) throws Exception {
		Game game;
		
		Parent root = FXMLLoader.load(getClass().getResource("/view/startMenu/Scene.fxml"));
	    // 🔸 Create scene with the FXML root
	    Scene scene = new Scene(root);
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
