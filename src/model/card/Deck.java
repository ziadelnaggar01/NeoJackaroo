package model.card;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import engine.GameManager;
import engine.board.BoardManager;
import model.card.standard.Ace;
import model.card.standard.Five;
import model.card.standard.Four;
import model.card.standard.Jack;
import model.card.standard.King;
import model.card.standard.Queen;
import model.card.standard.Seven;
import model.card.standard.Standard;
import model.card.standard.Suit;
import model.card.standard.Ten;
import model.card.wild.Burner;
import model.card.wild.Saver;

public class Deck {
private final String  CARDS_FILE = "Cards.csv"; // path
private ArrayList<Card> cardsPool = new ArrayList<>();
/*
 Arraylist to store the available cards. This attribute is a member
 of the class rather than its instances.
 * */

// method
public void loadCardPool(BoardManager boardManager, GameManager gameManager)
throws IOException
{
	try (BufferedReader br = new BufferedReader(new FileReader(CARDS_FILE)))
	{
		String line = "";
		while((line = br.readLine()) != null)
		{
			String [] data = line.split(",");
			int code = Integer.parseInt(data[0]);
			System.out.println(code);
			int frequency =Integer.parseInt(data[1]);
			String name = data[2];
			String description = data[3];
			int rank = Integer.parseInt(data[4]);
			Suit suit = Suit.valueOf(data[5]);
			// Determine the type of the card
			for(int i = 0;i<frequency;i++)
			{
				Card card;
				if(code == 14)
				{
					card = new Burner(name,description,boardManager,gameManager);
				}else if(code  == 15)
				{
					card = new Saver(name,description,boardManager,gameManager);
				}else if(code == 13) // king
				{
					card = new King(name,description,suit,boardManager,gameManager);
				}else if(code == 12)
				{
					card = new Queen(name,description,suit,boardManager,gameManager);
				}else if(code == 11)
				{
					card = new Jack(name,description,suit,boardManager,gameManager);
				}else if(code == 10)
				{
					card = new Ten(name,description,suit,boardManager,gameManager);
				}else if(code == 7)
				{
					card = new Seven(name,description,suit,boardManager,gameManager);
				}else if(code == 5)
				{
					card = new Five(name,description,suit,boardManager,gameManager);
				}else if(code == 4)
				{
					card = new Four(name,description,suit,boardManager,gameManager);
				}else if(code == 1)
				{
					card = new Ace(name,description,suit,boardManager,gameManager);
				}else // standard
				{
					card = new Standard(name,description,rank,suit,boardManager,gameManager);
				}
			}
		}
	}
}
public ArrayList<Card> drawCards()
{ 
	Collections.shuffle(cardsPool);
	 ArrayList<Card> drawnCards = new ArrayList<>();
	 for(int i = 0;i<4;i++)
	 {
		 if(!cardsPool.isEmpty())
		 {
			 drawnCards.add(cardsPool.remove(0));
		 }
	 }
	 return drawnCards;
}
}
