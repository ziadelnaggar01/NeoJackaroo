package model.player;

import model.card.Card;
import model.Colour;

// A class representing the Players available in the game.

public class Player {
	private final String name; // A String representing the name of a player, cannot be changed once initialized
	private final Colour colour; //  An object of type Colour acting as a reference to associate a player to their SafeZone and Marbles, cannot be changed once initialized
	private Card[] hand; // An arraylist representing the hand of cards each player has. 
	private final Marble[] marbles; // An arraylist representing the marbles each player has in their HomeZone, cannot be changed once initialized
	private Card selectedCard; // A Card object representing the card to be played.
	private final Marble[] selectedMarbles; // An arraylist representing the marbles to be played, cannot be changed once initialized

	Player(String name, Colour colour)
	{
		//Player object constructor, with default access, no need to access it outside package.

		/*
		 * Constructor that initializes a Player object with the player name and colour as attributes, 
		 * and does the following:
			 1. Initialize the hand, selectedMarbles, and marbles to empty ArrayLists
			 2. Create 4 marbles with the same colour as the player and add them to the marbles.
			 3. Initialize the selectedCard to null (Default Value)
		 */
		this.name = name;
		this.colour = colour;
		hand = new Card[4];
		marbles = new Marble[4];
		selectedMarbles = new Marble[4];
		selectedCard = null;
	}

	public String getName()
	{
		/**
		 * Retrieves the player's name.
		 *
		 * @return The player's name as a String.
		 */
		return name;
	}

	public Colour getColour()
	{
		/**
		 * Retrieves the player's assigned colour.
		 *
		 * @return The player's Colour.
		 */
		return colour;
	}

	public Card[] getHand()
	{
		/**
		 * Retrieves the player's hand of cards.
		 *
		 * @return An array of Cards representing the player's hand.
		 */
		return hand;
	}

	public void setHand(Card[] newHand)
	{
		/**
		 * Updates the player's hand with a new set of cards.
		 *
		 * @param newHand An array of Cards representing the new hand.
		 */
		for(int i=0; i<newHand.length;i++)
			hand[i] = newHand[i];
	}

	public Marble[] getMarbles()
	{
		/**
		 * Retrieves the marbles in the player's Home Zone.
		 *
		 * @return An array of Marbles representing the player's marbles.
		 */
		return marbles;
	}


	public Card getSelectedCard()
	{
		/**
		 * Retrieves the selected card for the player's turn.
		 *
		 * @return The selected Card object.
		 */
		return selectedCard;
	}
}
