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
	
	 /**
     * Executes the Five card action:
     * - Moves any marble (including opponent's) 5 steps forward.
     *
     * @param marbles The list of selected marbles (should be exactly 1).
     * @throws InvalidMarbleException If more than one marble is selected.
     * @throws ActionException If the action cannot be executed due to game constraints.
     */
	@Override
	public void act(ArrayList<Marble> marbles) throws ActionException , InvalidMarbleException
	{
		if(!validateMarbleSize(marbles))
			throw new InvalidMarbleException("Five card allows selecting only one marble.");
		
		Marble selectedMarble = marbles.get(0);
		boardManager.moveBy(selectedMarble, 5, false);
	}
}
