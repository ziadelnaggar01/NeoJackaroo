package model.card.standard;

import java.util.ArrayList;

import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;
import model.Colour;
import model.player.Marble;

/** Represents a Jack card, a subclass of Standard with a rank of 11. */

public class Jack extends Standard {

	public Jack(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
		/**
		 * Constructs a Jack card with the specified parameters.
		 *
		 * @param name         The name of the card.
		 * @param description  The description of the card's effect.
		 * @param suit         The suit of the card (HEART, DIAMOND, SPADE, CLUB).
		 * @param boardManager The board manager interface for game interactions.
		 * @param gameManager  The game manager interface for managing the game state.
		 */
		super(name, description, 11, suit, boardManager, gameManager);
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
	 * Validates the selected marbles:
	 * - If one marble is selected, it must belong to the active player.
	 * - If two marbles are selected, exactly one must belong to the active player.
	 *
	 * @param marbles The list of marbles to check.
	 * @return true if the selection follows the rules, false otherwise.
	 */
	@Override
	public boolean validateMarbleColours(ArrayList<Marble> marbles)
	{
		Colour activePlayerColour = gameManager.getActivePlayerColour();
		int numberOfMarbles = marbles.size();
		int activePlayerMarbles = 0;
		for(int i=0; i<numberOfMarbles; i++)
			if(marbles.get(i).getColour().equals(activePlayerColour)) 
				activePlayerMarbles++;
		return activePlayerMarbles==1;
	}
	
	/**
	 * Executes the Jack card action:
	 * - If one marble is selected, it moves forward by 11 steps.
	 * - If two marbles are selected, swaps their positions on the board.
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
			boardManager.moveBy(selectedMarble, 11, false);
			break;
		case 2:
			Marble marble1 = marbles.get(0);
			Marble marble2 = marbles.get(1);
			boardManager.swap(marble1, marble2);
			break;
		}
	}

}
