package view.exception;

import controller.GenericController;
import controller.SceneConfig;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Controller {

	// The exception pop up method must be called to update the exception scene
	// before switching to the exception scene
	//send a node to get the stage of the board scene
	public void exceptionPopUp(Exception e, Node someNode) {
		String msg = e.getMessage();
	    exceptionMessageLabel.setText(msg);

	    Parent root = SceneConfig.getInstance().getExceptionScene();
	    Stage stage = (Stage) someNode.getScene().getWindow();
	    GenericController.switchScene(stage, root);
	}

	@FXML
	private ImageView okExceptionButton;

	@FXML
	private Label exceptionMessageLabel;

	@FXML
	private void okExceptionButtonOnClick(MouseEvent event) {
		Parent root = SceneConfig.getInstance().getGameScene();
		GenericController.switchScene(event, root);
	}

	@FXML
	private void okbuttonOnMouseExited() {
		GenericController.buttonGlowOFF(okExceptionButton);
	}

	@FXML
	private void okButtonOnMouseEntered() {
		GenericController.buttonGlowON(okExceptionButton, Color.PURPLE);
	}

}
