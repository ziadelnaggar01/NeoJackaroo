package model.player;
import model.Colour;
//A class representing the Marbles available in the game
public class Marble {
 final private Colour colour;

 public Marble(Colour colour) {
		this.colour = colour;
	}
 // read only
public Colour getColour() {
	return colour;
}
 
}
