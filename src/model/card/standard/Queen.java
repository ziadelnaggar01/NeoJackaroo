package model.card.standard;

import java.util.ArrayList;

import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;
import model.Colour;
import model.player.Marble;

/** Represents a Queen card, a subclass of Standard with a rank of 12. */

public class Queen extends Standard {
	/**
	 * Constructs a Queen card with the specified parameters.
	 *
	 * @param name         The name of the card.
	 * @param description  The description of the card's effect.
	 * @param suit         The suit of the card (HEART, DIAMOND, SPADE, CLUB).
	 * @param boardManager The board manager interface for game interactions.
	 * @param gameManager  The game manager interface for managing the game state.
	 */

	public Queen(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
		super(name, description, 12, suit, boardManager, gameManager);
	}
	
	/**
     * Validates whether the given list of marbles contains at most one marble.
     * 
     * @param marbleList The list of marbles to validate.
     * @return true if the list contains at most one marble, false otherwise.
     */
	@Override
	public boolean validateMarbleSize(ArrayList<Marble> marbles)
	{
		return marbles.size()<=1;
	}
	
	/**
	 * Executes the Queen card action:
	 * - If no marble is selected, discards a random card from the hand of a random player (excluding the active player).
	 * - If one marble is selected, it moves forward by 12 steps.
	 *
	 * @param marbles The list of selected marbles (should be 0 or 1).
	 * @throws InvalidMarbleException If more than one marble is selected or if the marble does not belong to the active player.
	 * @throws ActionException If the action cannot be executed due to game constraints.
	 */
	@Override
	public void act(ArrayList<Marble> marbles) throws ActionException , InvalidMarbleException
	{
		if(!validateMarbleSize(marbles))
			throw new InvalidMarbleException("Queen card allows selecting at most one marble.");
		if(!validateMarbleColours(marbles))
			throw new InvalidMarbleException("Selected marble does not belong to the active player.");
		
		if(marbles.isEmpty())
			gameManager.discardCard();
		else
		{
		Marble selectedMarble = marbles.get(0);
		boardManager.moveBy(selectedMarble, 12, false);
		}
	}
}
