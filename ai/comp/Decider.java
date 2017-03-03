package ai.comp;

import game.card.Cards;

import java.util.ArrayList;

import ai.GameAI;
import ai.util.Evaluation;

/**
 * The decider will take a list of evaluations and choose an action as a result
 * for the AI to react to current game environment.
 * 
 * @author Riki
 * 
 */
public class Decider {
	/**
	 * The game ai.
	 */
	private GameAI gameAI;
	/**
	 * The debug flag.
	 */
	public final boolean DEBUG = true;

	/**
	 * The constructor of the Decider.
	 * 
	 * @param gameAI
	 *            The game ai.
	 */
	public Decider(GameAI gameAI) {
		this.gameAI = gameAI;
	}

	/**
	 * Choose cards for action.
	 * 
	 * @param evaluations
	 *            Candidates cards.
	 * @param analyser
	 *            Use rules to reason about the situation.
	 * @param probCalc
	 *            Use mathematics to reason about the situation.
	 * @return
	 */
	public Cards chooseCards(ArrayList<Evaluation> evaluations) {
		report("Decider{");
		Analyzer analyser = gameAI.getAnalyzer();
		Cards deskCards = analyser.getDeskCards();
		report("Desk cards: " + deskCards);

		Cards cards = new Cards();
		/*
		 * best score means the lowest value
		 */
		double bestScore = Integer.MAX_VALUE;
		ArrayList<Evaluation> candidates = new ArrayList<Evaluation>();

		for (int i = 0; i < evaluations.size(); i++) {
			if (bestScore > evaluations.get(i).getScore()) {
				candidates.clear();
				candidates.add(evaluations.get(i));
			} else {
				candidates.add(evaluations.get(i));
			}
		}

		if (candidates.isEmpty()) {
			return cards;
		}

		for (int i = 0; i < candidates.size(); i++) {
			report("------candidates: " + i + "\t" + candidates.get(i));
		}
		
		Evaluation evaluation = candidates.get((int) (candidates.size() * Math
				.random()));
		cards = evaluation.getActions().getCards(0);


		report("------Final decision: \t" + cards);

		report("} Decider completed ");

		return cards;

	}

	/**
	 * for debug purpose.
	 * 
	 * @param content
	 *            The report content.
	 */
	private void report(String content) {
		if (DEBUG) {
			gameAI.report("Decider:->\t" + content);
		}
	}

}
