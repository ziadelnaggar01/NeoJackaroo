package model.card.standard;

import java.util.ArrayList;

import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;
import model.Colour;
import model.card.Card;
import model.player.Marble;

/**
 * A class representing the standard deck cards available in the game. This
 * serves as the parent class for other specific card types.
 */
public class Standard extends Card {

	private final int rank; // The rank of the card (e.g., 2-10, face cards).
	private final Suit suit; // The suit of the card (e.g., Hearts, Diamonds, Clubs, Spades).

	/**
	 * Constructs a standard card with the given attributes.
	 *
	 * @param name         The name of the card.
	 * @param description  A brief description of the card's function.
	 * @param rank         The numerical rank of the card.
	 * @param suit         The suit of the card.
	 * @param boardManager The board manager instance.
	 * @param gameManager  The game manager instance.
	 */
	public Standard(String name, String description, int rank, Suit suit, BoardManager boardManager, GameManager gameManager) {
		super(name, description, boardManager, gameManager);
		this.rank = rank;
		this.suit = suit;
	}

	/**
	 * Gets the rank of the card.
	 *
	 * @return The rank of the card.
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * Gets the suit of the card.
	 *
	 * @return The suit of the card.
	 */
	public Suit getSuit() {
		return suit;
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
		Marble selectedMarble = marbles.getFirst();
		return activePlayerColour.equals(selectedMarble.getColour());
	}
	
	/**
	 * Moves one of active player marbles a number of forwards steps based on the card rank.
	 * 
	 * @param marbles The list of marbles selected for this card's action.
	 * @throws InvalidMarbleException if the marbles are the wrong color or the wrong number.
	 * @throws ActionException if the action cannot be executed.
	 */
	public void act(ArrayList<Marble> marbles) throws ActionException , InvalidMarbleException
	{
		if(!validateMarbleColours(marbles))
			throw new InvalidMarbleException("Invalid marbles colour");
		if(!validateMarbleSize(marbles))
			throw new InvalidMarbleException("Invalid number of marbles");
		Marble selectedMarble = marbles.getFirst();
		boardManager.moveBy(selectedMarble, rank, false);
	}
}
	