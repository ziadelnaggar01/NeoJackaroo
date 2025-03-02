package model.card;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

import engine.GameManager;
import engine.board.BoardManager;
import model.card.standard.*;
import model.card.wild.*;

/**
 * Represents the deck of cards used in the game. Handles loading cards from an
 * external file and drawing cards for players.
 */
public class Deck {
	/**
	 * The file path where the card data is stored.
	 */
	private final static String CARDS_FILE = "Cards.csv";

	/**
	 * Stores the available cards in the deck.
	 */
	private static ArrayList<Card> cardsPool = new ArrayList<>();

	/**
	 * Loads the card pool from a CSV file, creating instances of different types of
	 * cards. Each card's attributes are read from the file and instantiated
	 * accordingly.
	 *
	 * @param boardManager The board manager to associate with each card.
	 * @param gameManager  The game manager to associate with each card.
	 * @throws IOException If an error occurs while reading the file.
	 */
	public static void loadCardPool(BoardManager boardManager, GameManager gameManager) throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(CARDS_FILE))) {
			String line = "";
			while ((line = br.readLine()) != null) {
				String[] data = line.split(",");
				int code = Integer.parseInt(data[0]);
				int frequency = Integer.parseInt(data[1]);
				String name = data[2];
				String description = data[3];
				System.out.println(name + " " + description + " ");
				// Determine the type of the card
				for (int i = 0; i < frequency; i++) {
					Card card;
					if (code == 14) {
						card = new Burner(name, description, boardManager, gameManager);
					} else if (code == 15) {
						card = new Saver(name, description, boardManager, gameManager);
					} else if (code == 13) { // King
						Suit suit = Suit.valueOf(data[5]);
						card = new King(name, description, suit, boardManager, gameManager);
					} else if (code == 12) {
						Suit suit = Suit.valueOf(data[5]);
						card = new Queen(name, description, suit, boardManager, gameManager);
					} else if (code == 11) {
						Suit suit = Suit.valueOf(data[5]);
						card = new Jack(name, description, suit, boardManager, gameManager);
					} else if (code == 10) {
						card = new Ten(name, description, Suit.valueOf(data[5]), boardManager, gameManager);
					} else if (code == 7) {
						card = new Seven(name, description, Suit.valueOf(data[5]), boardManager, gameManager);
					} else if (code == 5) {
						card = new Five(name, description, Suit.valueOf(data[5]), boardManager, gameManager);
					} else if (code == 4) {
						card = new Four(name, description, Suit.valueOf(data[5]), boardManager, gameManager);
					} else if (code == 1) {
						card = new Ace(name, description, Suit.valueOf(data[5]), boardManager, gameManager);
					} else { // Standard card
						card = new Standard(name, description, Integer.parseInt(data[4]), Suit.valueOf(data[5]), boardManager, gameManager);
					}
					cardsPool.add(card);
				}
			}
		}
	}

	/**
	 * Draws a set of four cards randomly from the deck. The drawn cards are removed
	 * from the pool to prevent reuse.
	 *
	 * @return An ArrayList containing four drawn cards.
	 */
	public static ArrayList<Card> drawCards() {
		Collections.shuffle(cardsPool);
		ArrayList<Card> drawnCards = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			if (!cardsPool.isEmpty()) {
				drawnCards.add(cardsPool.remove(0));
			}
		}
		return drawnCards;
	}
}