package view;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PathTransition;
import javafx.animation.ScaleTransition;
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
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
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

	@FXML
	Circle shit;
	@FXML 
	private AnchorPane animationPane;
	// Deck position (e.g., center of the board or anywhere you like)
	private final double deckX = 448;
	private final double deckY = 396;
	@FXML
	List<Circle> track = new ArrayList<>();
	@FXML
	List<Circle> movableMarbles = new ArrayList<>();
	@FXML
	Circle mA1;
	@FXML
	public void initialize() {
	    // Load the marble image
	    System.out.println(track.size());
	    Set_Your_Track();
	    Set_movable_marbles();
	    Circle ss = movableMarbles.get(0);
	    moveThroughPath(ss,track);
	}
	private void Set_movable_marbles() {
	    for (Node node : animationPane.getChildren()) {
	        if (node instanceof Circle) {
	            // Add the circle to the movableMarbles list if its ID starts with "move"
	        	  Circle circle = (Circle) node;
	        	if (circle.getId().startsWith("move")) {
	  	            circle.setFill(Color.TRANSPARENT);
	  	            circle.setStroke(Color.TRANSPARENT);
	               movableMarbles.add(circle);
	            }
	        	if(circle.getId().charAt(1) == 'A' || circle.getId().charAt(1) == 'B'
	        			|| circle.getId().charAt(1) == 'C' || circle.getId().charAt(1) == 'D'  )
	        	{
	        		  circle.setFill(Color.TRANSPARENT);
		  	            circle.setStroke(Color.TRANSPARENT);
	        	}
	        }
	    }
	}

	private void Set_Your_Track() {
	    for (int i = 0; i < 100; i++) {
	        Circle circle = (Circle) animationPane.lookup("#m" + i);
	     
	        if (circle != null) {
	        	System.out.println(i);
	            circle.setFill(Color.TRANSPARENT);  // Make the fill transparent
	            circle.setStroke(Color.TRANSPARENT);  // Optionally, make the stroke transparent too
	            track.add(circle);  // Add to your track list
	        } else {
	            // Debugging: If the circle doesn't exist, print a warning
	            System.out.println("Warning: Circle with fx:id m" + i + " not found.");
	        }
	    }
	}
	private void smoothlyResize(Circle circle1, Circle circle2) {
	    // Get the current radius of both circles
	    double circle1Radius = circle1.getRadius();
	    double circle2Radius = circle2.getRadius();

	    // Calculate the scaling factor to match the size of circle2
	    double scaleFactor = circle2Radius / circle1Radius;

	    // Create a ScaleTransition to smoothly change the size of circle1
	    ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1.5), circle1);
	    scaleTransition.setToX(scaleFactor); // Set the scale to match circle2's size
	    scaleTransition.setToY(scaleFactor); // Set the scale to match circle2's size

	    // Optionally, you can apply some interpolation to make the transition smoother
	    scaleTransition.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);

	    // Play the transition
	    scaleTransition.play();
	}

	private void moveThroughPath(Circle marble, List<Circle> pathNodes) {
	    // Set marble image
	    Image image = new Image(getClass().getResourceAsStream("/view/assests/scene/BlueMarble.png"));
	    marble.setFill(new ImagePattern(image));

	    // First resize the marble to match the first node's size
	    smoothlyResize(marble, pathNodes.get(0));

	    // Create list of TranslateTransitions for moving the marble
	    List<TranslateTransition> transitions = new ArrayList<>();

	    double startX = marble.getLayoutX();
	    double startY = marble.getLayoutY();

	    double currentToX = 0;
	    double currentToY = 0;

	    for (int i = 0; i < pathNodes.size(); i++) {
	        Circle target = pathNodes.get(i);

	        // Calculate target position relative to marble's original position
	        double targetX = target.getLayoutX() - startX;
	        double targetY = target.getLayoutY() - startY;

	        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.3), marble);
	        transition.setToX(targetX);
	        transition.setToY(targetY);
	        transition.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);

	        transitions.add(transition);

	        currentToX = targetX;
	        currentToY = targetY;
	    }

	    // Create a SequentialTransition to play the resize and movement sequentially
	    SequentialTransition sequence = new SequentialTransition();

	    // Add the resize transition first
	    sequence.getChildren().addAll(transitions);

	    // On completion of the movement sequence, update the movable marbles list
	    sequence.setOnFinished(e -> {
	        movableMarbles.remove(marble);  // Remove the old position
	        movableMarbles.add(pathNodes.get(pathNodes.size() - 1));  // Add the last node as the new position
	        System.out.println("Smooth and accurate movement finished.");
	    });

	    // Start the sequential transition
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
