package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class StartMenu {

	public void startGame()
	{
		System.out.print("hi");
	}

	@FXML
	private ImageView startButton;  // This should match the fx:id in Scene Builder
	@FXML
	private ImageView settingsButton;
	@FXML
	private ImageView HWTButton;
	@FXML
	private ImageView exitButton;

	// Method for Mouse Entered event
	@FXML
	private void startButtonOnMouseEntered() {
		// Apply a red DropShadow effect on the image
		DropShadow shadow = new DropShadow();
		shadow.setColor(Color.RED);
		shadow.setRadius(15);
		shadow.setOffsetX(0);
		shadow.setOffsetY(0);
		startButton.setEffect(shadow);
	}
	
	// Method for Mouse Exited event
	@FXML
	private void startButtonOnMouseExited() {
		// Remove the effect when the mouse exits
		startButton.setEffect(null);
	}
	
	@FXML
	private void settingsButtonOnMouseEntered() {
		// Apply a red DropShadow effect on the image
		DropShadow shadow = new DropShadow();
		shadow.setColor(Color.PURPLE);
		shadow.setRadius(15);
		shadow.setOffsetX(0);
		shadow.setOffsetY(0);
		settingsButton.setEffect(shadow);
	}
	
	// Method for Mouse Exited event
	@FXML
	private void settingsButtonOnMouseExited() {
		// Remove the effect when the mouse exits
		settingsButton.setEffect(null);
	}
	
	@FXML
	private void HWTButtonOnMouseEntered() {
		// Apply a red DropShadow effect on the image
		DropShadow shadow = new DropShadow();
		shadow.setColor(Color.CYAN);
		shadow.setRadius(15);
		shadow.setOffsetX(0);
		shadow.setOffsetY(0);
		HWTButton.setEffect(shadow);
	}
	
	// Method for Mouse Exited event
	@FXML
	private void HWTButtonOnMouseExited() {
		// Remove the effect when the mouse exits
		HWTButton.setEffect(null);
	}
	
	@FXML
	private void exitButtonOnMouseEntered() {
		// Apply a red DropShadow effect on the image
		DropShadow shadow = new DropShadow();
		shadow.setColor(Color.RED);
		shadow.setRadius(15);
		shadow.setOffsetX(0);
		shadow.setOffsetY(0);
		exitButton.setEffect(shadow);
	}
	
	// Method for Mouse Exited event
	@FXML
	private void exitButtonOnMouseExited() {
		// Remove the effect when the mouse exits
		exitButton.setEffect(null);
	}



	@FXML
	private void switchSceneToNameEntry(MouseEvent event) throws Exception {
		// Load the new scene's FXML
		Parent root = FXMLLoader.load(getClass().getResource("playerName.fxml"));
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
	
	@FXML
	private void switchSceneToHWTPlay(MouseEvent event) throws Exception {
		// Load the new scene's FXML
		Parent root = FXMLLoader.load(getClass().getResource("HWTPlay.fxml"));
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
	
	
	public static void initalizeStage(Stage primaryStage) throws Exception
	{
		int initialWidth = 1920;
		int initialHeight = 1080;
		
		primaryStage.setTitle("NeoJackaroo");
		primaryStage.setFullScreen(true);
		primaryStage.getIcons().add(new Image("/view/assets/Game Icon.png"));
		primaryStage.setFullScreenExitHint("");

        // Intercept maximize button click to enter fullscreen
        primaryStage.maximizedProperty().addListener((obs, wasMaximized, isMaximized) -> {
            if (isMaximized) {
                primaryStage.setFullScreen(true); // Enter fullscreen
            }
        });

        // Reset to fixed size when exiting fullscreen
        primaryStage.fullScreenProperty().addListener((obs, wasFull, isFull) -> {
            if (isFull) 
            {
                // entering full-screen → remove constraints
                primaryStage.setMinWidth(0);
                primaryStage.setMinHeight(0);
                primaryStage.setMaxWidth(Double.MAX_VALUE);
                primaryStage.setMaxHeight(Double.MAX_VALUE);
            } 
            else  
            {
                // Restore fixed constraints and size
                primaryStage.setMinWidth(initialWidth);
                primaryStage.setMinHeight(initialHeight);
                primaryStage.setMaxWidth(initialWidth);
                primaryStage.setMaxHeight(initialHeight);
                primaryStage.setWidth(initialWidth);
                primaryStage.setHeight(initialHeight);
            }
        });
	}
	
	@FXML
	private void closeGame(MouseEvent event) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
	}
}
