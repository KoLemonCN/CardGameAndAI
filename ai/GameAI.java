package ai;

import game.card.Cards;
import game.sprite.SpriteManager;
import game.util.GameEnvironment;

import java.util.ArrayList;

import ai.comp.Analyzer;
import ai.comp.Decider;
import ai.comp.Evaluator;
import ai.comp.Recorder;
import ai.comp.SeedGenerator;
import ai.util.Evaluation;
import ai.util.ProbabilityCalculator;
import ai.util.Seed;

/**
 * The AI for the game. This AI will automatically generate an action for the
 * Opponent.
 * 
 * @author Riki
 * 
 */
public class GameAI {

	/**
	 * The components and utilities.
	 */
	private SpriteManager manager;
	private ProbabilityCalculator probCalc;
	private SeedGenerator seedGenerator;
	private Recorder recorder;
	private Analyzer analyzer;
	private Decider decider;
	private Evaluator evaluator;
	private GameEnvironment environment;

	/**
	 * The constructor for the GameAI.
	 * 
	 * @param manager
	 *            The manager for the GameAI.
	 */
	public GameAI(SpriteManager manager) {
		this.manager = manager;
		probCalc = new ProbabilityCalculator(this);
		seedGenerator = new SeedGenerator(this);
		recorder = new Recorder(this);
		analyzer = new Analyzer(this);
		decider = new Decider(this);
		evaluator = new Evaluator(this);
	}

	/**
	 * Get the Game environment.
	 * 
	 * @return The GameEnvironment
	 */
	public GameEnvironment getEnvironment() {
		return environment;
	}

	/**
	 * Get the probability calculator
	 * 
	 * @return The ProbabilityCalculator.
	 */
	public ProbabilityCalculator getProbCalc() {
		return probCalc;
	}

	/**
	 * Get the SeedGenerator.
	 * 
	 * @return SeedGenerator.
	 */
	public SeedGenerator getSeedGenerator() {
		return seedGenerator;
	}

	/**
	 * Get the Recorder.
	 * 
	 * @return Recorder
	 */
	public Recorder getRecorder() {
		return recorder;
	}

	/**
	 * Get the Analyzer
	 * 
	 * @return Analyzer
	 */
	public Analyzer getAnalyzer() {
		return analyzer;
	}

	/**
	 * Get the Decider
	 * 
	 * @return Decider
	 */
	public Decider getDecider() {
		return decider;
	}

	/**
	 * Get the Evaluator
	 * 
	 * @return Evaluator
	 */
	public Evaluator getEvaluator() {
		return evaluator;
	}

	/**
	 * The main method of the AI.
	 * 
	 * The procedure of this method is summarized in following steps:
	 * 
	 * 1. the Recorder will update its data according to the game environment.
	 * 
	 * 2. the SeedGenerator will generate a list of initial seeds as candidates.
	 * 
	 * 3. the seeds will be passed to evaluator to generate evaluations.
	 * 
	 * 4. the evaluations will be passed to Decider to make a final decision.
	 * 
	 * 5. the GameAI will double check whether this action is valid or not.
	 * 
	 * 6. after the checking, the action will be finalized. and the GameAI will
	 * call the method manager.opponnetDashCards(cards) to make this action.
	 * 
	 * @param env
	 *            The environment of the Game, designed for AI system.
	 */
	public synchronized void act(GameEnvironment env) {
		// copy environment
		this.environment = env;

		// get some important cards.
		Cards desk = environment.getCurrentDesk();
		Cards p1Hand = environment.getP1Hand();

		// add history to recorder.
		recorder.addHistory(desk, Recorder.PLAYER2_ID);

		// for debug purpose.
		report(env);

		// generate the seed for the game AI.
		ArrayList<Seed> seeds = seedGenerator.generateSeeds(p1Hand);
		// for debug purpose.
		report(seedGenerator);

		// generate evaluation from seeds.
		ArrayList<Evaluation> evaluations = evaluator.evaluate(seeds);
		// for debug purpose.
		report(evaluator);

		// get the cards for next action.
		Cards cards = decider.chooseCards(evaluations);

		// make an action.
		if (desk.isEmpty()) {
			manager.opponnetDashCards(cards);
		} else {
			if (cards.isGreaterThan(desk)) {
				manager.opponnetDashCards(cards);
			} else {
				cards = new Cards();
				manager.opponnetDashCards(cards);
			}
		}

		// add history for the cards.
		recorder.addHistory(cards, Recorder.PLAYER1_ID);
		// for debug purpose.
		report(recorder);
	}

	/**
	 * For debug purpose.
	 * 
	 * @param str
	 *            The Report content.
	 */
	public void report(Object str) {
		manager.report("GameAI:->" + str);
	}

}
