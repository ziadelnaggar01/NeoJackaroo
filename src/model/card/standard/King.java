package model.card.standard;

public class King extends Standard {

	public King(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
		super(name, description, 13, suit, boardManager, gameManager);//initialize using super constructer, with rank as 13
	}

}
