package model.card.standard;

import java.util.ArrayList;

import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;
import model.player.Marble;

/** Represents a Four card, a subclass of Standard with a rank of 4. */

public class Four extends Standard {

	/**
	 * Constructs a Four card with the specified parameters.
	 *
	 * @param name         The name of the card.
	 * @param description  The description of the card's effect.
	 * @param suit         The suit of the card (HEART, DIAMOND, SPADE, CLUB).
	 * @param boardManager The board manager interface for game interactions.
	 * @param gameManager  The game manager interface for managing the game state.
	 */

	public Four(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
		super(name, description, 4, suit, boardManager, gameManager);
	}
	
	 /**
     * Executes the Four card action:
     * - Moves one of the active player's marbles 4 steps backward.
     *
     * @param marbles The list of selected marbles (should be exactly 1).
     * @throws InvalidMarbleException If more than one marble is selected or if the marble belongs to another player.
     * @throws ActionException If the action cannot be executed due to game constraints.
     */
	@Override
	public void act(ArrayList<Marble> marbles) throws ActionException , InvalidMarbleException
	{
		Marble selectedMarble = marbles.get(0);
		boardManager.moveBy(selectedMarble, -4, false);
	}
}