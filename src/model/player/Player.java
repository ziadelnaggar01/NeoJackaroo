package model.player;

import java.util.ArrayList;
import model.card.Card;
import model.Colour;

/** A class representing the Players available in the game. */
@SuppressWarnings("unused")

public class Player {

	private final String name; // A String representing the name of a player, cannot be changed once
								// initialized
	private final Colour colour; // An object of type Colour acting as a reference to associate a player to their
									// SafeZone and Marbles, cannot be changed once initialized
	private ArrayList<Card> hand; // An arraylist representing the hand of cards each player has.
	private final ArrayList<Marble> marbles; // An arraylist representing the marbles each player has in their HomeZone,
												// cannot be changed once initialized
	private Card selectedCard; // A Card object representing the card to be played.
	private final ArrayList<Marble> selectedMarbles; // An arraylist representing the marbles to be played, cannot be
														// changed once initialized

	public Player(String name, Colour colour) {
		// Player object constructor

		/**
		 * Constructor that initializes a Player object with the player name and colour
		 * as attributes, and does the following: 
		 * 1. Initialize the hand, selectedMarbles, and marbles to empty ArrayLists 
		 * 2. Create 4 marbles with the same colour as the player and add them to the marbles. 
		 * 3. Initialize the selectedCard to null (Default Value)
		 */
		this.name = name;
		this.colour = colour;
		this.hand = new ArrayList<>();
		this.marbles = new ArrayList<>();
		this.selectedMarbles = new ArrayList<>();
		
		// Create 4 marbles with the same colour as the player
		for(int i=0; i<4; i++)
			marbles.add(new Marble(colour));
		
		this.selectedCard = null;

	}

	public String getName() {
		/**
		 * Retrieves the player's name.
		 *
		 * @return The player's name as a String.
		 */
		return name;
	}

	public Colour getColour() {
		/**
		 * Retrieves the player's assigned colour.
		 *
		 * @return The player's Colour.
		 */
		return colour;
	}

	public ArrayList<Card> getHand() {
		/**
		 * Retrieves the player's hand of cards.
		 *
		 * @return An ArrayList of Card objects representing the player's hand.
		 */
		return hand;
	}

	public void setHand(ArrayList<Card> newHand) {
		/**
		 * Updates the player's hand with a new set of cards.
		 *
		 * @param newHand An arraylist of Cards representing the new hand.
		 */
		hand.clear();
		hand.addAll(newHand);
	}

	public ArrayList<Marble> getMarbles() {
		/**
		 * Retrieves the marbles in the player's Home Zone.
		 *
		 * @return An array of Marbles representing the player's marbles.
		 */
		return marbles;
	}

	public Card getSelectedCard() {
		/**
		 * Retrieves the selected card for the player's turn.
		 *
		 * @return The selected Card object.
		 */
		return selectedCard;
	}
}
