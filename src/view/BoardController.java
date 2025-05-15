package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.Parent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import controller.GenericController;
import controller.SceneConfig;

//import com.sun.media.jfxmedia.events.PlayerStateEvent.PlayerState;

import model.Colour;
import model.player.CPU;
import model.player.Player;
import engine.Game;

public class BoardController {

	// Tamer's work

	@FXML
	private BorderPane boardPane;

	private final List<ImageView> cards = new ArrayList<>();

	// Deck position (e.g., center of the board or anywhere you like)
	private final double deckX = 448;
	private final double deckY = 396;

	// Target positions on the board
	private final double[][] targetPositions = { { 593.33, 35.666 },
			{ 483.33, 35.666 }, { 373.33, 35.666 }, { 263.33, 35.666 } };

	@FXML
	public void initialize() {
		// boardPane.setOnMouseMoved(event -> {
		// double x = event.getX();
		// double y = event.getY();
		// System.out.println("Mouse X: " + x + ", Y: " + y);
		// });
		// initializeSettings();
		// loadCards();
		// distributeCards();

	}

	private void loadCards() {
		// for (int i = 0; i < 4; i++) {
		// ImageView card = new ImageView(new
		// Image(getClass().getResource("assests/deck/Clovers_10_white.png").toExternalForm()));
		// card.setFitWidth(80);
		// card.setFitHeight(120);
		// card.setLayoutX(deckX);
		// card.setLayoutY(deckY);
		// cards.add(card);
		// boardPane.getChildren().add(card);
		// }
	}

	private void distributeCards() {
		// for (int i = 0; i < cards.size(); i++) {
		// ImageView card = cards.get(i);
		// double targetX = targetPositions[i][0];
		// double targetY = targetPositions[i][1];
		//
		// TranslateTransition transition = new
		// TranslateTransition(Duration.seconds(0.5), card);
		// transition.setToX(targetX - deckX);
		// transition.setToY(targetY - deckY);
		// transition.setDelay(Duration.seconds(i * 0.3)); // Staggered
		// animation
		// transition.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);
		// transition.play();
		// }
	}

	// ------------------------------------------------------------------------------------------------------------------------------
	// Code for settings
	public static boolean gamePaused = false;

	@FXML
	private ImageView settingsIcon;

	@FXML
	private void settingsIconOnMouseEntered() {
		GenericController.buttonGlowON(settingsIcon, Color.BLUE, 1000);
	}

	// Method for Mouse Exited event
	@FXML
	private void settingsIconOnMouseExited() {
		GenericController.buttonGlowOFF(settingsIcon);
	}

	@FXML
	private void openSettings(MouseEvent event) {
		gamePaused = true;// if opening settings from boardscene, pause the game
		SceneConfig.getInstance().setInGame(true);
		Parent root = SceneConfig.getInstance().getSettingsScene();
		GenericController.switchScene(event, root);
	}

	// ----------------------------------------------------------------------------------------------------------------------------------

	// Setting up names & set current and next player

	
	//you need to initialize these values, get players from game
	private Game game;
	private ArrayList<Player> players;
	
	
	

	@FXML
	private Label nextPlayerLabel;
	@FXML
	private Label currentPlayerLabel;
	@FXML
	private Label CPU1Name;
	@FXML
	private Label CPU2Name;
	@FXML
	private Label CPU3Name;
	@FXML
	private Label userName;

	private ArrayList<String> cpuNames = new ArrayList<>(Arrays.asList(

			// Serious / Intellectual
			"Turing", "AdaNova", "Hypatia", "NeuroLynx", "Euler",
			// Funny / Meme-worthy
			"Hamoksha", "Balabizo", "Botzilla", "CPU-nicorn", "NullPointer",
			"NotABot"));

	private void assignNames(String playerName) {
		Collections.shuffle(cpuNames);
		CPU1Name.setText(cpuNames.get(0));
		CPU2Name.setText(cpuNames.get(1));
		CPU3Name.setText(cpuNames.get(2));
		userName.setText(playerName);
	}

	private void setCurrentPlayerLabel() {
		Colour colour = game.getActivePlayerColour();
		setPlayerLabel(currentPlayerLabel, colour);

	}

	private void setNextPlayerLabel() {
		Colour colour = game.getNextPlayerColour();
		setPlayerLabel(nextPlayerLabel, colour);
	}

	// Helper which given a label and a colour, searches for the correct player
	// given the colour and assigns the given label to the name of the colour
	// holder accordingly
	private void setPlayerLabel(Label label, Colour colour) {
		int cpuCount = 0;

		for (Player p : players) {
			if (!(p instanceof CPU)) {
				if (p.getColour() == colour) {
					currentPlayerLabel.setText(userName.getText());
					return;
				}
			} else {
				cpuCount++;
				if (p.getColour() == colour) {
					switch (cpuCount) {
					case 1: //first CPU to encounter
						label.setText(CPU1Name.getText());
						break;
					case 2:	//second CPU to encounter 
						label.setText(CPU2Name.getText());
						break;
					case 3:	//third CPU to  encounter
						label.setText(CPU3Name.getText());
						break;
					}
					return;
				}
			}
		}
	}

}
