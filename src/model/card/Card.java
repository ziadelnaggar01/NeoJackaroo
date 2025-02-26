package model.card;

public abstract class Card {

	private final String name;
	private final String description;
	protected BoardManager boardManager;// ???????????????getter setter final?????????
	protected GameManager gameManager;// ?????????????????getter setter final?????????

	public Card(String name, String description, BoardManager boardManager, GameManager gameManager) {
		super();
		this.name = name;
		this.description = description;
		this.boardManager = boardManager;
		this.gameManager = gameManager;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
	
	
	

}
