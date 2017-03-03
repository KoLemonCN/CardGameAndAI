package game.util;

import game.card.Cards;
import game.sprite.SpriteManager;

/**
 * This class describes the environment of the current game. Should contain the
 * following information:
 * 
 * PlayerA cards: cards that still on hand, and cards has been dashed.
 * 
 * PlayerB cards: cards that has been dashed.
 * 
 * Desk Cards: the current dashed cards. empty or the opponents dashed cards.
 * 
 * Current turn: must be the PlayerA.
 * 
 * @author Riki
 * 
 */
public class GameEnvironment {

	private SpriteManager manager;
	private Cards p1All, p1Hand, p1Desk;
	private Cards p2Desk, unknown, allCards;

	// cards to be compared with.
	private Cards currentDesk;

	public GameEnvironment(SpriteManager mgr) {
		this.manager = mgr;
		manager.report("GameEnvironment is initialized successfully!");
	}

	/**
	 * Initialize the Game Environment.
	 * 
	 * @param allCards
	 *            all cards that will be used in this game.
	 * @param p1
	 *            The cards of player 1.
	 */
	public synchronized void initEnvironment(Cards allCards, Cards p1) {
		this.allCards = allCards;
		p1All = (Cards) p1.clone();
		p1Hand = (Cards) p1.clone();
		p1Desk = new Cards();
		p2Desk = new Cards();
		currentDesk = new Cards();
		updateUnkown();
	}

	/**
	 * Calculate the cards that have not appeared in the game.
	 */
	private void updateUnkown() {
		unknown = (Cards) allCards.clone();

		for (int i = 0; i < p1All.getSize(); i++) {
			unknown.removeCard(p1All.getCard(i));
		}
		for (int i = 0; i < p2Desk.getSize(); i++) {
			unknown.removeCard(p2Desk.getCard(i));
		}
	}

	/**
	 * Get the cards of player1.
	 * 
	 * @return cards of player1.
	 */
	public synchronized Cards getP1All() {
		return (Cards) p1All.clone();
	}

	/**
	 * Get the hand cards of player1.
	 * 
	 * @return hand cards of player1.
	 */
	public synchronized Cards getP1Hand() {
		return (Cards) p1Hand.clone();
	}

	/**
	 * Get the desk cards of player1.
	 * 
	 * @return desk cards of player1.
	 */
	public synchronized Cards getP1Desk() {
		return (Cards) p1Desk.clone();
	}

	/**
	 * Get the desk cards of player2.
	 * 
	 * @return desk cards of player2.
	 */
	public synchronized Cards getP2Desk() {
		return (Cards) p2Desk.clone();
	}

	/**
	 * Get the current desk cards.
	 * 
	 * @return current desk cards.
	 */
	public synchronized Cards getCurrentDesk() {
		return (Cards) currentDesk.clone();
	}

	/**
	 * Get the current unknown cards.
	 * 
	 * @return current unknown cards.
	 */
	public synchronized Cards getUnknownCards() {
		return (Cards) unknown.clone();
	}

	/**
	 * Update the GameEnvironment.
	 * 
	 * @param p1OnDesk
	 *            desk cards of player1.
	 * @param p1Hand
	 *            hand cards of player1.
	 * @param p2OnDesk
	 *            desk cards of player2.
	 * @param desk
	 *            current desk cards.
	 */
	public synchronized void updateEnvironment(Cards p1OnDesk, Cards p1Hand,
			Cards p2OnDesk, Cards desk) {
		this.p1Hand = p1Hand;
		this.p1Desk = p1OnDesk;
		this.p2Desk = p2OnDesk;
		this.currentDesk = desk;
		updateUnkown();
	}

	/**
	 * String representation of the this class, for debug purpose.
	 */
	public String toString() {
		String str = "";

		str += "\t p1All \t: [" + p1All + "] \n";
		str += "\t p1Hand \t: [" + p1Hand + "] \n";
		str += "\t p1Desk \t: [" + p1Desk + "] \n";
		str += "\t p2Desk \t: [" + p2Desk + "] \n";
		str += "\t currentDesk \t: [" + currentDesk + "] \n";
		str += "\t *unknown* \t: [" + unknown + "] \n";

		return str;
	}

}
