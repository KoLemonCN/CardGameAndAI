package game.sprite;

import game.card.Card;

/**
 * The CardSpriteFactory provides one function to facilitate the creation of
 * CardSprite. The main function is to automatically generate the CardSprite.
 * 
 * @author Riki
 * 
 */
public class CardSpriteFactory {

	/**
	 * The main function to create a CardSprite.
	 * 
	 * @param gp
	 *            The GamePanel, where the GUI is Created.
	 * @param loader
	 *            The Image Loader to load images for sprite.
	 * @param card
	 *            The card used to generate the card sprite.
	 * @param layer
	 *            The layer of the sprite. The background is layer 0. The higher
	 *            the layer is, the higher priority it will have, that mean the
	 *            sprite will be drawn above others whose layer is lower.
	 * @param category
	 *            The category of the card. Please see in CardSprite for more
	 *            detailed information.
	 * @return The CardSprite.
	 */
	public static CardSprite createCardSprite(GamePanel gp,
			ImagesLoader loader, Card card, int layer, int category) {
		// the name of the file: the file is an image.
		String name = "";

		switch (card.getSuit()) {
		case SPADES:
			name += "1-";
			break;
		case HEARTS:
			name += "2-";
			break;
		case CLUBS:
			name += "3-";
			break;
		case DIAMONDS:
			name += "4-";
			break;
		}

		switch (card.getRank()) {
		case ACE:
			name += "1";
			break;
		case DEUCE:
			name += "2";
			break;
		case THREE:
			name += "3";
			break;
		case FOUR:
			name += "4";
			break;
		case FIVE:
			name += "5";
			break;
		case SIX:
			name += "6";
			break;
		case SEVEN:
			name += "7";
			break;
		case EIGHT:
			name += "8";
			break;
		case NINE:
			name += "9";
			break;
		case TEN:
			name += "10";
			break;
		case JACK:
			name += "11";
			break;
		case QUEEN:
			name += "12";
			break;
		case KING:
			name += "13";
			break;
		default:
			name = "white";
			break;
		}

		return new CardSprite(GamePanel.PWIDTH, GamePanel.PHEIGHT, loader,
				name, gp, card, layer, category);

	}
}
