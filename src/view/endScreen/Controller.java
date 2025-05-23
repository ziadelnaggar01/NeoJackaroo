package view.endScreen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Colour;
import controller.GenericController;
import controller.MusicManager;
import controller.SceneConfig;
import controller.SoundManager;

public class Controller {

	Image pointerImage = new Image(getClass().getResource("/view/assets/PurpleHand.png").toExternalForm());
	ImageCursor pointerCursor = new ImageCursor(pointerImage, 5, 2); // hotspot at tip
	
	@FXML
	private ImageView exitButton;

	@FXML
	private ImageView backButton;

	@FXML
	private Label winnerName;

	@FXML
	private Pane rootPane; // make sure this is the root pane of the EndScene

	@FXML
	private ImageView happyGif;
	@FXML
	private ImageView sadGif;
	@FXML
	StackPane sadGifContainer;
	@FXML
	private Label wisdomLabel;

	private ArrayList<String> lossWisdom = new ArrayList<>(
			Arrays.asList(
					"That went downhill faster than a marble on a slide.",
					"You tried, you cried, your marbles died.",
					"Even the cards were cringing.",
					"They say losing builds character. You’re practically a philosopher now.",

					"Every loss teaches you a new move… or at least how not to move.",
					"In Jackaroo, you either win or you plot your comeback.",
					"Even a fallen marble fields again.",
					"No shame in losing — unless you play the same way again."));

	private ArrayList<String> winWisdom = new ArrayList<>(Arrays.asList(
			"Your cards bowed in respect. Your marbles sang your name.",
			"You didn’t just win, you Jackaroo’d them into next week.",
			"Another day, another board wiped clean.",
			"You played like the rules were written in your DNA.",

			"Victory is sweet, but bragging rights are sweeter.",
			"The best moves are the ones nobody saw coming — except you.",
			"Today: the board. Tomorrow: the universe."));

	public void playFadeIn() {
		GenericController.applyGlowingEffect(winnerName);
		rootPane.setOpacity(0);
		rootPane.setScaleX(3.0);
		rootPane.setScaleY(3.0);

		// Zoom-in effect
		ScaleTransition zoom = new ScaleTransition(Duration.millis(800),
				rootPane);
		zoom.setFromX(3.0);
		zoom.setFromY(3.0);
		zoom.setToX(1.0);
		zoom.setToY(1.0);
		zoom.setInterpolator(Interpolator.EASE_OUT);

		// Fade-in effect
		FadeTransition fade = new FadeTransition(Duration.millis(800), rootPane);
		fade.setFromValue(0.0);
		fade.setToValue(1.0);
		fade.setInterpolator(Interpolator.EASE_OUT);

		// Play both in parallel
		ParallelTransition popIn = new ParallelTransition(zoom, fade);
		popIn.play();
		MusicManager.getInstance().playMusic("/view/assets/audio/Victory.mp3");

	}

	@FXML
	private void exitButtonOnMouseEntered() {
		exitButton.setOnMouseEntered(e -> exitButton.setCursor(pointerCursor));

		GenericController.buttonGlowON(exitButton, Color.RED);
	}

	// Method for Mouse Exited event
	@FXML
	private void exitButtonOnMouseExited() {
		GenericController.buttonGlowOFF(exitButton);
	}

	@FXML
	private void backButtonOnMouseEntered() {
		backButton.setOnMouseEntered(e -> backButton.setCursor(pointerCursor));

		GenericController.buttonGlowON(backButton, Color.YELLOW);
	}

	// Method for Mouse Exited event
	@FXML
	private void backButtonOnMouseExited() {
		GenericController.buttonGlowOFF(backButton);
		MusicManager.getInstance().stop();
		MusicManager.getInstance().playMusic(
				"/view/assets/audio/Digital Voyage - Twin Musicom.mp3");
		;

	}

	public void updateWinner(Colour winner, Colour userColour) {

		String winnerColor = " Player Won!!";
		switch (winner) {
		case RED:
			winnerColor = "Red" + winnerColor;
			break;
		case BLUE:
			winnerColor = "Blue" + winnerColor;
			break;
		case GREEN:
			winnerColor = "Green" + winnerColor;
			break;
		case YELLOW:
			winnerColor = "Yellow" + winnerColor;
			break;
		default:
			winnerColor = "Game Over";
		}
		String wisdom;
		if (userColour == winner) {
			happyGif.setVisible(true);
			sadGifContainer.setVisible(false);
			sadGif.setVisible(false);
			Collections.shuffle(winWisdom);
			wisdom = winWisdom.get(0);

		} else {
			sadGifContainer.setVisible(true);
			sadGif.setVisible(true);

			happyGif.setVisible(false);
			Collections.shuffle(lossWisdom);
			wisdom=lossWisdom.get(0);

		}
		wisdomLabel.setText(wisdom);
		TranslateTransition slide = new TranslateTransition(Duration.seconds(1), wisdomLabel);
		slide.setFromY(-100);
		slide.setToY(0);
		slide.setCycleCount(1);
		slide.play();
		
		winnerName.setText(winnerColor);
		ScaleTransition pulse = new ScaleTransition(Duration.seconds(1), winnerName);
		pulse.setFromX(1.0);
		pulse.setToX(1.05);
		pulse.setFromY(1.0);
		pulse.setToY(1.05);
		pulse.setAutoReverse(true);
		pulse.setCycleCount(Animation.INDEFINITE);
		pulse.play();
	}

	@FXML
	private void onBackButtonClicked(MouseEvent event) {
		// Go back to start screen, and launch a new game
		SoundManager.getInstance().playSound("button_click");
		Parent root = SceneConfig.getInstance().getStartScene();
		GenericController.switchScene(event, root);
		SceneConfig.getInstance().createNewGame();
	}

	@FXML
	private void closeGame(MouseEvent event) {
		SoundManager.getInstance().playSound("button_click");
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.close();
	}
}
