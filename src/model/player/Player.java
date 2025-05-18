package model.player;

import java.util.ArrayList;

import exception.*;
import model.card.Card;
import model.Colour;

/** A class representing the Players available in the game. */

public class Player {

	private final String name; // A String representing the name of a player, cannot be changed once initialized
	private final Colour colour; // An object of type Colour acting as a reference to associate a player to their SafeZone and Marbles, cannot be changed once initialized
	private ArrayList<Card> hand; // An arraylist representing the hand of cards each player has.
	private final ArrayList<Marble> marbles; // An arraylist representing the marbles each player has in their HomeZone, cannot be changed once initialized
	private Card selectedCard; // A Card object representing the card to be played.
	private final ArrayList<Marble> selectedMarbles; // An arraylist representing the marbles to be played, cannot be changed once initialized

	
	/**
	 * Player object constructor
	 * 
	 * Constructor that initializes a Player object with the player name and colour
	 * as attributes, and does the following: 
	 * 1. Initialize the hand, selectedMarbles, and marbles to empty ArrayLists 
	 * 2. Create 4 marbles with the same colour as the player and add them to the marbles. 
	 * 3. Initialize the selectedCard to null (Default Value)
	 */
	public Player(String name, Colour colour) {
		this.name = name;
		this.colour = colour;
		this.hand = new ArrayList<>();
		this.marbles = new ArrayList<>();
		this.selectedMarbles = new ArrayList<>();
		
		// Create 4 marbles with the same colour as the player
		for(int i=0; i<4; i++) // 0
			marbles.add(new Marble(colour));
		
		this.selectedCard = null;
	}
	
	/**
	 * Retrieves the player's name.
	 *
	 * @return The player's name as a String.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Retrieves the player's assigned colour.
	 *
	 * @return The player's Colour.
	 */
	public Colour getColour() {
		return colour;
	}
	
	/**
	 * Retrieves the player's hand of cards.
	 *
	 * @return An ArrayList of Card objects representing the player's hand.
	 */
	public ArrayList<Card> getHand() {
		return hand;
	}

	/**
	 * Updates the player's hand with a new set of cards.
	 *
	 * @param newHand An arraylist of Cards representing the new hand.
	 */
	public void setHand(ArrayList<Card> newHand) {
		hand.clear();
		hand.addAll(newHand);
	}

	/**
	 * Retrieves the marbles in the player's Home Zone.
	 *
	 * @return An array of Marbles representing the player's marbles.
	 */
	public ArrayList<Marble> getMarbles() {
		return marbles;
	}

	/**
	 * Retrieves the selected card for the player's turn.
	 *
	 * @return The selected Card object.
	 */
	public Card getSelectedCard() {
		return selectedCard;
	}
	
	/**
	 * Adds a specified marble to the player’s collection of marbles which acts as the player’s Home Zone.
	 *
	 * @param marble The marble to be added to Player's Home Zone.
	 */
	public void regainMarble(Marble marble)
	{
		marbles.add(marble);
	}
	
	/**
	 * Returns the first marble without removing it, if any from Home Zone.
	 *
	 * @return First Marble object in marbles ArrayList, and null if list is empty.
	 */
	public Marble getOneMarble()
	{
		if(marbles.isEmpty())
			return null;
		return marbles.get(0);
	}
	
	/**
	 * Checks if the given card is available in the player’s hand and sets it to the selectedCard.
	 *
	 * @param card Object to be checked in player's hand.
	 * @throws InvalidCardException if the card does not belong to the current player’s hand.
	 */
	public void selectCard(Card card) throws InvalidCardException
	{
		if (hand.contains(card))
		    selectedCard = card;
		else
		    throw new InvalidCardException("That card doesn’t exist, try pulling from this universe.");
	}
	
	/**
	 * Selects a marble to be used in the game by adding it to the selectedMarbles.
	 *
	 * @param marble to be added to selectedMarbles ArrayList.
	 * @throws InvalidMarbleException if player is trying to select more than two marbles.
	 */
	public void selectMarble(Marble marble) throws InvalidMarbleException
	{
		if (marble == null)
			throw new InvalidMarbleException("You just selected… nothing. That’s not very helpful.");
		
		// If marble is already selected, no change
        if (selectedMarbles.contains(marble)) return;
        
		if(selectedMarbles.size()>=2)
			throw new InvalidMarbleException("Slow down! You only get to pick two marbles.");
		selectedMarbles.add(marble);
	}
	
	/**
	 * Clears all selections, including the selected card and any selected marbles, resetting the player’s choices.
	 */
	public void deselectAll()
	{
		this.selectedCard = null;
		this.selectedMarbles.clear();
	}
	
    /**
     * Executes the selected card's action with the chosen marbles.
     * Ensures constraints on card selection and marble selection before execution.
     *
     * @throws GameException If any validation fails.
     */
	public void play() throws GameException
	{
		// Check there is a selected card
		if(selectedCard==null)
			throw new InvalidCardException("Did you forget something? Pick a card before proceeding.");
		// Check number of marbles selected is correct
		if(!selectedCard.validateMarbleSize(selectedMarbles))
			throw new InvalidMarbleException("Math is important, wrong number of marbles selected.");
		// Check color of marbles selected
		if(!selectedCard.validateMarbleColours(selectedMarbles))
			throw new InvalidMarbleException("Those marbles don’t match, maybe check your Trichromacy level?");
		// Perform card action on marbles
		selectedCard.act(selectedMarbles);
	}
	
}
