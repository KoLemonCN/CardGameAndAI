package game.card;

/**
 * This class provides some simple functions over Card and Cards.
 * 
 * 1. Create Card for specified suit and rank.
 * 
 * 2. Create a deck of cards for the specified game.
 * 
 * 
 * @author Riki
 * 
 */
public class CardFactory {

	/**
	 * Create a Card by specified suit and rank.
	 * 
	 * @param suit
	 *            The card suit.
	 * @param rank
	 *            The card rank.
	 * @return A card.
	 */
	public static Card createCard(Suit suit, Rank rank) {
		return new Card(suit, rank);
	}

	/**
	 * Create a Card by specified rank. The Suit is by defualt SPADES.
	 * 
	 * @param rank
	 *            The card rank.
	 * @return A card.
	 */
	public static Card createCard(Rank rank) {
		return new Card(Suit.SPADES, rank);
	}

	/**
	 * Create a deck of cards for the game. There are all 4 suits with all
	 * ranks, except:
	 * 
	 * ACE: only have SPADES, HEARTS, CLUBS.
	 * 
	 * DEUCE: only have SPADES.
	 * 
	 * @return Standard cards for the specified game.
	 */
	public static Cards createDeck() {
		Cards cards = new Cards();

		for (Rank r : Rank.values()) {
			for (Suit s : Suit.values()) {
				if (Rank.ACE == r && Suit.DIAMONDS == s) {
					continue;
				} else if (Rank.DEUCE == r) {
					cards.addCard(new Card(Suit.SPADES, Rank.DEUCE));
					break;
				}
				cards.addCard(new Card(s, r));
			}
		}
		cards.sort();
		return cards;
	}
}
