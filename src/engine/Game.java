package engine;

import java.io.*;
import java.util.ArrayList;
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

	/**
	 * Initializes a new game, setting up the board, shuffling colors, and dealing
	 * initial hands to players.
	 *
	 * @param playerName The name entered by the human player.
	 * @throws IOException If there is an issue loading the deck or initializing the
	 *                     game components.
	 */
	public Game(String playerName) throws IOException {

		// Initialize and randomize the colors for the players (used to assign player
		// colors).
		ArrayList<Colour> colours = new ArrayList<>();
		colours.add(Colour.RED);
		colours.add(Colour.GREEN);
		colours.add(Colour.BLUE);
		colours.add(Colour.YELLOW);
		Collections.shuffle(colours);// Shuffle the colors randomly to assign unique colors to players.

		// initialize core game components
		this.currentPlayerIndex = 0;
		this.turn = 0;
		this.firePit = new ArrayList<>();
		this.board = new Board(colours, this);
		this.players = new ArrayList<>();
		Deck.loadCardPool(this, board);

		// add the players
		players.add(new Player(playerName, colours.get(0)));
		players.add(new CPU("CPU 1", colours.get(1), board));
		players.add(new CPU("CPU 2", colours.get(2), board));
		players.add(new CPU("CPU 3", colours.get(3), board));

		// initialize hand of each player
		for (int i = 0; i < 4; i++) {
			players.get(i).setHand(Deck.drawCards());
		}

	}
}
