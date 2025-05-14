package settingsView;

import generic.GenericController;

import java.io.IOException;

import view.BoardController;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

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
		new GenericController().switchSceneWithFade((Stage) ((Node) event.getSource()).getScene().getWindow(),"/view/BoardScene.fxml");
	}
	
	@FXML
	private void exitButtonOnClick(MouseEvent event) throws IOException {
		//Go back to start screen, no boardscene 
		BoardController.gamePaused=false;//return game 
		new GenericController().switchSceneWithFade((Stage) ((Node) event.getSource()).getScene().getWindow(),"/view/BoardScene.fxml");
	}
	
	
	
	




}
