package ai.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Convert an array to a specified set.
 * 
 * The set has the following property:
 * 
 * 1. No elements duplicated.
 * 
 * 2. All duplicated object would be put together, and sum up.
 * 
 * @author Riki
 * 
 */
public class ProbSet<T> {

	/**
	 * The stored elements.
	 */
	protected List<T> elements;

	/**
	 * The number of element.
	 */
	protected List<Integer> nums;

	/**
	 * The length of the set. That is: total elements.
	 */
	public final int length;

	/**
	 * The Constructor for the Set.
	 * 
	 * @param list
	 *            The list to be summarized.
	 */
	public ProbSet(List<T> list) {
		length = list.size();
		// store the actual element.
		elements = new ArrayList<T>();
		// store the number of the related element.
		nums = new ArrayList<Integer>();

		int index = -1;
		for (int i = 0; i < list.size(); i++) {
			T object = list.get(i);
			index = elements.indexOf(object);
			if (index < 0) {
				// add new element,
				// and initialize the number of the element to 1
				elements.add(object);
				nums.add(1);
			} else {
				// duplicated element found: update the element
				nums.set(index, nums.get(index) + 1);
			}
		}
	}

	/**
	 * Get the particular element number.
	 * 
	 * @param o
	 *            The element.
	 * @return The number of occurrence of the element.
	 */
	public int getElementCount(Object o) {
		int index = elements.indexOf(o);
		if (index < 0) {
			return 0;
		} else {
			return nums.get(index);
		}
	}

	/**
	 * The string representation of the set.
	 */
	public String toString() {
		String str = "{";
		for (int i = 0; i < elements.size(); i++) {
			str += " [" + elements.get(i) + "," + nums.get(i) + "] ";
		}
		str += "}";
		return str;
	}

	/**
	 * Get the length of the set.
	 * 
	 * @return The length of the set.
	 */
	public int length() {
		return length;
	}

	/**
	 * Get the size of the set.
	 * 
	 * @return The size of the set.
	 */
	public int size() {
		return elements.size();
	}

	/**
	 * Get the specified element.
	 * 
	 * @param i
	 *            The i-th element.
	 * @return The specified element.
	 */
	public T getElement(int i) {
		return elements.get(i);
	}
}