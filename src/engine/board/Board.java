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

	/**
	 * Retrieves the safe zone cells associated with the specified colour.
	 *
	 * <p>
	 * The method iterates through the list of safe zones and returns the list of
	 * cells belonging to the safe zone that matches the given colour. If no
	 * matching safe zone is found, it returns {null}.
	 *
	 * @param colour The colour of the player whose safe zone is being retrieved.
	 * @return An {@code ArrayList} of {@code Cell} objects representing the
	 *         player's safe zone, or {@code null} if no matching safe zone is
	 *         found.
	 */
	private ArrayList<Cell> getSafeZone(Colour colour) {
		for (int i = 0; i < safeZones.size(); i++) {
			SafeZone safeZone = safeZones.get(i);
			if (safeZone.getColour() == colour) // Comparison with == works with enums
				return safeZone.getCells();

		}
		return null; // Default return value if colour not found is null
	}

	/**
	 * Retrieves the position of a given marble within a specified path.
	 *
	 * <p>
	 * The method iterates through the provided list of {@code Cell} objects to find
	 * the index of the cell that contains the given marble. The comparison is done
	 * using reference equality ({@code ==}) since the {@code Marble} class does not
	 * override the {@code equals} method.
	 *
	 * <p>
	 * If the marble is found, its index within the list is returned (0-based). If
	 * the marble is not found in the given path, the method returns {@code -1}.
	 *
	 * @param path   The list of {@code Cell} objects representing a track or safe
	 *               zone.
	 * @param marble The {@code Marble} object whose position needs to be found.
	 * @return The 0-based index of the marble within the path, or {@code -1} if not
	 *         found.
	 */
	private int getPositionInPath(ArrayList<Cell> path, Marble marble) {
		for (int i = 0; i < path.size(); i++) {
			if (path.get(i).getMarble().equals(marble)) // Comparing reference of the two marbles
				return i; // 0-based positioning (refer to page 3 in game description, figure 2)
		}
		return -1;
	}

	/**
	 * Retrieves the base position for a given colour.
	 *
	 * <p>
	 * This method determines the starting position of a player's base by checking
	 * the associated {@code Colour} of the {@code SafeZone} objects. Each player's
	 * base is located at fixed positions (0, 25, 50, 75) corresponding to their
	 * index in the {@code safeZones} list.
	 *
	 * @param colour The {@code Colour} representing the player.
	 * @return The base position of the specified colour, or {@code -1} if the
	 *         colour is invalid.
	 */
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

	/**
	 * Retrieves the entry position for a given colour.
	 *
	 * <p>
	 * The entry position is calculated based on the base position of the given
	 * colour. It is determined as {@code (base - 2 + 100) % 100} to ensure proper
	 * wrap-around on the board, which consists of 100 positions.
	 *
	 * @param colour The {@code Colour} representing the player.
	 * @return The entry position of the specified colour, or {@code -1} if the
	 *         colour is invalid.
	 */
	private int getEntryPosition(Colour colour) {
		int basePosition = getBasePosition(colour);

		if (basePosition != -1)
			return (((basePosition - 2) + 100) % 100);
		return -1;
	}

	/**
	 * Validates whether a marble can move along a specified path, checking for
	 * obstacles and other movement restrictions. This includes validating if the
	 * path is clear of marbles owned by the same player, ensuring that multiple
	 * marbles don't block the path, and ensuring that the marble doesn't violate
	 * any game-specific rules regarding Safe Zones, Base Cells, or King's movement.
	 * 
	 * @param marble   the marble to be moved
	 * @param fullPath a list of `Cell` objects representing the path the marble
	 *                 will take from the current position to the target position
	 * @param destroy  a flag indicating whether marbles encountered along the path
	 *                 should be destroyed (e.g., when passing over or landing on
	 *                 enemy marbles)
	 * 
	 * @throws IllegalMovementException if any movement rule is violated, such as: -
	 *                                  A marble cannot move if another marble owned
	 *                                  by the same player is in its path or at the
	 *                                  target position. - More than one marble is
	 *                                  blocking the path. - A marble cannot enter a
	 *                                  Safe Zone if the entry point is blocked. - A
	 *                                  marble cannot bypass or land on a Base cell
	 *                                  occupied by an opponent's marble regardless
	 *                                  of being king or not. - A King cannot bypass
	 *                                  or land on a Safe Zone.
	 */

	private void validatePath(Marble marble, ArrayList<Cell> fullPath, boolean destroy)
			throws IllegalMovementException {

		int otherMarbles = 0;
		for (int i = 1; i < fullPath.size(); i++) {
			Cell cell = fullPath.get(i);

			// Cell is occupied
			if (cell.getMarble() != null) {

				// Increment total otherMarbles if not target
				if (cell.getMarble().getColour() != marble.getColour() && i < fullPath.size() - 1)
					otherMarbles++;

				// A marble exists on the path or target with the same colour
				if (!destroy && cell.getMarble().getColour() == marble.getColour())
					throw new IllegalMovementException(
							"A marble cannot move if there is another marble owned by the same\r\n"
									+ " player either in its path or at the target position");

				// More than 1 marbles exist on the path execluding target
				if (!destroy && cell.getMarble().getColour() != marble.getColour() && otherMarbles >= 2)
					throw new IllegalMovementException(
							"Movement is invalid if there is more than one marble (owned by\r\n"
									+ " any player) blocking the path");

				// Your entry has a marble occupying it and you will enter the safe zone
				if (!destroy && track.get(getEntryPosition(cell.getMarble().getColour())).getMarble() != null
						&& fullPath.get(fullPath.size() - 1).getCellType() == CellType.SAFE)
					throw new IllegalMovementException("A marble cannot enter its player’s Safe Zone if any marble is "
							+ "stationed at its player’s Safe Zone Entry");

				// Cannot bypass a base cell with it's marble in it regardless of king or not
				if (getPositionInPath(track, cell.getMarble()) == getBasePosition(cell.getMarble().getColour()))
					throw new IllegalMovementException(
							"A marble’s movement is blocked if another player’s marble is\r\n"
									+ " in its player’s Base cell, either in the path or target position");

				// Cannot bypass or land on a safe cell with a king
				if (destroy && cell.getCellType() == CellType.SAFE)
					throw new IllegalMovementException("A King cannot bypass or land on a Safe Zone marble");

			}
		}
	}

	/**
	 * Moves a marble along the specified path, handling marble destruction and trap
	 * resetting. This method assumes that validation for the movement and
	 * conditions for destruction have already been handled elsewhere (e.g., in the
	 * `moveBy` method).
	 * 
	 * @param marble   the marble to be moved
	 * @param fullPath a list of `Cell` objects representing the path the marble
	 *                 will take from the current position to the target position
	 * @param destroy  a flag indicating whether marbles encountered along the path
	 *                 should be destroyed (i.e a King card)
	 * 
	 * @throws IllegalDestroyException if there is an error during the destruction
	 *                                 of a marble (e.g., attempting to destroy a
	 *                                 marble when it's not allowed)
	 */

	private void move(Marble marble, ArrayList<Cell> fullPath, boolean destroy) throws IllegalDestroyException {
		// No validation for anything required in this method, all is done in moveBy
		// method
		int steps = fullPath.size() - 1;
		fullPath.get(0).setMarble(null);// Same as doing it in track

		// Iterate through the path till target
		for (int i = 1; i <= steps; i++) {

			Cell cell = fullPath.get(i);

			// If cell is occupied, AND it is a king OR target cell, then destroy marble
			// No validation required for safe cells nor destroying own cell in target
			if (cell.getMarble() != null && (destroy || i == steps))
				destroyMarble(cell.getMarble());
		}

		// Get the target cell, last index in full path
		Cell targetCell = fullPath.get(steps);

		// Add marble to target cell
		targetCell.setMarble(marble);

		// If target is a trap, destroy marble and reset trap
		if (targetCell.isTrap()) {
			destroyMarble(marble);
			targetCell.setTrap(false);
			assignTrapCell();
		}
	}

	/**
	 * Validates whether a swap between two marbles is allowed.
	 *
	 * <p>
	 * This method ensures that both marbles are on the general track and that the
	 * opponent's marble is not positioned in its base cell, as swapping is
	 * prohibited in such cases.
	 *
	 * @param marble1 The first marble involved in the swap.
	 * @param marble2 The second marble involved in the swap.
	 * @throws IllegalSwapException If either marble is not on the track or if the
	 *                              swap is attempted with a marble that is
	 *                              positioned in its base cell.
	 */
	private void validateSwap(Marble marble1, Marble marble2) throws IllegalSwapException {
		// No need to validate illegal colors since it is handled in the Player class.

		int m1 = getPositionInPath(track, marble1);
		int m2 = getPositionInPath(track, marble2);

		// Swapping is prohibited if either marble is not on the general track.
		if (m1 == -1 || m2 == -1)
			throw new IllegalSwapException("The two marbles aren’t on the track.");

		Colour cm1 = marble1.getColour();
		Colour cm2 = marble2.getColour();
		Colour activeColour = gameManager.getActivePlayerColour();

		// Swapping is invalid if the opponent's marble is positioned in its Base cell.
		if (activeColour == cm1) { // cm2 is the opponent's marble
			if (m2 == getBasePosition(cm2)) { // Check if it's in its base
				throw new IllegalSwapException(
						"Swapping is invalid if the other marble is positioned in its Base cell.");
			}
		} else { // cm1 is the opponent's marble
			if (m1 == getBasePosition(cm1)) { // Check if it's in its base
				throw new IllegalSwapException(
						"Swapping is invalid if the other marble is positioned in its Base cell.");
			}
		}
	}

	/**
	 * Validates whether a marble at a given position can be destroyed.
	 *
	 * <p>
	 * This method ensures that the marble is on the general track and not
	 * positioned in its base cell, as marbles in base cells are protected from
	 * destruction.
	 *
	 * @param positionInPath The position of the marble in the track.
	 * @throws IllegalDestroyException If the marble is not on the general track or
	 *                                 if it is located in its base cell.
	 */
	private void validateDestroy(int positionInPath) throws IllegalDestroyException {
		// Ensure the marble exists on the general track.
		if (positionInPath == -1) {
			throw new IllegalDestroyException("Cannot destroy a marble not on the general track.");
		}

		Cell targetCell = track.get(positionInPath);
		Marble targetMarble = targetCell.getMarble();

		// Validate that the marble is not in its base cell.
		boolean isInItsBasePosition = (getBasePosition(targetMarble.getColour()) == positionInPath);

		if (isInItsBasePosition) {
			throw new IllegalDestroyException("Cannot destroy a marble in its base cell.");
		}
	}

	/**
	 * Validates whether a marble can be fielded to a given base cell.
	 *
	 * <p>
	 * This method checks if the base cell is occupied by the active player's
	 * marble. If the base cell contains the player's own marble, the fielding
	 * action is not allowed.
	 *
	 * @param occupiedBaseCell The base cell to be checked for fielding.
	 * @throws CannotFieldException If the base cell is occupied by the active
	 *                              player's own marble.
	 */
	private void validateFielding(Cell occupiedBaseCell) throws CannotFieldException {
		Colour activePlayerColour = gameManager.getActivePlayerColour();

		// Check if the base cell is occupied by the active player's own marble.
		if (occupiedBaseCell.getCellType() == CellType.BASE
				&& activePlayerColour == occupiedBaseCell.getMarble().getColour()) {
			throw new CannotFieldException("Cannot field a marble if base cell is occupied by your own marble.");
		}
	}

	/**
	 * Validates whether a marble can be saved to a safe zone.
	 *
	 * <p>
	 * This method checks if the marble is on the general track before it can be
	 * saved to a safe zone. If the marble is not on the general track (position is
	 * -1), an exception is thrown.
	 *
	 * <p>
	 * Note: The color check for the marble is not performed here as it is handled
	 * within the Player class.
	 *
	 * @param positionInSafeZone The position in the safe zone where the marble is
	 *                           to be saved.
	 * @param positionOnTrack    The position of the marble on the general track.
	 * @throws InvalidMarbleException If the marble is not on the general track.
	 */
	private void validateSaving(int positionInSafeZone, int positionOnTrack) throws InvalidMarbleException {
		// If the marble is not on the general track, throwing an exception.
		if (positionOnTrack == -1) {
			throw new InvalidMarbleException("Cannot save a marble not on the general track.");
		}
	}

	// BoardManager methods overriding

	/**
	 * Moves a marble by a specified number of steps on the track.
	 *
	 * <p>
	 * This method calculates the full path for the marble's movement based on the
	 * specified number of steps. It first validates the steps, then validates the
	 * path based on the marble's current position and the destination. If the path
	 * is valid, the marble is moved along the path. The method also handles special
	 * cases like "king" and other movement rules that are not implemented in the
	 * standard movement logic.
	 *
	 * @param marble  The marble to be moved.
	 * @param steps   The number of steps the marble should move.
	 * @param destroy Whether to destroy marbles in the path if needed (based on the
	 *                game rules).
	 * @throws IllegalMovementException If the movement is not allowed due to
	 *                                  invalid steps or rules.
	 * @throws IllegalDestroyException  If the destruction of marbles during
	 *                                  movement is invalid.
	 */
	@Override
	public void moveBy(Marble marble, int steps, boolean destroy)
			throws IllegalMovementException, IllegalDestroyException {
		// Validate the steps and get the full path if valid
		ArrayList<Cell> fullPath = validateSteps(marble, steps);

		// Validate the path taken
		validatePath(marble, fullPath, destroy);

		// Perform the actual movement
		move(marble, fullPath, destroy);
	}

	/**
	 * Swaps the positions of two marbles on the track.
	 *
	 * <p>
	 * This method swaps the positions of two specified marbles on the track. It
	 * first validates whether the swap can occur by ensuring the conditions for a
	 * valid swap are met. If valid, it retrieves the positions of both marbles on
	 * the track and then exchanges their positions.
	 *
	 * @param marble  The first marble to be swapped.
	 * @param marble2 The second marble to be swapped.
	 * @throws IllegalSwapException If the swap is not allowed due to invalid
	 *                              conditions (e.g., marbles are not on the track,
	 *                              or swapping would violate game rules).
	 */
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

	/**
	 * Destroys a marble from the track and sends it to the player's home zone.
	 *
	 * <p>
	 * This method removes the specified marble from the track, checks if the active
	 * player is allowed to destroy the marble based on the current game rules, and
	 * sends the destroyed marble to the player's home zone. The method also handles
	 * specific
	 *
	 *
	 * Note: if this method was called by King in move, it does not require
	 * validation since:
	 * 
	 * 1- it is guaranteed to be on track (king cannot move if there is a marble in
	 * safe zone whther in it's path or target)
	 * 
	 * 2- it is guaranteed to not destroy a marble in it's base cell since king
	 * cannot bypass a marble in it's base cell, making it an invalid move
	 * 
	 * 3- the two above validations are made by the move method before calling the
	 * move by method, which performs the destruction
	 * 
	 * @param marble The marble to be destroyed.
	 * @throws IllegalDestroyException If the destruction of the marble is not
	 *                                 allowed, such as if the marble is not the
	 *                                 opponent's or if the destruction violates the
	 *                                 game rules.
	 */
	@Override
	public void destroyMarble(Marble marble) throws IllegalDestroyException {
		// In this method no validation of same colour is required since that is done
		// when the burner card is played, which allows this method to be also used by
		// move method when it's king or POSSIBLy five, as they require self destruction
		int position = getPositionInPath(track, marble);

		// validate destruction if not the same colour as active player
		if (gameManager.getActivePlayerColour() != marble.getColour()) {
			validateDestroy(position);
		}

		// remove from track
		track.get(position).setMarble(null);

		// Send through the game manager interface to access home zone of the player
		gameManager.sendHome(marble);

	}

	/**
	 * Sends a marble to the base cell of the player.
	 *
	 * <p>
	 * This method attempts to move a specified marble to the base cell of the
	 * player, however it does not remove a marble from the home zone, that is done
	 * in fieldMarble method that utilizes this method. If the base cell is already
	 * occupied by another marble, the method validates the fielding conditions and,
	 * if necessary, destroys the occupying marble before moving the current marble
	 * to the base cell. If the marble cannot be fielded or destroyed, an exception
	 * will be thrown.
	 *
	 * @param marble The marble to be sent to the base cell.
	 * @throws CannotFieldException    If the marble cannot be fielded (e.g., if the
	 *                                 base cell is occupied by the player's own
	 *                                 marble).
	 * @throws IllegalDestroyException If the marble cannot be destroyed (e.g., if
	 *                                 there is an issue with the destruction
	 *                                 process).
	 */

	@Override
	public void sendToBase(Marble marble) throws CannotFieldException, IllegalDestroyException {
		int base = getBasePosition(marble.getColour());
		if (track.get(base).getMarble() != null) {// if occupied
			validateFielding(track.get(base));
			destroyMarble(track.get(base).getMarble());
		}
		track.get(base).setMarble(marble);
	}

	/**
	 * Sends a marble to the safe zone of the player.
	 *
	 * <p>
	 * This method attempts to move a specified marble from its current position on
	 * the track to a random empty cell in the player's safe zone. The method
	 * ensures that the marble is not already in the safe zone and is in a valid
	 * position before being sent to the safe zone. If the marble is not in a valid
	 * state to be saved, an exception will be thrown.
	 *
	 * <p>
	 * Note that selecting a marble of opponent to save is not allowed, however that
	 * exception is handled when playing the card
	 * 
	 * @param marble The marble to be sent to the safe zone.
	 * @throws InvalidMarbleException If the marble is not in a valid position to be
	 *                                saved (e.g., it's already in the safe zone or
	 *                                its position is invalid).
	 */
	@Override
	public void sendToSafe(Marble marble) throws InvalidMarbleException {

		ArrayList<Cell> safeZone = getSafeZone(marble.getColour());
		int positionInTrack = getPositionInPath(track, marble);

		// guarantee finding in track
		validateSaving(getPositionInPath(safeZone, marble), positionInTrack);

		// remove from current location
		track.get(positionInTrack).setMarble(null);

		// find a random unoccupied safeZone cell
		int randIndex = -1;
		do
			randIndex = (int) (Math.random() * safeZone.size());
		while (safeZone.get(randIndex).getMarble() != null);

		// assign marble to an empty random position in safe zone
		safeZone.get(randIndex).setMarble(marble);
	}

	/**
	 * Retrieves all marbles that are actionable by the active player.
	 *
	 * <p>
	 * This method identifies all marbles that belong to the active player and can
	 * be acted upon. It checks both the marbles on the general track and those in
	 * the active player's safe zone. If the active player has a marble at any
	 * position on the track or within their safe zone, those marbles are considered
	 * actionable and are added to the result list.
	 * 
	 * @return An ArrayList of actionable marbles that belong to the active player,
	 *         including those on the general track and in the safe zone.
	 */
	@Override
	public ArrayList<Marble> getActionableMarbles() {

		ArrayList<Marble> res = new ArrayList<>();
		Colour active = gameManager.getActivePlayerColour();

		// add all marbles on track of the active player's colours
		for (int i = 0; i < track.size(); i++) {
			if (track.get(i).getMarble() != null && track.get(i).getMarble().getColour() == active) {
				res.add(track.get(i).getMarble());
			}
		}

		// get safe zone of active player
		ArrayList<Cell> safeZone = getSafeZone(active);

		// add all marbles in safeZone of active player
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
			if (distanceToEntry >= 0) {// still on track, ADD CONDITION OR THAT ITS A FIVE AND THE MARBLE IS A
										// DIFFERENT
										// COLOUR SO WRAP AROUND, ELSE IT WILL GO IN SAFE ZONE 3ADY
				// AND REMOVE THE HELPER BELOW
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
	
	
	

}