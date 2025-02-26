package model.card.standard;

public class Ten extends Standard {
	public Ten(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
		super(name, description, 10, suit, boardManager, gameManager);//initialize using super constructer, with rank as 10
	}
}
