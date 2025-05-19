package view;

import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.util.Duration;
import javafx.scene.Parent;
import javafx.scene.input.MouseButton;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import controller.GenericController;
import controller.SceneConfig;
import controller.SoundManager;

//import com.sun.media.jfxmedia.events.PlayerStateEvent.PlayerState;

import model.Colour;
import model.card.Card;
import model.card.standard.Standard;
import model.card.standard.Suit;
import model.card.wild.Burner;
import model.card.wild.Saver;
import model.card.wild.Wild;
import model.player.CPU;
import model.player.Marble;
import model.player.Player;
import engine.Game;
import engine.board.Board;
import engine.board.SafeZone;
import exception.GameException;
import exception.InvalidCardException;
import exception.InvalidMarbleException;

public class BoardController {

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
	// Store selected marbles globally or in your controller class
	private final Set<Circle> selectedMarbles = new HashSet<>();

	@FXML
	private StackPane boardPane;

	@FXML
	private final List<ImageView> cards = new ArrayList<>();

	private String selectedCardID;
	@FXML
	private ImageView playerCard1;
	@FXML
	private ImageView playerCard2;
	@FXML
	private ImageView playerCard3;
	@FXML
	private ImageView playerCard4;
	@FXML
	private AnchorPane animationPane;

	
	@FXML
	List<Circle> track = new ArrayList<>();
	@FXML
	List<Circle> movableMarbles = new ArrayList<>();
	@FXML
	List<List<Circle>> safeZone = new ArrayList<>();

	Map<Circle, Circle> marbleToCellMap = new HashMap<>();
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

	int currentPlayerIndex = 0;

	@FXML
	public void initialize() throws IOException {
		game = new Game("PlayerName");
		Set_Your_Track();
		assignColours();
		Set_safe_zone();
		splitDistanceAnchorPane.setVisible(false);

		// use a valid name
		set_ALL_Hand(game.getPlayers());
		ArrayList<ImageView> gg = new ArrayList<>();
		gg.add(playerCard1);
		gg.add(playerCard2);
		gg.add(playerCard3);
		gg.add(playerCard4);
		setupCards(gg);

		continueGameLoop();
	}

	private void continueGameLoop() {
		if (game.checkWin() != null) {
			System.out.println("Game over. Winner: " + game.checkWin());
			Parent root = SceneConfig.getInstance().getEndScene();
			Node someNode = animationPane;
			Stage stage = (Stage) someNode.getScene().getWindow();
			GenericController.switchScene(stage, root);
		}

		Colour curPlayer = game.getActivePlayerColour();
		ArrayList<Player> players = game.getPlayers();
		currentPlayerIndex = GetIndex(players, curPlayer);

		if (!game.canPlayTurn()) {
			System.out.println("Player cannot play. Skipping turn.");
			game.endPlayerTurn();
			Platform.runLater(this::continueGameLoop); // Go to next player
			return;
		}

		if (currentPlayerIndex == 0) {
			System.out.println("Waiting for player to click Play.");
			// Wait until user clicks play button (onPlayClicked will call continueGameLoop)
		} else {
			System.out.println("AI is playing...");
			try {
				game.playPlayerTurn();
			} catch (GameException e) {
				e.printStackTrace();
			}
			game.endPlayerTurn();
			Platform.runLater(this::continueGameLoop); // Next turn
		}
	}

	@FXML
	private void onPlayClicked() throws Exception {
		ArrayList<SafeZone> safeZones = game.getBoard().getSafeZones();
		Player curPlayer = game.getPlayers().get(0);

		try {

			// Link card selected from GUI to back end
			Card card;
			if (selectedCardID != null) { // if the card is null, go straight away to play(), which will throw an
											// exception
				switch (selectedCardID.charAt(selectedCardID.length() - 1)) {
				case '1':
					card = curPlayer.getHand().get(0);
					break;
				case '2':
					card = curPlayer.getHand().get(1);
					break;
				case '3':
					card = curPlayer.getHand().get(2);
					break;
				case '4':
					card = curPlayer.getHand().get(3);
					break;
				default:
					card = null;
				}
				curPlayer.selectCard(card);
			}

			// Link marbles selected by player to back-end
			for (Circle marble : selectedMarbles) {
				Circle cellPositionOfMarble = marbleToCellMap.get(marble);
				Marble curMarble = null;
				switch (cellPositionOfMarble.getId().charAt(1)) {
				case 'A': // home cell (m+player+number), player would be A
					if (game.getPlayers().get(0).getMarbles().size() != 0) {
						curMarble = game.getPlayers().get(0).getMarbles()
								.get((cellPositionOfMarble.getId().charAt(2) - '0') - 1);
					}
					break;
				case 'B':
					if (game.getPlayers().get(0).getMarbles().size() != 0) {
						curMarble = game.getPlayers().get(1).getMarbles()
								.get((cellPositionOfMarble.getId().charAt(2) - '0') - 1);
					}
					break;
				case 'C':
					if (game.getPlayers().get(0).getMarbles().size() != 0) {
						curMarble = game.getPlayers().get(2).getMarbles()
								.get((cellPositionOfMarble.getId().charAt(2) - '0') - 1);

					}
					break;
				case 'D':
					if (game.getPlayers().get(0).getMarbles().size() != 0) {
						curMarble = game.getPlayers().get(3).getMarbles()
								.get((cellPositionOfMarble.getId().charAt(2) - '0') - 1);
					}
					break;
				case 'a': // safe zone (safe+player+number), second char is 'a'

					// get the safeZone of the player
					SafeZone playerSafeZone = null;
					for (SafeZone s : safeZones) {
						if (s.getColour().equals(game.getActivePlayerColour())) {
							playerSafeZone = s;
							break;
						}
					}
					curMarble = playerSafeZone.getCells().get((cellPositionOfMarble.getId().charAt(2) - '0') - 1)
							.getMarble();
					break;
				default: // marble is on track
					// get cell number from id (m+number)
					String positionString = cellPositionOfMarble.getId().substring(1);
					int num = Integer.parseInt(positionString);
					curMarble = game.getBoard().getTrack().get(num).getMarble();
				}
				curPlayer.selectMarble(curMarble);
			}

			// play according to selected cards and selected marbles
			curPlayer.play();
			game.endPlayerTurn();
			Platform.runLater(this::continueGameLoop); // Continue loop after user plays

		} catch (Exception e) {
			view.exception.Controller exceptionController = SceneConfig.getInstance().getExceptionController();
			exceptionController.exceptionPopUp(e, animationPane);
			game.deselectAll(); // deselct from back-end
			deselectAllMarbles(); // deselect animation
		}
	}

	private void deselectAllMarbles() {
		for (Circle x : movableMarbles) {
			if (selectedMarbles.contains(x)) {
				selectedMarbles.remove(x);
				x.setEffect(null);
			}
		}
	}

	
	// Link colours to back-end
	private void assignColours() {
		ArrayList<SafeZone> safeZones = game.getBoard().getSafeZones();

		ArrayList<Colour> colourOrderArrayList = new ArrayList<>();
		for (SafeZone s : safeZones) {
			colourOrderArrayList.add(s.getColour());
		}

		// Load marble images
		Map<Colour, Image> marbleImages = new HashMap<>();
		marbleImages.put(Colour.BLUE, new Image(getClass().getResourceAsStream("/view/assests/scene/BlueMarble.png")));
		marbleImages.put(Colour.GREEN,
				new Image(getClass().getResourceAsStream("/view/assests/scene/GreenMarble.png")));
		marbleImages.put(Colour.YELLOW,
				new Image(getClass().getResourceAsStream("/view/assests/scene/YellowMarble.png")));
		marbleImages.put(Colour.RED, new Image(getClass().getResourceAsStream("/view/assests/scene/RedMarble.png")));

		for (Node node : animationPane.getChildren()) {
			if (node instanceof Circle circle) {
				String id = circle.getId();
				if (id != null && id.startsWith("move") && id.length() > 5) {
					char who = id.charAt(4);
					int index = who - 'A'; // A->0, B->1, etc.

					Image marbleImage = marbleImages.getOrDefault(
							index >= 0 && index < colourOrderArrayList.size() ? colourOrderArrayList.get(index)
									: Colour.BLUE, // fallback
							marbleImages.get(Colour.BLUE));

					circle.setFill(new ImagePattern(marbleImage));
					circle.setStroke(Color.TRANSPARENT);
					movableMarbles.add(circle);

					String posId = "#m" + who + id.charAt(5);
					Node mapNode = animationPane.lookup(posId);
					if (mapNode instanceof Circle) {
						marbleToCellMap.put(circle, (Circle) mapNode);
					} else {
						System.out.println("Could not find target circle for ID: " + posId);
					}
				}

				// Reset fill for other circles marked A-D
				if (circle.getId() != null && circle.getId().length() > 1) {
					char playerChar = circle.getId().charAt(1);
					if (playerChar == 'A' || playerChar == 'B' || playerChar == 'C' || playerChar == 'D') {
						circle.setFill(Color.TRANSPARENT);
						circle.setStroke(Color.TRANSPARENT);
					}
				}
			}
		}
	}

	public int GetIndex(ArrayList<Player> y, Colour col) {
		for (int i = 0; i < 4; i++) {
			if (y.get(i).getColour() == col)
				return i;
		}
		return -1;
	}

	public void set_ALL_Hand(ArrayList<Player> players) {
		String basePathRoot = "/view/assests/deck/";

		int i = 0;
		Player player = players.get(i);

		for (int j = 0; j < 4; j++) {
			String basePath = basePathRoot;
			Card curCard = player.getHand().get(j);

			if (curCard instanceof Standard) {
				Standard card = (Standard) curCard;
				int rank = card.getRank();
				Suit suit = card.getSuit();

				switch (suit) {
				case CLUB:
					basePath += "club";
					break;
				case DIAMOND:
					basePath += "diamond";
					break;
				case SPADE:
					basePath += "spade";
					break;
				case HEART:
					basePath += "heart";
					break;
				}

				basePath += rank + ".png";

			} else if (curCard instanceof Wild) {
				if (curCard instanceof Burner) {
					basePath += "burner.jpg";
				} else if (curCard instanceof Saver) {
					basePath += "saver.jpg";
				}
			}
			Image cardImage = new Image(getClass().getResourceAsStream(basePath));

			switch (j) {
			case 0:
				playerCard1.setImage(cardImage);
				break;
			case 1:
				playerCard2.setImage(cardImage);
				break;
			case 2:
				playerCard3.setImage(cardImage);
				break;
			case 3:
				playerCard4.setImage(cardImage);
				break;
			}
		}

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

	private void Set_Your_Track() {
		for (int i = 0; i < 100; i++) {
			Circle circle = (Circle) animationPane.lookup("#m" + i);

			if (circle != null) {
				circle.setFill(Color.TRANSPARENT); // Make the fill transparent
				circle.setStroke(Color.TRANSPARENT); // Optionally, make the
														// stroke transparent
														// too
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
		marbleToCellMap.put(x, y); // set the new position of your marble
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

		Circle New_Pos = null;
		for (int i = st; i < st + steps; i++) {
			Circle target = (Circle) pathNodes.get((i % 100));
			New_Pos = target;
			double targetX = target.getLayoutX() - startX;
			double targetY = target.getLayoutY() - startY;

			TranslateTransition transition = new TranslateTransition(Duration.seconds(0.3), marble);
			transition.setToX(targetX);
			transition.setToY(targetY);
			transition.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);
			transitions.add(transition);
		}

		marbleToCellMap.put(marble, New_Pos); // set the new position of the marble to
		// be in a new location
		SequentialTransition movementSequence = new SequentialTransition();
		movementSequence.getChildren().addAll(transitions);

		// Now create a master sequence: first resize, then move
		SequentialTransition masterSequence = new SequentialTransition();
		masterSequence.getChildren().addAll(resizeTransition, movementSequence);

		masterSequence.setOnFinished(e -> {
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


	// ------------------------------------------------------------------------------------------------------------------------------
	// Code for settings
	public static boolean gamePaused = false;

	@FXML
	private ImageView settingsIcon;

	@FXML
	private void settingsIconOnMouseEntered() {
		GenericController.buttonGlowON(settingsIcon, Color.CYAN, 25);
	}

	// Method for Mouse Exited event
	@FXML
	private void settingsIconOnMouseExited() {
		GenericController.buttonGlowOFF(settingsIcon);
	}

	@FXML
	private void openSettings(MouseEvent event) {
		SoundManager.getInstance().playSound("button_click");
		gamePaused = true;// if opening settings from boardscene, pause the game
		SceneConfig.getInstance().setInGame(true);
		Parent root = SceneConfig.getInstance().getSettingsScene();
		GenericController.switchScene(event, root);
	}

	// ----------------------------------------------------------------------------------------------------------------------------------

	// Setting up names & set current and next player

	// you need to initialize these values, get players from game
	private ArrayList<String> cpuNames = new ArrayList<>(Arrays.asList(

			// Serious / Intellectual
			"Turing", "AdaNova", "Hypatia", "NeuroLynx", "Euler",
			// Funny / Meme-worthy
			"Hamoksha", "Balabizo", "Botzilla", "CPU-nicorn", "NullPointer", "NotABot"));

	public void assignNames(String playerName) {
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

	// ----------------------------------------------------------
	// Cards methods

	public void createCards() {
		ArrayList<ImageView> list = new ArrayList<>();
		list.add(playerCard1);
		list.add(playerCard2);
		list.add(playerCard3);
		list.add(playerCard4);
		setupCards(list);
	}

	// A method given a hand of a player, initializes the on mouse click
	// function of
	// each card
	// the on mouse click allows selection of only 1 card and updates the ID of
	// selected card accordingly, while providing animation
	private void setupCards(ArrayList<ImageView> cards) {
		for (ImageView card : cards) {
			card.setOnMouseClicked(event -> {
				if (event.getButton() == MouseButton.PRIMARY) {
					boolean alreadySelected = selectedCardID != null && selectedCardID.equals(card.getId());

					if (alreadySelected) {
						// Deselect all cards
						if (splitDistanceAnchorPane.isVisible()) {// the seven
																	// was
																	// deselected
							splitDistanceAnchorPane.setVisible(false);
						}
						for (ImageView c : cards) {
							selectCard(c, false);
						}
						selectedCardID = null;
						System.out.println("Deselected card.");
					} else {
						// Select this card and deselect others
						for (ImageView c : cards) {
							boolean isSelected = (c == card);
							selectCard(c, isSelected);
							if (isSelected) {
								// if the selected card is a seven, make the
								// controller visible
								if (GenericController.getCardRank(c, currentPlayerIndex, game) == 7)
									splitDistanceAnchorPane.setVisible(true);
								else
									// deselected the 7 by clicking on another
									// card
									splitDistanceAnchorPane.setVisible(false);
								selectedCardID = c.getId();
								System.out.println("Selected card ID: " + selectedCardID);
							}
						}
					}
				} else if (event.getButton() == MouseButton.SECONDARY) {
					// Right-click: show description
					view.description.Controller controller = SceneConfig.getInstance().getDescriptionController();
					controller.showCardDescription(event, card, currentPlayerIndex, game);
				}
			});
		}
	}

	private void selectCard(ImageView card, boolean select) {
		TranslateTransition tt = new TranslateTransition(Duration.millis(150), card);
		tt.setToY(select ? -15 : 0); // Move up if selected, reset if not
		tt.play();

		if (select) {
			DropShadow glow = new DropShadow();
			glow.setRadius(15);
			glow.setSpread(0.4);
			glow.setColor(Color.CORNFLOWERBLUE); // You can pick any glow color
			card.setEffect(glow);
		} else {
			card.setEffect(null); // Remove the glow effect
		}
	}

	private void disableCardSelection(ImageView card) {// called when a card is
														// played/sent to
														// firepit
		card.setOnMouseClicked(null);
	}

	// -----------------------------------------------------------------
	// Split distance feature
	private Board board;

	@FXML
	private AnchorPane splitDistanceAnchorPane;
	@FXML
	private ImageView splitDistanceOkButton;
	@FXML
	private Slider splitDistanceSlider;
	@FXML
	private Label splitDistanceLabel1;
	@FXML
	private Label splitDistanceLabel2;

	@FXML
	private void splitDistanceOkButtonOnMouseEntered() {
		GenericController.buttonGlowON(splitDistanceOkButton, Color.CYAN, 25);
	}

	@FXML
	private void splitDistanceOkButtonOnMouseExited() {
		GenericController.buttonGlowOFF(splitDistanceOkButton);
	}

	@FXML
	private void splitDistanceOkButtonOnClick() {
		SoundManager.getInstance().playSound("button_click");
		board.setSplitDistance(Integer.parseInt(splitDistanceLabel1.getText()));
		splitDistanceAnchorPane.setVisible(false);
	}

	@FXML
	private void onSplitDistanceSliderChanged() {
		int label1Val = (int) splitDistanceSlider.getValue();
		int label2Val = 7 - label1Val;

		splitDistanceLabel1.setText(String.valueOf(label1Val));
		splitDistanceLabel2.setText(String.valueOf(label2Val));
	}

}
