package model.card.standard;

import java.util.ArrayList;

import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;
import model.Colour;
import model.player.Marble;

/** Represents a Ten card, a subclass of Standard with a rank of 10. */

public class Ten extends Standard {

	/**
	 * Constructs a Ten card with the specified parameters.
	 *
	 * @param name         The name of the card.
	 * @param description  The description of the card's effect.
	 * @param suit         The suit of the card (HEART, DIAMOND, SPADE, CLUB).
	 * @param boardManager The board manager interface for game interactions.
	 * @param gameManager  The game manager interface for managing the game state.
	 */

	public Ten(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
		super(name, description, 10, suit, boardManager, gameManager);// initialize using super constructer, with rank																// as 10
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
	 * Executes the Ten card action:
	 * - If no marble is selected, forces the next player to discard a card. (Also skips their turn if required).
	 * - If one marble is selected, moves it forward by 10 steps.
	 *
	 * @param marbles The list of selected marbles (should be 0 or 1).
	 * @throws InvalidMarbleException If more than one marble is selected or if the marble belongs to another player.
	 * @throws ActionException If the action cannot be executed due to game constraints.
	 */
	@Override
	public void act(ArrayList<Marble> marbles) throws ActionException , InvalidMarbleException
	{
		if(marbles.isEmpty())
		{
			Colour nextPlayerColour = gameManager.getNextPlayerColour();
			gameManager.discardCard(nextPlayerColour);
		}
		else
		{
		Marble selectedMarble = marbles.get(0);
		boardManager.moveBy(selectedMarble, 10, false);
		}
	}
}
