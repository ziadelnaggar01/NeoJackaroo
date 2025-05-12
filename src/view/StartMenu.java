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

	// Method for Mouse Entered event
	@FXML
	private void redButtonOnMouseEntered() {
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
	private void buttonOnMouseExited() {
		// Remove the effect when the mouse exits
		startButton.setEffect(null);
	}

	@FXML
	private void switchSceswitchToNameEntry(MouseEvent event) throws Exception {
		// Load the new scene's FXML
		Parent root = FXMLLoader.load(getClass().getResource("playerName.fxml"));
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		// Switch to the new scene
		loadScene(stage, scene);
	}
	
	public static void loadScene(Stage primaryStage, Scene scene) throws Exception
	{
		primaryStage.setScene(scene);
		primaryStage.setFullScreen(true);
        primaryStage.show();
	}
	
	public static void initalizeStage(Stage primaryStage) throws Exception
	{
		int initialWidth = 1920;
		int initialHeight = 1080;
		
		primaryStage.setTitle("NeoJackaroo");
		primaryStage.setFullScreen(true);
		primaryStage.getIcons().add(new Image("/view/assets/Game Icon.png"));
		primaryStage.setFullScreenExitHint("");
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
	}
}
