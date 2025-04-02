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

		if (safeZones.get(0).getColour() == colour)
			return 0;
		if (safeZones.get(1).getColour() == colour)
			return 25;
		if (safeZones.get(2).getColour() == colour)
			return 50;
		if (safeZones.get(3).getColour() == colour)
			return 75;

		return -1;// default return value in case of an invalid colour
	}

	private int getEntryPosition(Colour colour) {
		int base = getBasePosition(colour);

		if (base != -1)
			return (((base - 2) + 100) % 100);
		return -1;
	}

	private ArrayList<Cell> validateSteps(Marble marble, int steps) throws IllegalMovementException {

		// Identify the marble’s current position, whether on the track or within the
		// player’s Safe Zone, If the marble is neither on the track nor in the Safe
		// Zone, throw an IllegalMovementException
		ArrayList<Cell> pathTaken = new ArrayList<>();
		int positionInTrack = getPositionInPath(track, marble);
		int positionInSafeZone = getPositionInPath(getSafeZone(marble.getColour()), marble);// safe zone of the marble
		int position; // and the marble itself

		if (positionInTrack != -1) {// found marble in track
			position = positionInTrack;

			if (steps == 4) {// move backwards
				recordBackwards(steps, position, pathTaken);
			} else if (steps == 5) {// move forward with no entry to safe zone
				recordForward(steps, position, pathTaken, track);// wrap around version
			}

			else {// move normally

				// first check if the movement is valid by getting where the entry position for
				// this marble is and adding 4 to it (notice it's guaranteed we find it in track
				// if we are here)

				int entry = getEntryPosition(marble.getColour());
				// get distance to entry keeping in mind circular nature
				int distanceToEntry;
				if (entry >= positionInTrack) {
					distanceToEntry = entry - positionInTrack;
				} else {
					distanceToEntry = (track.size() - positionInTrack) + entry;
				}

				if (distanceToEntry + 4 < steps) {// steps taken bigger than total possible steps
					throw new IllegalMovementException("Rank is too high");
				} else if (steps <= distanceToEntry) { // record path on track only
					recordForward(steps, position, pathTaken, track);
				} else {// record path that will include both track and safe zone cells
					recordForward(steps, position, track, getSafeZone(marble.getColour()), pathTaken, distanceToEntry);
				}
			}

		} // found marble in safe zone
		else if (positionInSafeZone != -1) {
			position = positionInSafeZone;

			// if its a four throw an illegalMovement exception since can't move backwards
			if (steps == 4) {
				throw new IllegalMovementException("Cannot move backwards in safezones");
			}

			// if it's a five it'd always too high of a rank (seperate from below for
			// clarity)
			else if (steps == 5) {
				throw new IllegalMovementException("Rank is too high");
			}

			// if normal, check if it will not pass end
			else {// normal movement
					// check if valid (doesnt pass end, i.e 3 since 0 indexed)
				if (position + steps > 3) {// more than 3 is invalid (0 based)
					throw new IllegalMovementException("Rank is too high");
				} else {// record path in safeZone only
					recordForward(steps, position, pathTaken, getSafeZone(marble.getColour()));
				}
			}

		} else// Not in track nor safe zone, i.e it's in base cells, so it cannot be moved
				// except with a 1 or a king, but since it's a special activity it is defined
				// elsewhere
			throw new IllegalMovementException("Marble cannot be moved");

		// Return the full path, where the current position is at the first index and
		// the target position is at the last index.
		return pathTaken;
	}

	private void validatePath(Marble marble, ArrayList<Cell> fullPath, boolean destroy)
			throws IllegalMovementException {

		boolean selfBlock = false;
		boolean pathBlock = false;
		boolean safeZoneEntryBlock = false;// cant bypass at entry
		boolean safeZoneBlock = false;// kind cant bypass a marble WITHIN safezone
		boolean baseCellBlock = false;
		int entry = getEntryPosition(marble.getColour());
		int positionInTrack = getPositionInPath(track, marble);

		int distanceToEntry;
		if (entry >= positionInTrack) {
			distanceToEntry = entry - positionInTrack;
		} else {
			distanceToEntry = (track.size() - positionInTrack) + entry;
		}

		if (!destroy) {
			int selfMarbles = 0;
			int otherMarbles = 0;
			Colour colour = marble.getColour();
			for (int i = 1; i < fullPath.size(); i++) {// start from after current till the end
				Cell cell = fullPath.get(i);
				if (cell.getMarble() != null) {
					if (colour == cell.getMarble().getColour())
						selfMarbles++;
					// different marble AND NOT TARGET ELEMENT
					if (i != fullPath.size() - 1 && colour != cell.getMarble().getColour())
						otherMarbles++;

					// if there is a base cell in the path or target and it's it's base then cant
					// move (comparing position of base of marble with actual current position)
					if (cell.getCellType() == CellType.BASE
							&& getBasePosition(cell.getMarble().getColour()) == getPositionInPath(track,
									cell.getMarble()))
						baseCellBlock = true;
				}
			}
			if (selfMarbles >= 1)
				selfBlock = true;
			if (otherMarbles + selfMarbles >= 2)// CLEARLY MENTIONS MORE THAN ONE
				pathBlock = true;

			// if the marble exists on track, and it will pass the entry, and a marble of
			// same colour exists in entry, then invalid
			if (positionInTrack != -1 && distanceToEntry <= fullPath.size() - 1 && track.get(entry).getMarble() != null
					&& track.get(entry).getMarble().getColour() == marble.getColour())
				safeZoneEntryBlock = true;

		} else {
			// king cannot break two rules, baseBlock and cannot bypass nor land on a marble
			// in safeZone

			for (int i = 1; i < fullPath.size(); i++) {
				Cell cell = fullPath.get(i);
				if (cell.getMarble() != null) {
					if (cell.getCellType() == CellType.BASE
							&& getBasePosition(cell.getMarble().getColour()) == getPositionInPath(track,
									cell.getMarble()))
						baseCellBlock = true;
					if (cell.getCellType() == CellType.SAFE) {
						safeZoneBlock = true;
					}
				}
			}
		}

		if (selfBlock) {
			throw new IllegalMovementException("A marble cannot move if there is another marble owned by the same\r\n"
					+ " player either in its path or at the target position");
		}
		if (pathBlock) {
			throw new IllegalMovementException("Movement is invalid if there is more than one marble (owned by\r\n"
					+ " any player) blocking the path");
		}
		if (safeZoneEntryBlock) {
			throw new IllegalMovementException("A marble cannot enter its player’s Safe Zone if any marble is "
					+ "stationed at its player’s Safe Zone Entry");
		}
		if (baseCellBlock) {
			throw new IllegalMovementException("A marble’s movement is blocked if another player’s marble is\r\n"
					+ " in its player’s Base cell, either in the path or target position");
		}
		if (safeZoneBlock) {
			throw new IllegalMovementException("A King cannot bypass or land on a Safe Zone marble");
		}

	}

	private void move(Marble marble, ArrayList<Cell> fullPath, boolean destroy) throws IllegalDestroyException {
		// destroy target with normal cell, no need for validation
		int position = getPositionInPath(track, marble);

		if (fullPath.get(fullPath.size() - 1).getCellType() == CellType.SAFE) {// you will enter a safe zone
			// target in safezone is guaranteed to be free since validation, and guaranteed
			// if king no safe marbles are before it GUARANTEED FROM VALIDATE PATH
			if (destroy) {
				for (int i = 1; i < fullPath.size() - 1; i++) {// path execluding target and current
					if (fullPath.get(i).getMarble() != null) {// if there is a marble destroy it SINCE NOT FREE
						destroyMarbleNoConstraint(fullPath.get(i).getMarble());
					}
				}
			}
			track.get(position).setMarble(null);// remove marble in the track, not fullPath
			int safeIndex = -1;
			for (int i = 0; i < fullPath.size(); i++) {// search how many safe does it path
				if (fullPath.get(i).getCellType() == CellType.SAFE) {
					safeIndex++;
				}
			}
			getSafeZone(marble.getColour()).get(safeIndex).setMarble(marble);

		} else {

			int target = position + (fullPath.size() - 1);// position + steps taken
			Cell targetCell = track.get(target);
			if (targetCell.getMarble() != null) {// it will be destroyed, no neeed to validate destroy since you cannot
													// land
													// on a base cell nor on ur own cell , it is validated in validate
													// steps

				destroyMarble(targetCell.getMarble());
			}
			if (destroy) {
				for (int i = 1; i < fullPath.size() - 1; i++) {// path execluding target and current
					if (fullPath.get(i).getMarble() != null) {// if there is a marble destroy it, no need to validate
						destroyMarbleNoConstraint(fullPath.get(i).getMarble());
					}
				}
			}

			track.get(target).setMarble(marble);
			track.get(position).setMarble(null);// change in the track, not fullPath
			if (targetCell.isTrap()) {
				destroyMarbleNoConstraint(marble);
				targetCell.setTrap(false);
				assignTrapCell();
			}
		}
	}

	private void validateSwap(Marble marble1, Marble marble2) throws IllegalSwapException {

		int m1 = getPositionInPath(track, marble1);
		int m2 = getPositionInPath(track, marble2);
// Swapping is prohibited if either of the involved marbles are not on the general track
		if (m1 == -1 || m2 == -1)
			throw new IllegalSwapException("The two marbles aren’t on the track.");
		Colour cm1 = marble1.getColour();
		Colour cm2 = marble2.getColour();
// Marbles owned by the same player are ineligible for swapping
		if (cm1 == cm2) {
			throw new IllegalSwapException(" Marbles owned by the same player are ineligible for swapping");
		}

//cant swap two enemy marbles
		Colour activeColour = gameManager.getActivePlayerColour();
		if (cm1 != activeColour && cm2 != activeColour) {
			throw new IllegalSwapException("Cannot swap two oponents' marbles");
		}

//swapping is invalid if the other marble is positioned in its Base cell.		
		if (activeColour == cm1) {// cm2 is the oponent marble
			if (m2 == getBasePosition(cm2)) {// check if it's in it's base
				throw new IllegalSwapException(
						"Swapping is invalid if the other marble is positioned in its Base cell");
			}
		} else {// cm1 is the oponent marble
			if (m1 == getBasePosition(cm1)) {// check if it's in it's base
				throw new IllegalSwapException(
						"Swapping is invalid if the other marble is positioned in its Base cell");
			}
		}
	}

	private void validateDestroy(int positionInPath) throws IllegalDestroyException {
		if (positionInPath == -1) {
			throw new IllegalDestroyException("Cannot destroy a marble not on general track");
		}
		// get position to check, if not it's base cell
		Cell cell = track.get(positionInPath);
		Marble marble = cell.getMarble();
		// first part of condition is not needed but for clarity
		if (cell.getCellType() == CellType.BASE && getBasePosition(marble.getColour()) == positionInPath) {
			throw new IllegalDestroyException("Cannot destroy a marble in it's base cell");
		}
	}

	private void validateFielding(Cell occupiedBaseCell) throws CannotFieldException {
		Colour active = gameManager.getActivePlayerColour();
		if (occupiedBaseCell.getCellType() == CellType.BASE && active == occupiedBaseCell.getMarble().getColour()) {
			throw new CannotFieldException("Cannot field a marble if base cell is occupied by you own marble");
		}
	}

	private void validateSaving(int positionInSafeZone, int positionOnTrack) throws InvalidMarbleException {

		if (positionOnTrack == -1) {
			throw new InvalidMarbleException("Cannot save a card not on the general track");
		}

		// check if opponents marble or not (game description)
		Marble marble = track.get(positionOnTrack).getMarble();
		if (marble.getColour() != gameManager.getActivePlayerColour()) {
			throw new InvalidMarbleException("Cannot save an opponent's card");
		}

	}

	// BoardManager methods overriding

	@Override
	public void moveBy(Marble marble, int steps, boolean destroy)
			throws IllegalMovementException, IllegalDestroyException {
		// this method will validate all standard and special that was not implemented
		// below it so the 4,5,7,king, get the full path, validate and call move
		ArrayList<Cell> fullPath = validateSteps(marble, steps);
		validatePath(marble, fullPath, destroy);
		move(marble, fullPath, destroy);
	}

	@Override
	public void swap(Marble marble, Marble marble2) throws IllegalSwapException {
		// get position in track of both marbles, put second in the first slot and put
		// the first in the second slot
		validateSwap(marble, marble2);
		int pos1 = getPositionInPath(track, marble);
		int pos2 = getPositionInPath(track, marble2);

		track.get(pos2).setMarble(marble);
		track.get(pos1).setMarble(marble2);

	}

	@Override
	public void destroyMarble(Marble marble) throws IllegalDestroyException {
		// the implementation here assumes this will only be used by the burner card
		// since it does not allow destruction of you own card, for the king's
		// destruction it will call another method
		// check if not my marble
		if (gameManager.getActivePlayerColour() == marble.getColour()) {
			throw new IllegalDestroyException("Cannot destroy your own marble with a burner");
		}
		int position = getPositionInPath(track, marble);
		// validate destruction
		validateDestroy(position);

		// remove from track
		track.get(position).setMarble(null);

		// put in home
		gameManager.sendHome(marble);// the power of an interface!!!

	}

	@Override
	public void sendToBase(Marble marble) throws CannotFieldException, IllegalDestroyException {// to field using a king
																								// or an ace
		int base = getBasePosition(marble.getColour());
		if (track.get(base).getMarble() != null) {// if occupied
			validateFielding(track.get(base));
			destroyMarble(track.get(base).getMarble());

		}
		gameManager.fieldMarble();// only called for fielding
	}

	@Override
	public void sendToSafe(Marble marble) throws InvalidMarbleException {
		ArrayList<Cell> safeZone = getSafeZone(marble.getColour());

		int positionInTrack = getPositionInPath(track, marble);
		int positionInSafeZone = getPositionInPath(safeZone, marble);
		validateSaving(positionInSafeZone, positionInTrack);

		// remove from current location
		track.get(positionInTrack).setMarble(null);// guaranteed to be found in track as else it would have thrown an
													// excpetion in validating

		// add to a random unoccupied safeZone cell
		int randIndex = -1;

		do
			randIndex = (int) (Math.random() * safeZone.size());// max is 3
		while (safeZone.get(randIndex).getMarble() != null);

		safeZone.get(randIndex).setMarble(marble);// safeZone is a pointer pointing to the safeZone in the list of
													// safeZones in class attributes

	}

	@Override
	public ArrayList<Marble> getActionableMarbles() {
		ArrayList<Marble> res = new ArrayList<>();
		Colour active = gameManager.getActivePlayerColour();

		// add all marbles on track
		for (int i = 0; i < track.size(); i++) {
			if (track.get(i).getMarble() != null) {
				res.add(track.get(i).getMarble());
			}
		}
		// get safe zone of current player
		ArrayList<Cell> safeZone = getSafeZone(active);

		// add all marbles in safeZone of current player
		for (int i = 0; i < safeZone.size(); i++) {
			if (safeZone.get(i).getMarble() != null) {
				res.add(safeZone.get(i).getMarble());
			}
		}

		return res;

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

	// HELPERS

	private void recordBackwards(int steps, int position, ArrayList<Cell> path) {
		// Gets path of moving backwards, since passed by reference return type is void

		// no need to first check if movement is valid, i.e not in safe zone, since it's
		// guaranteed we find it in track if we are here, so just record path

		for (int i = 0; i <= steps; i++) {
			path.add(track.get(((position - i) + 100) % 100));// all movements are on track only, adjusting wrap
																// arounds for each cell
		}
	}

	private void recordForward(int steps, int position, ArrayList<Cell> track, ArrayList<Cell> safeZone,
			ArrayList<Cell> pathTaken, int distanceToEntry) {
//IMPORTANT ASSUMPTION: THE LAST CELL THE MARBLE CAN GO TO BEFORE THE SAFEZONE IS THE SAFEZONE ENTRY, THE CELL RIGHT BEFORE BASE
		// IS USELESS
		pathTaken.add(track.get(position));
		int trackIndex = 0;
		int safeZoneIndex = 0;

		while (steps-- > 0) {
			// Records path on both track and safezone, no wrapping around
			if (distanceToEntry >= 0) {// still on track
				pathTaken.add(track.get((trackIndex + position) % 100));
				trackIndex++;
				distanceToEntry--;
			} else {
				pathTaken.add(safeZone.get(safeZoneIndex));
				safeZoneIndex++;
			}
		}
	}

	private void recordForward(int steps, int position, ArrayList<Cell> pathTaken, ArrayList<Cell> path) {
		// record path on a single path either safeZone or track, regardless of on track
		// or wrapped around it works
		for (int i = 0; i <= steps; i++)
			pathTaken.add(path.get((position + i) % 100));// even when it's a safezone, it doesnt matter
	}

	private void destroyMarbleNoConstraint(Marble marble) throws IllegalDestroyException {
		// remove from track
		track.get(getPositionInPath(track, marble)).setMarble(null);

		// put in home
		gameManager.sendHome(marble);// the power of an interface!!! }

	}
}