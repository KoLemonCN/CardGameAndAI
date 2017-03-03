package ai.comp;

import game.card.Cards;
import game.card.Rank;
import game.util.GameEnvironment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ai.GameAI;
import ai.util.Actions;
import ai.util.ProbabilityCalculator;
import ai.util.Seed;
import ai.util.Summary;

/**
 * The main function for the analyzer is to generate scores for seeds.
 * 
 * @author Riki
 * 
 */
public class Analyzer {

	// the game ai.
	private GameAI gameAI;
	/**
	 * the ActionList.
	 */
	private ArrayList<Actions> actionsList;
	/**
	 * The original seed.
	 */
	private ArrayList<Seed> originSeeds;
	/**
	 * The comparator to compare the dominator with regard to value.
	 */
	private DominatorComparator dominatorComparator = new DominatorComparator();

	public Analyzer(GameAI gameAI) {
		this.gameAI = gameAI;
		actionsList = new ArrayList<Actions>();
	}

	/**
	 * Create the action list for a list of seeds.
	 * 
	 * @param seeds
	 *            The seeds to be decide the sequence in which the cards are
	 *            played.
	 * @return The list of actions.
	 */
	public ArrayList<Actions> createActionsList(ArrayList<Seed> seeds) {
		this.originSeeds = seeds;
		initActionsList();

		return actionsList;
	}

	/**
	 * Initialize the action list.
	 */
	private void initActionsList() {

		actionsList = new ArrayList<Actions>();

		/*
		 * Summary -> [Actions, ...]
		 */

		Actions actions;
		Seed seed;
		Cards deskCards = gameAI.getRecorder().getLastCards();

		/*
		 * Two choices:
		 * 
		 * 1. Give reaction cards.
		 * 
		 * 2. Choose to pass.
		 */
		if (!deskCards.isEmpty()) {
			for (int i = 0; i < originSeeds.size(); i++) {
				seed = originSeeds.get(i);
				actions = createConstraintActions(seed, deskCards);
				if (actions != null) {
					actionsList.add(actions);
				}
			}
		} else {
			for (int i = 0; i < originSeeds.size(); i++) {
				seed = originSeeds.get(i);
				actions = createFreeActions(seed);
				actionsList.add(actions);
			}
		}

	}

	/**
	 * Create an action with constraint. the first element of the action shall
	 * deal with the current game environment: The action shall start with an
	 * action to win the deskCards.
	 * 
	 * @param seed
	 *            The seed.
	 * @param deskCards
	 *            The deskCards.
	 * @return A sequence of actions.
	 */
	private Actions createConstraintActions(Seed seed, Cards deskCards) {
		Summary summary = new Summary(seed);

		ArrayList<Cards> candidates = new ArrayList<Cards>();

		for (int i = 0; i < seed.size(); i++) {
			if (seed.get(i).isGreaterThan(deskCards)) {
				candidates.add(seed.get(i));
			}
		}

		if (candidates.isEmpty()) {
			return null;
		}

		ArrayList<Cards> sequence = new ArrayList<Cards>();

		/*
		 * Get the summarized cards information.
		 */

		ArrayList<Cards> sCards = summary.getSingleCards();
		ArrayList<Cards> pCards = summary.getPairCards();
		ArrayList<Cards> tCards = summary.getTripleCards();
		ArrayList<Cards> tpCards = summary.getTPCards();
		ArrayList<Cards> mpCards = summary.getMPairCards();
		ArrayList<Cards> mtCards = summary.getMTripleCards();
		ArrayList<Cards> fCards = summary.getFlushCards();
		ArrayList<Cards> bCards = summary.getBombCards();

		/*
		 * Regular types: s, p, t, tp, mp, pt, f.
		 */
		ArrayList<Dominator> sDominators = createDominators(sCards);
		ArrayList<Dominator> pDominators = createDominators(pCards);
		ArrayList<Dominator> tDominators = createDominators(tCards);
		ArrayList<Dominator> tpDominators = createDominators(tpCards);
		ArrayList<Dominator> mpDominators = createDominators(mpCards);
		ArrayList<Dominator> mtDominators = createDominators(mtCards);
		ArrayList<Dominator> fDominators = createDominators(fCards);
		ArrayList<Dominator> bDominators = createDominators(bCards);

		printList(sDominators);
		printList(pDominators);
		printList(tDominators);
		printList(tpDominators);
		printList(mpDominators);
		printList(mtDominators);
		printList(fDominators);
		printList(bDominators);

		/*
		 * save the value to the actions.
		 */
		double value = 0;
		value += calcRisk(sDominators);
		value += calcRisk(pDominators);
		value += calcRisk(tDominators);
		value += calcRisk(tpDominators);
		value += calcRisk(mpDominators);
		value += calcRisk(mtDominators);
		value += calcRisk(fDominators);
		value += calcRisk(bDominators);

		ArrayList<Dominator> dominators = createDominators(candidates);
		Collections.sort(dominators, dominatorComparator);
		for (int i = 0; i < dominators.size(); i++) {
			sequence.add(dominators.get(i).cards);
		}

		Actions actions = new Actions(summary, sequence);
		actions.setValue(value);
		actions.setStrategy(Actions.REACT);

		return actions;
	}

	private double calcRisk(ArrayList<Dominator> dominators) {
		Collections.sort(dominators, dominatorComparator);

		double value = 0;

		for (int i = 0; i < dominators.size() / 2; i++) {
			value += dominators.get(i).risk
					* dominators.get(dominators.size() - i - 1).risk;
		}

		if (dominators.size() % 2 == 1) {
			value += dominators.get(dominators.size() / 2).risk;
		}

		return value;
	}

	/**
	 * Freely create sequence of actions.
	 * 
	 * @param seed
	 *            The seed.
	 * @return A sequence of actions.
	 */
	private Actions createFreeActions(Seed seed) {
		Summary summary = new Summary(seed);

		ArrayList<Cards> sequence = new ArrayList<Cards>();

		/*
		 * Get the summarized cards information.
		 */

		ArrayList<Cards> sCards = summary.getSingleCards();
		ArrayList<Cards> pCards = summary.getPairCards();
		ArrayList<Cards> tCards = summary.getTripleCards();
		ArrayList<Cards> tpCards = summary.getTPCards();
		ArrayList<Cards> mpCards = summary.getMPairCards();
		ArrayList<Cards> mtCards = summary.getMTripleCards();
		ArrayList<Cards> fCards = summary.getFlushCards();
		ArrayList<Cards> bCards = summary.getBombCards();

		/*
		 * Regular types: s, p, t, tp, mp, pt, f.
		 */
		ArrayList<Dominator> sDominators = createDominators(sCards);
		ArrayList<Dominator> pDominators = createDominators(pCards);
		ArrayList<Dominator> tDominators = createDominators(tCards);
		ArrayList<Dominator> tpDominators = createDominators(tpCards);
		ArrayList<Dominator> mpDominators = createDominators(mpCards);
		ArrayList<Dominator> mtDominators = createDominators(mtCards);
		ArrayList<Dominator> fDominators = createDominators(fCards);
		ArrayList<Dominator> bDominators = createDominators(bCards);

		printList(sDominators);
		printList(pDominators);
		printList(tDominators);
		printList(tpDominators);
		printList(mpDominators);
		printList(mtDominators);
		printList(fDominators);
		printList(bDominators);

		ArrayList<Dominator> dominators = new ArrayList<Dominator>();
		dominators.addAll(sDominators);
		dominators.addAll(pDominators);
		dominators.addAll(tDominators);
		dominators.addAll(tpDominators);
		dominators.addAll(mpDominators);
		dominators.addAll(mtDominators);
		dominators.addAll(fDominators);
		dominators.addAll(bDominators);

		/*
		 * save the value to the actions.
		 */
		double value = 0;
		value += calcRisk(sDominators);
		value += calcRisk(pDominators);
		value += calcRisk(tDominators);
		value += calcRisk(tpDominators);
		value += calcRisk(mpDominators);
		value += calcRisk(mtDominators);
		value += calcRisk(fDominators);
		value += calcRisk(bDominators);

		Collections.sort(dominators, dominatorComparator);
		for (int i = 0; i < dominators.size(); i++) {
			sequence.add(dominators.get(i).cards);
		}

		Cards p1All = gameAI.getEnvironment().getP1All();
		Cards p2Desk = gameAI.getEnvironment().getP2Desk();

		int numOfPlayer2 = p1All.getSize() - p2Desk.getSize();

		if (numOfPlayer2 == 1 || dominators.size() == 2) {
			Collections.reverse(sequence);
		}

		Actions actions = new Actions(summary, sequence);
		actions.setValue(value);
		actions.setStrategy(Actions.FREE);
		return actions;
	}

	/**
	 * For debug purpose: print the list.
	 * 
	 * @param list
	 *            The list to be printed.
	 */
	private void printList(ArrayList<?> list) {
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}

	/**
	 * Create the dominators for the current cards.
	 * 
	 * @param cardsArray
	 *            The cards.
	 * @return the a list of dominator fur current cards.
	 */
	private ArrayList<Dominator> createDominators(ArrayList<Cards> cardsArray) {
		ArrayList<Dominator> ds = new ArrayList<Dominator>();

		for (int i = 0; i < cardsArray.size(); i++) {
			ds.add(new Dominator(cardsArray.get(i)));
		}

		return ds;
	}

	/**
	 * Get the action list.
	 * 
	 * @return A list of actions.
	 */
	public ArrayList<Actions> getActionsList() {
		return actionsList;
	}

	/**
	 * Get the current desk cards.
	 * 
	 * @return the current desk cards.
	 */
	public Cards getDeskCards() {
		Recorder recorder = gameAI.getRecorder();
		return recorder.getCardHistory().get(
				recorder.getCardHistory().size() - 1);
	}

	/**
	 * A helper class to create the value for cards in current game environment.
	 * 
	 * @author Riki
	 * 
	 */
	private class Dominator {
		Cards cards;
		/*
		 * The lower, the better cards.
		 */
		double risk;
		GameEnvironment env;
		Cards p1All, p2Desk, unkown;
		List<Rank> listA;
		int lengthOfB;
		ProbabilityCalculator probCalc;

		/**
		 * Constructor.
		 * 
		 * @param cs
		 *            The cards to be analyzed.
		 */
		public Dominator(Cards cs) {
			probCalc = gameAI.getProbCalc();
			env = gameAI.getEnvironment();
			p1All = env.getP1All();
			p2Desk = env.getP2Desk();
			unkown = env.getUnknownCards();
			listA = getRanks(unkown);
			lengthOfB = p1All.getSize() - p2Desk.getSize();
			risk = 0;
			this.cards = cs;
			initRisk();
		}

		/**
		 * For debug purpose.
		 */
		public String toString() {
			return "Dominator:\t chance:_" + risk + " Cards: " + cards;
		}

		/**
		 * Calculate the risk for given cards by doing case analysis.
		 */
		void initRisk() {
			switch (cards.getCardsType()) {
			case SINGLE:
				initS(); // case study for Single
				break;
			case PAIR:
				initP(); // case study for Pair
				break;
			case TRIPLE:
				initT(); // case study for Triple
				break;
			case TRIPLE_WITH_PAIR:
				initTP(); // case study for Triple with Pair
				break;
			case MULTIPAIR:
				initMP(); // case study for MultiPair
				break;
			case MULTITRIPLE:
				initMT(); // case study for MultiTriple
				break;
			case FLUSH:
				initF(); // case study for Flush (Straight)
				break;
			case BOMB:
				initB(); // case study for Bomb
				break;
			default:
				break;
			}

		}

		/**
		 * initialize the value for cards: Bomb
		 */
		private void initB() {
			List<Rank> listS = new ArrayList<Rank>();

			Rank HigherRank = getHigherRank(cards.getCard(0).getRank());

			while (HigherRank != null) {
				// 4
				listS.add(HigherRank);
				listS.add(HigherRank);
				listS.add(HigherRank);
				listS.add(HigherRank);
				risk += probCalc.calc(listA, lengthOfB, listS);
				listS.clear();
				HigherRank = getHigherRank(HigherRank);
			}
		}

		/**
		 * initialize the value for cards: Flush
		 */
		private void initF() {
			if (lengthOfB < cards.getSize()) {
				risk = 0;
			} else {
				// simple calculation
				risk = 1 - (cards.getCardsValue() / Rank.TEN.getValue());
			}

		}

		/**
		 * initialize the value for cards: Multi-Triple
		 */
		private void initMT() {
			List<Rank> listS = new ArrayList<Rank>();

			int numCount = cards.getSize() / 3;

			List<Rank> ranks = new ArrayList<Rank>();
			for (int i = 0; i < numCount; i++) {
				ranks.add(cards.getCard(i * 3).getRank());
			}

			List<Rank> higherRanks = new ArrayList<Rank>();
			for (int i = 0; i < ranks.size(); i++) {
				higherRanks.add(ranks.get(i));
			}

			// simplified...
			while (containsNull(higherRanks)) {
				// 3 3 ... 3
				for (int i = 0; i < higherRanks.size(); i++) {
					listS.add(higherRanks.get(i));
					listS.add(higherRanks.get(i));
					listS.add(higherRanks.get(i));
				}
				risk += probCalc.calc(listA, lengthOfB, listS);
				ranks = new ArrayList<Rank>();
				for (int i = 0; i < higherRanks.size(); i++) {
					ranks.add(getHigherRank(higherRanks.get(i)));
				}
				higherRanks = ranks;
			}
			risk /= numCount;
		}

		/**
		 * initialize the value for cards: multi-pair
		 */
		private void initMP() {
			List<Rank> listS = new ArrayList<Rank>();

			int numCount = cards.getSize() / 2;

			List<Rank> ranks = new ArrayList<Rank>();
			for (int i = 0; i < numCount; i++) {
				ranks.add(cards.getCard(i * 2).getRank());
			}

			List<Rank> higherRanks = new ArrayList<Rank>();
			for (int i = 0; i < ranks.size(); i++) {
				higherRanks.add(ranks.get(i));
			}

			// simplified...
			while (containsNull(higherRanks)) {
				// 2 2 ... 2
				for (int i = 0; i < higherRanks.size(); i++) {
					listS.add(higherRanks.get(i));
					listS.add(higherRanks.get(i));
				}
				risk += probCalc.calc(listA, lengthOfB, listS);

				ranks = new ArrayList<Rank>();
				for (int i = 0; i < higherRanks.size(); i++) {
					ranks.add(getHigherRank(higherRanks.get(i)));
				}
				higherRanks = ranks;
			}
			risk /= numCount;
		}

		/**
		 * Test if the list contains null values.
		 * 
		 * @param list
		 *            Tested list.
		 * @return True if the list contains null values, otherwise false.
		 */
		private boolean containsNull(List<?> list) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) == null) {
					return true;
				}
			}
			return false;
		}

		/**
		 * initialize the value for cards: Triple-with-pair.
		 */
		private void initTP() {
			List<Rank> listS = new ArrayList<Rank>();

			Rank highRank = null;

			if (cards.getCard(1).getRank() == cards.getCard(2).getRank()) {
				highRank = getHigherRank(cards.getCard(0).getRank());
			} else {
				highRank = getHigherRank(cards.getCard(3).getRank());
			}

			double prob = 0;
			while (highRank != null) {
				// 3
				listS.add(highRank);
				listS.add(highRank);
				listS.add(highRank);
				prob = probCalc.calc(listA, lengthOfB, listS);
				prob *= calcPair(lengthOfB - 3);
				risk += prob;

				// 4
				listS.add(highRank);
				prob = probCalc.calc(listA, lengthOfB, listS);
				prob *= calcPair(lengthOfB - 4);
				risk += prob;
				listS.clear();
				highRank = getHigherRank(highRank);
			}
		}

		/**
		 * Calculate the pair in given length
		 * 
		 * @param lengthOfB
		 *            The length.
		 * @return the value.
		 */
		private double calcPair(int lengthOfB) {
			List<Rank> listS = new ArrayList<Rank>();

			Rank HigherRank = getHigherRank(Rank.THREE);
			double prob = 0;
			while (HigherRank != null) {
				// 2
				listS.add(HigherRank);
				listS.add(HigherRank);
				prob += probCalc.calc(listA, lengthOfB, listS);
				// 3
				listS.add(HigherRank);
				prob += probCalc.calc(listA, lengthOfB, listS);
				// 4
				listS.add(HigherRank);
				prob += probCalc.calc(listA, lengthOfB, listS);
				listS.clear();
				HigherRank = getHigherRank(HigherRank);
			}
			return prob;
		}

		/**
		 * initialize the value for cards: Triple
		 */
		private void initT() {
			List<Rank> listS = new ArrayList<Rank>();

			Rank HigherRank = getHigherRank(cards.getCard(0).getRank());

			while (HigherRank != null) {
				// 3
				listS.add(HigherRank);
				listS.add(HigherRank);
				listS.add(HigherRank);
				risk += probCalc.calc(listA, lengthOfB, listS);
				// 4
				listS.add(HigherRank);
				risk += probCalc.calc(listA, lengthOfB, listS);
				listS.clear();
				HigherRank = getHigherRank(HigherRank);
			}

		}

		/**
		 * initialize the value for cards: Pair
		 */
		private void initP() {
			List<Rank> listS = new ArrayList<Rank>();

			Rank HigherRank = getHigherRank(cards.getCard(0).getRank());

			while (HigherRank != null) {
				// 2
				listS.add(HigherRank);
				listS.add(HigherRank);
				risk += probCalc.calc(listA, lengthOfB, listS);
				// 3
				listS.add(HigherRank);
				risk += probCalc.calc(listA, lengthOfB, listS);
				// 4
				listS.add(HigherRank);
				risk += probCalc.calc(listA, lengthOfB, listS);
				listS.clear();
				HigherRank = getHigherRank(HigherRank);
			}

		}

		/**
		 * initialize the value for cards: Single
		 */
		private void initS() {
			/*
			 * listA: Unknown Cards.
			 * 
			 * lengthOfB: Number of Cards in Opponent.
			 * 
			 * listS: To be calculated cards.
			 */

			List<Rank> listS = new ArrayList<Rank>();

			Rank HigherRank = getHigherRank(cards.getCard(0).getRank());

			while (HigherRank != null) {
				// 1
				listS.add(HigherRank);
				risk += probCalc.calc(listA, lengthOfB, listS);
				// 2
				listS.add(HigherRank);
				risk += probCalc.calc(listA, lengthOfB, listS);
				// 3
				listS.add(HigherRank);
				risk += probCalc.calc(listA, lengthOfB, listS);
				// 4
				listS.add(HigherRank);
				risk += probCalc.calc(listA, lengthOfB, listS);
				listS.clear();
				HigherRank = getHigherRank(HigherRank);
			}
		}

		/**
		 * Get the next higher rank than the given {@code r}.
		 * 
		 * @param r
		 *            the current rank
		 * @return a higher rank
		 */
		private Rank getHigherRank(Rank r) {
			switch (r) {
			case ACE:
				return Rank.DEUCE;
			case DEUCE:
				return null;
			case THREE:
				return Rank.FOUR;
			case FOUR:
				return Rank.FIVE;
			case FIVE:
				return Rank.SIX;
			case SIX:
				return Rank.SEVEN;
			case SEVEN:
				return Rank.EIGHT;
			case EIGHT:
				return Rank.NINE;
			case NINE:
				return Rank.TEN;
			case TEN:
				return Rank.JACK;
			case JACK:
				return Rank.QUEEN;
			case QUEEN:
				return Rank.KING;
			case KING:
				return Rank.ACE;
			}
			return null;
		}

		/**
		 * Turn cards into ranks.
		 * 
		 * @param cards
		 *            the cards.
		 * @return a list of ranks.
		 */
		private List<Rank> getRanks(Cards cards) {

			List<Rank> list = new ArrayList<Rank>(cards.getSize());

			for (int i = 0; i < cards.getSize(); i++) {
				list.add(cards.getCard(i).getRank());
			}

			return list;
		}
	}

	/**
	 * The Dominator Comparator will compare the value of dominator.
	 * 
	 * Will sort the dominators in descending order.
	 * 
	 * @author Riki
	 * 
	 */
	private class DominatorComparator implements Comparator<Dominator> {

		@Override
		public int compare(Dominator d1, Dominator d2) {
			if (d1.risk < d2.risk) {
				return 1;
			} else if (d1.risk > d2.risk) {
				return -1;
			}
			return 0;
		}

	}
}
