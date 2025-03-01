package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;

/** Represents an Ace card, a subclass of Standard with a rank of 1. */

public class Ace extends Standard {
	/**
	 * Constructs an Ace card with the specified parameters.
	 *
	 * @param name         The name of the card.
	 * @param description  The description of the card's effect.
	 * @param suit         The suit of the card (HEART, DIAMOND, SPADE, CLUB).
	 * @param boardManager The board manager interface for game interactions.
	 * @param gameManager  The game manager interface for managing the game state.
	 */

	public Ace(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {

		super(name, description, 1, suit, boardManager, gameManager);
	}

}
