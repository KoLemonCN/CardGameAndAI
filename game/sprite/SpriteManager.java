package game.sprite;

import game.card.Card;
import game.card.CardFactory;
import game.card.Cards;
import game.util.GameEnvironment;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import ai.GameAI;

public class SpriteManager {

	/**
	 * The cards number.
	 */
	private final int CARDS_NUM = 16;

	/**
	 * One card sprite has CARD_STEP pixels to next.
	 */
	private static final int CARD_STEP = 30;

	/**
	 * Flag to denotes the Player's turn.
	 */
	public static final int PLAYER_TURN = 0;
	/**
	 * Flag to denotes the Opponent's turn.
	 */
	public static final int OPPONENT_TURN = 1;

	/**
	 * The initial point for player cards.
	 */
	private static final Point PLAYER_POINT = new Point(100,
			GamePanel.PWIDTH / 2 + 100);
	/**
	 * The initial point for opponent cards.
	 */
	private static final Point OPPONENT_POINT = new Point(100, 100);
	/**
	 * The initial point for desk cards.
	 */
	private static final Point DESK_POINT = new Point(CARD_STEP,
			GamePanel.PWIDTH / 2 - 100);

	/**
	 * The initial point for player's desk cards.
	 */
	private static final Point PLAYER_DESK_POINT = new Point(200,
			GamePanel.PWIDTH / 2);
	/**
	 * The initial point for opponent's desk cards.
	 */
	private static final Point OPPONENT_DESK_POINT = new Point(200, 200);

	/**
	 * The comparator for card sprite, with respect to layer.
	 */
	private static LayerComparator comparator = new LayerComparator();

	/**
	 * The game panel.
	 */
	private GamePanel gamePanel;

	/**
	 * By default, it is Player's turn to start.
	 */
	private int currentTurn = PLAYER_TURN;

	/**
	 * Some cards states.
	 */
	private Cards playerSelectedCards;
	private Cards opponentSelectedCards;
	private Cards playerDashedCards;
	private Cards opponentDashedCards;

	/**
	 * These represents the belongs.
	 */
	private Vector<CardSprite> playerCards;
	private Vector<CardSprite> opponentCards;
	private Vector<CardSprite> deskCards;

	/**
	 * The game environment.
	 */
	private GameEnvironment environment;

	/**
	 * The game AI.
	 */
	private GameAI gameAI;

	/**
	 * The constructor will initialize all necessary components listed in the
	 * field.
	 * 
	 * @param gamePanel
	 *            The game panel, where all the sprite will be displayed.
	 */

	/**
	 * Current game state.
	 */
	private boolean gameOver = false;
	private volatile boolean gameStarted = false;
	/**
	 * is the AI activated.
	 */
	public volatile boolean aiActivated = false;

	public SpriteManager(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
		playerCards = new Vector<CardSprite>(CARDS_NUM);
		opponentCards = new Vector<CardSprite>(CARDS_NUM);
		deskCards = new Vector<CardSprite>(CARDS_NUM);
		playerSelectedCards = new Cards();
		opponentSelectedCards = new Cards();
		playerDashedCards = new Cards();
		opponentDashedCards = new Cards();
		environment = new GameEnvironment(this);
		gameAI = new GameAI(this);
	}

	/**
	 * This is called when the game needs to update its state. This method will
	 * update each sprite.
	 */
	public synchronized void update() {
		if (playerCards.isEmpty()) {
			return;
		}
		Sprite sprite;

		// update the cards of game AI
		for (int i = 0; i < CARDS_NUM; i++) {
			sprite = opponentCards.get(i);
			sprite.updateSprite();
		}
		// update the cards of player
		for (int i = 0; i < CARDS_NUM; i++) {
			sprite = playerCards.get(i);
			sprite.updateSprite();
		}
		// update the cards on the desk
		for (int i = 0; i < CARDS_NUM; i++) {
			sprite = deskCards.get(i);
			sprite.updateSprite();
		}
	}

	/**
	 * This is called when the game needs to draw its state. This method will
	 * draw each sprite.
	 */
	public synchronized void drawSprites(Graphics g) {
		if (playerCards.isEmpty()) {
			return;
		}
		Sprite sprite;

		for (int i = 0; i < CARDS_NUM; i++) {
			sprite = opponentCards.get(i);
			sprite.drawSprite(g);
		}
		for (int i = 0; i < CARDS_NUM; i++) {
			sprite = playerCards.get(i);
			sprite.drawSprite(g);
		}
		for (int i = 0; i < CARDS_NUM; i++) {
			sprite = deskCards.get(i);
			sprite.drawSprite(g);
		}
	}

	/**
	 * Handle all mouse event. Left click to select card, right click to
	 * un-select card, middle button to make action.
	 * 
	 * @param e
	 *            The mouse event.
	 */
	public void handleMouseEvent(MouseEvent e) {
		if (!gameStarted) {
			return;
		}

		// if the AI is activated, then the mouse event should be ignored.
		if (isAiActivated() && OPPONENT_TURN == currentTurn) {
			return;
		}

		Vector<CardSprite> playerA = null;
		Cards playerASelected = null;
		Cards playerADashed = null;
		Vector<CardSprite> playerB = null;
		Cards playerBDashed = null;
		Cards playerBSelected = null;
		Point playerADeskPoint = null;
		Point playerAPoint = null;

		// decide whose turn to to handled.
		if (PLAYER_TURN == currentTurn) {
			report("handle player mouse evnet");
			playerA = playerCards;
			playerASelected = playerSelectedCards;
			playerADashed = playerDashedCards;
			playerB = opponentCards;
			playerBDashed = opponentDashedCards;
			playerBSelected = opponentSelectedCards;
			playerADeskPoint = PLAYER_DESK_POINT;
			playerAPoint = PLAYER_POINT;
		} else {
			report("handle opponent mouse evnet");
			playerA = opponentCards;
			playerASelected = opponentSelectedCards;
			playerADashed = opponentDashedCards;
			playerB = playerCards;
			playerBDashed = playerDashedCards;
			playerBSelected = playerSelectedCards;
			playerADeskPoint = OPPONENT_DESK_POINT;
			playerAPoint = OPPONENT_POINT;
		}

		// reportPlayers();
		{
			// report("playerA " + playerA);
			// report("playerASelected " + playerASelected);
			// report("playerBDashed " + playerBDashed);
		}
		boolean changeTurn = false;
		if (e.getButton() == MouseEvent.BUTTON2) {
			// compare the cards.
			boolean win = false;

			if (playerASelected.isEmpty()) {
				win = true;
			} else if (!playerBDashed.isEmpty()) {
				if (playerASelected.isGreaterThan(playerBDashed)) {
					win = true;
				}
			} else if (playerASelected.getCardsValue() > 0) {
				win = true;
			}

			if (win) {
				// put the cards onto dashed.
				playerADashed = (Cards) playerASelected.clone();
				for (int i = 0; i < playerADashed.getSize(); i++) {
					Card deshed = playerADashed.getCard(i);
					for (int n = 0; n < CARDS_NUM; n++) {
						CardSprite sprite = playerA.get(n);
						if (deshed.equals(sprite.getCard())) {
							sprite.setOnDesk(true);
							sprite.setPosition(playerADeskPoint.x + i
									* CARD_STEP, playerADeskPoint.y);
							break;
						}
					}
				}

				playerASelected.remvoeAll();

				// clear opponent's desk
				for (int i = 0; i < CARDS_NUM; i++) {
					CardSprite sprite = playerB.get(i);
					if (sprite.isOnDesk()) {
						sprite.setActive(false);
					}
				}
				playerBDashed.remvoeAll();

				changeTurn = true;

				// set new locations for cards in hand
				for (int i = 0, counter = 0; i < CARDS_NUM; i++) {
					CardSprite sprite = playerA.get(i);
					if (sprite.isActive() && !sprite.isOnDesk()) {
						sprite.setPosition(
								playerAPoint.x + counter * CARD_STEP,
								playerAPoint.y);
						counter++;
					}
				}

			} else {
				// un-select all current cards.

				while (!playerASelected.isEmpty()) {
					Card c = playerASelected.getCard(0);
					for (int n = 0; n < CARDS_NUM; n++) {
						CardSprite sprite = playerA.get(n);
						if (sprite.getCard().equals(c)) {
							unselectCardSprite(sprite, playerASelected);
							break;
						}
					}
				}

				playerASelected.remvoeAll();
			}

		} else {
			CardSprite card = getClickedCardSprite(e, playerA);

			if (card == null || card.isOnDesk()) {
				return;
			}

			if (e.getButton() == MouseEvent.BUTTON1) {
				selectCardSprite(card, playerASelected);
			} else if (e.getButton() == MouseEvent.BUTTON3) {
				// Unselect
				unselectCardSprite(card, playerASelected);
			}
		}

		// copy back
		if (PLAYER_TURN == currentTurn) {
			playerSelectedCards = playerASelected;
			playerDashedCards = playerADashed;
			opponentDashedCards = playerBDashed;
			opponentSelectedCards = playerBSelected;
		} else {
			opponentSelectedCards = playerASelected;
			opponentDashedCards = playerADashed;
			playerDashedCards = playerBDashed;
			playerSelectedCards = playerBSelected;
		}

		// decide whther to switch turn or not.
		if (changeTurn) {
			if (PLAYER_TURN == currentTurn) {
				setTurn(OPPONENT_TURN);
			} else {
				setTurn(PLAYER_TURN);
			}
		}
	}

	/**
	 * Update the Game Environment.
	 */
	private void updateEnvironment() {
		report("updating the environemnt for the AI");

		Cards p1OnDesk, p2OnDesk, desk, p1Hand;

		p1OnDesk = new Cards();
		p2OnDesk = new Cards();
		desk = new Cards();
		p1Hand = new Cards();
		for (int i = 0; i < CARDS_NUM; i++) {
			CardSprite cp = opponentCards.get(i);

			if (cp.isOnDesk()) {
				p1OnDesk.addCard(cp.getCard());
			} else {
				p1Hand.addCard(cp.getCard());
			}

		}

		for (int i = 0; i < CARDS_NUM; i++) {
			CardSprite cp = playerCards.get(i);
			if (cp.isOnDesk()) {
				p2OnDesk.addCard(cp.getCard());
			}
		}

		for (int i = 0; i < playerDashedCards.getSize(); i++) {
			desk.addCard(playerDashedCards.getCard(i));
		}

		environment.updateEnvironment(p1OnDesk, p1Hand, p2OnDesk, desk);

		report("environment udpated:" + environment);
	}

	/**
	 * Initialize the Environment
	 * 
	 * @param allCards
	 *            All cards.
	 */
	private void initEnvironment(Cards allCards) {
		Cards p1 = new Cards();

		for (int i = 0; i < CARDS_NUM; i++) {
			p1.addCard(opponentCards.get(i).getCard());
		}

		environment.initEnvironment(allCards, p1);
	}

	/**
	 * Test if the game is over.
	 * 
	 * @return True if the game is over, otherwise false.
	 */
	public boolean isGameOver() {
		return gameOver;
	}

	/**
	 * Calculate which card sprite has been clicked by mouse.
	 * 
	 * @param e
	 *            The mouse event.
	 * @param cardSprites
	 *            Card sprite.
	 * @return The clicked card sprite.
	 */
	private CardSprite getClickedCardSprite(MouseEvent e,
			Vector<CardSprite> cardSprites) {

		CardSprite playerSprite = null;
		Point mousePoint = new Point(e.getX(), e.getY());

		CardSprite s = null;
		for (int i = CARDS_NUM - 1; i >= 0; i--) {
			s = cardSprites.get(i);
			if (s.isActive() && s.getSpriteRectangle().contains(mousePoint)) {
				playerSprite = s;
				break;
			}
		}
		return playerSprite;
	}

	/**
	 * Unselect a card sprite.
	 * 
	 * @param cardSprite
	 *            The card sprite to be unselected.
	 * @param selectedCards
	 *            The cards which contains the card sprite.
	 */
	private void unselectCardSprite(CardSprite cardSprite, Cards selectedCards) {
		// unselect
		if (cardSprite.isSelected()) {
			report("unselect: " + cardSprite.getCard());
			cardSprite
					.setPosition(cardSprite.locx, cardSprite.locy + CARD_STEP);
			cardSprite.unselect();
			selectedCards.removeCard(cardSprite.getCard());
		}

	}

	/**
	 * Select a card sprite.
	 * 
	 * @param cardSprite
	 *            The card sprite to be selected.
	 * @param selectedCards
	 *            The cards which contains the card sprite.
	 */
	private void selectCardSprite(CardSprite cardSprite, Cards selectedCards) {
		// select
		if (cardSprite.isSelected()) {
			return;
		}
		report("select: " + cardSprite.getCard());
		cardSprite.setPosition(cardSprite.locx, cardSprite.locy - CARD_STEP);
		cardSprite.select();
		selectedCards.addCard(cardSprite.getCard());
	}

	/**
	 * Reset the sprite manager. This also means the game will start over from
	 * the start point: a new game.
	 */
	public synchronized void reset() {
		environment = new GameEnvironment(this);
		gameAI = new GameAI(this);

		playerCards = new Vector<CardSprite>(CARDS_NUM);
		opponentCards = new Vector<CardSprite>(CARDS_NUM);
		deskCards = new Vector<CardSprite>(CARDS_NUM);

		/*
		 * add card-sprite to it.
		 */
		CardSprite sprite;

		Cards allCards = CardFactory.createDeck();

		allCards.shuffle();

		for (int i = 0; i < allCards.getSize(); i++) {
			int category = i % 3;
			sprite = CardSpriteFactory.createCardSprite(gamePanel,
					GamePanel.IMG_LOADER, allCards.getCard(i), allCards
							.getCard(i).getID(), category);
			switch (category) {
			case CardSprite.CATEGORY_PLAYER:
				playerCards.add(sprite);
				break;
			case CardSprite.CATEGORY_OPPONENT:
				opponentCards.add(sprite);
				break;
			case CardSprite.CATEGORY_DESK:
				deskCards.add(sprite);
				break;
			}
		}

		// sort the cards.
		Collections.sort(playerCards, comparator);
		Collections.sort(opponentCards, comparator);
		Collections.sort(deskCards, comparator);

		initLocations();

		// randomly decide who to start.
		initTurn();

		initEnvironment(allCards);
		
		gameOver = false;
		gameStarted = true;
		playerSelectedCards = new Cards();
		opponentSelectedCards = new Cards();
		playerDashedCards = new Cards();
		opponentDashedCards = new Cards();

		reportGameInfo();
		
		// start the ai process if required.
		if (isAiActivated() && currentTurn == OPPONENT_TURN) {
			updateEnvironment();
			gameAI.act(environment);
		}
	}

	/**
	 * For debug purpose. Print the initial cards information.
	 */
	private void reportGameInfo() {
		Card card = null;
		
		// print AI cards;
		System.out.print("AI cards: \t");
		for (int i = 0; i < CARDS_NUM; i++) {
			card = opponentCards.get(i).getCard();
			System.out.print(card + " ");
		}
		System.out.println();
		
		// print player cards;
		System.out.print("Player cards: \t");
		for (int i = 0; i < CARDS_NUM; i++) {
			card = playerCards.get(i).getCard();
			System.out.print(card + " ");
		}
		System.out.println();
	}

	/**
	 * Test if the game started.
	 * 
	 * @return True is the game started, otherwise false.
	 */
	public boolean isGameStarted() {
		return gameStarted;
	}

	/**
	 * Set the game state.
	 * 
	 * @param gameStarted
	 *            True if the game started, otherwise false.
	 */
	public void setGameStarted(boolean gameStarted) {
		this.gameStarted = gameStarted;
	}

	/**
	 * Randomly decide who to start.
	 */
	private void initTurn() {
		if (Math.random() > 0.5) {
			currentTurn = PLAYER_TURN;
		} else {
			currentTurn = OPPONENT_TURN;
		}
	}

	/**
	 * Initialize all card sprite locations.
	 */
	private void initLocations() {
		CardSprite sprite;
		for (int i = 0; i < CARDS_NUM; i++) {
			sprite = deskCards.get(i);
			sprite.setPosition(DESK_POINT.x + i * CARD_STEP, DESK_POINT.y);
		}

		for (int i = 0; i < CARDS_NUM; i++) {
			sprite = opponentCards.get(i);
			sprite.setPosition(OPPONENT_POINT.x + i * CARD_STEP,
					OPPONENT_POINT.y);
		}

		for (int i = 0; i < CARDS_NUM; i++) {
			sprite = playerCards.get(i);
			sprite.setPosition(PLAYER_POINT.x + i * CARD_STEP, PLAYER_POINT.y);
		}

	}

	/**
	 * Test if the AI is activated.
	 * 
	 * @return True if the AI is activated, otherwise false.
	 */
	public boolean isAiActivated() {
		return aiActivated;
	}

	/**
	 * Set AI state. True to activate AI, otherwise false.
	 * 
	 * @param aiActivated
	 *            True to activate AI, otherwise false.
	 */
	public void setAiActivated(boolean aiActivated) {
		this.aiActivated = aiActivated;
		if (aiActivated && OPPONENT_TURN == currentTurn) {
			updateEnvironment();
			gameAI.act(environment);
		}
	}

	/**
	 * Handle the user key event.
	 * 
	 * @param e
	 *            The key event.
	 */
	public synchronized void handleKeyEvent(KeyEvent e) {
		// A: switch AI.
		if (e.getKeyCode() == KeyEvent.VK_A) {
			setAiActivated(!aiActivated);
		} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			reset();
		}
		if (!gameStarted) {
			return;
		}
		if (e.getKeyCode() == KeyEvent.VK_O) {
			for (int i = 0; i < CARDS_NUM; i++) {
				opponentCards.get(i).setImage(opponentCards.get(i).IMG_NAME);
			}
		} else if (e.getKeyCode() == KeyEvent.VK_D) {
			for (int i = 0; i < CARDS_NUM; i++) {
				deskCards.get(i).setImage(deskCards.get(i).IMG_NAME);
				deskCards.get(i).setActive(true);
			}
		} else if (e.getKeyCode() == KeyEvent.VK_R) {
			for (int i = 0; i < CARDS_NUM; i++) {
				deskCards.get(i).setImage(CardSprite.BACKUP_IMG);
				deskCards.get(i).setActive(false);
				if (!opponentCards.get(i).isOnDesk()) {
					opponentCards.get(i).setImage(CardSprite.BACKUP_IMG);
				}
			}
		}
	}

	/**
	 * Set the turn of player.
	 * 
	 * @param flag
	 *            The flag to denote who to play.
	 */
	public void setTurn(int flag) {
		// test if the game is over
		testGameOver();

		if (gameOver) {
			return;
		}

		switch (flag) {
		case PLAYER_TURN:
			currentTurn = PLAYER_TURN;
			break;
		case OPPONENT_TURN:
			currentTurn = OPPONENT_TURN;
			if (isAiActivated()) {
				updateEnvironment();
				gameAI.act(environment);
			}
			break;
		default:
			System.err.println("SpriteManager: No player ID match.");
		}
	}

	/**
	 * Get the current Turn.
	 * 
	 * @return The current Turn.
	 */
	public int getTurn() {
		return currentTurn;
	}

	/**
	 * The LayerComparator to compare the sprite by layer.
	 * 
	 * @author Riki
	 * 
	 */
	private static class LayerComparator implements Comparator<Sprite> {
		@Override
		public int compare(Sprite o1, Sprite o2) {
			if (o1.getLayer() > o2.getLayer()) {
				return 1;
			} else if (o1.getLayer() < o2.getLayer()) {
				return -1;
			}
			return 0;
		}
	}

	/**
	 * The main method for the AI to call. The AI shall select some cards, and
	 * make action.
	 * 
	 * @param cards
	 *            The cards to be dashed.
	 */
	public void opponnetDashCards(Cards cards) {
		Vector<CardSprite> playerA = null;
		Cards playerASelected = null;
		Cards playerADashed = null;
		Vector<CardSprite> playerB = null;
		Cards playerBDashed = null;
		Cards playerBSelected = null;
		Point playerADeskPoint = null;
		Point playerAPoint = null;

		playerA = opponentCards;
		playerASelected = opponentSelectedCards;
		playerADashed = opponentDashedCards;
		playerB = playerCards;
		playerBDashed = playerDashedCards;
		playerBSelected = playerSelectedCards;
		playerADeskPoint = OPPONENT_DESK_POINT;
		playerAPoint = OPPONENT_POINT;
		// selection first.
		for (int i = 0; i < cards.getSize(); i++) {
			Card c = cards.getCard(i);
			for (int n = 0; n < playerA.size(); n++) {
				if (playerA.get(n).getCard().equals(c)) {
					selectCardSprite(playerA.get(n), playerASelected);
				}
			}
		}

		// reportPlayers();
		{
			// gamePanel.report("playerA " + playerA);
			// gamePanel.report("playerASelected " + playerASelected);
			// gamePanel.report("playerBDashed " + playerBDashed);
		}
		// dash

		// compare the cards.

		boolean win = false;

		if (playerASelected.isEmpty()) {
			win = true;
		} else if (!playerBDashed.isEmpty()) {
			if (playerASelected.isGreaterThan(playerBDashed)) {
				win = true;
			}
		} else if (playerASelected.getCardsValue() > 0) {
			win = true;
		}

		if (win) {
			// put the cards onto dashed.
			playerADashed = (Cards) playerASelected.clone();
			for (int i = 0; i < playerADashed.getSize(); i++) {
				Card deshed = playerADashed.getCard(i);
				for (int n = 0; n < CARDS_NUM; n++) {
					CardSprite sprite = playerA.get(n);
					if (deshed.equals(sprite.getCard())) {
						sprite.setOnDesk(true);
						sprite.setPosition(playerADeskPoint.x + i * CARD_STEP,
								playerADeskPoint.y);
						break;
					}
				}
			}

			playerASelected.remvoeAll();
			// set new locations for cards in hand
			for (int i = 0, counter = 0; i < CARDS_NUM; i++) {
				CardSprite sprite = playerA.get(i);
				if (sprite.isActive() && !sprite.isOnDesk()) {
					sprite.setPosition(playerAPoint.x + counter * CARD_STEP,
							playerAPoint.y);
					counter++;
				}
			}
		} else {
			// unselect all current cards.

			while (!playerASelected.isEmpty()) {
				Card c = playerASelected.getCard(0);
				for (int n = 0; n < CARDS_NUM; n++) {
					CardSprite sprite = playerA.get(n);
					if (sprite.getCard().equals(c)) {
						unselectCardSprite(sprite, playerASelected);
						break;
					}
				}
			}

			playerASelected.remvoeAll();

		}
		// clear opponent's desk
		for (int i = 0; i < CARDS_NUM; i++) {
			CardSprite sprite = playerB.get(i);
			if (sprite.isOnDesk()) {
				sprite.setActive(false);
			}
		}
		playerBDashed.remvoeAll();
		// copy back
		opponentSelectedCards = playerASelected;
		opponentDashedCards = playerADashed;
		playerDashedCards = playerBDashed;
		playerSelectedCards = playerBSelected;

		// set the current turn to player.
		setTurn(PLAYER_TURN);
	}

	/**
	 * Test if the game is over.
	 */
	private void testGameOver() {
		boolean allOnDesk = true;
		for (int i = 0; i < CARDS_NUM; i++) {
			if (!playerCards.get(i).isOnDesk()) {
				allOnDesk = false;
				break;
			}
		}
		if (allOnDesk) {
			gameOver = true;
			return;
		}
		allOnDesk = true;
		for (int i = 0; i < CARDS_NUM; i++) {
			if (!opponentCards.get(i).isOnDesk()) {
				allOnDesk = false;
				break;
			}
		}
		if (allOnDesk) {
			gameOver = true;
		}

	}

	/**
	 * For debug purpose.
	 * 
	 * @param str
	 *            The information to be displayed.
	 */
	public void report(String str) {
		gamePanel.report("SpriteManager:->" + str);
	}

}
