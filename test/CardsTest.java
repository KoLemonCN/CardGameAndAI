package test;

import game.card.Card;
import game.card.CardFactory;
import game.card.Cards;
import game.card.ICards;
import game.card.Rank;
import game.card.Suit;

/**
 * The test shows, if you want to printing thing, especially the ♠ kind of stuff
 * to work, you need to change the saving type. and the simplest way is to copy
 * this character and save the file, and the eclipse will pop up a box, and you
 * just need to select the OK.
 * 
 * @author Riki
 * 
 */

public class CardsTest {

	public static ICards deck = CardFactory.createDeck();
	public static Cards testCards = new Cards();
	public static ICards simpleDeck;

	public static void main(String[] args) {
		simpleDeck = new Cards();
		for (Rank r1 : Rank.values()) {
			if (Rank.DEUCE == r1) {
				continue;
			}
			simpleDeck.addCard(new Card(Suit.CLUBS, r1));
		}
		System.out.println(simpleDeck);
		
		
		testDeck();

		one();

		two();

		three();

		four();

		five();

	}

	private static void randomTest(int num) {
		System.out.println("Testing " + num + " cards:");
		for (int i = 0; i < 1000; i++) {
			for (int k = 0; k < num; k++) {
				testCards
						.addCard(simpleDeck.getCard((int) (Math.random() * 12)));
			}
			if (testCards.getCardsValue() > 0) {
				System.out.println("\t" + testCards);
			}

		}
		System.out.println("Finish Testing " + num + " cards");
	}

	private static void five() {
		System.out.println("Testing 5 cards:");
		for (int l1 = 0; l1 < deck.getSize(); l1++) {
			for (int l2 = l1 + 1; l2 < deck.getSize(); l2++) {
				for (int l3 = l2 + 1; l3 < deck.getSize(); l3++) {
					for (int l4 = l3 + 1; l4 < deck.getSize(); l4++) {
						for (int l5 = l4 + 1; l5 < deck.getSize(); l5++) {
							testCards = new Cards();
							testCards.addCard(deck.getCard(l1));
							testCards.addCard(deck.getCard(l2));
							testCards.addCard(deck.getCard(l3));
							testCards.addCard(deck.getCard(l4));
							testCards.addCard(deck.getCard(l5));
							if (testCards.getCardsValue() > 0) {
								System.out.println("\t" + testCards);
							}
						}
					}
				}
			}
		}
		System.out.println("Finish Testing 5 cards");
	}

	private static void four() {
		System.out.println("Testing 4 cards:");
		for (int i = 0; i < deck.getSize(); i++) {
			for (int j = i + 1; j < deck.getSize(); j++) {
				for (int k = j + 1; k < deck.getSize(); k++) {
					for (int m = k + 1; m < deck.getSize(); m++) {
						testCards = new Cards();
						testCards.addCard(deck.getCard(i));
						testCards.addCard(deck.getCard(j));
						testCards.addCard(deck.getCard(k));
						testCards.addCard(deck.getCard(m));
						if (testCards.getCardsValue() > 0) {
							System.out.println("\t" + testCards);
						}
					}
				}
			}
		}
		System.out.println("Finish Testing 4 cards");
	}

	private static void three() {
		System.out.println("Testing 3 cards:");
		for (int i = 0; i < deck.getSize(); i++) {
			for (int j = i + 1; j < deck.getSize(); j++) {
				for (int k = j + 1; k < deck.getSize(); k++) {
					testCards = new Cards();
					testCards.addCard(deck.getCard(i));
					testCards.addCard(deck.getCard(j));
					testCards.addCard(deck.getCard(k));
					if (testCards.getCardsValue() > 0) {
						System.out.println("\t" + testCards);
					}
				}
			}
		}
		System.out.println("Finish Testing 3 cards");

	}

	private static void two() {
		System.out.println("Testing 2 cards:");
		for (int i = 0; i < deck.getSize(); i++) {
			for (int j = i + 1; j < deck.getSize(); j++) {
				testCards = new Cards();
				testCards.addCard(deck.getCard(i));
				testCards.addCard(deck.getCard(j));
				if (testCards.getCardsValue() > 0) {
					System.out.println("\t" + testCards);
				}
			}
		}
		System.out.println("Finish Testing 2 cards");

	}

	private static void one() {
		// test for single card

		System.out.println("Testing 1 card:");
		for (int i = 0; i < deck.getSize(); i++) {
			testCards = new Cards();
			testCards.addCard(deck.getCard(i));
			System.out.println("\t" + testCards);
		}
		System.out.println("Finish Testing 1 card");

	}

	private static void testDeck() {
		System.out.println("Testing Card Printing");
		for (int i = 0; i < deck.getSize(); i++) {
			System.out.println(deck.getCard(i));
		}
		System.out.println("Finish Testing Card Printing");

	}
}
