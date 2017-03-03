package game.sprite;

import game.util.UpdateCalculator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import javax.swing.JPanel;

import main.TheEscape;

public class GamePanel extends JPanel implements Runnable {

	/**
	 * The System generated serialVerion Unique ID.
	 */
	private static final long serialVersionUID = 5122795970779642874L;
	/**
	 * Dimension of the Panel: Width
	 */
	public static final int PWIDTH = 800;
	/**
	 * Dimension of the Panel: Height
	 */
	public static final int PHEIGHT = 600;

	// The main game class.
	private TheEscape escape;
	// period of the update.
	private final long PERIOD;
	// max frame skip constant.
	private static int MAX_FRAME_SKIPS = 5;
	private static final int NO_DELAYS_PER_YIELD = 16;
	// message font1
	private Font msgsFont;
	// message font2
	private Font msgsFont2;
	// the metrics of strings.
	private FontMetrics metrics;
	// is the game running.
	private volatile boolean running = false;
	// the game start time.
	private long gameStartTime;
	// the game run time in total.
	private int timeSpentInGame;
	// background graphics
	private Graphics dbg;
	// background image
	private Image dbImage = null;
	// the buffered background image.
	private BufferedImage bgImage = null;
	// is the game over.
	private volatile boolean gameOver = false;
	// the animation thread.
	private Thread animator;
	// update calculators: calculates the average fps and ups.
	private UpdateCalculator fpsCalculator, upsCalculator;
	// default decimal format to format the decimal number.
	private DecimalFormat decimalFormat = new DecimalFormat("0.##");
	// the sprite manager that controls the game logic.
	private SpriteManager manager;
	/**
	 * Default image loader for this game.
	 */
	public static final ImagesLoader IMG_LOADER = new ImagesLoader(
			"imsInfo.txt");

	/**
	 * The constructor for the game panel, where the game animation is
	 * displayed.
	 * 
	 * @param theEscape
	 *            The main class.
	 * @param period
	 *            The update period.
	 */
	public GamePanel(TheEscape theEscape, long period) {
		this.escape = theEscape;
		this.PERIOD = period;
		initPanel();
		escape.report("GamePanel is initialized successfully!");
	}

	/**
	 * Initialize the panel properties.
	 */
	private void initPanel() {
		setDoubleBuffered(false);
		setBackground(Color.black);
		setPreferredSize(new Dimension(PWIDTH, PHEIGHT));

		setFocusable(true);
		requestFocus(); // the JPanel now has focus, so receives key events
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				System.out.println("key event received: " + e.getKeyChar());
				manager.handleKeyEvent(e);
			}
		});

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				System.out.println("Mouse event received: (" + e.getX() + ","
						+ e.getY() + ") " + e.getButton());
				manager.handleMouseEvent(e);
			} // handle mouse presses
		});

		// set up message font
		msgsFont = new Font("SansSerif", Font.BOLD, 24);
		msgsFont2 = new Font("SansSerif", Font.BOLD, 10);
		metrics = this.getFontMetrics(msgsFont);
		fpsCalculator = new UpdateCalculator(10);
		upsCalculator = new UpdateCalculator(10);
		manager = new SpriteManager(this);
		// bgImage = IMG_LOADER.getImage("background");
	}

	/**
	 * The main animation loop.
	 * 
	 * This function is triggered every period of time, the period is specified
	 * in the constructor.
	 * 
	 * To precisely control the time interval to keep the animation and update
	 * fluent, some methods are used.
	 * 
	 * 1. The animation loop contains 3 very important phases. Firstly, the
	 * gameUpdate() function will be triggered to update the game data. Then
	 * gameRender() will be called to prepare the GUI for the current state.
	 * Finally, paintScreen() will be called to actually paint the current
	 * animation GUI.
	 * 
	 * 2. Time is calculated in Nano time to accurately make the timing
	 * precisely. For example, if the picture takes 0.05 seconds to prepare, but
	 * the time allowed is 0.002 seconds. In other words, the picture takes
	 * longer than expected time, then this function will automatically record
	 * this, and will call update more times to make the game timing correct.
	 * That is, it will still update, but without rendering. To show this, one
	 * simple approach is to disconnect the electric source. As a result, the
	 * performance of the lap-top will be lower, and then look at the statistics
	 * shown in the game panel. The average FPS will be lower, but the UPS will
	 * stay almost as expected.
	 */
	@Override
	public void run() {
		long beforeTime, afterTime, timeDiff, sleepTime;
		long overSleepTime = 0L;
		int noDelays = 0;
		long overRunTime = 0L;

		gameStartTime = System.nanoTime();
		beforeTime = gameStartTime;

		running = true;

		while (running) {
			// update the game information
			gameUpdate();
			// render the state
			gameRender();
			// paint the current state onto the screen
			paintScreen();

			afterTime = System.nanoTime();
			timeDiff = afterTime - beforeTime;
			sleepTime = (PERIOD - timeDiff) - overSleepTime;

			// some time left in this cycle
			if (sleepTime > 0) {
				try {
					// nano -> ms
					Thread.sleep(sleepTime / 1000000L);
				} catch (InterruptedException ex) {
				}
				overSleepTime = (System.nanoTime() - afterTime) - sleepTime;
			} else {
				overRunTime -= sleepTime;
				overSleepTime = 0L;
				// update the delay counter, and if excess a limit, give other
				// thread priority to run
				if (++noDelays >= NO_DELAYS_PER_YIELD) {
					Thread.yield();
					noDelays = 0;
				}
			}

			beforeTime = System.nanoTime();

			int skips = 0;
			while ((overRunTime > PERIOD) && (skips < MAX_FRAME_SKIPS)) {
				overRunTime -= PERIOD;
				// update state only, without rendering.
				gameUpdate();
				skips++;
			}
		}
	}

	/**
	 * Render the current game state.
	 */
	private void gameRender() {
		if (dbImage == null) {
			dbImage = createImage(PWIDTH, PHEIGHT);
			if (dbImage == null) {
				System.out.println("dbImage is null");
				return;
			} else {
				dbg = dbImage.getGraphics();
			}
		}

		// if there is a background image, draw it. if not use a black block
		// instead.
		if (bgImage == null) {
			dbg.setColor(Color.black);
			dbg.fillRect(0, 0, PWIDTH, PHEIGHT);
		} else {
			dbg.drawImage(bgImage, 0, 0, this);
		}

		this.paintComponents(dbg);

		manager.drawSprites(dbg);

		reportStats(dbg);

		/*
		 * simulating the situation, where the rendering is time-consuming.
		 */
		// try {
		// Thread.sleep((long) (Math.random() * 100));
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }

		if (manager.isGameOver()) {
			gameOverMessage(dbg);
		}

	}

	/**
	 * Report the game statistics on the game screen.
	 * 
	 * @param g
	 *            The graphics where the report is written.
	 */
	private void reportStats(Graphics g) {
		if (!gameOver) {
			timeSpentInGame = (int) ((System.nanoTime() - gameStartTime) / 1000000000L); // ns
		}

		g.setColor(Color.yellow);
		g.setFont(msgsFont);

		g.drawString("Time: " + timeSpentInGame + " secs", 15, 50);

		String ct = "";
		if (manager.isGameStarted()) {
			if (manager.getTurn() == SpriteManager.PLAYER_TURN) {
				ct = "Player";
			} else {
				ct = "Opponent";
			}
			g.drawString("Current Turn: " + ct, 315, 50);
		} else {
			g.setColor(Color.red);
			g.drawString("Game not Started" + ct, 315, 50);
			g.setColor(Color.yellow);
		}

		if (manager.isAiActivated()) {
			ct = "On";
		} else {
			ct = "Off";
		}
		g.drawString("AI: " + ct, 615, 50);

		g.setColor(Color.white);
		g.setFont(msgsFont2);
		g.drawString(
				"Average FPS: "
						+ decimalFormat.format(fpsCalculator.getResult()), 15,
				600);
		g.drawString(
				"Average UPS: "
						+ decimalFormat.format(upsCalculator.getResult()), 150,
				600);

		/*
		 * Game instruction.
		 */
		g.setColor(Color.yellow);
		g.drawString("Space: Restart the game", 10, 200);
		g.drawString("A: Turn On/Off AI", 10, 220);
		g.drawString("R: Hide [Opponent & Desk] Cards", 10, 240);
		g.drawString("O: Display [Opponents] Cards", 10, 260);
		g.drawString("D: Display [Desk] Cards", 10, 280);

		g.drawString(
				"Left/Right click on \"your\" turn to select/unselect card, middle button to dash.",
				10, 10);

		g.setColor(Color.black);
	}

	/**
	 * Show the game over message.
	 * 
	 * @param g
	 */
	private void gameOverMessage(Graphics g) {
		String msg = "Game Over!";

		int x = (PWIDTH - metrics.stringWidth(msg)) / 2;
		int y = (PHEIGHT - metrics.getHeight()) / 2;
		g.setColor(Color.red);
		g.setFont(msgsFont);
		g.drawString(msg, x, y);
	}

	/**
	 * Update the game state.
	 */
	private void gameUpdate() {
		if (!gameOver) {
			upsCalculator.update();
			manager.update();
		}
	}

	/**
	 * Paint the rendered game state onto the screen. Use active render to put
	 * the image onto screen
	 */
	private void paintScreen() {
		Graphics g;
		try {
			g = this.getGraphics();
			if ((g != null) && (dbImage != null)) {
				g.drawImage(dbImage, 0, 0, null);
				fpsCalculator.update();
			}

			// Synchronize the display
			Toolkit.getDefaultToolkit().sync();

			g.dispose();
		} catch (Exception e) {
			System.out.println("Graphics context error: " + e);
		}
	}

	/**
	 * When this component is added, this method would be triggered. In this
	 * method, it defines the start of the animation thread.
	 */
	public void addNotify() {
		// wait for the JPanel to be added to the JFrame before starting
		super.addNotify();
		startAnimation();
	}

	/**
	 * Start the animation thread.
	 */
	private void startAnimation() {
		if (animator == null || !running) {
			animator = new Thread(this);
			animator.start();
		}
	}

	/**
	 * For debug purpose.
	 * 
	 * @param str
	 *            The information of the report.
	 */
	public void report(String str) {
		escape.report("GamePanel:->" + str);
	}
}
