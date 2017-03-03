package game.card;

import java.util.Comparator;

/**
 * This is a Comparator for card: First compare rank, then compare suit.
 * 
 * @author Riki
 * 
 */
public class CardComparator implements Comparator<Card> {

	@Override
	public int compare(Card card1, Card card2) {
		if (card1.getID() > card2.getID()) {
			return 1;
		} else if (card1.getID() < card2.getID()) {
			return -1;
		}
		return 0;
	}

}
