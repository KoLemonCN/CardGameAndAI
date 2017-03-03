package game.util;

/**
 * Calculate the Average Number of updating in a defined period.
 * 
 * This Class takes an int in the constructor, which specifies the time period
 * in seconds.
 * 
 * Then, the Object will automatically calculates the number that the method
 * update() being called in that given time.
 * 
 * The purpose is to generate the data for FPS (Frame Per Second) and UPS
 * (Update Per Second).
 * 
 * @author Riki
 * 
 */
public class UpdateCalculator {
	/**
	 * A list of counters.
	 */
	private Counter[] counters;

	/**
	 * A list of Clocks.
	 */
	private Clock[] clocks;

	/**
	 * The average number of calls in given time period.
	 */
	private double result;

	/**
	 * The clock for this Object.
	 */
	private Clock clock;

	/**
	 * The current index: indicates which clock in clocks will be used.
	 */
	private int index;

	/**
	 * The constructor for the UpdateCalculator.
	 * 
	 * @param time
	 *            The time period, in which the average number of calls will be
	 *            recorded and calculated.
	 */
	public UpdateCalculator(int time) {
		// initalize all variables.
		result = 0;
		index = 0;
		counters = new Counter[time];
		clocks = new Clock[time];
		for (int i = 0; i < time; i++) {
			counters[i] = new Counter("Count " + i + " of UpdateCalculator");
			clocks[i] = new Clock();
		}
		clock = new Clock();
	}

	/**
	 * The number of calls will be automatically recorded and calculated.
	 */
	public void update() {
		// if this is first time the update is called. start the clock.
		if (clock.getStartTime() == 0) {
			clock.start();
		}

		// add the number of count for each counters.
		for (int i = 0; i < counters.length; i++) {
			counters[i].count();
		}

		/*
		 * There are basically two phases:
		 * 
		 * 1: The time has not exceed the expected period time.
		 * 
		 * --calculate the average number of calls according to current number
		 * of calls.
		 * 
		 * 2: The time equals or exceed the expected period time.
		 * 
		 * --calculate the average number of calls according to a specific time
		 * period.
		 */

		// 1: The time has not exceed the expected period time.
		if (clocks[clocks.length - 1].getStartTime() == 0) {
			// stop the clock and calculate the average number of calls
			// according to current number of calls.
			clock.stop();
			long interval = clock.getNanoTimeInterval();
			result = 1000000000.0 * counters[index].getCounter() / interval;
			for (int i = 0; i < clocks.length; i++) {
				if (interval >= i * 1000000000l
						&& clocks[i].getStartTime() == 0) {
					clocks[i].start();
					counters[i].reset();
					break;
				}
			}
		} else {
			// 2: The time equals or exceed the expected period time.

			// after time secs
			// let the clock stop
			clocks[index].stop();
			// update the result
			long interval = clocks[index].getNanoTimeInterval();
			// --calculate the average number of calls according to a specific
			// time
			result = 1000000000.0 * counters[index].getCounter() / interval;
			// if the time interval has exceeded the expected time, reset the
			// clock and counters.
			if (interval >= 1000000000l * counters.length) {
				clocks[index].start();
				counters[index].reset();
				index = (index + 1) % counters.length;
			}
		}

	}

	/**
	 * The get the average number of calls in a time period.
	 * 
	 * @return The average number of calls in a time period.
	 */
	public double getResult() {
		return result;
	}
}
