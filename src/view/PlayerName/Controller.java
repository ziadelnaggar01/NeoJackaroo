package view.PlayerName;

import controller.GenericController;
import controller.SceneConfig;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

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
		Parent root = SceneConfig.getInstance().getStartScene();
		GenericController.switchScene(event, root);
	}
	
	@FXML
	private void switchSceneToGame(MouseEvent event) throws Exception {
		if(nameTextField.getText().equals(""))
			popUpLabel.setText("Please enter a name!");
		else
		{
		Parent root = SceneConfig.getInstance().getGameScene();
		GenericController.switchScene(event, root);
		}
	}
	
	@FXML
	private void keyboardSwitchSceneToGame(KeyEvent  event) throws Exception {
		if (event.getCode() == KeyCode.ENTER)
		{
		if(nameTextField.getText().equals(""))
			popUpLabel.setText("Please enter a name!");
		else
		{
		Parent root = SceneConfig.getInstance().getGameScene();
		GenericController.switchScene(event, root);
		}
		}
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
