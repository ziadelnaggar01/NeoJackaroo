package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class BoardView extends Application {

	int initialWidth = 1920;
	int initialHeight = 1080;
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BoardScene.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 900, 800);
            primaryStage.setFullScreen(true);
        	primaryStage.setTitle("NeoJackaroo");
    		primaryStage.setFullScreen(true);
        	primaryStage.setFullScreenExitHint("");
    		scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

            // Optional: Uncomment if you have a CSS file
            // scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

    		 primaryStage.setScene(scene);
    		    
    	        // Initial fixed size constraints
    	        primaryStage.setMinWidth(initialWidth);
    	        primaryStage.setMinHeight(initialHeight);
    	        primaryStage.setMaxWidth(initialWidth);
    	        primaryStage.setMaxHeight(initialHeight);

    	        // Intercept maximize button click to enter fullscreen
    	        primaryStage.maximizedProperty().addListener((obs, wasMaximized, isMaximized) -> {
    	            if (isMaximized) {
    	                primaryStage.setFullScreen(true); // Enter fullscreen
    	            }
    	        });

    	        // Reset to fixed size when exiting fullscreen
    	        primaryStage.fullScreenProperty().addListener((obs, wasFull, isFull) -> {
    	            if (!isFull) {
    	                // Restore fixed constraints and size
    	                primaryStage.setMinWidth(initialWidth);
    	                primaryStage.setMinHeight(initialHeight);
    	                primaryStage.setMaxWidth(initialWidth);
    	                primaryStage.setMaxHeight(initialHeight);
    	                primaryStage.setWidth(initialWidth);
    	                primaryStage.setHeight(initialHeight);
    	            }
    	        });
    		    primaryStage.show();
        } catch (IOException e) {
            System.err.println("FXML file could not be loaded.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
