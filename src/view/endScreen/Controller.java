package view.endScreen;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Controller {
	@FXML
	private ImageView exitButton;
	
	@FXML
	private Label winnerName;
	
	@FXML
	private void exitButtonOnMouseEntered() {
		// Apply a red DropShadow effect on the image
		DropShadow shadow = new DropShadow();
		shadow.setColor(Color.RED);
		shadow.setRadius(15);
		shadow.setOffsetX(0);
		shadow.setOffsetY(0);
		exitButton.setEffect(shadow);
	}
	
	// Method for Mouse Exited event
	@FXML
	private void exitButtonOnMouseExited() {
		// Remove the effect when the mouse exits
		exitButton.setEffect(null);
	}
	
	@FXML
	private void closeGame(MouseEvent event) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
	}
}
