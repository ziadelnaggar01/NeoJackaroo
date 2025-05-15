package view.endScreen;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import controller.GenericController;
import controller.SoundManager;

public class Controller {
	@FXML
	private ImageView exitButton;
	
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
	private void closeGame(MouseEvent event) {
		SoundManager.getInstance().playSound("button_click");
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
	}
}
