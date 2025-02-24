package engine.board;

import model.player.Marble;

// A class representing the Cells available on the board.

public class Cell {
	private Marble marble; // It's either null of occupied by a marble object
	private CellType cellType; // To specify cell type (NORMAL, SAFE, BASE, ENTRY)
	private boolean trap; // Indicates if this cell is a trap cell

	Cell(CellType cellType)
	{
		// Cell Object constructor with default access, no need to access it outside the board package

		/*
		 *  initializes a Cell object with the cell type 
		 *  sets the marble to null
		 *  sets trap to false
		 */
		this.cellType = cellType;
		marble = null;
		trap = false;
	}

	Marble getMarble()
	{
		/**
		 * Retrieves the marble occupying this cell.
		 *
		 * @return The Marble object or null if unoccupied.
		 */
		return marble;
	}

	CellType getCellType()
	{
		/**
		 * Retrieves the type of the cell.
		 *
		 * @return The CellType of this cell.
		 */
		return cellType;
	}

	boolean isTrap()
	{
		/**
		 * Checks if this cell is a trap.
		 *
		 * @return true if this cell is a trap, false otherwise.
		 */
		return trap;
	}

	void setMarble(Marble marble)
	{
		/**
		 * Assigns a marble to this cell.
		 *
		 * @param marble The marble to place in the cell.
		 */
		this.marble = marble;
	}

	void setCellType(CellType cellType)
	{
		/**
		 * Sets the type of this cell.
		 *
		 * @param cellType The new type of the cell (NORMAL, SAFE, BASE, ENTRY).
		 */
		this.cellType = cellType;
	}

	void setTrap(boolean trap)
	{
		/**
		 * Marks this cell as a trap or removes the trap status.
		 *
		 * @param trap true to mark as a trap, false to remove the trap status.
		 */
		this.trap = trap;
	}




}
