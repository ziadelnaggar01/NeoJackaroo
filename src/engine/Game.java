package engine;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import engine.board.Board;
import exception.*;
import model.*;
import model.card.Card;
import model.card.Deck;
import model.player.CPU;
import model.player.Marble;
import model.player.Player;

/**
 * Represents the engine of the game, where it allows the game to start and
 * initializes all core elements of the game
 */

public class Game implements GameManager {

	/**
	 * The current game board instance.
	 */
	private final Board board;
	/**
	 * List of players in the game, including the human player and CPUs.
	 */
	private final ArrayList<Player> players;
	/**
	 * The pile of cards in the center of the board where players discard cards.
	 */
	private final ArrayList<Card> firePit;
	/**
	 * Index representing the current player's turn in the game.
	 */
	private int currentPlayerIndex;
	/**
	 * The current turn number, indicating how many turns have passed in the game.
	 */
	private int turn;

	public Board getBoard() {
		return board;
	}

	public ArrayList<Card> getFirePit() {
		return firePit;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	/**
	 * Initializes a new game, setting up the board, shuffling colors, and dealing
	 * initial hands to players.
	 *
	 * @param playerName The name entered by the human player.
	 * @throws IOException If there is an issue loading the deck or initializing the
	 *                     game components.
	 */
	public Game(String playerName) throws IOException {

		// initialize core game components

        turn = 0;
        currentPlayerIndex = 0;
        firePit = new ArrayList<>();

        ArrayList<Colour> colourOrder = new ArrayList<>();
        
        colourOrder.addAll(Arrays.asList(Colour.values()));
        
        Collections.shuffle(colourOrder);
        
        this.board = new Board(colourOrder, this);
        
        Deck.loadCardPool(this.board, (GameManager)this);
        
		// add the players

        this.players = new ArrayList<>();
        this.players.add(new Player(playerName, colourOrder.get(0)));
        for (int i = 1; i < 4; i++) 
            this.players.add(new CPU("CPU " + i, colourOrder.get(i), this.board));
        
		// initialize hand of each player

        for (int i = 0; i < 4; i++) 
            this.players.get(i).setHand(Deck.drawCards());
        
    }
     
	
	/**
	 * Allows the current player to select a card from their hand.
	 * 
	 * @param card The card to be selected.
	 * @throws InvalidCardException If the card is not in the player's hand.
	 */
	public void selectCard(Card card) throws InvalidCardException
	{
		players.get(currentPlayerIndex).selectCard(card);
	}
	
	/**
	 * Allows the current player to select the given marble.
	 * Ensures that the player does not select more than the allowed number of marbles.
	 *
	 * @param marble The marble to be selected.
	 * @throws InvalidMarbleException if the player tries to select an invalid marble
	 *         or exceeds the allowed number of selected marbles.
	 */
	public void selectMarble(Marble marble) throws InvalidMarbleException
	{
		players.get(currentPlayerIndex).selectMarble(marble);
	}
	/**
	 * Clears all selections made by the current player.
	 * This method resets the selected card and any selected marbles,
	 * ensuring a fresh selection for the player's turn.
	 */
	public void deselectAll()
	{
		players.get(currentPlayerIndex).deselectAll();
	}
	/**
	 * Sets the splitDistance to the given value, ensuring it is within the valid range (1-6).
	 * Throws a SplitOutOfRangeException if the value is out of range.
	 * 
	 * @param splitDistance The value to set for splitDistance.
	 * @throws SplitOutOfRangeException If the value is not between 1 and 6.
	 */
	public void editSplitDistance(int splitDistance) throws SplitOutOfRangeException
	{
		if(splitDistance >= 1 && splitDistance <= 6)
		board.setSplitDistance(splitDistance);
		else throw new SplitOutOfRangeException("Split distance must be between 1 and 6.");
	}
	/**
	 * Determines if the player's turn should be skipped based on their hand card count
	 * and the current turn number
	 * @return true if the player can play, false if their turn should be skipped.
	 */
	public boolean canPlayTurn()
	{
		int x = players.get(currentPlayerIndex).getHand().size();
		if(x == 4-turn)
		return true;
		return false;
	}
	public void playPlayerTurn() throws GameException
	{
		players.get(currentPlayerIndex).play();
	}
	/**
	 * Ends the current player's turn after they have either played a card or their turn was skipped.
	 * The method performs the following tasks:
	 * 1. Removes the selected card from the player's hand and adds it to the firePit.
	 * 2. Deselects any currently selected card by the player.
	 * 3. Advances to the next player in the turn order, wrapping around if necessary.
	 * 4. Starts a new round if all players have completed a turn.
	 * 5. Refills all players' hands from the deck when starting a new round.
	 * 6. Refills the deck's card pool from the firePit if the deck has fewer than 4 cards.
	 */
	public void endPlayerTurn()
	{
		Card x = players.get(currentPlayerIndex).getSelectedCard();
		// I should add if condition here
			players.get(currentPlayerIndex).getHand().remove(x); // remove from hand directly
			firePit.add(x);
	    
		deselectAll();
		currentPlayerIndex++;
		currentPlayerIndex %= 4;
		if(currentPlayerIndex == 0)
		turn++;
		
		if(turn == 4) // start a new round
		{
			turn = 0;
			for (int i = 0; i < 4; i++) 
			{
				 if(Deck.getPoolSize() < 4)
					{
						Deck.refillPool(firePit);
						firePit.clear();
					}
				 this.players.get(i).setHand(Deck.drawCards());
			}
		}
    }
	
	/**
	 * Checks if any player has won the game by filling their respective safe zone.
	 * A player wins when their safe zone is full, and this method checks each player's 
	 * safe zone to determine if that condition is met.
	 * 
	 * @return the colour of the player who has won the game, or null if no player has won yet.
	 */
	public Colour checkWin()
	{
		Colour x = null;
		for (int i = 0; i < 4; i++) 
            if(board.getSafeZones().get(i).isFull())
            	x = players.get(i).getColour();
		return x;
	}
	
	public void sendHome(Marble marble)
	{
		int idx = 0;
		for(int i = 0;i<4;i++)
		{
			if(players.get(i).getColour() == marble.getColour())
			{
				idx = i;
				break;
			}
		}
		players.get(idx).regainMarble(marble);
	}
	
	/**
	 * Fields a marble from the current player's collection to their Base Cell on the board.
	 * 
	 * This method retrieves one marble from the current player's Home Zone. If the player has no
	 * marbles to field, a {@link CannotFieldException} is thrown. If the player has a marble,
	 * it is sent to the player's Base Cell on the board. The marble is then removed from the player's
	 * Home Zone (collection of marbles).
	 * 
	 * @throws CannotFieldException If the current player has no marble to field.
	 * @throws IllegalDestroyException If an opponent's marble is destroyed while fielding the marble.
	 */
	public void fieldMarble() throws CannotFieldException, IllegalDestroyException
	{
		Marble x = players.get(currentPlayerIndex).getOneMarble();
		if(x == null)
		{
			throw new CannotFieldException("The current player has no marble to field.");
		}else
		{
			board.sendToBase(x); // throws 
			players.get(currentPlayerIndex).getMarbles().remove(0);
		}
	}
	
	/**
	 * Discards a random card from the player with the given colour.
	 * If the player with the specified colour does not exist or has no cards in hand, a CannotDiscardException is thrown.
	 * 
	 * @param colour The colour of the player from whom the card is to be discarded.
	 * @throws CannotDiscardException If the player has no cards to discard or if the player with the given colour is not found.
	 */
	public void discardCard(Colour colour) throws CannotDiscardException
	{
		int idx = -1;
		for(int i = 0; i < 4;i++)
		{
			if(players.get(i).getColour() == colour)
			{
				idx = i;
			}
		}
		if(idx == -1 || players.get(idx).getHand().size() == 0)
		{
			throw new CannotDiscardException("The player has no cards to discard.");
		}else
		{
		    int randomIndex = (int) (Math.random() * players.get(idx).getHand().size());
		    Card cardToDiscard = players.get(idx).getHand().get(randomIndex);
		    
		    // Remove the selected card from the player's hand
		    players.get(idx).getHand().remove(randomIndex);
		    firePit.add(cardToDiscard); // add to firepit
		}
	}
	
	/**
	 * Discards a random card from a player other than the current player.
	 * This method generates a random index (from 0 to 3) and calls the discardCard method 
	 * for the player at the generated index, ensuring the current player is excluded from selection.
	 * 
	 * @throws CannotDiscardException If the selected player has no cards to discard or if any other discard error occurs.
	 */
	public void discardCard() throws CannotDiscardException
	{
		int randomIndex;
		do
		{
			randomIndex = (int) (Math.random() * 4);
		}while(randomIndex == currentPlayerIndex);
		
		discardCard(players.get(randomIndex).getColour());
	}
	
	
	public Colour getActivePlayerColour()
	{
		return players.get(currentPlayerIndex).getColour();
	}
	
	
	public Colour getNextPlayerColour()
	{
		return players.get((currentPlayerIndex+1)%4).getColour();
	}
	
	
	
	}
