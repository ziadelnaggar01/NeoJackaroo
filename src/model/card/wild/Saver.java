package model.card.wild;

import engine.GameManager;
import engine.board.BoardManager;

/** Represents a Saver card, a subclass of Wild. */

public class Saver extends Wild {

	/**
	 * Constructs a Saver card with the specified parameters.
	 *
	 * @param name         The name of the card.
	 * @param description  The description of the card's effect.
	 * @param boardManager The board manager interface for game interactions.
	 * @param gameManager  The game manager interface for managing the game state.
	 */
	public Saver(String name, String description, BoardManager boardManager, GameManager gameManager) {
		super(name, description, boardManager, gameManager);
	}
}
