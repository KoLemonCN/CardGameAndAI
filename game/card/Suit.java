package game.card;

/**
 * This enum is used to represent the Suit of a card.
 * 
 * There are basically 4 Suit: Spades, Hearts, Clubs and Diamonds.
 * 
 * @author Riki
 * 
 */
public enum Suit {

	/**
	 * A Suit: Spades
	 */
	SPADES {

		@Override
		public int getValue() {
			return 4;
		}

		@Override
		public String toString() {
			return "s";
		}
	},
	/**
	 * A Suit: Hearts
	 */
	HEARTS {

		@Override
		public int getValue() {
			return 3;
		}

		@Override
		public String toString() {
			return "h";
		}
	},
	/**
	 * A Suit: Clubs
	 */
	CLUBS {

		@Override
		public int getValue() {
			return 2;
		}

		@Override
		public String toString() {
			return "c";
		}
	},
	/**
	 * A Suit: Diamonds
	 */
	DIAMONDS {

		@Override
		public int getValue() {
			return 1;
		}

		@Override
		public String toString() {
			return "d";
		}
	};

	/**
	 * The simple test: 
	 * 
	 * 1. Print all String representations of the Suits.
	 * 
	 * 2. Print all Integer representations of the Suits.
	 * 
	 * @param args No arguments required.
	 */
	public static void main(String args[]) {
		for (Suit s : Suit.values()) {
			System.out.println("Name:" + s.name() + "\tString representation:"
					+ s.toString() + "\tValue:" + s.getValue());
		}
	}

	/**
	 * Get the integer value to represent a suit.
	 * 
	 * @return The value of a suit.
	 */
	abstract int getValue();
}
