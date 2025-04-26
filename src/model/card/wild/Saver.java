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
	 * Moves an active player's marble from the track or base to a random empty safe zone cell.
	 * 
	 * @param marbles The list of marbles selected for this card's action.
	 * @throws InvalidMarbleException if the marbles are the wrong color or the wrong number.
	 * @throws ActionException if the action cannot be executed.
	 */
	public void act(ArrayList<Marble> marbles) throws ActionException , InvalidMarbleException
	{
		Marble selectedMarble = marbles.get(0);
		boardManager.sendToSafe(selectedMarble);
	}
}
