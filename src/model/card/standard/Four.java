package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;

public class Four extends Standard{
	Four(String name, String description, Suit suit, BoardManager boardManager, GameManager
			gameManager){
		super(name, description, 4, suit, boardManager, gameManager);//initialize using super constructer, with rank as 4
	}
}
