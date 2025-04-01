package engine.board;
import model.Colour;
import model.player.Marble;

import java.util.ArrayList;

import exception.*;

import model.player.Marble;

import java.util.ArrayList;

import exception.*;

/**
 * An interface that will allow other classes down the hierarchy to communicate
 * with the Board class.
 */
public interface BoardManager {
	int getSplitDistance();
	
	void moveBy(Marble marble, int steps, boolean destroy) throws IllegalMovementException, IllegalDestroyException;

	void swap(Marble marble, Marble marble2) throws IllegalSwapException;

	void destroyMarble(Marble marble) throws IllegalDestroyException;

	void sendToBase(Marble marble) throws CannotFieldException, IllegalDestroyException;

	void sendToSafe(Marble marble) throws InvalidMarbleException;

	ArrayList<Marble> getActionableMarbles();
}
