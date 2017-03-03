package ai.util;

import game.card.Cards;

import java.util.ArrayList;

/**
 * Auto summary of a seed.
 * 
 * @author Riki
 * 
 */
public class Summary {

	/**
	 * The summarized seed.
	 */
	private Seed seed;

	/**
	 * The summarized contents.
	 */
	private ArrayList<Cards> sCards, pCards, tCards, mpCards, mtCards, tpCards,
			fCards, bCards;
	private int typeCounter;

	/**
	 * The constructor of the Summary.
	 * 
	 * @param s
	 *            The seed to be summarized.
	 */
	public Summary(Seed s) {
		this.seed = s;
		autoSummary();
	}

	/**
	 * Get the seed of the summary
	 * 
	 * @return the seed.
	 */
	public Seed getSeed() {
		return seed;
	}

	/**
	 * get the cards type counter.
	 * 
	 * @return the number of types in the seed.
	 */
	public int getCardsTypeCount() {
		return typeCounter;
	}

	/**
	 * Automatically summarize the contents of the seed.
	 */
	private void autoSummary() {

		sCards = new ArrayList<Cards>();
		pCards = new ArrayList<Cards>();
		tCards = new ArrayList<Cards>();
		mpCards = new ArrayList<Cards>();
		mtCards = new ArrayList<Cards>();
		tpCards = new ArrayList<Cards>();
		fCards = new ArrayList<Cards>();
		bCards = new ArrayList<Cards>();

		Cards tempCards;
		for (int i = 0; i < seed.size(); i++) {
			tempCards = seed.get(i);
			// get the type, and store in corresponding list.
			switch (tempCards.getCardsType()) {
			case SINGLE:
				sCards.add(tempCards);
				break;
			case PAIR:
				pCards.add(tempCards);
				break;
			case TRIPLE:
				tCards.add(tempCards);
				break;
			case MULTIPAIR:
				mpCards.add(tempCards);
				break;
			case MULTITRIPLE:
				mtCards.add(tempCards);
				break;
			case TRIPLE_WITH_PAIR:
				tpCards.add(tempCards);
				break;
			case FLUSH:
				fCards.add(tempCards);
				break;
			case BOMB:
				bCards.add(tempCards);
				break;
			default:
				System.err.println("INVALID CARDS IN EVALUATION: " + tempCards);
			}
		}

		typeCounter = 0;
		// count for each type.
		if (!sCards.isEmpty()) {
			typeCounter++;
		}
		if (!pCards.isEmpty()) {
			typeCounter++;
		}
		if (!tCards.isEmpty()) {
			typeCounter++;
		}
		if (!mpCards.isEmpty()) {
			typeCounter++;
		}
		if (!mtCards.isEmpty()) {
			typeCounter++;
		}
		if (!tpCards.isEmpty()) {
			typeCounter++;
		}
		if (!fCards.isEmpty()) {
			typeCounter++;
		}
		if (!bCards.isEmpty()) {
			typeCounter++;
		}
	}

	/**
	 * @return the SingleCards
	 */
	public ArrayList<Cards> getSingleCards() {
		return sCards;
	}

	/**
	 * @return the PairCards
	 */
	public ArrayList<Cards> getPairCards() {
		return pCards;
	}

	/**
	 * @return the TripleCards
	 */
	public ArrayList<Cards> getTripleCards() {
		return tCards;
	}

	/**
	 * @return the MultiPairCards
	 */
	public ArrayList<Cards> getMPairCards() {
		return mpCards;
	}

	/**
	 * @return the getMultiTripleCards
	 */
	public ArrayList<Cards> getMTripleCards() {
		return mtCards;
	}

	/**
	 * @return the Triple with Pair Cards
	 */
	public ArrayList<Cards> getTPCards() {
		return tpCards;
	}

	/**
	 * @return the FlushCards
	 */
	public ArrayList<Cards> getFlushCards() {
		return fCards;
	}

	/**
	 * @return the BombCards
	 */
	public ArrayList<Cards> getBombCards() {
		return bCards;
	}

	/**
	 * The string representation of the summary.
	 */
	public String toString() {
		String str = "";
		str += "---\t\t---Single : " + cardsArray2String(sCards) + "\n ";
		str += "---\t\t---Pair   : " + cardsArray2String(pCards) + "\n ";
		str += "---\t\t---MPair  : " + cardsArray2String(mpCards) + "\n ";
		str += "---\t\t---MTriple: " + cardsArray2String(mtCards) + "\n ";
		str += "---\t\t---TP     : " + cardsArray2String(tpCards) + "\n ";
		str += "---\t\t---Flush  : " + cardsArray2String(fCards) + "\n ";
		str += "---\t\t---Bomb   : " + cardsArray2String(bCards) + "\n ";
		return str;
	}

	/**
	 * For Debug purpose. Turn a list of cards into string.
	 * 
	 * @param cardsArray
	 *            The cards List to be transformed.
	 * @return The string representation of the list of cards.
	 */
	private String cardsArray2String(ArrayList<Cards> cardsArray) {
		String str = cardsArray.size() + " < ";

		for (int i = 0; i < cardsArray.size(); i++) {
			str += cards2String(cardsArray.get(i)) + ";";
		}

		str += " >";
		return str;
	}

	/**
	 * The String representation of card.
	 * 
	 * @param cards
	 *            The cards to be represented.
	 * @return The String representation of card.
	 */
	private String cards2String(Cards cards) {
		String str = "[";
		for (int i = 0; i < cards.getSize(); i++) {
			str += cards.getCard(i).getRank() + " ";
		}
		str += "]";
		return str;
	}
}
