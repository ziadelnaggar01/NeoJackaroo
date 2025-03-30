package model.card.standard;

import java.util.ArrayList;

import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;
import model.Colour;
import model.player.Marble;

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
     * Validates whether the given list of marbles is empty or contains a marble that belongs to the active player.
     * 
     * @param marbleList The list of marbles to check.
     * @return true if the marble belongs to the active player or if the list is empty, false otherwise.
     */
	@Override
	public boolean validateMarbleColours(ArrayList<Marble> marbles)
	{
		Colour activePlayerColour = gameManager.getActivePlayerColour();
		if(marbles.isEmpty()) return true;
		Marble selectedMarble = marbles.get(0);
		return activePlayerColour.equals(selectedMarble.getColour());
	}
	
	/**
	 * Executes the Ace card action:
	 * - If no marble is selected, the active player fields a new marble from home.
	 * - If one marble is selected, it moves forward by 1 step.
	 *
	 * @param marbles The list of selected marbles (should be 0 or 1).
	 * @throws InvalidMarbleException If more than one marble is selected or if the marble belongs to another player.
	 * @throws ActionException If the action cannot be executed due to game constraints.
	 */
	@Override
	public void act(ArrayList<Marble> marbles) throws ActionException , InvalidMarbleException
	{
		if(!validateMarbleSize(marbles))
			throw new InvalidMarbleException("Ace card allows selecting at most one marble.");
		if(!validateMarbleColours(marbles))
			throw new InvalidMarbleException("Selected marble does not belong to the active player.");
		
		if(marbles.isEmpty())
			gameManager.fieldMarble();
		else
		{
		Marble selectedMarble = marbles.get(0);
		boardManager.moveBy(selectedMarble, 1, false);
		}
	}

}
