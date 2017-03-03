package game.sprite;

import game.card.Card;

public class CardSprite extends Sprite {

	/**
	 * The Category: PLAYER
	 * 
	 * Means this CardSprite belongs to PLAYER.
	 */
	public static final int CATEGORY_PLAYER = 0;
	/**
	 * The Category: OPPONENT
	 * 
	 * Means this CardSprite belongs to OPPONENT.
	 */
	public static final int CATEGORY_OPPONENT = 1;
	/**
	 * The Category: DESK
	 * 
	 * Means this CardSprite belongs to DESK.
	 */
	public static final int CATEGORY_DESK = 2;

	/**
	 * The default image file name for the CardSprite.
	 */
	public static final String BACKUP_IMG = "rear";

	/**
	 * The Image Name of the CardSprite.
	 */
	public final String IMG_NAME;

	// The game panel
	private GamePanel gamePanel;

	// The card
	private final Card card;

	// boolean: if selected, true. otherwise false.
	private boolean isSelected = false;
	// boolean: if the card sprite is put on desk, true. otherwise false.
	private boolean isOnDesk = false;
	// the category of the card sprite.
	private final int category;

	/**
	 * The Constructor for the CardSprite.
	 * 
	 * @param w
	 *            The width of the sprite.
	 * @param h
	 *            The height of the sprite.
	 * @param imsLd
	 *            The image loader of the sprite.
	 * @param name
	 *            The name of the sprite.
	 * @param gp
	 *            The GamePanel of the sprite.
	 * @param card
	 *            The card of the sprite.
	 * @param layer
	 *            The layer of the sprite. For more information, see Sprite.
	 * @param category
	 *            The category of the card sprite.
	 */
	public CardSprite(int w, int h, ImagesLoader imsLd, String name,
			GamePanel gp, Card card, int layer, int category) {
		super(w / 2, h / 2, w, h, imsLd, name, layer);
		this.IMG_NAME = name;
		this.gamePanel = gp;
		this.card = card;
		this.category = category;

		if (CATEGORY_PLAYER != category) {
			setImage(BACKUP_IMG);
		}

		// if the card sprite is desk cards, don't draw it.
		setActive(CATEGORY_DESK != category);
		// if the category is desk, set the cards to be on desk.
		isOnDesk = (CATEGORY_DESK == category);

		// cards stays at position, no movement.
		setStep(0, 0);

		gamePanel.report("CardSprite is initialized successfully!" + this);
	}

	/**
	 * Get the state, whether this card sprite is selected or not.
	 * 
	 * @return True if the card is selected, otherwise false.
	 */
	public boolean isSelected() {
		return isSelected;
	}

	/**
	 * Get the category of the card sprite.
	 * 
	 * @return The category of the card sprite.
	 */
	public int getCategory() {
		return category;
	}

	/**
	 * Get the information: whether the card sprite belongs to player.
	 * 
	 * @return True if the card sprite belongs to player, otherwise false.
	 */
	public boolean isPlayer() {
		return CATEGORY_PLAYER == category;
	}

	/**
	 * Get the information: whether the card sprite belongs to opponent.
	 * 
	 * @return True if the card sprite belongs to opponent, otherwise false.
	 */
	public boolean isOpponent() {
		return CATEGORY_OPPONENT == category;
	}

	/**
	 * Get the information: whether the card sprite belongs to Desk.
	 * 
	 * @return True if the card sprite belongs to Desk, otherwise false.
	 */
	public boolean isDesk() {
		return CATEGORY_DESK == category;
	}

	/**
	 * Get the information: whether the card is on desk.
	 * 
	 * @return True is the card sprite is on desk, otherwise false.
	 */
	public boolean isOnDesk() {
		return isOnDesk;
	}

	/**
	 * Set the card onto/off desk.
	 * 
	 * @param b
	 *            True to set the card sprite onto desk, otherwise false.
	 */
	public void setOnDesk(boolean b) {
		isOnDesk = b;
		setImage(IMG_NAME);
	}

	/**
	 * Get the card of the card sprite.
	 */
	public Card getCard() {
		return card;
	}

	/**
	 * Select the card. The image effect is controlled here.
	 */
	public void select() {
		// player image effect here
		System.out.println(this.getCard() + " play image effect: selected");
		isSelected = true;
	}

	/**
	 * Un-Select the card. The image effect is controlled here.
	 */
	public void unselect() {
		// player image effect here
		System.out.println(this.getCard() + " play image effect: unselected.");
		isSelected = false;
	}

	/**
	 * String representation of this class.
	 */
	public String toString() {
		return "[" + card.toString() + " " + isActive() + "]";
	}

}
