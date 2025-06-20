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

	@SuppressWarnings("resource")
	public static void loadCardPool(BoardManager boardManager, GameManager gameManager) throws IOException {
		cardsPool = new ArrayList<>();

		// Get resource as stream using class loader
		InputStream inputStream = Deck.class.getClassLoader().getResourceAsStream(CARDS_FILE);
		if (inputStream == null) {
			throw new FileNotFoundException("Resource not found: " + CARDS_FILE);
		}

		// Use try-with-resources for automatic closing
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			String nextLine;
			// Use standard null-check reading pattern
			while ((nextLine = br.readLine()) != null) {
				String[] data = nextLine.split(",");

				if (data.length == 0)
					throw new IOException(nextLine);

				String name = data[2];
				String description = data[3];

				int code = Integer.parseInt(data[0]);
				int frequency = Integer.parseInt(data[1]);

				for (int i = 0; i < frequency; i++) {
					Card card;

					if (code > 13)
						switch (code) {
						case 14:
							card = new Burner(name, description, boardManager, gameManager);
							break;
						case 15:
							card = new Saver(name, description, boardManager, gameManager);
							break;
						default:
							throw new IOException(nextLine);
						}

					else {
						int rank = Integer.parseInt(data[4]);
						Suit cardSuit = Suit.valueOf(data[5]);
						switch (code) {
						case 0:
							card = new Standard(name, description, rank, cardSuit, boardManager, gameManager);
							break;
						case 1:
							card = new Ace(name, description, cardSuit, boardManager, gameManager);
							break;
						case 4:
							card = new Four(name, description, cardSuit, boardManager, gameManager);
							break;
						case 5:
							card = new Five(name, description, cardSuit, boardManager, gameManager);
							break;
						case 7:
							card = new Seven(name, description, cardSuit, boardManager, gameManager);
							break;
						case 10:
							card = new Ten(name, description, cardSuit, boardManager, gameManager);
							break;
						case 11:
							card = new Jack(name, description, cardSuit, boardManager, gameManager);
							break;
						case 12:
							card = new Queen(name, description, cardSuit, boardManager, gameManager);
							break;
						case 13:
							card = new King(name, description, cardSuit, boardManager, gameManager);
							break;
						default:
							throw new IOException(nextLine);
						}
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
		ArrayList<Card> cards = new ArrayList<>(cardsPool.subList(0, 4));
		cardsPool.subList(0, 4).clear();
		return cards;
	}

	/*
	 *  Adds a collection of cards to the existing card pool. 
	 *  This static method receives an ArrayList of Card objects and appends them all to the 
	 *  cardsPool. Used to refill the cardspool with the cards in the firepit once empty.
	 * */
	public static void refillPool(ArrayList<Card> cards)
	{
		cardsPool.addAll(cards);
	}

	public static int getPoolSize()
	{
		return cardsPool.size();
	}


}