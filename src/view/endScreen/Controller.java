package view.endScreen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Colour;
import controller.GenericController;
import controller.MusicManager;
import controller.SceneConfig;
import controller.SoundManager;

public class Controller {

	Image pointerImage = new Image(getClass().getResource(
			"/view/assets/PurpleHand.png").toExternalForm());
	ImageCursor pointerCursor = new ImageCursor(pointerImage, 5, 2); // hotspot
																		// at
																		// tip

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

		//winner = Colour.RED;

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
			wisdom = lossWisdom.get(0);

		}
		wisdomLabel.setText(wisdom);
		TranslateTransition slide = new TranslateTransition(
				Duration.seconds(1), wisdomLabel);
		slide.setFromY(-100);
		slide.setToY(0);
		slide.setCycleCount(1);
		slide.play();

		winnerName.setText(winnerColor);
		ScaleTransition pulse = new ScaleTransition(Duration.seconds(1),
				winnerName);
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

	// statistics theme
	private int time;
	private int discards;
	private int traps;
	private int turns;

	@FXML
	private AnchorPane statisticsAnchorPane;
	@FXML
	private ImageView statisticsImageView;

	public void updateStatistics(int time, int discards, int traps, int turns) {
		this.time = time;
		this.turns = turns;
		this.discards = discards;
		this.traps = traps;
	}

	@FXML
	private void statisticsImageViewOnMouseEntered() {
		backButton.setOnMouseEntered(e -> statisticsImageView
				.setCursor(pointerCursor));
		GenericController.buttonGlowON(statisticsImageView, Color.YELLOW);
	}

	@FXML
	private void statisticsImageViewOnMouseExited() {
		GenericController.buttonGlowOFF(statisticsImageView);
	}

	@FXML
	private void statisticsImageViewOnMouseClicked() {
		SoundManager.getInstance().playSound("button_click");
		showStatistics();
	}

	private void showStatistics() {
		// === Dim Background Overlay ===
		Region dimOverlay = new Region();
		dimOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6);");
		dimOverlay.setPrefSize(statisticsAnchorPane.getWidth(),
				statisticsAnchorPane.getHeight());

		// === Neon Popup ===
		VBox popup = new VBox(20);
		popup.setPadding(new Insets(30));
		popup.setAlignment(Pos.TOP_CENTER);
		popup.setStyle("-fx-background-color: black;"
				+ "-fx-border-color: #00ffff;" + "-fx-border-width: 3;"
				+ "-fx-effect: dropshadow(gaussian, #00ffff, 30, 0.7, 0, 0);"
				+ "-fx-background-radius: 12;" + "-fx-border-radius: 12;");

		// === Title ===
		Label title = new Label("Game Review");
		title.setStyle("-fx-text-fill: #00ffff;" + "-fx-font-size: 28px;"
				+ "-fx-font-weight: bold;");

		// === Stat rows ===
		VBox statsBox = new VBox(15);
		statsBox.setAlignment(Pos.CENTER_LEFT);

		List<HBox> rows = Arrays.asList(
				createStatRow("/view/assets/timeIcon.png", "Time Elapsed: "
						+ time + "s"),
				createStatRow("/view/assets/discardIcon.png", "Discards: "
						+ discards),
				createStatRow("/view/assets/trapIcon.png", "Traps Hit: "
						+ traps),
				createStatRow("/view/assets/turnsIcon.png", "Turns Taken: "
						+ turns));

		statsBox.getChildren().addAll(rows);

		// Initially make rows transparent
		for (HBox row : rows) {
			row.setOpacity(0);
		}

		// === OK Button ===
		Button okButton = new Button("OK");
		okButton.setStyle("-fx-text-fill: #00ffff;" + "-fx-font-size: 16px;"
				+ "-fx-background-color: transparent;"
				+ "-fx-border-color: #00ffff;" + "-fx-border-radius: 6;"
				+ "-fx-border-width: 2;" + "-fx-cursor: hand;"
				+ "-fx-padding: 6 20 6 20;");
		okButton.setOnAction(e -> statisticsAnchorPane.getChildren().removeAll(
				dimOverlay, popup));

		// === Add all to popup ===
		popup.getChildren().addAll(title, statsBox, okButton);

		// === Center Popup ===
		popup.setLayoutX((statisticsAnchorPane.getWidth() - 400) / 2);
		popup.setLayoutY((statisticsAnchorPane.getHeight() - 350) / 2);
		popup.setPrefWidth(400);
		popup.setPrefHeight(350);

		// === Dismiss on overlay click ===
		dimOverlay.setOnMouseClicked(e -> statisticsAnchorPane.getChildren()
				.removeAll(dimOverlay, popup));

		// === Add to AnchorPane ===
		statisticsAnchorPane.getChildren().addAll(dimOverlay, popup);

		// === Animate rows with fade-in ===
		Duration baseDelay = Duration.millis(300);
		for (int i = 0; i < rows.size(); i++) {
			HBox row = rows.get(i);
			FadeTransition ft = new FadeTransition(Duration.millis(500), row);
			ft.setFromValue(0);
			ft.setToValue(1);
			ft.setDelay(baseDelay.multiply(i));
			ft.play();
		}
	}

	private HBox createStatRow(String iconPath, String text) {
		ImageView icon = new ImageView(new Image(getClass()
				.getResourceAsStream(iconPath)));
		icon.setFitHeight(36);
		icon.setFitWidth(36);

		Label label = new Label(text);
		label.setStyle("-fx-text-fill: #00ffff; -fx-font-size: 18px;");

		HBox row = new HBox(15, icon, label);
		row.setAlignment(Pos.CENTER_LEFT);
		return row;
	}

}
