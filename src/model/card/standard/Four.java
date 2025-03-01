package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;

/** Represents a Four card, a subclass of Standard with a rank of 4. */

public class Four extends Standard {

	/**
	 * Constructs a Four card with the specified parameters.
	 *
	 * @param name         The name of the card.
	 * @param description  The description of the card's effect.
	 * @param suit         The suit of the card (HEART, DIAMOND, SPADE, CLUB).
	 * @param boardManager The board manager interface for game interactions.
	 * @param gameManager  The game manager interface for managing the game state.
	 */

	public Four(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
		super(name, description, 4, suit, boardManager, gameManager);
	}
}