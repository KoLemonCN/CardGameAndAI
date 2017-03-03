package game.card;

/**
 * The Rank of Cards.
 * 
 * As the Rank is fixed: Ace, Deuce, 3, 4, 5, 6, 7, 8, 9, 10, Jack, Queen, King
 * The Rank is defined as Enum.
 * 
 * Also, This Enum also provide functions for each Rank
 * 
 * 1. A string representation for each Rank.
 * 
 * 2. A value for each Rank for Rank comparison.
 * 
 * 3. A function int compare(Rank r1, Rank r2) to compare two ranks.
 * 
 * 4. a function int compare(Card c1, Card c2) to compare two cards with regard
 * of their ranks.
 * 
 * 5. a function boolean isConsecutive(Rank r1, Rank r2) to decide whether two
 * ranks are consecutive or not.
 * 
 * 
 * 
 * @author Riki
 * 
 */
public enum Rank {

	/**
	 * A Rank: ACE
	 */
	ACE {

		public static final int VALUE = 14 * STEP;
		public static final String SP = "A";

		@Override
		public int getValue() {
			return VALUE;
		}

		@Override
		public String toString() {
			return SP;
		}
	},
	/**
	 * A Rank: DEUCE
	 */
	DEUCE {

		public static final int VALUE = 16 * STEP;
		public static final String SP = "2";

		@Override
		public int getValue() {
			return VALUE;
		}

		@Override
		public String toString() {
			return SP;
		}
	},
	/**
	 * A Rank: 3
	 */
	THREE {

		public static final int VALUE = 3 * STEP;
		public static final String SP = "3";

		@Override
		public int getValue() {
			return VALUE;
		}

		@Override
		public String toString() {
			return SP;
		}
	},
	/**
	 * A Rank: 4
	 */
	FOUR {

		public static final int VALUE = 4 * STEP;
		public static final String SP = "4";

		@Override
		public int getValue() {
			return VALUE;
		}

		@Override
		public String toString() {
			return SP;
		}
	},
	/**
	 * A Rank: 5
	 */
	FIVE {

		public static final int VALUE = 5 * STEP;
		public static final String SP = "5";

		@Override
		public int getValue() {
			return VALUE;
		}

		@Override
		public String toString() {
			return SP;
		}
	},
	/**
	 * A Rank: 6
	 */
	SIX {

		public static final int VALUE = 6 * STEP;
		public static final String SP = "6";

		@Override
		public int getValue() {
			return VALUE;
		}

		@Override
		public String toString() {
			return SP;
		}
	},
	/**
	 * A Rank: 7
	 */
	SEVEN {

		public static final int VALUE = 7 * STEP;
		public static final String SP = "7";

		@Override
		public int getValue() {
			return VALUE;
		}

		@Override
		public String toString() {
			return SP;
		}
	},
	/**
	 * A Rank: 8
	 */
	EIGHT {

		public static final int VALUE = 8 * STEP;
		public static final String SP = "8";

		@Override
		public int getValue() {
			return VALUE;
		}

		@Override
		public String toString() {
			return SP;
		}
	},
	/**
	 * A Rank: 9
	 */
	NINE {

		public static final int VALUE = 9 * STEP;
		public static final String SP = "9";

		@Override
		public int getValue() {
			return VALUE;
		}

		@Override
		public String toString() {
			return SP;
		}
	},
	/**
	 * A Rank: 10
	 */
	TEN {

		public static final int VALUE = 10 * STEP;
		public static final String SP = "10";

		@Override
		public int getValue() {
			return VALUE;
		}

		@Override
		public String toString() {
			return SP;
		}
	},
	/**
	 * A Rank: Jack
	 */
	JACK {

		public static final int VALUE = 11 * STEP;
		public static final String SP = "J";

		@Override
		public int getValue() {
			return VALUE;
		}

		@Override
		public String toString() {
			return SP;
		}
	},
	/**
	 * A Rank: Queen
	 */
	QUEEN {

		public static final int VALUE = 12 * STEP;
		public static final String SP = "Q";

		@Override
		public int getValue() {
			return VALUE;
		}

		@Override
		public String toString() {
			return SP;
		}
	},
	/**
	 * A Rank: King
	 */
	KING {

		public static final int VALUE = 13 * STEP;
		public static final String SP = "K";

		@Override
		public int getValue() {
			return VALUE;
		}

		@Override
		public String toString() {
			return SP;
		}
	};

	/**
	 * The value step or each consecutive rank.
	 */
	public static final int STEP = 10;

	/**
	 * Given two Cards, card1 and card2: decide the relation of two cards with
	 * regard to their ranks.
	 * 
	 * @param c1
	 *            A Card
	 * @param c2
	 *            A Card
	 * @return 1 if the rank of card {@code c1} is higher, -1 if the rank of
	 *         card {@code c2} is higher, 0 if equal.
	 */
	public static int compare(Card c1, Card c2) {
		if (c1.getRank().getValue() > c2.getRank().getValue()) {
			return 1;
		} else if (c1.getRank().getValue() < c2.getRank().getValue()) {
			return -1;
		}
		return 0;
	}

	/**
	 * Given two ranks, rank1 and rank2: decide the relation of two ranks.
	 * 
	 * @param r1
	 *            A rank
	 * @param r2
	 *            A rank
	 * @return 1 if {@code r1} is higher, -1 if {@code r2} is higher, 0 if
	 *         equal.
	 */
	public static int compare(Rank r1, Rank r2) {
		if (r1.getValue() > r2.getValue()) {
			return 1;
		} else if (r1.getValue() < r2.getValue()) {
			return -1;
		}
		return 0;
	}

	/**
	 * Decide whether two ranks are consecutive. That is there are no other
	 * ranks between those two ranks.
	 * 
	 * @param r1
	 *            A rank
	 * @param r2
	 *            A rank
	 * @return True if rank {@code r1} and rank {@code r2} are consecutive,
	 *         otherwise false.
	 */
	public static boolean isConsecutive(Rank r1, Rank r2) {
		return Math.abs(r1.getValue() - r2.getValue()) == Rank.STEP;
	}

	// tests
	public static void main(String args[]) {
		for (Rank r : Rank.values()) {
			System.out.println(r.ordinal() + " Name: " + r.name()
					+ "\t String representation: " + r + "\t Value : "
					+ r.getValue());
		}

		for (Rank r1 : Rank.values()) {
			for (Rank r2 : Rank.values()) {
				System.out.println("CompareRank(" + r1 + "," + r2 + ")\t"
						+ Rank.compare(r1, r2));
			}
		}
		for (Rank r1 : Rank.values()) {
			for (Rank r2 : Rank.values()) {
				if (Rank.isConsecutive(r1, r2)) {
					System.out.println("isConsecutive(" + r1 + "," + r2 + ")");
				}
			}
		}

		// The following shows the identity quality.
		System.out.println(Rank.ACE.equals(Rank.ACE));
	}

	/**
	 * Get the int value to represent a Rank.
	 * 
	 * @return the int value of a rank.
	 */
	public abstract int getValue();
}