package engine;

import model.*;
import model.player.Marble;
import exception.*;
/**
 * An interface that will allow other classes down the hierarchy to communicate
 * with the Game class.
 */

public interface GameManager {
	void sendHome(Marble marble);

	void fieldMarble() throws CannotFieldException, IllegalDestroyException;

	void discardCard(Colour colour) throws CannotDiscardException;

	void discardCard() throws CannotDiscardException;

	Colour getActivePlayerColour();

	Colour getNextPlayerColour();
}
