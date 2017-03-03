package game.card;

import java.util.List;

/**
 * This interface is used to design a Cards type:
 * 
 * Actually, this is used to represent a list of cards.
 * 
 * But instead of using an array of Card, which would require other data types
 * to hold extra information, such as the type of the cards (e.g. Sing or
 * Pair...), this interface perfectly defines properties of cards.
 * 
 * 1. Management of a list of Card.
 * 
 * add card / delete card / sort list of cards / shuffle list of cards
 * 
 * 2. Management of cards type.
 * 
 * calculate the type of cards for current list of cards
 * 
 * 3. Functions to string representations of cards.
 * 
 * full information of the cards / simplified representation of cards
 * 
 * @author Riki
 * 
 */
public interface ICards {
	/**
	 * Add a card to current Cards.
	 * 
	 * @param card
	 *            The card to be added.
	 * @return The added card.
	 */
	public Card addCard(Card card);

	/**
	 * Get the specified card.
	 * 
	 * @param index
	 *            The index of a card.
	 * @return A card.
	 */
	public Card getCard(int index);

	/**
	 * Get the list of cards.
	 * 
	 * @return A list of cards.
	 */
	public List<Card> getCards();

	/**
	 * Get the CardsType of the current Cards.
	 * 
	 * @return The CardsType of the current Cards.
	 */
	public CardsType getCardsType();

	/**
	 * Get the int value representing the current Cards.
	 * 
	 * @return the int value representing the current Cards.
	 */
	public int getCardsValue();

	/**
	 * Get the number of cards in this Cards.
	 * 
	 * @return The number of cards in the list.
	 */
	public int getSize();

	/**
	 * Compare the cards.
	 * 
	 * @param cards
	 *            The cards to be compared with.
	 * @return True if this cards is greater, false otherwise.
	 */
	public boolean isGreaterThan(ICards cards);

	/**
	 * Test if the cards is empty.
	 * 
	 * @return True if the cards is empty, otherwise false.
	 */
	public boolean isEmpty();

	/**
	 * Remove the specified Card from current Cards.
	 * 
	 * @param card
	 *            The card.
	 * @return The removed card.
	 */
	public Card removeCard(Card card);

	/**
	 * Remove the specified Card from current Cards.
	 * 
	 * @param index
	 *            The index of the card.
	 * @return The removed card.
	 */
	public Card removeCard(int index);

	/**
	 * Remove all cards from the current Cards.
	 */
	public void remvoeAll();

	/**
	 * Set the current CardsType of the current cards.
	 * 
	 * @param type
	 *            A CardsType.
	 */
	public void setCardsType(CardsType type);

	/**
	 * Set the int value representing the current Cards.
	 * 
	 * @param value
	 *            The int value representing the current Cards
	 */
	public void setCardsValue(int value);

	/**
	 * Randomly shuffle the current list of cards.
	 */
	public void shuffle();

	/**
	 * Sort the cards by ranks, and then by suits, in ascending order.
	 */
	public void sort();

	/**
	 * Get the simplified String representation for the current Cards.
	 * 
	 * @return The String representation for the the current Cards.
	 */
	public String toCardsString();
}