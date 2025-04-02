package engine.board;

import java.util.ArrayList;

import engine.GameManager;
import model.Colour;

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
