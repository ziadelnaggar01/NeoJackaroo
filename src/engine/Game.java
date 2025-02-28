package engine;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

import engine.board.Board;
import model.Colour;
import model.card.Card;
import model.card.Deck;
import model.player.CPU;
import model.player.Player;

public class Game implements GameManager {
	private final Board board;
	private final ArrayList<Player> players;
	private final ArrayList<Card> firePit;
	private int currentPlayerIndex;
	private int turn;
    
	public Game(String playerName) throws IOException {
		ArrayList<Colour> colours = new ArrayList<>();
		colours.add(Colour.RED);
		colours.add(Colour.GREEN);
		colours.add(Colour.BLUE);
		colours.add(Colour.YELLOW);

		Collections.shuffle(colours);// built in method to shuffle elements in a list, can use a seed with it
		this.board = new Board(colours, this);
		Deck.loadCardPool(this, board);
		players.add(new Player(playerName, colours.get(0)));
		players.add(new CPU("CPU 1", colours.get(1), board));
		players.add(new CPU("CPU 2", colours.get(2), board));
		players.add(new CPU("CPU 3", colours.get(3), board));
		for (int i=0;i<4;i++) {
			players.get(i).setHand(Deck.drawCards());
		}
		this.currentPlayerIndex = 0;
		this.turn = 0;
		this.firePit = new ArrayList<>();

	}
}
