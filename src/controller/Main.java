package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application{
int initialWidth = 1920;
int initialHeight = 1080;
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("NeoJackaroo");
		primaryStage.setFullScreen(true);
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/view/assets/Game Icon.png")));

		
		Parent root = FXMLLoader.load(getClass().getResource("/view/startMenu.fxml"));
	    // 🔸 Create scene with the FXML root
	    Scene scene = new Scene(root);
	    // 🔸 Attach scene to stage and display
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
	}
	
	public static void main(String[] args) {
        // Launch the application
        launch(args);
    }

}
