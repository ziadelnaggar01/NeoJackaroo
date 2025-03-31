package engine.board;

import java.util.ArrayList;

import engine.GameManager;
import exception.CannotFieldException;
import exception.IllegalDestroyException;
import exception.IllegalMovementException;
import exception.IllegalSwapException;
import exception.InvalidMarbleException;
import model.Colour;
import model.player.Marble;

/**
 * Represents the game board, including the main track and safe zones. Manages
 * cell assignments and trap placements.
 */
@SuppressWarnings("unused")

public class Board implements BoardManager {

	private final GameManager gameManager;
	private final ArrayList<Cell> track;
	private final ArrayList<SafeZone> safeZones;
	private int splitDistance;

	private ArrayList<Cell> getSafeZone(Colour colour) {

		for (int i = 0; i < safeZones.size(); i++) {
			SafeZone safeZone = safeZones.get(i);
			if (safeZone.getColour() == colour) {// comparison with == works with enums

				return safeZone.getCells();

			}
		}
		return null;// default return value if colour not found is null
	}

	private int getPositionInPath(ArrayList<Cell> path, Marble marble) {
		for (int i = 0; i < path.size(); i++) {
			if (path.get(i).getMarble().equals(marble)) {// comparing reference of the two marbles, since no overriden
															// .equals method
				return i;// 0 based positioning (refer to page 3 in game description, figure 2)
			}
		}
		return -1;
	}

	private int getBasePosition(Colour colour) {

//main idea: 
//look for all base cells on track (only four on the %25==0 positions), and look if that base cell contains the required colour
		if (this.track.get(0).getMarble().getColour() == colour)
			return 0;

		if (this.track.get(25).getMarble().getColour() == colour)
			return 25;

		if (this.track.get(50).getMarble().getColour() == colour)
			return 50;

		if (this.track.get(75).getMarble().getColour() == colour)
			return 75;

		return -1;// default return value in case of an invalid colour
	}

	private int getEntryPosition(Colour colour) {
		// main idea:
		// look for all entry cells on track (only four on the (i+2) %25==0
		// positions), and look if that base cell contains the required colour
		if (this.track.get(23).getMarble().getColour() == colour)
			return 23;

		if (this.track.get(48).getMarble().getColour() == colour)
			return 48;

		if (this.track.get(73).getMarble().getColour() == colour)
			return 73;

		if (this.track.get(98).getMarble().getColour() == colour)
			return 98;

		return -1;// default return value in case of an invalid colour
	}

	/**
	 * Constructs a game board with the given player color order. Initializes the
	 * track, safe zones, and assigns trap cells.
	 *
	 * @param colourOrder The order of colors assigned to players.
	 * @param gameManager The game manager responsible for overall game control.
	 */
	public Board(ArrayList<Colour> colourOrder, GameManager gameManager) {
		this.gameManager = gameManager;
		this.track = new ArrayList<>();
		this.safeZones = new ArrayList<>();
		assignCells();

		// Assign trap cells randomly on the track
		for (int i = 0; i < 8; i++)
			assignTrapCell();

		// Initialize safe zones for each player
		for (int i = 0; i < 4; i++)
			safeZones.add(new SafeZone(colourOrder.get(i)));

		this.splitDistance = 3;

	}

	/**
	 * Assigns cells to the main track, including base, normal, and entry cells.
	 */
	private void assignCells() {
		for (int i = 0; i < 100; i++) {
			this.track.add(new Cell(CellType.NORMAL));

			if (i % 25 == 0)
				this.track.get(i).setCellType(CellType.BASE);

			else if ((i + 2) % 25 == 0)
				this.track.get(i).setCellType(CellType.ENTRY);
		}
	}

	/**
	 * Assigns a trap to a random normal cell that does not already contain one.
	 */
	private void assignTrapCell() {
		int randIndex = -1;

		do
			randIndex = (int) (Math.random() * 100);
		while (this.track.get(randIndex).getCellType() != CellType.NORMAL || this.track.get(randIndex).isTrap());

		this.track.get(randIndex).setTrap(true);
	}

	@Override
	public int getSplitDistance() {
		return splitDistance;
	}

	public ArrayList<Cell> getTrack() {
		return track;
	}

	public ArrayList<SafeZone> getSafeZones() {
		return safeZones;
	}

	public void setSplitDistance(int splitDistance) {
		this.splitDistance = splitDistance;
	}

}
