package view;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.util.Duration;
import javafx.scene.Parent;
import javafx.scene.input.MouseButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import view.endScreen.Controller;
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
import engine.board.Cell;
import engine.board.SafeZone;
import exception.GameException;

public class BoardController {
	Image pointerImage = new Image(getClass().getResource(
			"/view/assets/PurpleHand.png").toExternalForm());
	ImageCursor pointerCursor = new ImageCursor(pointerImage, 5, 2); // hotspot
																		// at
																		// tip

	Image sharpImage = new Image(getClass().getResource(
			"/view/assets/Mouse Cursor.png").toExternalForm());
	ImageCursor sharpCursor = new ImageCursor(sharpImage, 5, 2); // hotspot
																	// at
																	// tip

	private Game game;
	private ArrayList<Player> players;

	@FXML
	private Label nextPlayerLabel;
	@FXML
	private Label currentPlayerLabel;

	// Players Names
	@FXML
	private Label CPU1Name;
	@FXML
	private Label CPU2Name;
	@FXML
	private Label CPU3Name;
	@FXML
	private Label userName;

	@FXML
	private ImageView playerB1, playerB2, playerB3, playerB4;
	@FXML
	private ImageView playerC1, playerC2, playerC3, playerC4;
	@FXML
	private ImageView playerD1, playerD2, playerD3, playerD4;

	private ImageView[][] cpuCards;

	// Store selected marbles globally or in your controller class
	private final Set<Circle> selectedMarbles = new HashSet<>();

	@FXML
	private StackPane boardPane;

	@FXML
	private final List<ImageView> cards = new ArrayList<>();

	private ImageView selectedCardImageView;

	private String selectedCardID;
	@FXML
	private ImageView playerCard1;
	@FXML
	private ImageView playerCard2;
	@FXML
	private ImageView playerCard3;
	@FXML
	private ImageView playerCard4;
	private ImageView[] playerHand;

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
		players = game.getPlayers();

		// set up stats variables
		totalTurns = 0;
		totalTraps = 0;
		totalDiscards = 0;
		timeElapsedInSeconds = 0;
		realPlayerColour = players.get(0).getColour();
		startTimer();

		cpuCards = new ImageView[][] {
				{ playerB1, playerB2, playerB3, playerB4 },
				{ playerC1, playerC2, playerC3, playerC4 },
				{ playerD1, playerD2, playerD3, playerD4 } };
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 4; j++) {
				ImageView slot = cpuCards[i][j];

				// 1) reset transforms & make new Z + Y Rotates
				slot.getTransforms().clear();

				// pivot at center of slot
				double cx = slot.getFitWidth() / 2;
				double cy = slot.getFitHeight() / 2;

				Rotate zRotate = new Rotate(90, cx, cy, 0, Rotate.Z_AXIS);

				slot.getTransforms().addAll(zRotate);
			}

		playerHand = new ImageView[] { playerCard1, playerCard2, playerCard3,
				playerCard4 };

		setTrack();
		setSafeZones();
		assignColours();
		updatePlayerHand();

		splitDistanceAnchorPane.setVisible(false);
		createCards();

		continueGameLoop();
	}

	public void updateCpuHand() {
		List<Player> players = game.getPlayers();
		int handSize = players.get(0).getHand().size();
		if (handSize == 4 && newHand) {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 4; j++) {
					ImageView slot = cpuCards[i][j];
					slot.setVisible(true);

					// start hidden/off‑scale
					slot.setScaleX(0);
					slot.setScaleY(0);
					slot.setOpacity(0);

					ScaleTransition st = new ScaleTransition(
							Duration.millis(1000), slot);
					st.setFromX(0);
					st.setToX(1);
					st.setFromY(0);
					st.setToY(1);

					FadeTransition ft = new FadeTransition(
							Duration.millis(1000), slot);
					ft.setFromValue(0);
					ft.setToValue(1);

					// no delay → truly simultaneous
					new ParallelTransition(st, ft).play();
				}
			}
			return;
		}
		for (int i = 1; i <= 3; i++) {
			Player cpu = players.get(i);
			handSize = cpu.getHand().size();
			for (int j = 0; j < 4; j++) {
				ImageView slot = cpuCards[i - 1][j];
				if (j < handSize) {
					slot.setVisible(true); // keep it visible
					slot.setOpacity(1.0); // in case it was fading out before
				} else if (slot.isVisible()) {
					// If currently visible but should be hidden → animate
					// fade-out
					SoundManager.getInstance().playSound("playCardSoundEffect");

					FadeTransition fadeOut = new FadeTransition(
							Duration.millis(400), slot);
					fadeOut.setFromValue(1.0);
					fadeOut.setToValue(0.0);
					fadeOut.setOnFinished(e -> slot.setVisible(false));
					fadeOut.play();
				}
			}
		}
	}

	@FXML
	private Pane pitPane;

	private static final int MAX_PIT_CARDS = 102;
	private final Random rnd = new Random();

	/**
	 * Clone the given card ImageView and animate it into the pit.
	 * 
	 * @param sourceSlot
	 *            the ImageView in the player's hand
	 */
	private void sendToPit(ImageView sourceSlot) {
		// 1) Create the “ghost” card
		ImageView ghost = new ImageView(sourceSlot.getImage());
		ghost.setFitWidth(sourceSlot.getFitWidth());
		ghost.setFitHeight(sourceSlot.getFitHeight());
		ghost.setPreserveRatio(true);

		// 2) Copy any transforms (e.g. your Z/Y rotates):
		ghost.getTransforms().setAll(sourceSlot.getTransforms());

		// 3) Position it exactly over the source slot:
		Point2D start = sourceSlot.localToScene(0, 0);
		Point2D pitLocal = pitPane.sceneToLocal(start);
		ghost.setLayoutX(pitLocal.getX());
		ghost.setLayoutY(pitLocal.getY());

		// 4) Add to pitPane and bring to front:
		pitPane.getChildren().add(ghost);
		ghost.toFront(); // Ensure it's on top of everything else

		// 5) Random final rotation & offset:
		double finalRotate = (rnd.nextDouble() * 20) - 90; // –90°…–70° roughly
		double offsX = 0;
		double offsY = 0;

		// 6) Compute translation to center of pitPane + offset
		Bounds pitBounds = pitPane.getLayoutBounds();
		double targetX = (pitBounds.getWidth() / 2)
				- (sourceSlot.getFitWidth() / 2) + offsX;
		double targetY = (pitBounds.getHeight() / 2)
				- (sourceSlot.getFitHeight() / 2) + offsY;

		// 7) Build the move + rotate animation
		TranslateTransition tt = new TranslateTransition(Duration.millis(800),
				ghost);
		tt.setToX(targetX - pitLocal.getX());
		tt.setToY(targetY - pitLocal.getY());
		tt.setInterpolator(Interpolator.EASE_IN);

		RotateTransition rt = new RotateTransition(Duration.millis(800), ghost);
		rt.setByAngle(finalRotate);
		rt.setInterpolator(Interpolator.EASE_IN);

		ParallelTransition toss = new ParallelTransition(tt, rt);
		toss.setOnFinished(e -> cleanupPitIfNeeded());
		toss.play();
		// Play sound
		SoundManager.getInstance().playSound("playCardSoundEffect");

	}

	/** Remove oldest ghosts if we’ve exceeded MAX_PIT_CARDS */
	private void cleanupPitIfNeeded() {
		if (pitPane.getChildren().size() > MAX_PIT_CARDS) {
			// Remove the first N to bring us back under limit
			int toRemove = pitPane.getChildren().size() - MAX_PIT_CARDS;
			pitPane.getChildren().subList(0, toRemove).clear();
		}
	}

	@FXML
	private ImageView firepitImage;

	private void updatePit() {
		if (game.getFirePit().isEmpty() || currentPlayerIndex == 0)
			return;

		ArrayList<Card> firePit = game.getFirePit();
		Image newCardImage = getCardImage(firePit.get(firePit.size() - 1));

		// Create a temporary overlay image
		ImageView overlay = new ImageView(newCardImage);
		overlay.setFitWidth(firepitImage.getFitWidth());
		overlay.setFitHeight(firepitImage.getFitHeight());
		overlay.setPreserveRatio(true);
		overlay.setOpacity(0);

		// Position the overlay exactly over the firepitImage
		overlay.setLayoutX(firepitImage.getLayoutX());
		overlay.setLayoutY(firepitImage.getLayoutY());

		// Add the overlay on top of the pit
		pitPane.getChildren().add(overlay);

		// Fade in the new card
		FadeTransition fadeIn = new FadeTransition(Duration.millis(600),
				overlay);
		fadeIn.setFromValue(0);
		fadeIn.setToValue(1);
		fadeIn.setInterpolator(Interpolator.EASE_OUT);

		fadeIn.setOnFinished(e -> {
			// Replace the actual firepitImage and remove the overlay
			firepitImage.setImage(newCardImage);
			pitPane.getChildren().remove(overlay);

			// Ensure firepitImage is on top again
			pitPane.getChildren().remove(firepitImage);
			pitPane.getChildren().add(firepitImage);
		});

		fadeIn.play();
	}

	private boolean newHand = true;

	public void updatePlayerHand() {
		ArrayList<Card> hand = players.get(0).getHand();
		if (hand.size() == 4) {
			if (!newHand)
				return;
			newHand = false;
			Image backImage = new Image(getClass().getResourceAsStream(
					"/view/assests/deck/NeonBack2.png"));

			for (int i = 0; i < 4; i++) {
				playerHand[i].setVisible(true);
				playerHand[i].setDisable(false);
				final int idx = i;
				ImageView slot = playerHand[idx];

				// 1) reset transforms & make new Z + Y Rotates
				slot.getTransforms().clear();

				// pivot at center of slot
				double cx = slot.getFitWidth() / 2;
				double cy = slot.getFitHeight() / 2;

				Rotate zRotate = new Rotate(90, cx, cy, 0, Rotate.Z_AXIS);
				Rotate yRotate = new Rotate(0, cx, cy, 0, Rotate.Y_AXIS);

				slot.getTransforms().addAll(zRotate, yRotate);

				// 2) reset to back image, hidden/off‑scale
				slot.setImage(backImage);
				slot.setScaleX(0);
				slot.setScaleY(0);
				slot.setOpacity(0);

				// 3) deal‑in: scale + fade
				ScaleTransition st = new ScaleTransition(Duration.millis(1000),
						slot);
				st.setFromX(0);
				st.setToX(1);
				st.setFromY(0);
				st.setToY(1);

				FadeTransition ft = new FadeTransition(Duration.millis(1000),
						slot);
				ft.setFromValue(0);
				ft.setToValue(1);

				ParallelTransition dealIn = new ParallelTransition(st, ft);
				dealIn.setDelay(Duration.millis(idx * 100));

				// 4) pause before flip
				PauseTransition pause = new PauseTransition(
						Duration.millis(200 + idx * 50));

				// 5) first half flip: yRotate.angle 0→90
				Timeline flipOut = new Timeline(new KeyFrame(Duration.ZERO,
						new KeyValue(yRotate.angleProperty(), 0,
								Interpolator.EASE_IN)), new KeyFrame(
						Duration.millis(500), new KeyValue(
								yRotate.angleProperty(), 90,
								Interpolator.EASE_IN)));
				flipOut.setOnFinished(e -> slot.setImage(getCardImage(hand
						.get(idx))));

				// 6) second half flip: 90→0
				Timeline flipIn = new Timeline(new KeyFrame(Duration.ZERO,
						new KeyValue(yRotate.angleProperty(), 90,
								Interpolator.EASE_OUT)), new KeyFrame(
						Duration.millis(500), new KeyValue(
								yRotate.angleProperty(), 0,
								Interpolator.EASE_OUT)));

				SequentialTransition flip = new SequentialTransition(pause,
						flipOut, flipIn);
				flip.setDelay(Duration.millis(idx * 100 + 300));

				// 7) play deal‑in then flip
				new SequentialTransition(dealIn, flip).play();
			}
			return;
		} else {
			newHand = true;
		}
		if (game.getActivePlayerColour() == players.get(1).getColour()
				&& selectedCardImageView != null)
			sendToPit(selectedCardImageView);
		int i = 0;
		for (; i < hand.size(); i++) {
			Card curCard = hand.get(i);
			Image cardImage = getCardImage(curCard);
			playerHand[i].setVisible(true);
			playerHand[i].setDisable(false);
			playerHand[i].setImage(cardImage);
		}
		for (; i < 4; i++) {
			playerHand[i].setVisible(false);
			playerHand[i].setDisable(true);

		}
		deselectAllCards(playerHand);
	}

	public void Change_Track() {
		// Initialize 4x4 list to track marbles already placed
		List<List<Integer>> tot = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			List<Integer> row = new ArrayList<>(Collections.nCopies(4, 0));
			tot.add(row);
		}

		// Prepare colour order for indexing
		ArrayList<Colour> colourOrderArrayList = new ArrayList<>();
		for (SafeZone s : game.getBoard().getSafeZones()) {
			colourOrderArrayList.add(s.getColour());
			// System.out.println(s.getColour().toString()); // check if he uses
			// the colour
			// correctly or not
		}
		// Handle Home Cell marbles
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < game.getPlayers().get(i).getMarbles().size(); j++) {
				Marble marble = game.getPlayers().get(i).getMarbles().get(j);
				if (marble != null && tot.get(i).get(j) == 0) {
					tot.get(i).set(j, 1);
					String newPos = "move" + (char) ('A' + i) + (j + 1);
					String destId = "m" + (char) ('A' + i) + (j + 1);

					Circle from = (Circle) animationPane.lookup("#" + newPos);
					Circle to = (Circle) animationPane.lookup("#" + destId);

					if (from != null && to != null) {
						marbleToCellMap.put(from, to);
						from.setLayoutX(to.getLayoutX());
						from.setLayoutY(to.getLayoutY());
						from.setRadius(to.getRadius()); // Resize from to match
														// to
					}
				}
			}
		}
		// Handle Safe Zone marbles
		for (int i = 0; i < 4; i++) {

			for (int j = 0; j < 4; j++) {
				Cell cell = game.getBoard().getSafeZones().get(i).getCells()
						.get(j);
				if (cell.getMarble() != null) {
					for (int k = 0; k < 4; k++) {
						if (tot.get(i).get(k) == 0) {
							tot.get(i).set(k, 1);
							String newPos = "move" + (char) ('A' + i) + (k + 1);
							String destId = "safe" + (char) ('A' + i) + (j + 1);
							Circle from = (Circle) animationPane.lookup("#"
									+ newPos);
							Circle to = (Circle) animationPane.lookup("#"
									+ destId);

							if (from != null && to != null) {
								marbleToCellMap.put(from, to);
								from.setLayoutX(to.getLayoutX());
								from.setLayoutY(to.getLayoutY());
								from.setRadius(to.getRadius()); // Resize from
																// to match to
							}
							break;
						}
					}
				}
			}
		}

		// Handle Track marbles
		for (int i = 0; i < 100; i++) {
			Cell cell = game.getBoard().getTrack().get(i);
			Marble marble = cell.getMarble();
			if (marble != null) {
				Colour col = marble.getColour();
				for (int j = 0; j < 4; j++) {

					if (col == colourOrderArrayList.get(j)) {
						List<Integer> playerPos = tot.get(j);
						for (int k = 0; k < 4; k++) {
							if (playerPos.get(k) == 0) {
								playerPos.set(k, 1);
								String newPos = "move" + (char) ('A' + j)
										+ (k + 1);
								Circle from = (Circle) animationPane.lookup("#"
										+ newPos);
								Circle to = (Circle) animationPane.lookup("#"
										+ track.get(i).getId());

								if (from != null && to != null) {
									marbleToCellMap.put(from, to);
									from.setLayoutX(to.getLayoutX());
									from.setLayoutY(to.getLayoutY());
									from.setRadius(to.getRadius()); // Resize
																	// from to
																	// match to
								}
								break;
							}
						}
						break;
					}
				}
			}
		}
	}

	private void disablePlayerButtons() {
		skipTurnButton.setDisable(true);
		skipTurnButton.setOpacity(0.5);
		playButton.setDisable(true);
		playButton.setOpacity(0.5);
	}

	public void enablePlayerButtons() {
		skipTurnButton.setDisable(false);
		skipTurnButton.setOpacity(1);
		playButton.setDisable(false);
		playButton.setOpacity(1);
	}

	private void continueGameLoop() {

		setCurrentPlayerLabel();
		setNextPlayerLabel();
		totalTurns++;

		if (game.checkWin() != null) {// someone won
			stopTimer();

			SceneConfig.getInstance().setWinnerName(game.checkWin(),
					players.get(0).getColour());

			SceneConfig.getInstance().setStatistics(timeElapsedInSeconds,
					totalDiscards, totalTraps, totalTurns);

			Controller controller = SceneConfig.getInstance()
					.getEndScreenController();

			// Play the first sound
			SoundManager.getInstance().playSoundOnce(
					"/view/assets/audio/GameOverVoiceOver.mp3");

			// Schedule the second sound to play 1 second later
			PauseTransition delay = new PauseTransition(Duration.seconds(1));
			delay.setOnFinished(event -> {
				SoundManager.getInstance().playSoundOnce(
						"/view/assets/audio/GameOverSoundEffect.mp3");
			});
			delay.play();

			// Fade out the current pane
			FadeTransition fadeOut = new FadeTransition(Duration.millis(2000),
					animationPane);
			fadeOut.setFromValue(1.0);
			fadeOut.setToValue(0.0);
			fadeOut.setOnFinished(e -> {
				Parent root = SceneConfig.getInstance().getEndScene();

				controller.updateWinner(game.checkWin(), players.get(0)
						.getColour());
				controller.playFadeIn();

				Stage stage = (Stage) animationPane.getScene().getWindow();
				stage.getScene().setRoot(root);
			});

			fadeOut.play();
			return;
		}

		Colour curPlayer = game.getActivePlayerColour();
		currentPlayerIndex = getIndex(players, curPlayer);

		if (!game.canPlayTurn()) {

			if (currentPlayerIndex == 0) {
				totalDiscards++;// increase total player discards
				disablePlayerButtons();
			}

			// get grid Pane and stack Pane to be shown
			GridPane skippedPlayerGridPane;
			AnchorPane skippedPlayerStackPane;

			switch (currentPlayerIndex) {
			case 0:
				skippedPlayerGridPane = A;
				skippedPlayerStackPane = skippedPaneP1;
				break;
			case 1:
				skippedPlayerGridPane = B;
				skippedPlayerStackPane = skippedPaneP2;
				break;
			case 2:
				skippedPlayerGridPane = C;
				skippedPlayerStackPane = skippedPaneP3;
				break;
			case 3:
				skippedPlayerGridPane = D;
				skippedPlayerStackPane = skippedPaneP4;
				break;
			default:
				skippedPlayerGridPane = null;
				skippedPlayerStackPane = null;
			}

			visualizeSkippedTurn(skippedPlayerGridPane, skippedPlayerStackPane);
			// Wait before showing animation or calling Change_Track
			PauseTransition delay = new PauseTransition(Duration.seconds(2));
			delay.setOnFinished(event -> {

				// add popup you got hacked ..... skip
				PauseTransition postAnimationDelay = new PauseTransition(
						Duration.seconds(2));
				postAnimationDelay.setOnFinished(e -> {
					game.endPlayerTurn();
					Platform.runLater(this::continueGameLoop);
				});
				postAnimationDelay.play();
			});
			delay.play();
			return;
		}

		if (currentPlayerIndex == 0) {
			System.out.println("Waiting for player to click Play.");
			enablePlayerButtons();
			// Human player: wait for button click
		} else {
			System.out.println("AI is playing...");
			disablePlayerButtons();
			playAITurnWithDelay();
		}
	}

	private void playAITurnWithDelay() {
		try {
			game.playPlayerTurn(); // AI chooses move immediately

			// Wait before showing animation or calling Change_Track
			PauseTransition delay = new PauseTransition(Duration.seconds(2));
			delay.setOnFinished(event -> {
				game.endPlayerTurn();
				updateBoard();
				PauseTransition postAnimationDelay = new PauseTransition(
						Duration.seconds(2));
				postAnimationDelay.setOnFinished(e -> {
					Platform.runLater(this::continueGameLoop);
				});
				postAnimationDelay.play();
			});

			delay.play();

		} catch (GameException ee) {
			ee.printStackTrace();
			game.endPlayerTurn();
			PauseTransition postAnimationDelay = new PauseTransition(
					Duration.seconds(2));
			updateCpuHand();
			updatePlayerHand();
			postAnimationDelay.setOnFinished(e -> {
				Platform.runLater(this::continueGameLoop);
			});
			postAnimationDelay.play();
		}
	}

	private void updateBoard() {
		Change_Track();
		updatePit();
		updateCpuHand();
		updatePlayerHand();
	}

	@FXML
	private void onPlayClicked() throws Exception {
		ArrayList<SafeZone> safeZones = game.getBoard().getSafeZones();
		Player curPlayer = game.getPlayers().get(0);
		if (game.getActivePlayerColour() != curPlayer.getColour())
			return;

		try {
			// Link card selected from GUI to back end
			Card card = null;
			if (selectedCardID != null) { // if the card is null, go straight
				// away to play(), which will throw
				// an
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

			// System.out.println(card.getName()); // here

			// Link marbles selected by player to back-end
			for (Circle marble : selectedMarbles) {
				Circle cellPositionOfMarble = marbleToCellMap.get(marble);
				Marble curMarble = null;
				switch (cellPositionOfMarble.getId().charAt(1)) {
				case 'A': // home cell (m+player+number), player would be A
					if (game.getPlayers().get(0).getMarbles().size() != 0) {
						curMarble = game
								.getPlayers()
								.get(0)
								.getMarbles()
								.get((cellPositionOfMarble.getId().charAt(2) - '0') - 1);
					}
					break;
				case 'B':
					if (game.getPlayers().get(1).getMarbles().size() != 0) {
						curMarble = game
								.getPlayers()
								.get(1)
								.getMarbles()
								.get((cellPositionOfMarble.getId().charAt(2) - '0') - 1);
					}
					break;
				case 'C':
					if (game.getPlayers().get(2).getMarbles().size() != 0) {
						curMarble = game
								.getPlayers()
								.get(2)
								.getMarbles()
								.get((cellPositionOfMarble.getId().charAt(2) - '0') - 1);

					}
					break;
				case 'D':
					if (game.getPlayers().get(3).getMarbles().size() != 0) {
						curMarble = game
								.getPlayers()
								.get(3)
								.getMarbles()
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
					System.out.println(cellPositionOfMarble.getId());
					curMarble = playerSafeZone
							.getCells()
							.get((cellPositionOfMarble.getId().charAt(5) - '0') - 1)
							.getMarble();
					break;
				default: // marble is on track
					// get cell number from id (m+number)
					String positionString = cellPositionOfMarble.getId()
							.substring(1);
					int num = Integer.parseInt(positionString);
					curMarble = game.getBoard().getTrack().get(num).getMarble();
				}
				curPlayer.selectMarble(curMarble);
			}

			// play according to selected cards and selected marbles

			game.playPlayerTurn();
			Change_Track(); // Animate human move
			// sendToPit(selectedCardImageView); // Optional visual logic
			game.endPlayerTurn();
			PauseTransition delay = new PauseTransition(Duration.seconds(2));
			delay.setOnFinished(event -> {
				updateBoard();
				deselectAllMarbles();
				Platform.runLater(this::continueGameLoop);
			});
			delay.play();

		} catch (Exception e) {
			SoundManager.getInstance().playSound("errorSoundEffect");
			view.exception.Controller exceptionController = SceneConfig
					.getInstance().getExceptionController();
			exceptionController.exceptionPopUp(e, animationPane,
					selectedCardImageView, game);
			game.deselectAll(); // deselct from back-end
			deselectAllMarbles(); // deselect animation
			Platform.runLater(this::continueGameLoop);
		}

		disablePlayerButtons();
	}

	// Used in onPlayClicked
	private void deselectAllMarbles() {
		for (Circle x : movableMarbles) {
			if (selectedMarbles.contains(x)) {
				selectedMarbles.remove(x);
				x.setEffect(null);
			}
		}
	}

	@FXML
	private Label basePlayer1;
	@FXML
	private Label basePlayer2;
	@FXML
	private Label basePlayer3;
	@FXML
	private Label basePlayer4;

	@FXML
	private Label colorPlayer1;
	@FXML
	private Label colorPlayer2;
	@FXML
	private Label colorPlayer3;
	@FXML
	private Label colorPlayer4;

	// Link colours to back-end
	private void assignColours() {
		ArrayList<SafeZone> safeZones = game.getBoard().getSafeZones();

		ArrayList<Colour> colourOrderArrayList = new ArrayList<>();
		for (SafeZone s : safeZones) {
			colourOrderArrayList.add(s.getColour());
		}

		// Load marble images
		Map<Colour, Image> marbleImages = new HashMap<>();
		marbleImages.put(
				Colour.BLUE,
				new Image(getClass().getResourceAsStream(
						"/view/assests/scene/BlueMarble.png")));
		marbleImages.put(Colour.GREEN, new Image(getClass()
				.getResourceAsStream("/view/assests/scene/GreenMarble.png")));
		marbleImages.put(Colour.YELLOW, new Image(getClass()
				.getResourceAsStream("/view/assests/scene/YellowMarble.png")));
		marbleImages.put(
				Colour.RED,
				new Image(getClass().getResourceAsStream(
						"/view/assests/scene/RedMarble.png")));

		for (Node node : animationPane.getChildren()) {
			if (node instanceof Circle) {
				Circle circle = (Circle) node;
				String id = circle.getId();
				if (id != null && id.startsWith("move") && id.length() > 5) {
					char who = id.charAt(4);
					int index = who - 'A'; // A->0, B->1, etc.

					Image marbleImage = marbleImages
							.getOrDefault(
									index >= 0
											&& index < colourOrderArrayList
													.size() ? colourOrderArrayList
											.get(index) : Colour.BLUE, // fallback
									marbleImages.get(Colour.BLUE));

					circle.setFill(new ImagePattern(marbleImage));
					circle.setStroke(Color.TRANSPARENT);
					movableMarbles.add(circle);

					String posId = "#m" + who + id.charAt(5);
					Node mapNode = animationPane.lookup(posId);
					if (mapNode instanceof Circle) {
						marbleToCellMap.put(circle, (Circle) mapNode);
					} else {
						System.out
								.println("Could not find target circle for ID: "
										+ posId);
					}
				}

				// Reset fill for other circles marked A-D
				if (circle.getId() != null && circle.getId().length() > 1) {
					char playerChar = circle.getId().charAt(1);
					if (playerChar == 'A' || playerChar == 'B'
							|| playerChar == 'C' || playerChar == 'D') {
						circle.setFill(Color.TRANSPARENT);
						circle.setStroke(Color.TRANSPARENT);
					}
				}
			}
		}

		// Map each label to its player’s color
		Label[] bases = { basePlayer1, basePlayer2, basePlayer3, basePlayer4 };
		Label[] indicators = { colorPlayer1, colorPlayer2, colorPlayer3,
				colorPlayer4 };
		for (int i = 0; i < colourOrderArrayList.size() && i < bases.length; i++) {
			Colour colour = colourOrderArrayList.get(i);
			Color textFill;
			Glow glow = new Glow(0.3);
			DropShadow neonEffect = new DropShadow();

			switch (colour) {
			case RED:
				textFill = Color.web("#FF1C3A"); // Neon red
				neonEffect.setColor(textFill);
				break;
			case BLUE:
				textFill = Color.web("#00FFFF"); // Bright cyan
				neonEffect.setColor(textFill);
				break;
			case GREEN:
				textFill = Color.web("#32CD32"); // Neon green
				neonEffect.setColor(textFill);
				break;
			case YELLOW:
				textFill = Color.web("#FFA000"); // Orange yellow
				neonEffect.setColor(textFill);
				break;
			default:
				textFill = Color.web("#CCCCCC"); // Soft gray fallback
				neonEffect.setColor(textFill);

			}

			bases[i].setTextFill(textFill);
			bases[i].setEffect(new Blend(BlendMode.ADD, neonEffect, glow));
			indicators[i].setTextFill(textFill);
			indicators[i].setEffect(new Blend(BlendMode.ADD, neonEffect, glow));
			if (i == 0) {
				userName.setTextFill(textFill);
				userName.setEffect(new Blend(BlendMode.ADD, neonEffect, glow));
			} else if (i == 1) {
				CPU1Name.setTextFill(textFill);
				CPU1Name.setEffect(new Blend(BlendMode.ADD, neonEffect, glow));
			} else if (i == 2) {
				CPU2Name.setTextFill(textFill);
				CPU2Name.setEffect(new Blend(BlendMode.ADD, neonEffect, glow));
			} else {
				CPU3Name.setTextFill(textFill);
				CPU3Name.setEffect(new Blend(BlendMode.ADD, neonEffect, glow));
			}

		}
	}

	// needed in continueGameLoop
	public int getIndex(ArrayList<Player> y, Colour col) {
		for (int i = 0; i < 4; i++) {
			if (y.get(i).getColour() == col)
				return i;
		}
		return -1;
	}

	private void setSafeZones() {
		for (Node node : animationPane.getChildren()) {
			if (node instanceof Circle && node.getId() != null
					&& node.getId().startsWith("safe")) {
				((Circle) node).setFill(Color.TRANSPARENT);
			}
		}
	}

	@FXML
	private void setupMarbleSelection(MouseEvent event) {
		SoundManager.getInstance().playSound("Marble_Selection");
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

	private void setTrack() {
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
				System.out.println("Warning: Circle with fx:id m" + i
						+ " not found.");
			}
		}
	}

	private ScaleTransition smoothlyResize(Circle circle1, Circle circle2) {
		double circle1Radius = circle1.getRadius();
		double circle2Radius = circle2.getRadius();
		double scaleFactor = circle2Radius / circle1Radius;

		ScaleTransition scaleTransition = new ScaleTransition(
				Duration.seconds(1.5), circle1);
		scaleTransition.setToX(scaleFactor);
		scaleTransition.setToY(scaleFactor);
		scaleTransition
				.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);

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
		TranslateTransition move = new TranslateTransition(
				Duration.millis(1500), x); // match duration
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

	public void moveToBase(Circle x) {
		char To = x.getId().charAt(4);
		String targetId = "#m";
		if (To == 'A')
			targetId += 0;
		else if (To == 'B')
			targetId += 25;
		else if (To == 'C') {
			targetId += 50;
		} else {
			targetId += 75;
		}
		System.out.println(targetId);
		// Find the destination circle
		Circle y = (Circle) animationPane.lookup(targetId);
		if (y != null) {
			move_from_to(x, y);
		} else {
			System.out.println("Destination circle not found: " + targetId);
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------
	// Code for settings
	@FXML
	private ImageView settingsIcon;

	@FXML
	private void settingsIconOnMouseEntered() {
		GenericController.buttonGlowON(settingsIcon, Color.PURPLE, 25);
	}

	// Method for Mouse Exited event
	@FXML
	private void settingsIconOnMouseExited() {
		GenericController.buttonGlowOFF(settingsIcon);
	}

	@FXML
	private void openSettings(MouseEvent event) {
		SoundManager.getInstance().playSound("button_click");
		SceneConfig.getInstance().setInGame(true);
		Parent root = SceneConfig.getInstance().getSettingsScene();
		GenericController.switchScene(event, root);
	}

	// ----------------------------------------------------------------------------------------------------------------------------------

	// Setting up names & set current and next player

	// you need to initialize these values, get players from game
	private ArrayList<String> cpuNames = new ArrayList<>(
			Arrays.asList(

					// Serious / Intellectual
					"Turing", "AdaNova", "Hypatia", "NeuroLynx", "Euler",
					// Funny / Meme-worthy
					"Hamoksha", "Balabizo", "Botzilla", "CPU-nicorn", "Meow",
					"NotABot"));

	// Called from outside the class
	public void assignNames(String playerName) {
		Collections.shuffle(cpuNames);
		CPU1Name.setText(cpuNames.get(0));
		CPU2Name.setText(cpuNames.get(1));
		CPU3Name.setText(cpuNames.get(2));
		userName.setText(playerName);
	}

	public void setCurrentPlayerLabel() {
		Colour colour = game.getActivePlayerColour();
		setPlayerLabel(currentPlayerLabel, colour);
	}

	public void setNextPlayerLabel() {
		Colour colour = game.getNextPlayerColour();
		setPlayerLabel(nextPlayerLabel, colour);
	}

	// Helper which given a label and a colour, searches for the correct player
	// given the colour and assigns the given label to the name of the colour
	// holder accordingly
	private void setPlayerLabel(Label label, Colour colour) {

		Color textFill;
		Glow glow = new Glow(0.3);
		DropShadow neonEffect = new DropShadow();

		// get appropriate filling for label
		switch (colour) {
		case RED:
			textFill = Color.web("#FF1C3A"); // Neon red
			neonEffect.setColor(textFill);
			break;
		case BLUE:
			textFill = Color.web("#00FFFF"); // Bright cyan
			neonEffect.setColor(textFill);
			break;
		case GREEN:
			textFill = Color.web("#32CD32"); // Neon green
			neonEffect.setColor(textFill);
			break;
		case YELLOW:
			textFill = Color.web("#FFA000"); // Orange yellow
			neonEffect.setColor(textFill);
			break;
		default:
			textFill = Color.web("#CCCCCC"); // Soft gray fallback
			neonEffect.setColor(textFill);

		}

		// assign fillings based on colour in backend
		label.setTextFill(textFill);
		label.setEffect(new Blend(BlendMode.ADD, neonEffect, glow));

		// assign names based on entered and randomly distributed in frontend
		int cpuCount = 0;
		for (Player p : players) {
			if (!(p instanceof CPU)) {
				if (p.getColour() == colour) {
					label.setText(userName.getText());
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
			card.setOnMouseEntered(e -> card.setCursor(pointerCursor));

			card.setOnMouseClicked(event -> {
				if (event.getButton() == MouseButton.PRIMARY) {
					SoundManager.getInstance().playSound("Card_Selection");
					boolean alreadySelected = selectedCardID != null
							&& selectedCardID.equals(card.getId());

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
						selectedCardImageView = null;
						System.out.println("Deselected card.");
					} else {
						// Select this card and deselect others
						for (ImageView c : cards) {
							boolean isSelected = (c == card);
							selectCard(c, isSelected);
							if (isSelected) {
								// if the selected card is a seven, make the
								// controller visible
								if (GenericController.getCardRank(c,
										currentPlayerIndex, game) == 7)
									splitDistanceAnchorPane.setVisible(true);
								else
									// deselected the 7 by clicking on another
									// card
									splitDistanceAnchorPane.setVisible(false);
								selectedCardID = c.getId();
								selectedCardImageView = c;
								System.out.println("Selected card ID: "
										+ selectedCardID);
							}
						}
					}
				} else if (event.getButton() == MouseButton.SECONDARY) {
					// Right-click: show description
					view.description.Controller controller = SceneConfig
							.getInstance().getDescriptionController();
					controller.showCardDescription(event, card,
							currentPlayerIndex, game);
				}
			});
		}
	}

	private void selectCard(ImageView card, boolean select) {
		TranslateTransition tt = new TranslateTransition(Duration.millis(150),
				card);
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

	private void deselectAllCards(ImageView[] cards) {
		for (ImageView card : cards) {
			TranslateTransition tt = new TranslateTransition(
					Duration.millis(150), card);
			tt.setToY(0); // Move up if selected, reset if not
			tt.play();
			card.setEffect(null);
			selectedCardID = null;
			selectedCardImageView = null;
		}
	}

	// -----------------------------------------------------------------
	// Split distance feature

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
		game.getBoard().setSplitDistance(
				Integer.parseInt(splitDistanceLabel1.getText()));
		splitDistanceAnchorPane.setVisible(false);
	}

	@FXML
	private void onSplitDistanceSliderChanged() {
		int label1Val = (int) splitDistanceSlider.getValue();
		int label2Val = 7 - label1Val;

		splitDistanceLabel1.setText(String.valueOf(label1Val));
		splitDistanceLabel2.setText(String.valueOf(label2Val));
	}

	// -----------------------------------------------------------------
	//

	@FXML
	private Button skipTurnButton;

	@FXML
	private void skipTurnButtonOnClick() throws Exception {
		if (game.getActivePlayerColour() != game.getPlayers().get(0)
				.getColour())
			return;
		if (selectedCardImageView == null) {
			SoundManager.getInstance().playSound("errorSoundEffect");
			view.exception.Controller exceptionController = SceneConfig
					.getInstance().getExceptionController();
			exceptionController
					.exceptionPopUp(
							new Exception(
									"You must first choose a card to sacrifice before you can pass the turn."),
							animationPane, selectedCardImageView, game);
		} else {
			// Link card selected from GUI to back end
			Card card = null;
			Player curPlayer = game.getPlayers().get(0);
			if (selectedCardID != null) { // if the card is null, go straight
				// away to play(), which will throw
				// an
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
			game.endPlayerTurn();
			updateBoard();
			deselectAllMarbles();
			PauseTransition delay = new PauseTransition(Duration.seconds(2));
			delay.setOnFinished(event -> {
				Platform.runLater(this::continueGameLoop);
			});
			delay.play();
		}
		disablePlayerButtons();
	}

	@FXML
	private void fieldMarbleShortcut(KeyEvent event) {
		ArrayList<Marble> homeMarbles = players.get(currentPlayerIndex)
				.getMarbles();

		int Num = 6;
		Circle CurMarble = null;
		for (Map.Entry<Circle, Circle> entry : marbleToCellMap.entrySet()) {
			Circle marble = entry.getKey();
			Circle cell = entry.getValue();
			System.out.println(marble.getId() + "   " + cell.getId());
			if (cell.getId().charAt(1) == 'A') {
				// Home Cell
				if ((cell.getId().charAt(2) - '0') < Num) {
					Num = (cell.getId().charAt(2) - '0');
					CurMarble = marble;
				}
			}
		}

		if (currentPlayerIndex == 0 && event.getCode() == KeyCode.F) {
			SoundManager.getInstance().playSound("button_click");
			for (int i = 0; i < homeMarbles.size(); i++) {
				if (homeMarbles.get(i) != null) {
					try {
						game.fieldMarble();
						updateBoard();
						PauseTransition delay = new PauseTransition(
								Duration.seconds(2));
						delay.setOnFinished(ae -> Platform
								.runLater(this::continueGameLoop));
						delay.play();

					} catch (Exception e) {
						view.exception.Controller exceptionController = SceneConfig
								.getInstance().getExceptionController();
						exceptionController.exceptionPopUp(e, animationPane,
								selectedCardImageView, game);
					}
					return;
				}
			}
			view.exception.Controller exceptionController = SceneConfig
					.getInstance().getExceptionController();
			exceptionController.exceptionPopUp(new Exception(
					"No marbles to field"), animationPane,
					selectedCardImageView, game);
		}
	}

	// getters
	public ImageView getImageSource(String x) {
		if (playerCard1.getId().equals(x))
			return playerCard1;
		if (playerCard2.getId().equals(x))
			return playerCard2;
		if (playerCard3.getId().equals(x))
			return playerCard3;
		if (playerCard4.getId().equals(x))
			return playerCard4;
		return null;

	}

	private Image getCardImage(Card curCard) {
		String basePath = "/view/assests/deck/";
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
		return new Image(getClass().getResourceAsStream(basePath));
	}

	@FXML
	private Button playButton;

	// ---------------------------------------------------------------
	@FXML
	private AnchorPane skippedPaneP1;
	@FXML
	private AnchorPane skippedPaneP2;
	@FXML
	private AnchorPane skippedPaneP3;
	@FXML
	private AnchorPane skippedPaneP4;

	private void visualizeSkippedTurn(GridPane playerGrid,
			AnchorPane overlayPane) {
		SoundManager.getInstance().playSound("skipTurnSound");

		// === 1. Shake Animation ===
		TranslateTransition shake = new TranslateTransition(
				Duration.millis(80), playerGrid);
		shake.setFromX(-8);
		shake.setToX(8);
		shake.setCycleCount(6); // 3 full shakes
		shake.setAutoReverse(true);
		shake.play();

		// === 2. Create Neon "SKIPPED" Label ===
		Label skippedLabel = new Label("SKIPPED");
		skippedLabel
				.setStyle("-fx-text-fill: linear-gradient(to bottom, #222, #000);"
						+ // Neon black theme
						"-fx-font-size: 40px;"
						+ // Increased font size
						"-fx-font-weight: bold;"
						+ "-fx-effect: dropshadow(gaussian, #FFFFFF, 8, 0.3, 0, 0);"
						+ // Cyan glow
						"-fx-background-color: transparent;");
		skippedLabel.setOpacity(0); // Start invisible

		overlayPane.getChildren().add(skippedLabel);
		StackPane.setAlignment(skippedLabel, Pos.CENTER);

		// === 3. Fade In and Out Animation ===
		FadeTransition fadeIn = new FadeTransition(Duration.millis(300),
				skippedLabel);
		fadeIn.setFromValue(0);
		fadeIn.setToValue(1);

		PauseTransition stay = new PauseTransition(Duration.seconds(3.5));

		FadeTransition fadeOut = new FadeTransition(Duration.millis(400),
				skippedLabel);
		fadeOut.setFromValue(1);
		fadeOut.setToValue(0);

		// === 4. Remove label after fade out ===
		fadeOut.setOnFinished(e -> overlayPane.getChildren().remove(
				skippedLabel));

		// === 5. Temporarily dim the player grid ===
		Timeline dim = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(
				playerGrid.opacityProperty(), 1.0)), new KeyFrame(
				Duration.seconds(0.25), new KeyValue(
						playerGrid.opacityProperty(), 0.4)), // fade to dim
				new KeyFrame(Duration.seconds(4), new KeyValue(
						playerGrid.opacityProperty(), 0.4)), // stay dimmed
				new KeyFrame(Duration.seconds(4.25), new KeyValue(
						playerGrid.opacityProperty(), 1.0)) // fade back
		);
		dim.play();

		// === 6. Play label animation sequence ===
		SequentialTransition sequence = new SequentialTransition(fadeIn, stay,
				fadeOut);
		sequence.play();
	}

	// stats resources

	private int totalTurns;
	private static int totalTraps;
	private int totalDiscards;
	private int timeElapsedInSeconds;
	private Timeline timer;

	private static Colour realPlayerColour;

	private void startTimer() {

		timeElapsedInSeconds = 0;

		timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
			timeElapsedInSeconds++;
			// Optional: update UI label here if needed
			}));
		timer.setCycleCount(Timeline.INDEFINITE);
		timer.play();
	}

	private void stopTimer() {
		if (timer != null) {
			timer.stop();
		}
	}

	public static void incrementTotalTrapsIfPlayer(Colour colour) {
		if (colour == realPlayerColour)
			;
		totalTraps++;
	}

	public void visualizeTrap(Cell targetCell) {
		// Animate the cell to change and then go back
		ArrayList<Cell> track = game.getBoard().getTrack();
		int pos = -1;
		for (int i = 0; i < track.size(); i++) {
			if (targetCell == track.get(i)) {
				pos = i;
				break;
			}
		}


		// Get FXID of cell
		String fxid = "m" + pos;
		Circle targetCircle = (Circle) animationPane.lookup("#" + fxid);

		// Animate cell

		if (targetCircle == null)
			return;

		// Load the image as pattern fill
		Image image = new Image(getClass().getResource("/view/assets/neonAbyss.png")
				.toExternalForm());
		ImagePattern pattern = new ImagePattern(image);

		// Apply the pattern (you can store the old fill if you want to restore
		// later)
		Paint originalFill = targetCircle.getFill();
		targetCircle.setFill(pattern);

		// Fade in
		FadeTransition fadeIn = new FadeTransition(Duration.millis(500),
				targetCircle);
		fadeIn.setFromValue(0);
		fadeIn.setToValue(1);

		// Shake animation (translate X back and forth)
		TranslateTransition shake = new TranslateTransition(
				Duration.millis(200), targetCircle);
		shake.setByX(5);
		shake.setAutoReverse(true);
		shake.setCycleCount(6); // 3 left-right shakes

		// Fade out
		FadeTransition fadeOut = new FadeTransition(Duration.millis(400),
				targetCircle);
		fadeOut.setFromValue(1);
		fadeOut.setToValue(0);

		// Restore fill after fadeOut
		fadeOut.setOnFinished(e -> {
			targetCircle.setFill(originalFill);
			targetCircle.setOpacity(1); // restore visibility
		});

		// Play trap sound (if applicable)
		SoundManager.getInstance().playSound("trapSoundEffect");

		// Sequential + parallel transitions
		SequentialTransition sequence = new SequentialTransition(fadeIn,
				new ParallelTransition(shake), fadeOut);

		sequence.play();

		// Create the "TRAPPED" label
		Label trappedLabel = new Label("TRAPPED");
		trappedLabel.setStyle("-fx-font-size: 50px;" + "-fx-text-fill: red;"
				+ "-fx-font-weight: bold;"
				+ "-fx-effect: dropshadow(gaussian, white, 15, 0.5, 0, 0);");

		// Set initial opacity to 0 for animation
		trappedLabel.setOpacity(0);

		// Add it to the pane before binding to ensure proper layout
		pitPane.getChildren().add(trappedLabel);

		// Bind to center of the pane
		trappedLabel.layoutXProperty().bind(
				pitPane.widthProperty().subtract(trappedLabel.widthProperty())
						.divide(2));
		trappedLabel.layoutYProperty().bind(
				pitPane.heightProperty()
						.subtract(trappedLabel.heightProperty()).divide(2));

		// Create fade-in animation
		FadeTransition fadeIn2 = new FadeTransition(Duration.millis(300),
				trappedLabel);
		fadeIn2.setFromValue(0.0);
		fadeIn2.setToValue(1.0);

		// Pause in the middle
		PauseTransition stay = new PauseTransition(Duration.seconds(1.5));

		// Create fade-out animation
		FadeTransition fadeOut2 = new FadeTransition(Duration.millis(500),
				trappedLabel);
		fadeOut2.setFromValue(1.0);
		fadeOut2.setToValue(0.0);

		// Chain the animations
		SequentialTransition sequence2 = new SequentialTransition(fadeIn2, stay,
				fadeOut2);
		sequence2.setOnFinished(e -> pitPane.getChildren().remove(trappedLabel)); // Clean
																					// up
																					// after

		// Start the animation
		sequence2.play();
	}

}