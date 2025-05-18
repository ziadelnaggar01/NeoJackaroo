package view.settingsMenu;
import java.io.IOException;

import controller.GenericController;
import controller.MusicManager;
import controller.SceneConfig;
import controller.SoundManager;
import view.BoardController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Controller {

	// audio related

	@FXML
	private Slider musicSlider;
	@FXML
	private Slider sfxSlider;
//---------------------------------------------------------------------------------------------------------------
	// buttons related

	@FXML
	private ImageView exitSettingsIcon;
	@FXML
	private ImageView continueGameIcon;
	
	public void initialize() {
        // Bind volume slider to music player
		MusicManager musicPlayer = MusicManager.getInstance();
		musicSlider.setValue(musicPlayer.getVolume());
		musicSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            musicPlayer.setVolume(newVal.doubleValue());
        });
		
        SoundManager soundManager = SoundManager.getInstance();
        sfxSlider.setValue(soundManager.getVolume());
        sfxSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            soundManager.setVolume(newVal.doubleValue());
        });
    }

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
		SoundManager.getInstance().playSound("button_click");
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
		SoundManager.getInstance().playSound("button_click");
		BoardController.gamePaused=false;//return game 
		Parent root;
		if(!SceneConfig.getInstance().isInGame())
		{
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	        stage.close();
		}
		root = SceneConfig.getInstance().getStartScene();
		GenericController.switchScene(event, root);
	}
}
