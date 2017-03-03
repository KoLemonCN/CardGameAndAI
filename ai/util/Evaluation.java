package ai.util;

/**
 * The Evaluation hold the information for the evaluated actions.
 * 
 * @author Riki
 * 
 */
public class Evaluation {

	/**
	 * The summary of the actions.
	 */
	private Summary summary;
	/**
	 * The evaluated actions.
	 */
	private Actions actions;

	/**
	 * The constructor of the Evaluation
	 * 
	 * @param as
	 *            The evaluated actions.
	 */
	public Evaluation(Actions as) {
		this.summary = as.getSummary();
		this.actions = as;
	}

	/**
	 * Get the Seed of the summary.
	 * 
	 * @return The seed of the evaluation.
	 */
	public Seed getSeed() {
		return summary.getSeed();
	}

	/**
	 * How many types exist in the seed.
	 * 
	 * @return The number of Cards Type.
	 */
	public int getCardsTypeCount() {
		return summary.getCardsTypeCount();
	}

	/**
	 * Get the evaluated actions.
	 * 
	 * @return The actions.
	 */
	public Actions getActions() {
		return actions;
	}

	/**
	 * Get the score of the evaluation.
	 * 
	 * @return The score.
	 */
	public double getScore() {
		return actions.getValue();
	}

	/**
	 * String representation of the evaluation.
	 */
	public String toString() {
		return "Evaluation: Score_" + getScore() + "\n actions_" + actions
				+ "\nsummary: " + summary;
	}
}
