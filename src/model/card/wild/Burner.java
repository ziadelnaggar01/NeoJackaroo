package model.card.wild;

import java.util.ArrayList;

import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;
import model.Colour;
import model.player.Marble;

/** Represents a burner card, a subclass of Wild. */

public class Burner extends Wild {

	/**
	 * Constructs a Burner card with the specified parameters.
	 *
	 * @param name         The name of the card.
	 * @param description  The description of the card's effect.
	 * @param boardManager The board manager interface for game interactions.
	 * @param gameManager  The game manager interface for managing the game state.
	 */
	public Burner(String name, String description, BoardManager boardManager, GameManager gameManager) {
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
	 * Validates whether the given list of marbles contains a marble that belongs to opponent player.
	 * @param marbles The list of marbles to check.
	 * @return true if the marble belongs to the opponent player, false otherwise.
	 */
	public boolean validateMarbleColours(ArrayList<Marble> marbles)
	{
		Colour activePlayerColour = gameManager.getActivePlayerColour();
		if(marbles.isEmpty()) return false;
		Marble selectedMarble = marbles.get(0);
		return !activePlayerColour.equals(selectedMarble.getColour());
	}
	
	/**
	 * Sends an opponent's marble back to its player's Home Zone.
	 * 
	 * @param marbles The list of marbles selected for this card's action.
	 * @throws InvalidMarbleException if the marbles are the wrong color or the wrong number.
	 * @throws ActionException if the action cannot be executed.
	 */
	public void act(ArrayList<Marble> marbles) throws ActionException , InvalidMarbleException
	{
		if(!validateMarbleColours(marbles))
			throw new InvalidMarbleException("You must select an opponent's marble.");
		if(!validateMarbleSize(marbles))
			throw new InvalidMarbleException("You must select exactly one marble.");
		Marble selectedMarble = marbles.get(0);
		gameManager.sendHome(selectedMarble);
	}
}
