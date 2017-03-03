package main;

import game.sprite.GamePanel;

import java.awt.Container;

import javax.swing.JFrame;

/**
 * This is the main class for the Card Game named "The Escape".
 * 
 * This card game is implemented using Animation and Sprite Frameworks.
 * 
 * The card game also contains an AI for human player to play with.
 * 
 * The instructions for the game are shown in the User Interface.
 * 
 * @author Riki
 * 
 */
public class TheEscape extends JFrame {

	/**
	 * System generated serial version Unique ID.
	 */
	private static final long serialVersionUID = 2324339878039595812L;

	/**
	 * Main method for this class.
	 * 
	 * It would generate an instance of this class, set the FPS (Frame Per
	 * Second).
	 * 
	 * @param args
	 *            The arguments. In this case, it is not required.
	 */
	public static void main(String[] args) {
		// period in millis
		long period = (long) 1000.0 / TheEscape.DEFAULT_FPS;

		// Millis -> Nano
		TheEscape gameWindow = new TheEscape(period * 1000000L);

		// create the GUI for the game.
		gameWindow.launchFrame();
	}

	/**
	 * The main User Interface Panel.
	 */
	private GamePanel gp;

	/**
	 * The update period: update interval.
	 * 
	 * The GUI will update within every period of time.
	 */
	public final long PERIOD;

	public TheEscape(long period) {
		// By default, create the title of the JFrame.
		super("The Escape");

		// set the period of update interval.
		this.PERIOD = period;

		gp = new GamePanel(this, PERIOD);

		// default BorderLayout used
		Container c = getContentPane();

		c.add(gp, "Center");

		// set the default close operation.
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * The default FPS (Update Per Second) for this game.
	 */
	public static final int DEFAULT_FPS = 50;

	private void launchFrame() {
		// pack the window.
		pack();
		// the game window will not be resized.
		setResizable(false);
		// show the window.
		setVisible(true);
	}
	
	/**
	 * For debug purpose.
	 * @param str The information to be reported.
	 */
	public void report(String str) {
		System.out.println("TheEscape::->" + str);
	}

}
