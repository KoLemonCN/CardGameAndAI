package game.card;

/**
 * The enum defines several CardsTypes for Cards:
 * 
 * SINGLE: any one card.
 * 
 * PAIR: 2 cards, same rank.
 * 
 * TRIPLE: 3 cards, same rank. special case: [Ace, Ace, Ace] is a BOMB.
 * 
 * BOMB: 4 cards, same rank. special case: [Ace, Ace, Ace] is a BOMB.
 * 
 * MULTIPAIR: 2 or more PAIRs, with consecutive relation. E.g. [3,3,4,4,5,5]
 * 
 * MULTITRIPLE: 2 or more TRIPLEs, with consecutive relation. E.g.
 * [3,3,3,4,4,4,5,5,5]
 * 
 * TRIPLE_WITH_PAIR: 1 Triple and 1 Pair.
 * 
 * FLUSH: 5 or more consecutive cards.
 * 
 * ERR: represent an invalid cards combination.
 * 
 * @author Riki
 * 
 */
public enum CardsType {

	/**
	 * SINGLE: any one card.
	 */
	SINGLE {

		@Override
		public boolean validateType(ICards cards) {
			if (cards.getSize() == 1) {
				cards.setCardsType(this);
				cards.setCardsValue(cards.getCard(0).getRank().getValue());
				return true;
			}
			return false;
		}
	},

	/**
	 * PAIR: 2 cards, same rank.
	 */
	PAIR {

		@Override
		public boolean validateType(ICards cards) {
			if (cards.getSize() == 2 && inSameRank(cards)) {
				cards.setCardsType(this);
				cards.setCardsValue(cards.getCard(0).getRank().getValue());
				return true;
			}
			return false;
		}
	},

	/**
	 * TRIPLE: 3 cards, same rank. special case: [Ace, Ace, Ace] is a BOMB.
	 */
	TRIPLE {

		@Override
		public boolean validateType(ICards cards) {
			if (cards.getSize() == 3) {
				// 3 Aces is special bomb, not triple.
				if (Rank.ACE == cards.getCard(0).getRank()) {
					return false;
				}
				if (inSameRank(cards)) {
					cards.setCardsType(this);
					cards.setCardsValue(cards.getCard(0).getRank().getValue());
					return true;
				}
			}
			return false;
		}
	},

	/**
	 * BOMB: 4 cards, same rank. special case: [Ace, Ace, Ace] is a BOMB.
	 */
	BOMB {

		@Override
		public boolean validateType(ICards cards) {
			// 3 Aces is special bomb
			if (cards.getSize() == 3) {
				if (Rank.ACE == cards.getCard(0).getRank()) {
					if (inSameRank(cards)) {
						cards.setCardsType(this);
						cards.setCardsValue(cards.getCard(0).getRank()
								.getValue());
						return true;
					}
				}
			} else if (cards.getSize() == 4 && inSameRank(cards)) {
				cards.setCardsType(this);
				cards.setCardsValue(cards.getCard(0).getRank().getValue());
				return true;
			}

			return false;
		}

	},

	/**
	 * MULTIPAIR: 2 or more PAIRs, with consecutive relation. E.g. [3,3,4,4,5,5]
	 */
	MULTIPAIR {

		@Override
		public boolean validateType(ICards cards) {
			if (cards.getSize() > 2 && cards.getSize() % 2 == 0
					&& validateMultiPairs(cards)) {
				cards.setCardsType(this);
				cards.setCardsValue(cards.getCard(0).getRank().getValue());
				return true;
			}
			return false;
		}

		private boolean validateMultiPairs(ICards cards) {
			int pair = cards.getSize() / 2;

			for (int i = 0; i < pair; i++) {
				if (!inSameRank(cards, new int[] { i * 2, i * 2 + 1 })) {
					return false;
				}
			}

			int[] indices = new int[pair];
			for (int i = 0; i < pair; i++) {
				indices[i] = i * 2;
			}
			return isConsecutiveRank(cards, indices);
		}
	},
	/**
	 * MULTITRIPLE: 2 or more TRIPLEs, with consecutive relation. E.g.
	 * [3,3,3,4,4,4,5,5,5]
	 */
	MULTITRIPLE {

		@Override
		public boolean validateType(ICards cards) {
			if (cards.getSize() > 3 && cards.getSize() % 3 == 0
					&& validateMultiTriples(cards)) {
				cards.setCardsType(this);
				cards.setCardsValue(cards.getCard(0).getRank().getValue());
				return true;
			}
			return false;
		}

		private boolean validateMultiTriples(ICards cards) {
			int triple = cards.getSize() / 3;

			for (int i = 0; i < triple; i++) {
				if (!inSameRank(cards,
						new int[] { i * 3, i * 3 + 1, i * 3 + 2 })) {
					return false;
				}
			}

			int[] indices = new int[triple];
			for (int i = 0; i < triple; i++) {
				indices[i] = i * 3;
			}
			return isConsecutiveRank(cards, indices);
		}
	},
	/**
	 * TRIPLE_WITH_PAIR: 1 Triple and 1 Pair.
	 */
	TRIPLE_WITH_PAIR {

		@Override
		public boolean validateType(ICards cards) {
			if (cards.getSize() == 5) {
				if (inSameRank(cards, new int[] { 0, 1, 2 })
						&& inSameRank(cards, new int[] { 3, 4 })
						&& cards.getCard(0).getRank() != Rank.ACE) {
					cards.setCardsType(this);
					cards.setCardsValue(cards.getCard(0).getRank().getValue());
					return true;
				} else if (inSameRank(cards, new int[] { 0, 1 })
						&& inSameRank(cards, new int[] { 2, 3, 4 })
						&& cards.getCard(2).getRank() != Rank.ACE) {
					cards.setCardsType(this);
					cards.setCardsValue(cards.getCard(4).getRank().getValue());
					return true;
				}
			}
			return false;
		}
	},
	/**
	 * FLUSH: 5 or more consecutive cards.
	 */
	FLUSH {

		@Override
		public boolean validateType(ICards cards) {
			if (cards.getSize() >= 5) {
				int[] indices = new int[cards.getSize()];
				for (int i = 0; i < indices.length; i++) {
					indices[i] = i;
				}
				if (isConsecutiveRank(cards, indices)) {
					cards.setCardsType(this);
					cards.setCardsValue(cards.getCard(0).getRank().getValue());
					return true;
				}
			}
			return false;
		}

	},

	/**
	 * ERR: represent an invalid cards combination.
	 */
	ERR {

		@Override
		public boolean validateType(ICards cards) {
			cards.setCardsType(this);
			cards.setCardsValue(-1);
			return false;
		}
	};

	/**
	 * Validate he CardsType for the cards.
	 * 
	 * @param cards
	 *            The cards to be decides which CardsType it has.
	 * @return True if cards matches a valid CardsType, false otherwise.
	 */
	abstract boolean validateType(ICards cards);

	/**
	 * Calculates if each card shares a same rank.
	 * 
	 * @param cards
	 *            The cards to be calculated.
	 * @return True if all cards are in same rank, false otherwise.
	 */
	private static boolean inSameRank(ICards cards) {
		if (cards.isEmpty()) {
			return false;
		}

		Rank r = cards.getCard(0).getRank();

		for (Card c : cards.getCards()) {
			if (r != c.getRank()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Calculates if some card shares a same rank. The cards are specified by
	 * the indices: {@code indices}.
	 * 
	 * @param cards
	 *            The cards.
	 * @param indices
	 *            The indices that specified which cards to be compared.
	 * @return True if the chosen cards are in same rank, otherwise false.
	 */
	private static boolean inSameRank(ICards cards, int[] indices) {
		if (cards.isEmpty()) {
			return false;
		}

		try {
			int index = indices[0];
			Rank r = cards.getCard(index).getRank();
			for (int i = 1; i < indices.length; i++) {
				index = indices[i];
				if (r != cards.getCard(index).getRank()) {
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * Calculates if some card shares are consecutive in sequence. The cards are
	 * specified by the indices: {@code indices}.
	 * 
	 * @param cards
	 *            The cards.
	 * @param indices
	 *            The indices that specified which cards to be compared.
	 * @return True if the chosen cards are consecutive in sequence, otherwise
	 *         false.
	 */
	private static boolean isConsecutiveRank(ICards cards, int[] indices) {
		if (cards.isEmpty()) {
			return false;
		}

		try {
			for (int i = 1; i < indices.length; i++) {
				if (!Rank.isConsecutive(
						cards.getCard(indices[i - 1]).getRank(),
						cards.getCard(indices[i]).getRank())) {
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * Validate the CardsType for cards.
	 * 
	 * @param cards
	 *            The cards to be calculated.
	 * @return True if a valid CardsType is found to match cards, otherwise
	 *         false.
	 */
	public static boolean validateCards(ICards cards) {
		cards.sort();
		for (CardsType t : CardsType.values()) {
			if (t.validateType(cards)) {
				return true;
			}
		}
		return false;
	}
}
