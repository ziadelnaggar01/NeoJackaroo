package view.endScreen;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Colour;
import controller.GenericController;
import controller.SceneConfig;
import controller.SoundManager;

public class Controller {
	@FXML
	private ImageView exitButton;
	
	@FXML
	private ImageView backButton;

	@FXML
	private Label winnerName;

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
	private void backButtonOnMouseEntered() {
		GenericController.buttonGlowON(backButton, Color.YELLOW);
	}

	// Method for Mouse Exited event
	@FXML
	private void backButtonOnMouseExited() {
		GenericController.buttonGlowOFF(backButton);
	}
	
	public void updateWinner(Colour winner)
	{
		String winnerColor = " Player Won!!";
		switch (winner) {
		case RED:
			winnerColor = "Red" +winnerColor;
			break;
		case BLUE:
			winnerColor = "Blue" +winnerColor;
			break;
		case GREEN:
			winnerColor = "Green" +winnerColor;
			break;
		case YELLOW:
			winnerColor = "Yellow" +winnerColor;
			break;
		default:
			winnerColor = "Game Over";
		}
		winnerName.setText(winnerColor);
	}
	
	@FXML
	private void onBackButtonClicked(MouseEvent event) {
		//Go back to start screen, and launch a new game
		SoundManager.getInstance().playSound("button_click");
		Parent root = SceneConfig.getInstance().getStartScene();
		GenericController.switchScene(event, root);
		SceneConfig.getInstance().createNewGame();
	}

	@FXML
	private void closeGame(MouseEvent event) {
		SoundManager.getInstance().playSound("button_click");
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.close();
	}
}
