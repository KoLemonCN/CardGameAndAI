package ai.util;

import game.card.Card;
import game.card.CardFactory;
import game.card.Cards;
import game.card.Rank;
import game.card.Suit;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The seed for some cards. The cards are organized to form the cards.
 * 
 * @author Riki
 * 
 */
public class Seed {
	/*
	 * for later test use.
	 */
	private String description;
	/*
	 * for later test use.
	 */
	private String autoSummary;

	/**
	 * The cards comparator used to sort the seed in order.
	 */
	private static CardsComparator comparator = new CardsComparator();

	/**
	 * The cards of the seed.
	 */
	private Cards cards;

	/**
	 * list of cards.
	 */
	private ArrayList<Cards> seedCards;

	/**
	 * The constructor of the cards.
	 */
	public Seed() {
		cards = new Cards();
		seedCards = new ArrayList<Cards>();
		description = "";
		autoSummary = "";
	}

	/**
	 * Get the Cards of the seed.
	 * 
	 * @return The cards of the seed.
	 */
	public Cards getCards() {
		return cards;
	}

	/**
	 * Get the seed cards.
	 * 
	 * @return the Seed cards.
	 */
	public ArrayList<Cards> getSeedCards() {
		return seedCards;
	}

	/**
	 * Get the size of the seed.
	 * 
	 * @return the size of the seed.
	 */
	public int size() {
		return seedCards.size();
	}

	/**
	 * get the i-th card in the seed.
	 * 
	 * @param i
	 *            the index
	 * @return the i-th card of the seed.
	 */
	public Cards get(int i) {
		return seedCards.get(i);
	}

	/**
	 * add a cards to seed.
	 * 
	 * @param cs
	 *            the cards.
	 */
	public void add(Cards cs) {
		for (int i = 0; i < cs.getSize(); i++) {
			cards.addCard(cs.getCard(i));
		}
		seedCards.add(cs);
	}

	/**
	 * Set the description of the seed.
	 */
	public void setDescription(String des) {
		this.description = des;
	}

	/**
	 * Get the description of the seed.
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	public String getAutoSummary() {
		return autoSummary;
	}

	@Override
	public Object clone() {
		Seed newSeed = new Seed();
		newSeed.cards = (Cards) this.cards.clone();
		newSeed.description = new String(this.description);
		newSeed.autoSummary = new String(this.autoSummary);
		newSeed.seedCards = (ArrayList<Cards>) this.seedCards.clone();
		return newSeed;
	}

	@Override
	public String toString() {
		String str = "Seed for " + cards + "{ \n";

		for (int i = 0; i < seedCards.size(); i++) {
			str += "\t" + (i + 1) + " : " + seedCards.get(i) + "\n";
		}
		str += "}\n";
		return str;
	}

	/**
	 * sort the seed.
	 */
	public void sort() {
		Collections.sort(seedCards, comparator);
	}

	/**
	 * shuffle the seed
	 */
	public void shuffle() {
		Collections.shuffle(seedCards);
	}

	@Override
	public boolean equals(Object o) {
		/**
		 * Assume the seeds are sorted.
		 */
		if (o instanceof Seed) {
			Seed s = (Seed) o;
			Cards cs1, cs2;

			if (this.size() != s.size()) {
				return false;
			}

			for (int i = 0; i < s.size(); i++) {
				cs1 = s.get(i);
				cs2 = this.get(i);
				if (cs1.getCardsType() != cs2.getCardsType()
						|| cs1.getCardsValue() != cs2.getCardsValue()) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * for test purpose.
	 */
	public static void main(String[] args) {

		Card c1 = CardFactory.createCard(Suit.HEARTS, Rank.ACE);
		Card c2 = CardFactory.createCard(Suit.CLUBS, Rank.THREE);
		Card c3 = CardFactory.createCard(Suit.HEARTS, Rank.THREE);
		Card c4 = CardFactory.createCard(Suit.CLUBS, Rank.FOUR);
		Card c5 = CardFactory.createCard(Suit.HEARTS, Rank.FOUR);
		Card c6 = CardFactory.createCard(Suit.HEARTS, Rank.FIVE);
		Card c7 = CardFactory.createCard(Suit.CLUBS, Rank.FIVE);
		Card c8 = CardFactory.createCard(Suit.SPADES, Rank.FIVE);
		Card c9 = CardFactory.createCard(Suit.SPADES, Rank.ACE);

		Cards cards5 = new Cards();
		cards5.addCard(c9);

		Cards cards1 = new Cards();
		cards1.addCard(c1);

		Cards cards2 = new Cards();
		cards2.addCard(c2);
		cards2.addCard(c3);

		Cards cards3 = new Cards();
		cards3.addCard(c4);
		cards3.addCard(c5);

		Cards cards4 = new Cards();
		cards4.addCard(c6);
		cards4.addCard(c7);
		cards4.addCard(c8);

		Seed seedCards = new Seed();

		seedCards.add(cards4);
		seedCards.add(cards2);
		seedCards.add(cards1);
		seedCards.add(cards3);
		seedCards.add(cards5);
		System.out.println(seedCards);

		seedCards.sort();

		System.out.println(seedCards);

		System.out.println("test for equality of seeds");
		Seed seed1 = new Seed();

		seed1.add(cards1);
		seed1.add(cards3);

		Seed seed2 = new Seed();
		seed2.add(cards3);
		seed2.add(cards5);

		System.out.println(seed1 + " " + seed2);
		seed1.sort();
		seed2.sort();

		System.out.println(seed1.equals(seed2));

	}

}
