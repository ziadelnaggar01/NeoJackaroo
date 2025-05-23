package model.card.standard;

import java.util.ArrayList;

import engine.GameManager;

import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;
import model.player.Marble;

/** Represents a Seven card, a subclass of Standard with a rank of 7. */

public class Seven extends Standard {

	/**
	 * Constructs a Seven card with the specified parameters.
	 *
	 * @param name         The name of the card.
	 * @param description  The description of the card's effect.
	 * @param suit         The suit of the card (HEART, DIAMOND, SPADE, CLUB).
	 * @param boardManager The board manager interface for game interactions.
	 * @param gameManager  The game manager interface for managing the game state.
	 */

	public Seven(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
		super(name, description, 7, suit, boardManager, gameManager);
	}
	
	/**
     * Validates whether the given list of marbles contains 1 or 2 marbles.
     * 
     * @param marbleList The list of marbles to validate.
     * @return true if the list contains one or two marbles, false otherwise.
     */
	@Override
	public boolean validateMarbleSize(ArrayList<Marble> marbles)
	{
		int numberOfMarbles = marbles.size();
		return numberOfMarbles==1 || numberOfMarbles==2;
	}
	
	/**
	 * Executes the Seven card action:
	 * - If one marble is selected, it moves forward by 7 steps.
	 * - If two marbles are selected, moves both marbles a combined total of 7 steps.
	 *   The first marble moves `getSplitDistance()`, and the second moves `7 - getSplitDistance()`.
	 *
	 * @param  marbles The list of selected marbles (should be 1 or 2)
	 * @throws InvalidMarbleException If more than one marble is selected or if the marble does not belong to the active player.
	 * @throws ActionException If the action cannot be executed due to game constraints.
	 */
	@Override
	public void act(ArrayList<Marble> marbles) throws ActionException , InvalidMarbleException
	{
		int numberOfMarbles = marbles.size();
		switch(numberOfMarbles)
		{
		case 1:
			Marble selectedMarble = marbles.get(0);
			boardManager.moveBy(selectedMarble, 7, false);
			break;
		case 2:
			Marble marble1 = marbles.get(0);
			Marble marble2 = marbles.get(1);
			boardManager.moveBy(marble1, boardManager.getSplitDistance(), false);
			boardManager.moveBy(marble2, 7-boardManager.getSplitDistance(), false);
			break;
		}
	}
}
