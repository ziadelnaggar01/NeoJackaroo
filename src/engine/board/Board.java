package engine.board;

import java.util.ArrayList;

public class Board implements BoardManager {

	private final GameManager gameManager;
	private final ArrayList<Cell> track;
	private final ArrayList<SafeZone> safeZones;
	private int splitDistance;

	public Board(ArrayList<Colour> colourOrder, GameManager gameManager) {
		this.gameManager = gameManager;
		this.track = new ArrayList<>();
		this.safeZones = new ArrayList<>();
		splitDistance = 3;
		assignCells();
		assignTrapCell();
		for (int i = 0; i < 4; i++) {
			safeZones.add(new SafeZone(colourOrder.get(i)));
		}

	}

	private void assignCells() {// Assigning cells on the main track (safeEntry, normal, base), neither safeZone
								// nor homeZone included
		for (int i = 0; i < 100; i++) {
			// if i%25 means start of quarter so base cell
			if (i % 25 == 0) {
				track.add(new Cell(CellType.BASE));
			}
			// before start of quarter by 2 cells means its the entry safe zone for the
			// quarter/player
			else if (i == 23 || i == 48 || i == 73 || i == 98) {
				track.add(new Cell(CellType.ENTRY));
			} else {
				track.add(new Cell(CellType.NORMAL));
			}
		}
	}

	private void assignTrapCell() {// Assigning 8 random trap cells to 8 NORMAL cells
		int assignedCellsCount = 0;
		while (assignedCellsCount < 8) {
			int random = (int) (Math.random() * 100);// generates a random integer from 0 to 99 inclusive
			Cell randomCell = track.get(random);
			if (randomCell.getCellType() == CellType.NORMAL && !randomCell.isTrap()) {
				randomCell.setTrap(true);
				assignedCellsCount++;
			}
		}
	}

	@Override
	public int getSplitDistance() {
		return splitDistance;
	}

	public GameManager getGameManager() {
		return gameManager;
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
