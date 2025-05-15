package view.PlayerName;

import controller.GenericController;
import controller.SceneConfig;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Controller {
	
	@FXML
	private ImageView okButton;
	@FXML
	private ImageView cancelButton;
	
	@FXML
	private void switchSceneToStartMenu(MouseEvent event) throws Exception {
		Parent root = SceneConfig.getInstance().getStartScene();
		GenericController.switchScene(event, root);
	}
	
	@FXML
	private void switchSceneToGame(MouseEvent event) throws Exception {
		// Load the new scene's FXML
		Parent root = SceneConfig.getInstance().getGameScene();
		GenericController.switchScene(event, root);
	}
	
	@FXML
	private void okButtonOnMouseEntered() {
		// Apply a red DropShadow effect on the image
		DropShadow shadow = new DropShadow();
		shadow.setColor(Color.PURPLE);
		shadow.setRadius(35);
		shadow.setOffsetX(0);
		shadow.setOffsetY(0);
		okButton.setEffect(shadow);
	}
	
	@FXML
	private void cancelButtonOnMouseEntered() {
		// Apply a red DropShadow effect on the image
		DropShadow shadow = new DropShadow();
		shadow.setColor(Color.CYAN);
		shadow.setRadius(15);
		shadow.setOffsetX(0);
		shadow.setOffsetY(0);
		cancelButton.setEffect(shadow);
	}

	// Method for Mouse Exited event
	@FXML
	private void okbuttonOnMouseExited() {
		// Remove the effect when the mouse exits
		okButton.setEffect(null);
	}
	
	@FXML
	private void cancelbuttonOnMouseExited() {
		// Remove the effect when the mouse exits
		cancelButton.setEffect(null);
	}
}
