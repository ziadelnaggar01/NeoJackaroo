package model.card.wild;

import java.util.ArrayList;

import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;
import model.Colour;
import model.player.Marble;

/** Represents a Saver card, a subclass of Wild. */

public class Saver extends Wild {

	/**
	 * Constructs a Saver card with the specified parameters.
	 *
	 * @param name         The name of the card.
	 * @param description  The description of the card's effect.
	 * @param boardManager The board manager interface for game interactions.
	 * @param gameManager  The game manager interface for managing the game state.
	 */
	public Saver(String name, String description, BoardManager boardManager, GameManager gameManager) {
		super(name, description, boardManager, gameManager);
	}
	
	/**
	 * Validates whether the given list of marbles contains only one marble.
	 * @param marbles
	 * @return true if number of marbles is one, false otherwise.
	 */
	public boolean validateMarbleSize(ArrayList<Marble> marbles)
	{
		return marbles.size()==1;
	}

	
	/**
	 * Validates whether the given list of marbles contains a marble that belongs to the active player.
	 * @param marbles The list of marbles to check.
	 * @return true if the marble belongs to the active player, false otherwise.
	 */
	public boolean validateMarbleColours(ArrayList<Marble> marbles)
	{
		Colour activePlayerColour = gameManager.getActivePlayerColour();
		if(marbles.isEmpty()) return false;
		Marble selectedMarble = marbles.get(0);
		return activePlayerColour.equals(selectedMarble.getColour());
	}
	
	/**
	 * Moves an active player's marble from the track or base to a random empty safe zone cell.
	 * 
	 * @param marbles The list of marbles selected for this card's action.
	 * @throws InvalidMarbleException if the marbles are the wrong color or the wrong number.
	 * @throws ActionException if the action cannot be executed.
	 */
	public void act(ArrayList<Marble> marbles) throws ActionException , InvalidMarbleException
	{
		if(!validateMarbleColours(marbles))
			throw new InvalidMarbleException("You can only select your own marbles.");
		if(!validateMarbleSize(marbles))
			throw new InvalidMarbleException("You must select exactly one marble.");
		Marble selectedMarble = marbles.get(0);
		boardManager.sendToSafe(selectedMarble);
	}
}
