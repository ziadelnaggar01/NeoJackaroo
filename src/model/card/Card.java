package model.card;


import java.util.ArrayList;

import exception.*;
import model.Colour;
import model.player.Marble;
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
	
	
	/**
	 * Validates whether the given list of marbles contains the correct number of marbles that each card is supposed to act on (0, 1 or 2 based on the type of card).
	 * @param marbles
	 * @return true if number of marbles matches type of card, false otherwise.
	 */
	public boolean validateMarbleSize(ArrayList<Marble> marbles)
	{
		return marbles.size()==1;
	}

	
	/**
	 * Validates whether the given list of marbles contains the correct color of marbles that each card is supposed to act on (same color as the player or not based on the type of card).
	 * @param marbles
	 * @return true if color of marble matches card action, false otherwise.
	 */
	public boolean validateMarbleColours(ArrayList<Marble> marbles)
	{
		Colour activePlayerColour = gameManager.getActivePlayerColour();
		if(marbles.isEmpty()) return false;
		Marble selectedMarble = marbles.get(0);
		return activePlayerColour.equals(selectedMarble.getColour());
	}
	
	
	/**
	 * Performs the specified action of the card on the selected marbles.
	 * 
	 * @param marbles The list of marbles selected for this card's action.
	 * @return true if the number of marbles is correct, false otherwise.
	 * @throws InvalidMarbleException if the number is invalid.
	 */
	public abstract void act(ArrayList<Marble> marbles) throws ActionException,InvalidMarbleException;
}
