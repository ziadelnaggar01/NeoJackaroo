package settingsView;
import java.io.IOException;

import controller.GenericController;
import controller.SceneConfig;
import view.BoardController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class settingsController {

	// audio related

	@FXML
	private Slider musicSlider;
	@FXML
	private Slider audioSlider;
//---------------------------------------------------------------------------------------------------------------
	// buttons related

	@FXML
	private ImageView exitSettingsIcon;
	@FXML
	private ImageView continueGameIcon;

	@FXML
	private void exitIconOnMouseEntered() {
		// Apply a red DropShadow effect on the image
		DropShadow shadow = new DropShadow();
		shadow.setColor(Color.RED);
		shadow.setRadius(23);
		exitSettingsIcon.setEffect(shadow);
	}

	// Method for Mouse Exited event
	@FXML
	private void exitIconOnMouseExited() {
		// Remove the effect when the mouse exits
		exitSettingsIcon.setEffect(null);
	}

	@FXML
	private void continueIconOnMouseEntered() {
		// Apply a red DropShadow effect on the image
		DropShadow shadow = new DropShadow();
		shadow.setColor(Color.GREEN);
		shadow.setRadius(23);
		continueGameIcon.setEffect(shadow);
	}

	// Method for Mouse Exited event
	@FXML
	private void continueIconOnMouseExited() {
		// Remove the effect when the mouse exits
		continueGameIcon.setEffect(null);
	}
	@FXML
	private void continueButtonOnClick(MouseEvent event) throws IOException {
		BoardController.gamePaused=false;//return game 
		Parent root;
		if(SceneConfig.getInstance().isInGame())
			root = SceneConfig.getInstance().getGameScene();
		else
			root = SceneConfig.getInstance().getStartScene();
		GenericController.switchScene(event, root);
	}
	
	@FXML
	private void exitButtonOnClick(MouseEvent event) throws IOException {
		//Go back to start screen, no boardscene 
		BoardController.gamePaused=false;//return game 
	}
	
	
	
	




}
