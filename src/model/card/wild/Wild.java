package model.card.wild;

import engine.GameManager;
import engine.board.BoardManager;
import model.card.Card;

//A class representing the wild cards available in the game
public abstract class Wild extends Card {
	public Wild(String name, String description, BoardManager boardManager, GameManager gameManager)
	{
		super(name,description,boardManager,gameManager);
	}
}
