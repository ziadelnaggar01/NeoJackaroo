package settingsView;
import java.io.IOException;

import controller.GenericController;
import controller.SceneConfig;
import view.BoardController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

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
		GenericController.buttonGlowON(exitSettingsIcon, Color.RED);
	}

	// Method for Mouse Exited event
	@FXML
	private void exitIconOnMouseExited() {
		GenericController.buttonGlowOFF(exitSettingsIcon);
	}

	@FXML
	private void continueIconOnMouseEntered() {
		GenericController.buttonGlowON(continueGameIcon, Color.GREEN);
	}

	// Method for Mouse Exited event
	@FXML
	private void continueIconOnMouseExited() {
		GenericController.buttonGlowOFF(continueGameIcon);
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
