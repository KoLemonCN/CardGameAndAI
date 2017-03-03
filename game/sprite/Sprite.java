package game.sprite;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Sprite {
	// default step sizes (how far to move in each update)
	protected static final int XSTEP = 5;
	protected static final int YSTEP = 5;

	// default dimensions when there is no image
	private static final int DEFAULT_DIMENSION = 12;

	// image-related
	private ImagesLoader imagesLoader;
	// the name of image
	private String imageName;
	private BufferedImage bufferedIamge;
	// image dimensions
	private int width, height;
	// panel dimensions
	private int pWidth, pHeight;
	// a sprite is updated and drawn only when it is active
	private boolean isActive = true;

	// protected vars
	// location of sprite
	protected int locx, locy;
	// amount to move for each update
	protected int dx, dy;
	// sprite layer
	protected int layer;

	/**
	 * 
	 * @param x
	 *            Initial location of the sprite: x coordinate
	 * @param y
	 *            Initial location of the sprite: y coordinate
	 * @param w
	 *            Panel dimensions: width
	 * @param h
	 *            Panel dimensions: height
	 * @param imsLd
	 *            The image loader for the sprite
	 * @param name
	 *            The name of the sprite
	 * @param lay
	 *            The layer of the sprite
	 */
	public Sprite(int x, int y, int w, int h, ImagesLoader imsLd, String name,
			int lay) {
		locx = x;
		locy = y;
		pWidth = w;
		pHeight = h;
		dx = XSTEP;
		dy = YSTEP;
		layer = lay;
		imagesLoader = imsLd;
		/*
		 * default image name is {@code name}
		 */
		setImage(name);
	}

	/**
	 * Set the image for the sprite.
	 * 
	 * @param name
	 *            The image name.
	 */
	public void setImage(String name) {
		// set the name to the sprite
		imageName = name;
		bufferedIamge = imagesLoader.getImage(imageName);
		// if no image of that name was found
		if (bufferedIamge == null) {
			System.err.println("Image for this prite not found " + imageName);
			width = DEFAULT_DIMENSION;
			height = DEFAULT_DIMENSION;
		} else {
			width = bufferedIamge.getWidth();
			height = bufferedIamge.getHeight();
		}
	}

	/**
	 * Get the width of the sprite.
	 * 
	 * @return The width of the sprite.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Get the height of the sprite.
	 * 
	 * @return The height of the sprite.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Get the width of the Panel that the sprite is in.
	 * 
	 * @return The width of the Panel.
	 */
	public int getPWidth() {
		return pWidth;
	}

	/**
	 * Get the height of the Panel that the sprite is in.
	 * 
	 * @return The height of the Panel.
	 */
	public int getPHeight() {
		return pHeight;
	}

	/**
	 * Get the active information of the sprite. Notice: the sprite is only
	 * drawn when it is active.
	 * 
	 * @return True is the sprite is active, otherwise false.
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * Set the active property of the sprite.
	 * 
	 * @param a
	 *            True to set the sprite active, otherwise fales.
	 */
	public void setActive(boolean a) {
		isActive = a;
	}

	/**
	 * Set the position of the sprite relative to panel.
	 * 
	 * @param x
	 *            The x coordinate of the location.
	 * @param y
	 *            The y coordinate of the location.
	 */
	public void setPosition(int x, int y) {
		locx = x;
		locy = y;
	}

	/**
	 * Set the step of the sprite.
	 * 
	 * @param dx
	 *            Step in x direction.
	 * @param dy
	 *            Step in y direction.
	 */
	public void setStep(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}

	/**
	 * Get the step in x direction.
	 * 
	 * @return The step distance in x direction.
	 */
	public int getXStep() {
		return dx;
	}

	/**
	 * Get the step in y direction.
	 * 
	 * @return The step distance in y direction.
	 */
	public int getYStep() {
		return dy;
	}

	/**
	 * Get the rectangle of the sprite.
	 * 
	 * @return The rectangle of the sprite.
	 */
	public Rectangle getSpriteRectangle() {
		return new Rectangle(locx, locy, width, height);
	}

	/**
	 * Update the sprite. In this case, the position.
	 */
	public void updateSprite() {
		if (isActive()) {
			locx += dx;
			locy += dy;
		}
	}

	/**
	 * Draw the sprite using Graphics g.
	 * 
	 * @param g
	 *            The graphics used to draw the sprite.
	 */
	public void drawSprite(Graphics g) {
		if (isActive()) {
			// the sprite has no image
			if (bufferedIamge == null) {
				// draw a black rectangle to represent the sprite.
				g.setColor(Color.black);
				g.fillRect(locx, locy, DEFAULT_DIMENSION, DEFAULT_DIMENSION);
			} else {
				g.drawImage(bufferedIamge, locx, locy, null);
			}
		}
	}

	/**
	 * Get the layer of the sprite.
	 * 
	 * @return The layer of the sprite.
	 */
	public int getLayer() {
		return layer;
	}

	/**
	 * Set the layer of the sprite.
	 * 
	 * @param layer
	 *            The layer of the sprite.
	 */
	public void setLayer(int layer) {
		this.layer = layer;
	}

}
