package model.card;

import engine.GameManager;
import engine.board.BoardManager;

/**
 * Represents an abstract card in the game. Each card has a name, a description,
 * and access to the game's board and manager. Specific card types should extend
 * this class and define their behavior.
 */
public abstract class Card {

	private final String name;
	private final String description;
	protected BoardManager boardManager;
	protected GameManager gameManager;

	/**
	 * Constructs a card with the given name, description, and references to the
	 * game managers.
	 *
	 * @param name         The name of the card.
	 * @param description  A brief explanation of the card's effect or purpose.
	 * @param boardManager The board manager controlling the game board.
	 * @param gameManager  The game manager overseeing the game state.
	 */
	public Card(String name, String description, BoardManager boardManager, GameManager gameManager) {
		this.name = name;
		this.description = description;
		this.boardManager = boardManager;
		this.gameManager = gameManager;
	}

	/**
	 * Retrieves the card's name.
	 *
	 * @return The name of the card.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Retrieves the card's description.
	 *
	 * @return A brief description of the card's effect or purpose.
	 */
	public String getDescription() {
		return description;
	}
}
