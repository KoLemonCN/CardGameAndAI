package test;

import game.card.Card;
import game.card.Rank;
import game.card.Suit;

/**
 * This class to ensure the layout and highlight manner.
 * 
 * After adjusting to my preference, the project will start shortly.
 * 
 * 
 * @author Riki
 * 
 */

public class HelloWorld {
	public static void main(String[] args) {
		String s = "h";
		s += "e";
		s += "l";
		s += "l";
		s += "o";
		System.out.println(s);
		
		Card card = new Card(Suit.SPADES, Rank.ACE);
	}
}
