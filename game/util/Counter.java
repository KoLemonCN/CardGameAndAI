package game.util;

/**
 * The counter class just simply count a integer number from 0 for some purpose.
 * 
 * Instead of using an Integer or int then apply some++, I tend to use Object to
 * perform as a counter.
 * 
 * Additionally, the Counter will take a piece of information in the
 * constructor, such that later, the programmer could see what the counter is
 * counting, what is it for.
 * 
 * Notice that the Counter can only count a limited number: an Integer in Java.
 * Thus the usage shall also consider the overflow issue.
 * 
 * However, this project will not require a huge number being counted, and the
 * overflow would never be an issue to produce any bugs, thus this class is made
 * as simple as possible to just manage the coding style simple.
 * 
 * @author Riki
 * 
 */
public class Counter {

	/**
	 * The counter to count a number, and it is synchronized.
	 */
	private volatile int counter;

	/**
	 * The description of the counter.
	 */
	private String description;

	/**
	 * 
	 * The constructor for the Counter. By default, the counter will be
	 * initialized to 0.
	 * 
	 * @param description
	 *            A description in String for the Counter.
	 */
	public Counter(String description) {
		this.description = description;
		this.counter = 0;
	}

	/**
	 * Synchronize the counting, add one each time.
	 */
	public synchronized void count() {
		counter++;
	}

	/**
	 * The the current value of the counter.
	 * 
	 * @return The count number.
	 */
	public int getCounter() {
		return counter;
	}

	/**
	 * Printing methods of the class for Debug purpose.
	 */
	public String toString() {
		return "Counter: (" + counter + ")\t " + description;
	}

	/**
	 * Reset the Counter to 0.
	 */
	public synchronized void reset() {
		counter = 0;
	}
}
