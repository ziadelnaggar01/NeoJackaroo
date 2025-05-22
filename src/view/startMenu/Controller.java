package view.startMenu;

import controller.GenericController;
import controller.SceneConfig;
import controller.SoundManager;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class Controller {

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
		GenericController.buttonGlowON(startButton, Color.RED);
	}

	// Method for Mouse Exited event
	@FXML
	private void startButtonOnMouseExited() {
		GenericController.buttonGlowOFF(startButton);
	}

	@FXML
	private void settingsButtonOnMouseEntered() {
		GenericController.buttonGlowON(settingsButton, Color.PURPLE);
	}

	// Method for Mouse Exited event
	@FXML
	private void settingsButtonOnMouseExited() {
		GenericController.buttonGlowOFF(settingsButton);
	}

	@FXML
	private void HWTButtonOnMouseEntered() {
		GenericController.buttonGlowON(HWTButton, Color.CYAN);
	}

	// Method for Mouse Exited event
	@FXML
	private void HWTButtonOnMouseExited() {
		GenericController.buttonGlowOFF(HWTButton);
	}

	@FXML
	private void exitButtonOnMouseEntered() {
		GenericController.buttonGlowON(exitButton, Color.RED);
	}

	// Method for Mouse Exited event
	@FXML
	private void exitButtonOnMouseExited() {
		GenericController.buttonGlowOFF(exitButton);
	}



	@FXML
	private void switchSceneToNameEntry(MouseEvent event) throws Exception {
		SoundManager.getInstance().playSound("button_click");
		Parent root = SceneConfig.getInstance().getPlayerNameScene();
		GenericController.switchScene(event, root);
	}

	@FXML
	private void switchSceneToHWTPlay(MouseEvent event) throws Exception {
		SoundManager.getInstance().playSound("button_click");
		Parent root = SceneConfig.getInstance().getHowToPlayScene();
		GenericController.switchScene(event, root);
	}

	@FXML
	private void switchSceneToSettings(MouseEvent event) throws Exception {
		SoundManager.getInstance().playSound("button_click");
		SceneConfig.getInstance().setInGame(false);
		Parent root = SceneConfig.getInstance().getSettingsScene();
		GenericController.switchScene(event, root);
	}

	@FXML
	private void closeGame(MouseEvent event) {
		SoundManager.getInstance().playSound("button_click");
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.close();
	}
}
