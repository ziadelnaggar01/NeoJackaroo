package model.card.standard;

import java.util.ArrayList;

import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;
import model.Colour;
import model.player.Marble;

/** Represents a Five card, a subclass of Standard with a rank of 5. */

public class Five extends Standard {
	/**
	 * Constructs a Five card with the specified parameters.
	 *
	 * @param name         The name of the card.
	 * @param description  The description of the card's effect.
	 * @param suit         The suit of the card (HEART, DIAMOND, SPADE, CLUB).
	 * @param boardManager The board manager interface for game interactions.
	 * @param gameManager  The game manager interface for managing the game state.
	 */

	public Five(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
		super(name, description, 5, suit, boardManager, gameManager);
	}
	
	/**
	 * Validates whether the given list of marbles contains the correct color of marbles that each card is supposed to act on (same color as the player or not based on the type of card).
	 * @param marbles
	 * @return true if color of marble matches card action, false otherwise.
	 */
	public boolean validateMarbleColours(ArrayList<Marble> marbles)
	{
		return true;
	}
}
