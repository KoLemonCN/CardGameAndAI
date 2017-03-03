package game.util;

/**
 * This clock uses the Nano second as main measurement.
 * 
 * 1 Second = 1000 Millis seconds
 * 
 * 1 Millis second = 1,000,000 Nano seconds
 * 
 * The ultimate aim of this class is to count time as precise as possible.
 * 
 * The functions of the clock:
 * 
 * 1: Start the clock 2: Stop the clock 3: Get the time interval of the Start
 * point to Stop point in Nano second.
 * 
 * @author Riki
 * 
 */
public class Clock {

	/**
	 * The start time.
	 */
	private long start = 0L;

	/**
	 * The stop time.
	 */
	private long stop = 0L;

	/**
	 * The constant to turn Nano into Millis.
	 * 
	 * 1 Millis Second = 1,000,000 Nano Seconds
	 */
	public static long NANO_TO_MILLIS = 1000000L;

	/**
	 * The Empty constructor for this Clock object.
	 */
	public Clock() {
	}

	/**
	 * Set the start point of the clock.
	 */
	public void start() {
		this.start = System.nanoTime();
	}

	/**
	 * Set the stop point of the clock.
	 */
	public void stop() {
		this.stop = System.nanoTime();
	}

	/**
	 * Calculate the time interval between start point and stop point in Nano
	 * seconds.
	 * 
	 * @return The time interval between start and stop in Nano.
	 */
	public long getNanoTimeInterval() {
		return stop - start;
	}

	/**
	 * Get the Start Time.
	 * 
	 * @return The start time in Nano.
	 */
	public long getStartTime() {
		return start;
	}

	/**
	 * Get the Stop time.
	 * 
	 * @return Stop time in Nano.
	 */
	public long getStopTime() {
		return stop;
	}

	/**
	 * Get the constant {@code NANO_TO_MILLIS}.
	 * 
	 * @return The constant turning Nano to Millis.
	 */
	public static long getNanoToMillis() {
		return NANO_TO_MILLIS;
	}
}
