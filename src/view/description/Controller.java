package view.description;

import java.util.HashMap;
import java.util.Map;

import controller.GenericController;
import controller.SceneConfig;
import controller.SoundManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Controller {

	@FXML
	private Label descriptionLabelID;

	@FXML
	private ImageView descriptionOkButton;

	private final Map<Integer, String> descriptions = new HashMap<Integer, String>() {
		{
			put(1,
					"Fields one of your marbles onto the track or moves one marble 1 step");
			put(2, "Moves one of your marbles 2 steps");
			put(3, "Moves one of your marbles 3 steps");
			put(4, "Moves one of your marbles 4 steps backwards");
			put(5,
					"Moves any marble 5 steps, if you move an opponent's mable, it does'nt enter a safe zone");
			put(6, "Moves one of your marbles 6 steps");
			put(7,
					"Moves of your marbles 7 steps or moves 2 marbles a total of 7 steps");
			put(8, "Moves of your marbles 8 steps");
			put(9, "Moves of your marbles 9 steps");
			put(10,
					"Moves of your marbles 10 steps or discards a random card from the next player and skips his turn");
			put(11,
					"Moves one of your marbles 11 steps or swaps one of your marbles with another");
			put(12,
					"Moves of your marbles 12 steps or discards a random card from a random player and skips his turn");
			put(13,
					"Fields one of your marbles onto the track or moves one marble 13 steps destroying all marbles in it's path");
			put(14, "Sends an opponent marble back to his home zone");
			put(15, "Sends one of you marbles to a random empty safe cell");
		}
	};

	public void showCardDescription(String cardID, MouseEvent event) {//card ID is the card's path
		SoundManager.getInstance().playSound("button_click");
		Parent root = SceneConfig.getInstance().getDescriptionScene();

		// update description according to card selected
		int rank = Integer.parseInt(GenericController.extractCardRank(cardID));
		descriptionLabelID.setText(descriptions.get(rank));

		GenericController.switchScene(event,root);

	}

	// on mouse click method for the ok button
	// closes the stage with the click sound
	@FXML
	private void OkIconOnMouseClicked(MouseEvent event) {
		SoundManager.getInstance().playSound("button_click");
		Parent root = SceneConfig.getInstance().getGameScene();
		GenericController.switchScene(event,root);
	}

	@FXML
	private void OkIconOnMouseEntered() {
		GenericController.buttonGlowON(descriptionOkButton, Color.CYAN, 25);
	}

	// Method for Mouse Exited event
	@FXML
	private void OkIconOnMouseExited() {
		GenericController.buttonGlowOFF(descriptionOkButton);
	}

}
