package engine.board;
import model.Colour;
import model.player.Marble;

import java.util.ArrayList;

import exception.*;

/**
 * An interface that will allow other classes down the hierarchy to communicate
 * with the Board class.
 */
public interface BoardManager {
	int getSplitDistance();
	
}
