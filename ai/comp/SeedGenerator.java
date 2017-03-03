package ai.comp;

import game.card.Cards;
import game.card.CardsType;
import game.card.Rank;

import java.util.ArrayList;

import ai.GameAI;
import ai.util.CardSet;
import ai.util.Seed;

/**
 * The Seed Generator will generate all possible combinations of cards for a
 * given cards.
 * 
 * @author Riki
 * 
 */
public class SeedGenerator {

	private CardSet originSet;
	private Seed originSeed;
	private ArrayList<Seed> seeds;
	private Cards cards;
	private GameAI gameAI;

	/**
	 * The constructor of the SeedGenerator.
	 * 
	 * @param ai
	 *            The gameAI.
	 */
	public SeedGenerator(GameAI ai) {
		this.gameAI = ai;
		gameAI.report("SeedGenerator:->Initialization completed.");
	}

	/**
	 * Generate seeds from the given cards.
	 * 
	 * This function will automatically generate all possible cards combinations
	 * for the given set of cards. The basic procedure is: by following the AST
	 * of CardsType, for each different type, do accordingly to transform and
	 * reorganize the combinations of cards.
	 * 
	 * @param cs
	 *            the cards source.
	 * @return A list of seeds.
	 */
	public ArrayList<Seed> generateSeeds(Cards cs) {
		// initialize the cards
		init(cs);
		// generate the origin seed from the cards.
		// the origin seed is a set of cards, with all members of type Single.
		generateOriginSeed();

		// use the origin seed, mutate the seed to populate new generation.
		populateSeeds();

		return seeds;
	}

	/**
	 * Initialize the origin set.
	 * 
	 * @param cs
	 *            The cards to be initialized.
	 */
	private void init(Cards cs) {
		originSet = new CardSet(cs);
		this.cards = cs;
		seeds = new ArrayList<Seed>();
		originSeed = new Seed();
	}

	/**
	 * Generate origin seed. The origin Seed is a special seed, all its member
	 * are of type Single.
	 */
	private void generateOriginSeed() {
		originSeed = new Seed();
		Cards cs;
		for (int i = 0; i < cards.getSize(); i++) {
			cs = new Cards();
			cs.addCard(cards.getCard(i));
			originSeed.add(cs);
		}
	}

	/**
	 * Populate the seeds from the originSeed.
	 */
	private void populateSeeds() {
		ArrayList<Seed> currentSeeds = new ArrayList<Seed>();
		// the seed shall be sorted for efficiency.
		originSeed.sort();
		// add the initial seed to the seeds.
		currentSeeds.add(originSeed);

		Seed currentSeed;
		ArrayList<Seed> newSeeds;
		while (!currentSeeds.isEmpty()) {
			// remove the first seed from the current seeds.
			currentSeed = currentSeeds.remove(0);

			// populate the current seed.
			newSeeds = populateNewSeeds(currentSeed);

			// update seeds.
			updateSeeds(currentSeeds, newSeeds, currentSeed);

		}
	}

	/**
	 * Assume all seeds are sorted. Update the newly generated seeds and the
	 * origin seed to current seeds.
	 * 
	 * @param currentSeeds
	 *            The current seeds.
	 * @param newSeeds
	 *            The newly generated seed.
	 * @param cSeed
	 *            The origin seed that generatest the new seeds.
	 */
	private void updateSeeds(ArrayList<Seed> currentSeeds,
			ArrayList<Seed> newSeeds, Seed cSeed) {
		/*
		 * keep the internal seeds.
		 */
		updateNewSeed(cSeed, seeds);
		// update the newSeeds to current seeds & to seeds.
		for (int i = 0; i < newSeeds.size(); i++) {
			updateNewSeed(newSeeds.get(i), currentSeeds);
			updateNewSeed(newSeeds.get(i), seeds);
		}
	}

	/**
	 * Populate the new seeds from the given current seed.
	 * 
	 * @param currentSeed
	 *            the current seed.
	 * @return a list of seeds generated from the current seed.
	 */
	private ArrayList<Seed> populateNewSeeds(Seed currentSeed) {
		ArrayList<Seed> newSeeds = new ArrayList<Seed>();

		/*
		 * Start generating new seeds from the current seed.
		 */

		/**
		 * Procedure: Generating newSeed
		 * 
		 * for each [currentCards] in [currentSeed] do {
		 * 
		 * ---- [newSeed] <- {promote} [currentCards]
		 * 
		 * ---- {update} the [newSeed] to [newSeeds]
		 * 
		 * }
		 * 
		 */
		ArrayList<Seed> promotedSeeds;
		for (int i = 0; i < currentSeed.size(); i++) {
			promotedSeeds = promote(currentSeed, i);

			for (int k = 0; k < promotedSeeds.size(); k++) {
				updateNewSeed(promotedSeeds.get(k), newSeeds);
			}

		}

		return newSeeds;
	}

	/**
	 * Update the newly generated new seed to current seeds.
	 * 
	 * @param newSeed
	 *            The newly generated seed.
	 * @param currentSeeds
	 *            All current seeds.
	 */
	private void updateNewSeed(Seed newSeed, ArrayList<Seed> currentSeeds) {
		Seed currentSeed;
		for (int i = 0; i < currentSeeds.size(); i++) {
			currentSeed = currentSeeds.get(i);
			if (newSeed.equals(currentSeed)) {
				return;
			}
		}

		currentSeeds.add(newSeed);
	}

	/**
	 * Populate seeds:
	 * 
	 * up-scaling and down-scaling.
	 * 
	 * Up Down
	 * 
	 * S S
	 * 
	 * P ¡¾S,S¡¿;
	 * 
	 * T ¡¾P,S¡¿;
	 * 
	 * MP ¡¾MP,?P¡¿;¡¾P,P¡¿;
	 * 
	 * MT ¡¾MT,?T¡¿;¡¾T,T¡¿;
	 * 
	 * TP ¡¾T,P¡¿;
	 * 
	 * F ¡¾F,S¡¿;
	 * 
	 * B ¡¾T,S¡¿;¡¾P,P¡¿;B
	 * 
	 * 
	 * @param seed
	 *            The seed to be promoted. (Down -> Up)
	 * @param index
	 *            the current index, updating from index.
	 * @return A list of Seed generated by seed from index.
	 */
	private ArrayList<Seed> promote(Seed seed, int index) {

		// update from the index i.
		/**
		 * as the seed is sorted. the sequence shall be:
		 * 
		 * SINGLE, [+s=p] [+p=t] [+p=b (AAA)] [+t=b] [+s+s+s+s=f] [+f=f]
		 * 
		 * PAIR, [+p=mp] [+mp=mp] [+t=tp] [+p=b]
		 * 
		 * TRIPLE, [+t=mt] [+mt=mt]
		 * 
		 * BOMB, []
		 * 
		 * MULTIPAIR, []
		 * 
		 * MULTITRIPLE, []
		 * 
		 * TRIPLE_WITH_PAIR, []
		 * 
		 * FLUSH, []
		 * 
		 * ERR, []
		 */

		ArrayList<Seed> newSeeds = new ArrayList<Seed>();
		// if is the last item, no promotion available.
		if (index + 1 >= seed.size()) {
			return newSeeds;
		}

		Cards currentCards = seed.get(index);
		Seed newSeed = null;
		Cards cs = null;

		switch (currentCards.getCardsType()) {
		case SINGLE:
			/*
			 * SINGLE, [+s=p] [+p=t] [+p=b (AAA)] [+t=b] [+s+s+s+s=f] [+f=f]
			 */

			// [+s=p] : as sorted, the next card is the only possibility for
			// this single card to promote to a pair.
			if (seed.get(index + 1).getCardsType() == CardsType.SINGLE) {
				// create a [newSeed] and store in [newSeeds]
				newSeed = mutateSeed(seed, new int[] { index, index + 1 });

				if (newSeed != null) {
					newSeeds.add(newSeed);
				}
			}

			{
				// [+s+s+s+s=f]
				int i1 = indexOfConsecutiveSingle(seed, index);
				int i2 = indexOfConsecutiveSingle(seed, i1);
				int i3 = indexOfConsecutiveSingle(seed, i2);
				int i4 = indexOfConsecutiveSingle(seed, i3);

				if (i1 != 0 && i2 != 0 && i3 != 0 && i4 != 0) {
					newSeed = mutateSeed(seed, new int[] { index, i1, i2, i3,
							i4 });
					if (newSeed != null) {
						newSeeds.add(newSeed);
					}
				}
			}

			for (int i = index + 1; i < seed.size(); i++) {
				newSeed = null;
				cs = seed.get(i);

				if (cs.getCardsType() == CardsType.PAIR
						&& cs.getCardsValue() == currentCards.getCardsValue()) {
					// [+p=t] [+p=b (AAA)]
					// create a [newSeed] and store in [newSeeds]
					newSeed = mutateSeed(seed, new int[] { index, i });

					if (newSeed != null) {
						newSeeds.add(newSeed);
					}
				} else if (cs.getCardsType() == CardsType.TRIPLE
						&& cs.getCardsValue() == currentCards.getCardsValue()) {
					// [+t=b]

					// create a [newSeed] and store in [newSeeds]
					newSeed = mutateSeed(seed, new int[] { index, i });

					if (newSeed != null) {
						newSeeds.add(newSeed);
					}
				} else if (cs.getCardsType() == CardsType.FLUSH) {
					// [+f=f]

					// create a [newSeed] and store in [newSeeds]
					newSeed = mutateSeed(seed, new int[] { index, i });

					if (newSeed != null) {
						newSeeds.add(newSeed);
					}
				}
			}

			break;
		case PAIR:
			/*
			 * PAIR, [+p=mp] [+p=b] [+mp=mp] [+t=tp]
			 */

			for (int i = index + 1; i < seed.size(); i++) {
				newSeed = null;
				cs = seed.get(i);

				if (cs.getCardsType() == CardsType.PAIR
						|| cs.getCardsType() == CardsType.TRIPLE
						|| cs.getCardsType() == CardsType.MULTIPAIR) {
					// create a [newSeed] and store in [newSeeds]
					newSeed = mutateSeed(seed, new int[] { index, i });

					if (newSeed != null) {
						newSeeds.add(newSeed);
					}
				}
			}

			break;
		case TRIPLE:
			/*
			 * TRIPLE, [+t=mt] [+mt=mt]
			 */
			for (int i = index + 1; i < seed.size(); i++) {
				newSeed = null;
				cs = seed.get(i);

				if (cs.getCardsType() == CardsType.TRIPLE
						|| cs.getCardsType() == CardsType.MULTITRIPLE) {
					// create a [newSeed] and store in [newSeeds]
					newSeed = mutateSeed(seed, new int[] { index, i });

					if (newSeed != null) {
						newSeeds.add(newSeed);
					}
				}
			}
			break;
		default:
			// do nothing
			break;
		}

		// seeds shall be sorted after generation.
		for (int i = 0; i < newSeeds.size(); i++) {
			newSeeds.get(i).sort();
		}

		return newSeeds;
	}

	/**
	 * Used to get the next index of the a single card, which is consecutive to
	 * current single card at index.
	 * 
	 * @param seed
	 *            The seed.
	 * @param index
	 *            The current single card index.
	 * @return the index of next single card, that is consecutive to current
	 *         single card at index.
	 */
	private int indexOfConsecutiveSingle(Seed seed, int index) {
		// the current rank is index, get the next rank index. 0 if not found.
		if (seed.get(index).getCardsType() != CardsType.SINGLE) {
			return 0;
		}

		Rank currentRank = seed.get(index).getCard(0).getRank();
		Rank nextRank;
		for (int i = index + 1; i < seed.size(); i++) {
			if (seed.get(i).getCardsType() == CardsType.SINGLE) {
				nextRank = seed.get(i).getCard(0).getRank();

				if (nextRank == currentRank) {
					continue;
				}

				// not in the same rank, the last chance to be consecutive.
				if (Rank.isConsecutive(currentRank, nextRank)) {
					return i;
				}
				break;
			} else {
				break;
			}
		}

		return 0;
	}

	/**
	 * Mutate the seed by the given indices. This method will take a seed and an
	 * array of indices.
	 * 
	 * This method would try to create new set of cards specified by the
	 * indices. If the cards created is has a valid type, A new Seed would be
	 * returned; otherwise null.
	 * 
	 * @param seed
	 *            The seed to be mutated.
	 * @param is
	 *            The indices: the cards chosen to be mutated.
	 * @return A mutated seed.
	 */
	private Seed mutateSeed(Seed seed, int[] is) {

		Cards cards = new Cards();
		// try to create new cards combination declared in [is].
		for (int i = 0; i < is.length; i++) {
			cards = mergeToNewCards(cards, seed.get(is[i]));
		}

		// if not valid combination, return null
		if (cards.getCardsType() == CardsType.ERR) {
			return null;
		}

		// if valid, return this mutated seed.
		Seed newSeed = new Seed();

		for (int i = 0; i < seed.size(); i++) {
			if (!contains(is, i)) {
				newSeed.add(seed.get(i));
			}
		}
		newSeed.add(cards);

		return newSeed;
	}

	/**
	 * This function is used to decide whether the array contains a specific
	 * number.
	 * 
	 * @param array
	 *            An integer array.
	 * @param num
	 *            A number.
	 * @return True if the array contains that number, otherwise false.
	 */
	private boolean contains(int[] array, int num) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == num) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Connect two cards, and then sort.
	 * 
	 * @param cards1
	 *            Cards 1
	 * @param cards2
	 *            Cards 2
	 * @return A new Cards that contains Cards1 and Cards2.
	 */
	private Cards mergeToNewCards(Cards cards1, Cards cards2) {
		Cards newCards = new Cards();

		for (int i = 0; i < cards1.getSize(); i++) {
			newCards.addCard(cards1.getCard(i));
		}
		for (int i = 0; i < cards2.getSize(); i++) {
			newCards.addCard(cards2.getCard(i));
		}
		// sort for display.
		newCards.sort();

		return newCards;
	}

	/**
	 * The string representation of the SeedGenerator.
	 */
	public String toString() {
		String str = "SeedGenerator{ \n";

		str += " cards " + cards + "\n";
		str += " originSet " + originSet + "\n";
		str += " originSeed " + originSeed + "\n";

		str += " all seeds :\n";
		for (int i = 0; i < seeds.size(); i++) {
			str += " -----------seeds_" + i + " \t" + seeds.get(i) + "\n";
		}
		str += " ----------- seeds list finished. ";
		str += "\n}";
		return str;
	}
}
