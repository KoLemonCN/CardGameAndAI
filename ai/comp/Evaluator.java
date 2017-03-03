package ai.comp;

import java.util.ArrayList;

import ai.GameAI;
import ai.util.Actions;
import ai.util.Evaluation;
import ai.util.Seed;

/**
 * Main function description:
 * 
 * The evaluator will take a list of seeds and evaluate them to generate a list
 * of evaluation.
 * 
 * @author Riki
 * 
 */
public class Evaluator {

	/**
	 * The game ai.
	 */
	private GameAI gameAI;

	/**
	 * a list of evaluations.
	 */
	private ArrayList<Evaluation> evaluations;

	/**
	 * a list of seeds to be evaluated.
	 */
	private ArrayList<Seed> seeds;

	/**
	 * The game AI.
	 * 
	 * @param gameAI
	 */
	public Evaluator(GameAI gameAI) {
		this.gameAI = gameAI;
		evaluations = new ArrayList<Evaluation>();
	}

	/**
	 * Evaluate a list of seeds to generate a list of evaluations.
	 * 
	 * @param sds
	 *            The seeds to be evaluated.
	 * @return A list of evaluations.
	 */
	public ArrayList<Evaluation> evaluate(ArrayList<Seed> sds) {
		this.seeds = sds;
		evaluations = new ArrayList<Evaluation>();

		/*
		 * Summary -> [Actions] Actions -> Evaluation
		 */
		Analyzer analyser = gameAI.getAnalyzer();
		ArrayList<Actions> actionsList = analyser.createActionsList(seeds);

		Evaluation evaluation;
		for (int i = 0; i < actionsList.size(); i++) {
			evaluation = new Evaluation(actionsList.get(i));
			evaluations.add(evaluation);
		}

		return evaluations;
	}

	/**
	 * Get the list of evaluations.
	 * 
	 * @return the list of evaluations.
	 */
	public ArrayList<Evaluation> getEvaluations() {
		return evaluations;
	}

	/**
	 * The string representation of the evaluator.
	 */
	public String toString() {
		String str = "Evaluator { \n";

		Evaluation evaluation;
		for (int i = 0; i < evaluations.size(); i++) {
			evaluation = evaluations.get(i);
			str += "---\t---{" + i + "}\n ";
			str += "---\t---" + evaluation.getSeed().getCards() + "{\n ";
			str += evaluation.toString();
			str += "---\t---}\n";
		}

		str += "Evaluations Completed}\n ";
		return str;
	}

}
