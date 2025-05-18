package controller;

import engine.Game;
import model.card.Card;
import model.card.standard.Standard;
import model.card.standard.Suit;
import model.card.wild.Burner;
import model.card.wild.Saver;
import model.card.wild.Wild;
import model.player.Player;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;

public final class GenericController {

	// Private constructor to prevent instantiation
	private GenericController() {
	}

	// general-purpose version of switch scene, not tied to a mouse or keyboard
	// event
	public static void switchScene(Stage stage, Parent root) {
		Scene currentScene = stage.getScene();
		if (currentScene != null)
			currentScene.setRoot(root);
		else
			stage.setScene(new Scene(root));
	}

	// keep this for MouseEvent-based transitions
	public static void switchScene(MouseEvent event, Parent root) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		switchScene(stage, root); // delegate to the new one
	}

	// keep this for KeyEvent-based transitions
	public static void switchScene(KeyEvent event, Parent root) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		switchScene(stage, root);
	}

	public static void buttonGlowON(ImageView image, Color color) {
		DropShadow shadow = new DropShadow();
		shadow.setColor(color);
		shadow.setRadius(20);
		image.setEffect(shadow);
	}

	public static void buttonGlowON(ImageView image, Color color, int radius) {
		DropShadow shadow = new DropShadow();
		shadow.setColor(color);
		shadow.setRadius(radius);
		image.setEffect(shadow);
	}

	public static void buttonGlowOFF(ImageView image) {
		image.setEffect(null);
	}

	public static int getCardRank(ImageView image, int currentPlayerIndex,
			Game game) {

		Player Cur_Player = game.getPlayers().get(currentPlayerIndex);
		Card card;
		String selectedCardID = image.getId();
		switch (selectedCardID.charAt(selectedCardID.length() - 1)) {
		case '1':
			card = Cur_Player.getHand().get(0);
		case '2':
			card = Cur_Player.getHand().get(1);
		case '3':
			card = Cur_Player.getHand().get(2);
		case '4':
			card = Cur_Player.getHand().get(3);
		default:
			card = null;
		}
		// card
		if (card instanceof Standard) {
			Standard card1 = (Standard) card;
			int rank = card1.getRank();
			return rank;

		} else if (card instanceof Wild) {
			if (card instanceof Burner) {
				Burner card1 = (Burner) card;
				int rank = 14;
				return rank;
			} else if (card instanceof Saver) {
				Saver card1 = (Saver) card;
				int rank = 15;
				return rank;
			}
		}
		return -1;
	}
}
