package ai.comp;

import game.card.Cards;

import java.util.ArrayList;

import ai.GameAI;

/**
 * Main function of the recorder is to manage the game history and provide some
 * valid access of the game source.
 * 
 * @author Riki
 * 
 */
public class Recorder {

	// the game ai.
	private GameAI gameAI;

	/**
	 * The ID of player 1.
	 */
	public static final int PLAYER1_ID = 1;
	/**
	 * The ID of player 2.
	 */
	public static final int PLAYER2_ID = 2;

	/**
	 * Contents to hold game history.
	 */
	private ArrayList<Cards> cardHistory;
	private ArrayList<Integer> playerHistory;
	private ArrayList<Integer> roundHistory;
	private int roundCounter;

	/**
	 * The constructor for Recorder.
	 * 
	 * @param ai
	 *            The GameAI.
	 */
	public Recorder(GameAI ai) {
		gameAI = ai;
		init();
		gameAI.report("Recorder:->Initialization completed.");
	}

	/**
	 * Initialize the recorder.
	 */
	private void init() {
		cardHistory = new ArrayList<Cards>();
		playerHistory = new ArrayList<Integer>();
		roundHistory = new ArrayList<Integer>();
		roundCounter = 1;
	}

	/**
	 * Add a history to the recorder.
	 * 
	 * @param cards
	 *            The most recent cards.
	 * @param playerID
	 *            The player ID.
	 */
	public void addHistory(Cards cards, int playerID) {
		cardHistory.add(cards);
		playerHistory.add(playerID);
		roundHistory.add(roundCounter);
		if (cards.isEmpty()) {
			roundCounter++;
		}
	}

	/**
	 * Get the CardHistory
	 * 
	 * @return the CardHistory
	 */
	public ArrayList<Cards> getCardHistory() {
		return cardHistory;
	}

	/**
	 * Get the most recent cards displayed in the game.
	 * 
	 * @return the most recent cards displayed in the game.
	 */
	public Cards getLastCards() {
		if (cardHistory.isEmpty()) {
			return new Cards();
		}

		return cardHistory.get(cardHistory.size() - 1);
	}

	/**
	 * Get the player History.
	 * 
	 * @return The player history.
	 */
	public ArrayList<Integer> getPlayerHistory() {
		return playerHistory;
	}

	/**
	 * Get the round history.
	 * 
	 * @return the round history.
	 */
	public ArrayList<Integer> getRoundHistory() {
		return roundHistory;
	}

	/**
	 * The String representation of the recorder.
	 */
	public String toString() {
		String str = "";

		int round;

		Cards cards;
		int playerID;

		for (int i = 0; i < cardHistory.size(); i++) {
			cards = cardHistory.get(i);
			playerID = playerHistory.get(i);
			round = roundHistory.get(i);

			if (cards.isEmpty()) {
				str += "round_" + round + "\t Player_" + playerID
						+ " passed. \n\n";
			} else {
				str += "round_" + round + "\t Player_" + playerID + " :\t"
						+ cards + "\n";
			}

		}

		return str;
	}

}
