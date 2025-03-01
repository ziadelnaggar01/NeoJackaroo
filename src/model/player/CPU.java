package model.player;

import engine.board.BoardManager;
import model.Colour;

/** Represents a CPU player, a subclass of Player. */

public class CPU extends Player {

	private final BoardManager boardManager; // The current board instance, initialized by the Game class, wrapped as
												// the interface BoardManager, cannot be changed once initialized

	public CPU(String name, Colour colour, BoardManager boardManager) {
		/**
		 * Constructs a CPU object with the given parameters and initializes the board
		 * manager.
		 *
		 * @param name         The name of the CPU player.
		 * @param colour       The assigned colour of the CPU player.
		 * @param boardManager The board manager interface for managing the game board.
		 */
		super(name, colour);
		this.boardManager = boardManager;
	}
}
