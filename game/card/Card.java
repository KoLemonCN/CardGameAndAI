package game.card;

/**
 * This is a Card class. A card contains a Suit, and a Rank.
 * 
 * The ID of a card is unique in a deck.
 * 
 * 
 * @author Riki
 * 
 */
public class Card {

	/**
	 * The ID of this card.
	 */
	public final int ID;

	/**
	 * The rank of this card.
	 */
	public final Rank rank;

	/**
	 * The suit of this card.
	 */
	public final Suit suit;

	/**
	 * The constructor: the creation of a card need a Suit and Rank.
	 * 
	 * The ID of the card is automatically generated using the value of rank and
	 * suit.
	 * 
	 * @param suit
	 *            The suit.
	 * @param rank
	 *            The rank.
	 */
	public Card(Suit suit, Rank rank) {
		this.suit = suit;
		this.rank = rank;
		this.ID = rank.getValue() + suit.getValue();
	}

	@Override
	public Object clone() {
		return new Card(suit, rank);
	}

	@Override
	public boolean equals(Object o) {
		Card c = (Card) o;
		return suit.equals(c.getSuit()) && rank.equals(c.getRank());
	}

	/**
	 * Get the ID of the card.
	 * 
	 * @return The ID of the card.
	 */
	public int getID() {
		return ID;
	}

	/**
	 * Get the rank of the card.
	 * 
	 * @return The rank.
	 */
	public Rank getRank() {
		return rank;
	}

	/**
	 * The suit of the card.
	 * 
	 * @return Suit of the card.
	 */
	public Suit getSuit() {
		return suit;
	}

	@Override
	public String toString() {
		return suit.toString() + rank.toString();
	}
}