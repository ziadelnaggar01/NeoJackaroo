package view;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
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

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import controller.GenericController;
import controller.SceneConfig;

//import com.sun.media.jfxmedia.events.PlayerStateEvent.PlayerState;

import model.Colour;
import model.player.CPU;
import model.player.Marble;
import model.player.Player;
import engine.Game;

public class BoardController {

	// Store selected marbles globally or in your controller class
	private final Set<Circle> selectedMarbles = new HashSet<>();

	@FXML
	private StackPane boardPane;

	@FXML
	private final List<ImageView> cards = new ArrayList<>();

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
	List<List<Circle>> safeZone = new ArrayList<>();
	@FXML
	ImageView firepit;
	@FXML
	private GridPane A;
	@FXML
	private GridPane B;
	@FXML
	private GridPane C;
	@FXML
	private GridPane D;

	@FXML
	public void initialize() {
		// Load the marble image
		Set_Your_Track();
		Set_movable_marbles();
		Set_safe_zone();
		// destroy_it(movableMarbles.get(0), 1);
		// swap(movableMarbles.get(0), movableMarbles.get(4));
		// destroy_it(movableMarbles.get(0));
	//	move_backword(movableMarbles.get(0), 99, 10);
		Trap(movableMarbles.get(0), 2);
	}

	private void Set_safe_zone() {
		for (Node node : animationPane.getChildren()) {
			if (node instanceof Circle && node.getId() != null && node.getId().startsWith("safe")) {
				((Circle) node).setFill(Color.TRANSPARENT);
			}
		}
	}

	@FXML
	private void setupMarbleSelection(MouseEvent event) {
		Circle clickedMarble = (Circle) event.getSource();
		// toggle selection like in setupMarbleSelection method
		if (selectedMarbles.contains(clickedMarble)) {
			selectedMarbles.remove(clickedMarble);
			clickedMarble.setEffect(null);
		} else {
			selectedMarbles.add(clickedMarble);
			clickedMarble.setEffect(new DropShadow(15, Color.YELLOW));
		}
	}

	@FXML
	private void onPlayClicked() {
		System.out.println("User selected marbles:");
		for (Circle marble : selectedMarbles) {
			System.out.println("Marble ID: " + marble.getId());
		}
		// Here you can process the selected marbles as needed
	}

	private void swap(Circle a, Circle b) {
		// Get original positions
		double ax = a.getLayoutX();
		double ay = a.getLayoutY();
		double bx = b.getLayoutX();
		double by = b.getLayoutY();

		// Create transitions for each marble
		TranslateTransition moveA = new TranslateTransition(Duration.millis(1000), a);
		moveA.setByX(bx - ax);
		moveA.setByY(by - ay);

		TranslateTransition moveB = new TranslateTransition(Duration.millis(1000), b);
		moveB.setByX(ax - bx);
		moveB.setByY(ay - by);

		// After animation, reset positions and clear transforms
		moveA.setOnFinished(e -> {
			a.setLayoutX(bx);
			a.setLayoutY(by);
			a.setTranslateX(0);
			a.setTranslateY(0);
		});

		moveB.setOnFinished(e -> {
			b.setLayoutX(ax);
			b.setLayoutY(ay);
			b.setTranslateX(0);
			b.setTranslateY(0);
		});

		// Play both at the same time
		ParallelTransition swapAnim = new ParallelTransition(moveA, moveB);
		swapAnim.play();
	}

	private void move_backword(Circle x, int st, int steps) {
		List<Circle> path = new ArrayList<>();
		for (int i = steps - 1; i >= 0; i--) {
			st = (st - 1 + 100) % 100;
			String targetId = "#m" + st;
			Circle y = (Circle) animationPane.lookup(targetId);
			if (y != null)
				path.add(y);
			else
				System.out.println("Missing circle at: " + targetId);
		}
		moveThroughPath(x, 0, path.size(), path);
	}

	private void to_safe_zone(Circle x, int st, int where) {
		int entry = 98;
		char To = x.getId().charAt(4);
		if (To == 'B')
			entry = 23;
		else if (To == 'C')
			entry = 48;
		else if (To == 'D')
			entry = 73;

		List<Circle> path = new ArrayList<>();

		for (int i = st + 1; i <= st + entry + 1; i++) {
			String targetId = "#m" + (i % 100);
			Circle y = (Circle) animationPane.lookup(targetId);
			if (y != null)
				path.add(y);
			else
				System.out.println("Missing circle at: " + targetId);
		}

		for (int i = 1; i <= where; i++) {
			String Idd = "#safe" + To + i;
			Circle yy = (Circle) animationPane.lookup(Idd);
			if (yy != null)
				path.add(yy);
			else
				System.out.println("Missing safe zone circle: " + Idd);
		}
		moveThroughPath(x, 0, path.size(), path);
	}

	private void Set_movable_marbles() { // add ArrayList<Player> players as parameter
		Image blueMarble = new Image(getClass().getResourceAsStream("/view/assests/scene/BlueMarble.png"));
		Image greenMarble = new Image(getClass().getResourceAsStream("/view/assests/scene/GreenMarble.png"));
		Image yellowMarble = new Image(getClass().getResourceAsStream("/view/assests/scene/YellowMarble.png"));
		Image redMarble = new Image(getClass().getResourceAsStream("/view/assests/scene/RedMarble.png"));
		for (Node node : animationPane.getChildren()) {
			if (node instanceof Circle) {
				Circle circle = (Circle) node;
				String id = circle.getId();
				if (id != null && id.startsWith("move")) {
					Image marbleImage;
					switch (id.charAt(4)) { // check the character after "move"
					case 'A':
						marbleImage = blueMarble;
						break;
					case 'B':
						marbleImage = greenMarble;
						break;
					case 'C':
						marbleImage = yellowMarble;
						break;
					case 'D':
						marbleImage = redMarble;
						break;
					default:
						marbleImage = blueMarble; // fallback
						break;
					}

					circle.setFill(new ImagePattern(marbleImage));
					circle.setStroke(Color.TRANSPARENT);
					movableMarbles.add(circle);
				}

				if (circle.getId().charAt(1) == 'A' || circle.getId().charAt(1) == 'B'
						|| circle.getId().charAt(1) == 'C' || circle.getId().charAt(1) == 'D') {
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
				circle.setFill(Color.TRANSPARENT); // Make the fill transparent
				circle.setStroke(Color.TRANSPARENT); // Optionally, make the stroke transparent too
				track.add(circle); // Add to your track list
			} else {
				// Debugging: If the circle doesn't exist, print a warning
				System.out.println("Warning: Circle with fx:id m" + i + " not found.");
			}
		}
	}

	private ScaleTransition smoothlyResize(Circle circle1, Circle circle2) {
		double circle1Radius = circle1.getRadius();
		double circle2Radius = circle2.getRadius();
		double scaleFactor = circle2Radius / circle1Radius;

		ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1.5), circle1);
		scaleTransition.setToX(scaleFactor);
		scaleTransition.setToY(scaleFactor);
		scaleTransition.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);

		return scaleTransition;
	}

	public void move_from_to(Circle x, Circle y) {
		// Smoothly resize x to match y
		smoothlyResize(x, y).play();

		// Compute the target position
		double targetX = y.getLayoutX();
		double targetY = y.getLayoutY();

		// Create translation animation
		TranslateTransition move = new TranslateTransition(Duration.millis(1500), x); // match duration
		move.setToX(targetX - x.getLayoutX());
		move.setToY(targetY - x.getLayoutY());

		// After animation, reset translate and set layout
		move.setOnFinished(e -> {
			x.setTranslateX(0);
			x.setTranslateY(0);
			x.setLayoutX(targetX);
			x.setLayoutY(targetY);
			x.setScaleX(1);
			x.setScaleY(1);
			x.setRadius(y.getRadius()); // final size adjustment
		});

		move.play();
	}
	public void Trap(Circle x, int where) {
		 // Make it grow big
	    ScaleTransition growBig = new ScaleTransition(Duration.millis(1000), x);
	    growBig.setToX(3.0);
	    growBig.setToY(3.0);
	    growBig.setInterpolator(Interpolator.EASE_OUT);
		 AudioClip sound = null;
	    try {
	        // Play trap sound
	        URL soundurl = getClass().getResource("/view/assests/sound/bonk.wav");
	        if (soundurl != null) {
	            sound = new AudioClip(soundurl.toString());
	            sound.play();
	        } else {
	            System.out.println("Sound file not found at /view/assests/sound/trap.mp3");
	        }
	    } catch (Exception e) {
	        System.out.println("Failed to play sound:");
	        e.printStackTrace();
	    }

	 

	    // After growing big, move to base
	    growBig.setOnFinished(e -> {
	        move_to_base(x, where);
	    });

	    growBig.play();
	}


	public void move_to_base(Circle x, int where) {
		// Extract label char from x's ID, like 'A' from "moveA"
		char To = x.getId().charAt(4);
		String targetId = "#m" + To + where;

		// Find the destination circle
		Circle y = (Circle) animationPane.lookup(targetId);

		if (y != null) {
			move_from_to(x, y);
		} else {
			System.out.println("Destination circle not found: " + targetId);
		}
	}

	public void destroy_it(Circle c, int where) {
		// Play sound
		try {
			// Corrected path and renamed variable to soundurl
			URL soundurl = getClass().getResource("/view/assests/sound/destroy.mp3");
			if (soundurl != null) {
				AudioClip sound = new AudioClip(soundurl.toString());
				sound.play();
			} else {
				System.out.println("Sound file not found at /view/assets/sound/destroy.mp3");
			}
		} catch (Exception e) {
			System.out.println("Failed to play sound:");
			e.printStackTrace();
		}
		move_to_base(c, where);
	}

	private void moveThroughPath(Circle marble, int st, int steps, List<Circle> pathNodes) {
		// Set marble image
		Image image = new Image(getClass().getResourceAsStream("/view/assests/scene/BlueMarble.png"));
		marble.setFill(new ImagePattern(image));

		// Create the resize transition but don't play it yet
		ScaleTransition resizeTransition = smoothlyResize(marble, pathNodes.get(0));

		// Create list of TranslateTransitions for moving the marble
		List<TranslateTransition> transitions = new ArrayList<>();

		double startX = marble.getLayoutX();
		double startY = marble.getLayoutY();

		for (int i = st; i < st + steps; i++) {
			Circle target = (Circle) pathNodes.get((i % 100));
			double targetX = target.getLayoutX() - startX;
			double targetY = target.getLayoutY() - startY;

			TranslateTransition transition = new TranslateTransition(Duration.seconds(0.3), marble);
			transition.setToX(targetX);
			transition.setToY(targetY);
			transition.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);
			transitions.add(transition);
		}

		SequentialTransition movementSequence = new SequentialTransition();
		movementSequence.getChildren().addAll(transitions);

		// Now create a master sequence: first resize, then move
		SequentialTransition masterSequence = new SequentialTransition();
		masterSequence.getChildren().addAll(resizeTransition, movementSequence);

		masterSequence.setOnFinished(e -> {
			movableMarbles.remove(marble);
			movableMarbles.add(pathNodes.get(pathNodes.size() - 1));
			System.out.println("Smooth and accurate movement finished.");
		});

		masterSequence.play();
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
		// transition .setInterpolator(javafx.animation.Interpolator.EASE_BOTH);
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

	// you need to initialize these values, get players from game
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
			"Hamoksha", "Balabizo", "Botzilla", "CPU-nicorn", "NullPointer", "NotABot"));

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
					case 1: // first CPU to encounter
						label.setText(CPU1Name.getText());
						break;
					case 2: // second CPU to encounter
						label.setText(CPU2Name.getText());
						break;
					case 3: // third CPU to encounter
						label.setText(CPU3Name.getText());
						break;
					}
					return;
				}
			}
		}
	}

}
