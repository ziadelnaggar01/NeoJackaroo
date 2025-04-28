package model.card.standard;

import java.util.ArrayList;

import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;
import model.player.Marble;

/** Represents a King card, a subclass of Standard with a rank of 13. */

public class King extends Standard {

	/**
	 * Constructs a King card with the specified parameters.
	 *
	 * @param name         The name of the card.
	 * @param description  The description of the card's effect.
	 * @param suit         The suit of the card (HEART, DIAMOND, SPADE, CLUB).
	 * @param boardManager The board manager interface for game interactions.
	 * @param gameManager  The game manager interface for managing the game state.
	 */

	public King(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
		super(name, description, 13, suit, boardManager, gameManager);// initialize using super constructer, with rank
																		// as 13
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
	 * Executes the King card action:
	 * - If no marble is selected, the active player fields a new marble from home.
	 * - If one marble is selected, it moves forward by 13 steps.
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
			gameManager.fieldMarble();
			return;
		}
		
		Marble selectedMarble = marbles.get(0);
		boardManager.moveBy(selectedMarble, 13, true);
	}

}
