package view.PlayerName;

import view.BoardController;
import controller.GenericController;
import controller.MusicManager;
import controller.SceneConfig;
import controller.SoundManager;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Controller {

	@FXML
	private ImageView okButton;
	@FXML
	private ImageView cancelButton;
	@FXML
	private TextField nameTextField;
	@FXML
	private Label popUpLabel;

	@FXML
	private void switchSceneToStartMenu(MouseEvent event) throws Exception {
		SoundManager.getInstance().playSound("button_click");
		Parent root = SceneConfig.getInstance().getStartScene();
		GenericController.switchScene(event, root);
	}

	@FXML
	private void switchSceneToGame(MouseEvent event) throws Exception {
		SoundManager.getInstance().playSound("button_click");
		if (nameTextField.getText().trim().isEmpty()) {
			popUpLabel.setText("Please enter a name!");
			shakeNode(popUpLabel);
		} else {
			BoardController controller = SceneConfig.getInstance()
					.getBoardController();

			Parent root = SceneConfig.getInstance().getGameScene();
			SceneConfig.getInstance().setPlayerName(
					nameTextField.getText().trim());
			GenericController.switchScene(event, root);
			controller.fadeIn();
			SoundManager.getInstance().playSound("startGame");

			MusicManager.getInstance().playMusic("/view/assets/audio/Vibe.mp3");

		}
	}

	@FXML
	private void keyboardSwitchSceneToGame(KeyEvent event) throws Exception {
		SoundManager.getInstance().playSound("button_click");
		if (event.getCode() == KeyCode.ENTER) {
			if (nameTextField.getText().trim().isEmpty()) {
				popUpLabel.setText("Please enter a name!");
				shakeNode(popUpLabel);
			} else {
				Parent root = SceneConfig.getInstance().getGameScene();
				SceneConfig.getInstance().setPlayerName(
						nameTextField.getText().trim());
				GenericController.switchScene(event, root);
				MusicManager.getInstance().playMusic(
						"/view/assets/audio/Vibe.mp3");
			}
		}
	}

	// tip

	private void shakeNode(Node node) {
		TranslateTransition tt = new TranslateTransition(Duration.millis(50),
				node);
		tt.setFromX(0);
		tt.setByX(10);
		tt.setCycleCount(6);
		tt.setAutoReverse(true);
		tt.play();
	}

	@FXML
	private void okButtonOnMouseEntered() {

		GenericController.buttonGlowON(okButton, Color.PURPLE);
	}

	@FXML
	private void cancelButtonOnMouseEntered() {

		GenericController.buttonGlowON(cancelButton, Color.CYAN);
	}

	// Method for Mouse Exited event
	@FXML
	private void okbuttonOnMouseExited() {
		GenericController.buttonGlowOFF(okButton);
	}

	@FXML
	private void cancelbuttonOnMouseExited() {
		GenericController.buttonGlowOFF(cancelButton);
	}
}
