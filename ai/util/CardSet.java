package ai.util;

import game.card.Card;
import game.card.Cards;
import game.card.Rank;

import java.util.ArrayList;
import java.util.List;

/**
 * The CardSet will summary the cards for probability calculator.
 * 
 * @author Riki
 * 
 */
public class CardSet {
	// the cards to be summarized.
	private Cards cards;
	// summarized rank
	private ProbSet<Rank> rankSet;
	// the cards in list.
	private List<Cards> cardsList;

	/**
	 * The constructor for the CardSet.
	 * 
	 * @param cs
	 *            Cards to be transformed into CardSet.
	 */
	public CardSet(Cards cs) {
		this.cards = cs;
		cards.sort();
		init();
	}

	/**
	 * Initialize the CardSet.
	 */
	private void init() {
		List<Rank> ranks = new ArrayList<Rank>();

		for (int i = 0; i < cards.getSize(); i++) {
			ranks.add(cards.getCard(i).getRank());
		}

		rankSet = new ProbSet<Rank>(ranks);

		cardsList = new ArrayList<Cards>();

		Cards cs = new Cards();
		cs.addCard(cards.getCard(0));
		Card c, lastC;
		for (int i = 1; i < cards.getSize(); i++) {
			c = cards.getCard(i);
			lastC = cs.getCard(cs.getSize() - 1);
			if (c.getRank() == lastC.getRank()) {
				cs.addCard(c);
			} else {
				cardsList.add(cs);
				cs = new Cards();
				cs.addCard(c);
			}
		}
		cardsList.add(cs);
	}

	/**
	 * Get the Cards list.
	 * 
	 * @return The list of Cards.
	 */
	public List<Cards> getCardsList() {
		return cardsList;
	}

	/**
	 * Get the set for Rank
	 * 
	 * @return The set for rank.
	 */
	public ProbSet<Rank> getRankSet() {
		return rankSet;
	}

	/**
	 * Get the Cards of the CardSet.
	 * 
	 * @return The cards.
	 */
	public Cards getCards() {
		return cards;
	}

	/**
	 * String representation of the CardSet.
	 */
	public String toString() {
		String str = "";

		str += " rank Set: " + rankSet + "\n";

		for (int i = 0; i < cardsList.size(); i++) {
			str += "\t cardsList_" + i + " " + cardsList.get(i) + "\n";
		}

		return str;
	}

}
