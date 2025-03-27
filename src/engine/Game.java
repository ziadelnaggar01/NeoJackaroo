package engine;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import engine.board.Board;
import model.Colour;
import model.card.Card;
import model.card.Deck;
import model.player.CPU;
import model.player.Player;

/**
 * Represents the engine of the game, where it allows the game to start and
 * initializes all core elements of the game
 */
@SuppressWarnings("unused")
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
}
