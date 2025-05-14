package view;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BoardController {

	@FXML
	private BorderPane boardPane;

	@FXML
	private final List<ImageView> cards = new ArrayList<>();

	@FXML private Circle m1;
	@FXML private Circle m2;
	@FXML private Circle m3;
	@FXML private Circle m4;
	@FXML private Circle m9;
	@FXML private Circle m10;
	@FXML private Circle m11;
	@FXML private Circle m12;
	@FXML private Circle m13;
	@FXML private Circle moving;
	// Deck position (e.g., center of the board or anywhere you like)
	private final double deckX = 448;
	private final double deckY = 396;

	// Target positions on the board
	private final double[][] targetPositions = { { 593.33, 35.666 },
			{ 483.33, 35.666 }, { 373.33, 35.666 }, { 263.33, 35.666 } };

	@FXML
	public void initialize() {
	    // Load the marble image
	    Image image = new Image(getClass().getResourceAsStream("/view/assests/scene/BlueMarble.png"));
	    moving.setFill(new ImagePattern(image));

	    // Define the full movement path
	    List<Circle> path = List.of(m1, m2, m3, m4, m9, m10, m11,m12,m13);

	    // Make the path circles transparent
	    for (Circle circle : path) {
	        circle.setFill(Color.TRANSPARENT);
	        circle.setStroke(Color.TRANSPARENT);
	    }

	    // Step 1: Move to first circle
	    Circle start = path.get(0);

	    // Compute offset
	    double dx = start.getLayoutX() - moving.getLayoutX();
	    double dy = start.getLayoutY() - moving.getLayoutY();

	    // Create position animation
	    TranslateTransition toStart = new TranslateTransition(Duration.seconds(1), moving);
	    toStart.setByX(dx);
	    toStart.setByY(dy);

	    // After position transition, animate radius then move along path
	    toStart.setOnFinished(e -> {
	        Timeline resize = new Timeline(
	            new KeyFrame(Duration.seconds(0.5),
	                new KeyValue(moving.radiusProperty(), start.getRadius())
	            )
	        );
	        resize.setOnFinished(ev -> moveThroughPath(moving, path));
	        resize.play();
	    });

	    toStart.play();
	}

	private void moveThroughPath(Circle marble, List<Circle> pathNodes) {
	    List<TranslateTransition> transitions = new ArrayList<>();

	    for (int i = 0; i < pathNodes.size() - 1; i++) {
	        Node from = pathNodes.get(i);
	        Node to = pathNodes.get(i + 1);

	        double dx = to.getLayoutX() - from.getLayoutX();
	        double dy = to.getLayoutY() - from.getLayoutY();

	        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), marble);
	        transition.setByX(dx);
	        transition.setByY(dy);
	        transitions.add(transition);
	    }

	    SequentialTransition sequence = new SequentialTransition();
	    sequence.getChildren().addAll(transitions);
	    sequence.play();
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

	// Code for settings icon
	@FXML
	private ImageView settingsIcon;

	@FXML
	private void settingsIconOnMouseEntered() {
		// Apply a red DropShadow effect on the image
		DropShadow shadow = new DropShadow();
		shadow.setColor(Color.BLUE);
		shadow.setRadius(1000);
		settingsIcon.setEffect(shadow);
	}

	// Method for Mouse Exited event
	@FXML
	private void settingsIconOnMouseExited() {
		// Remove the effect when the mouse exits
		settingsIcon.setEffect(null);
	}

	@FXML
	private void openSettings() {
		// opens up settings
		// sounds adjusting
		// continue
		// exit
	}
	
	//Setting up names

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
	
	
	
	
	
	
	// Setting up marbles 
	//Marble1 = (1,1), Marble2 = (1,2), Marble3 =(2,1), Mabrle4 = (2,2)
	
	
	
}
