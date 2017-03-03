package ai.util;

import game.card.Cards;

import java.util.Comparator;

/**
 * The CardsComparator for Cards. Firstly it will compare the type, then compare
 * the value of cards. Therefore, the sorted Cards will be in same type, and the
 * value will be in ascending order.
 * 
 * @author Riki
 * 
 */
public class CardsComparator implements Comparator<Cards> {

	@Override
	public int compare(Cards cards1, Cards cards2) {
		// sort by type.
		if (cards1.getCardsType().ordinal() < cards2.getCardsType().ordinal()) {
			return -1;
		}

		if (cards1.getCardsType().ordinal() > cards2.getCardsType().ordinal()) {
			return 1;
		}

		// sort by value.
		if (cards1.getCardsValue() < cards2.getCardsValue()) {
			return -1;
		}
		if (cards1.getCardsValue() > cards2.getCardsValue()) {
			return 1;
		}

		// the same.
		return 0;
	}

}
