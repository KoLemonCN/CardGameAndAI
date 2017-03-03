package ai.util;

import game.card.Cards;

import java.util.ArrayList;

/**
 * This is type to hold a sequence of Cards. This represents the sequence, which
 * the AI will possibly choose cards in sequence.
 * 
 * @author Riki
 * 
 */
public class Actions {

	/**
	 * FREE choose strategy: the action is made freely.
	 */
	public static final int FREE = 1;
	/**
	 * WAIT strategy: The action will have no effect on the game.
	 */
	public static final int WAIT = 2;
	/**
	 * REACT strategy: the action will deal with the current situation.
	 */
	public static final int REACT = 3;

	/**
	 * The sequence of cards.
	 */
	private ArrayList<Cards> actions;
	private Summary summary;
	private double value;
	private int strategy;

	/**
	 * The constructor for the Actions.
	 * 
	 * @param summary
	 * @param actions
	 */
	public Actions(Summary summary, ArrayList<Cards> actions) {
		this.summary = summary;
		this.actions = actions;
		value = Integer.MAX_VALUE;
		strategy = 0;
	}

	/**
	 * Set the value of the actions.
	 * 
	 * @param v
	 *            The value
	 */
	public void setValue(double v) {
		this.value = v;
	}

	/**
	 * Set the strategy of the actions.
	 * 
	 * @param s
	 *            The strategy ID.
	 */
	public void setStrategy(int s) {
		this.strategy = s;
	}

	/**
	 * Get the strategy of this actions.
	 * 
	 * @return The strategy of this actions.
	 */
	public int getStrategy() {
		return strategy;
	}

	/**
	 * Get the value of the actions.
	 * 
	 * @return The value of the actions.
	 */
	public double getValue() {
		return value;
	}

	/**
	 * Get the seed of the actions.
	 * 
	 * @return The seed of the actions.
	 */
	public Seed getSeed() {
		return summary.getSeed();
	}

	/**
	 * Get the summary of the actions.
	 * 
	 * @return The summary of the actions.
	 */
	public Summary getSummary() {
		return summary;
	}

	/**
	 * Get the size of the actions. How many steps contained.
	 * 
	 * @return Size of the actions
	 */
	public int getSize() {
		return actions.size();
	}

	/**
	 * Get the cards specified by index.
	 * 
	 * @param i
	 *            The index of the actions.
	 * @return The i-th cards.
	 */
	public Cards getCards(int i) {
		return actions.get(i);
	}

	/**
	 * String representation of the Actions.
	 */
	public String toString() {
		String str = "Action " + getSize() + " {";

		for (int i = 0; i < actions.size(); i++) {
			str += "----" + i + " " + actions.get(i) + "\n";
		}

		return str;
	}

}
