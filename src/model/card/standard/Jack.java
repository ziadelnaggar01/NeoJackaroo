package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;

// Represents a Jack card, a subclass of Standard with a rank of 11.

public class Jack extends Standard {

	public Jack(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager)
	{
		/**
		 * Constructs a Jack card with the specified parameters.
		 *
		 * @param name          The name of the card.
		 * @param description   The description of the card's effect.
		 * @param suit          The suit of the card (HEART, DIAMOND, SPADE, CLUB).
		 * @param boardManager  The board manager interface for game interactions.
		 * @param gameManager   The game manager interface for managing the game state.
		 */
		super(name, description, 11, suit, boardManager, gameManager);
	}

}
