package game.card;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * This class implements the ICards interface, providing the functions to manage
 * cards information, such as the type, value, comparison function, string
 * representation, and ect.
 * 
 * @author Riki
 * 
 */
public class Cards implements ICards {
	/**
	 * The shared CardComparator to sort cards by rank & suit in ascending
	 * order.
	 */
	private static CardComparator comparator = new CardComparator();

	/**
	 * The list to hold cards.
	 */
	private List<Card> cards;

	/**
	 * The CardsType of current Cards.
	 */
	private CardsType type;

	/**
	 * The value of the current type.
	 */
	private int value;

	/**
	 * The constructor for Cards. It will initialize the list to hold the cards,
	 * and set the initial value of the cards to -1, indicating the err in
	 * combination of cards.
	 */
	public Cards() {
		cards = new Vector<Card>();
		value = -1;
	}

	/**
	 * The constructor for Cards. Basically, this constructor will takes another
	 * Cards, and copy the information. Notice that the copy is not ensured to
	 * be safe. To get the safe copy of this Card, please use the method public
	 * void clone().
	 * 
	 * @param cs
	 *            The Cards to be copied.
	 */
	public Cards(ICards cs) {
		super();
		cards = cs.getCards();
		this.type = cs.getCardsType();
		this.value = cs.getCardsValue();
	}

	/**
	 * Add a card to current Cards. This action will trigger the
	 * fireCardsPropertyChanges() method. Once the method is trigger, the type
	 * and value of the Cards will be reset to initial values. This is designed
	 * for efficiency.
	 * 
	 * @param card
	 *            The card to be added.
	 * @return The added card.
	 */
	@Override
	public Card addCard(Card card) {
		fireCardsPropertyChanges();
		cards.add(card);
		return card;
	}

	@Override
	public Object clone() {
		Cards cs = new Cards();
		for (int i = 0; i < cards.size(); i++) {
			cs.addCard((Card) cards.get(i).clone());
		}
		cs.setCardsType(this.type);
		cs.setCardsValue(this.value);
		return cs;
	}

	/**
	 * When the property of the Cards changed: for example, add / remove a card
	 * from the current Cards.
	 */
	private void fireCardsPropertyChanges() {
		type = null;
		value = -1;
	}

	/**
	 * Get the specified card.
	 * 
	 * @param index
	 *            The index of a card.
	 * @return A card.
	 */
	@Override
	public Card getCard(int index) {
		return cards.get(index);
	}

	/**
	 * Get the list of cards.
	 * 
	 * @return A list of cards.
	 */
	@Override
	public List<Card> getCards() {
		return cards;
	}

	/**
	 * Get the CardsType of the current Cards.
	 * 
	 * @return The CardsType of the current Cards.
	 */
	@Override
	public CardsType getCardsType() {
		if (type == null) {
			initCardsType();
		}
		return type;
	}

	/**
	 * Get the int value representing the current Cards.
	 * 
	 * @return the int value representing the current Cards.
	 */
	@Override
	public int getCardsValue() {
		if (type == null) {
			initCardsType();
		}
		return value;
	}

	/**
	 * Get the number of cards in this Cards.
	 * 
	 * @return The number of cards in the list.
	 */
	@Override
	public int getSize() {
		return cards.size();
	}

	/**
	 * Initialize the CardsType for the current Cards.
	 */
	private void initCardsType() {
		sort();
		CardsType.validateCards(this);
	}

	/**
	 * Test if the cards is empty.
	 * 
	 * @return True if the cards is empty, otherwise false.
	 */
	@Override
	public boolean isEmpty() {
		return cards.isEmpty();
	}

	/**
	 * Compare the cards according to the rules specified in the game rule
	 * specifications.
	 * 
	 * @param cards
	 *            The cards to be compared with.
	 * @return True if this cards is greater, false otherwise.
	 */
	@Override
	public boolean isGreaterThan(ICards cards) {
		CardsType t1 = this.getCardsType();
		CardsType t2 = cards.getCardsType();

		/*
		 * any of the cards contains invalid Cardstype, return false.
		 */
		if (t1 == null || t1 == CardsType.ERR) {
			return false;
		}

		if (t2 == null || t2 == CardsType.ERR) {
			return false;
		}

		// special case considering bombs.
		if (t1 == CardsType.BOMB) {
			return (t2 == CardsType.BOMB) ? (this.getCardsValue() > cards
					.getCardsValue()) : true;
		}

		// normal types.
		if (t1 != t2) {
			return false;
		}

		switch (t2) {
		case FLUSH:
		case MULTIPAIR:
		case MULTITRIPLE:
			if (this.getSize() != cards.getSize()) {
				return false;
			}
		case PAIR:
		case SINGLE:
		case TRIPLE:
		case TRIPLE_WITH_PAIR:
			return this.getCardsValue() > cards.getCardsValue();
		}

		System.err.println("unexpected error!");
		return false;
	}

	/**
	 * Add a card to current Cards.
	 * 
	 * @param card
	 *            The card to be added.
	 * @return The added card.
	 */
	@Override
	public Card removeCard(Card card) {
		fireCardsPropertyChanges();
		return cards.remove(card) ? card : null;
	}

	/**
	 * Remove the specified Card from current Cards.
	 * 
	 * @param index
	 *            The index of the card.
	 * @return The removed card.
	 */
	@Override
	public Card removeCard(int index) {
		fireCardsPropertyChanges();
		return cards.remove(index);
	}

	/**
	 * Remove all cards from the current Cards.
	 */
	@Override
	public void remvoeAll() {
		fireCardsPropertyChanges();
		cards = new Vector<Card>();
	}

	/**
	 * Set the current CardsType of the current cards.
	 * 
	 * @param type
	 *            A CardsType.
	 */
	@Override
	public void setCardsType(CardsType type) {
		this.type = type;
	}

	/**
	 * Set the int value representing the current Cards.
	 * 
	 * @param value
	 *            The int value representing the current Cards
	 */
	@Override
	public void setCardsValue(int value) {
		this.value = value;
	}

	/**
	 * Randomly shuffle the current list of cards.
	 */
	@Override
	public void shuffle() {
		Collections.shuffle(cards);
	}

	/**
	 * Sort the cards by ranks, and then by suits, in ascending order.
	 */
	@Override
	public void sort() {
		Collections.sort(cards, comparator);
	}

	/**
	 * Get the simplified String representation for the current Cards.
	 * 
	 * @return The String representation for the the current Cards.
	 */
	@Override
	public String toCardsString() {
		String str = "[";

		for (Card c : cards) {
			str += c.toString() + " ";
		}

		str += "]";

		return str;
	}

	/**
	 * The complete version to represent the Cards in String.
	 * 
	 * @return A String representation of the Cards.
	 */
	@Override
	public String toString() {
		String str = "Cards " + getSize() + " ";

		str += toCardsString();

		str += " Type: " + getCardsType() + " Value: " + getCardsValue();

		return str;
	}

}
