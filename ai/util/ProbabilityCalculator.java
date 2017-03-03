package ai.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import ai.GameAI;

public class ProbabilityCalculator {

	private GameAI gameAI;

	public ProbabilityCalculator(GameAI ai) {
		this.gameAI = ai;
		gameAI.report("ProbabilityCalculator:->Initialization completed.");
	}

	/**
	 * The mathematical model for this problem could be represented as follow:
	 * 
	 * Assume that a set may contain several elements ( <= 4 ) that share the
	 * same value;
	 * 
	 * Given a set A = {a1, a2, a3, бн, an};
	 * 
	 * Given a subset of A, name it B = {?1,?2,бн,?m}, where all elements are
	 * unknown, and m+16 = n.
	 * 
	 * Given a set S = {?,?,?}, where all elements are given when use to perform
	 * calculation.
	 * 
	 * The question is: When given the set S, calculate the possibility that S
	 * is also an subset of B.
	 * 
	 * The relations:
	 * 
	 * A = {a1, a2, a3, бн, an}
	 * 
	 * B = {?1, ?2, бн, ?m | ?б╩A}
	 * 
	 * S = {s1,s2,s3бн,sk}
	 * 
	 * Probability(siб╩B) = ?, where 0<=i<=k
	 * 
	 * @param listA
	 *            the set of A.
	 * @param listB
	 *            the set of B.
	 * @param listS
	 *            the set of S.
	 * @return
	 */

	public double calc(List listA, int lengthOfB, List listS) {
		ProbSet setA = new ProbSet(listA); // Set_A
		ProbSet setS = new ProbSet(listS); // Set_B

		/*
		 * quick test if the possibility is 0. for example, if the length of
		 * setS is larger than the lenghtOfB or the element in setS not appeared
		 * or appeared more times than it does in setA
		 */
		if (testForZero(setA, lengthOfB, setS)) {
			return 0;
		}

		// all combinations of B.
		BigInteger cTotal = getCombinationNum(setA.length(), lengthOfB);

		int countSInA = 0;
		for (int i = 0; i < setS.size(); i++) {
			// getNumIn(A,[s1,s2,бн,sj])
			countSInA += setA.getElementCount(setS.getElement(i));
		}

		// __lengthOf(B)- lengthOf(S)
		// C0
		// __lengthOf(A)- getNumIn(A,[s1,s2,бн,sj])
		BigInteger c0 = getCombinationNum(setA.length() - countSInA, lengthOfB
				- setS.length());

		// __pi
		// ci
		// __getNumIn(A,si)
		BigInteger[] cs = new BigInteger[setS.size()];
		for (int i = 0; i < cs.length; i++) {
			cs[i] = getCombinationNum(setA.getElementCount(setS.getElement(i)),
					setS.getElementCount(setS.getElement(i)));
		}

		// C_1б┴ бн C_j б┴ C_0
		BigInteger sCombinations = c0;
		for (int i = 0; i < cs.length; i++) {
			sCombinations = sCombinations.multiply(cs[i]);
		}
		// all possible combination w.r.t S
		BigDecimal up = new BigDecimal(sCombinations);
		// all combinations of B
		BigDecimal down = new BigDecimal(cTotal);

		//P(p1s1,бн, pjsj)
		double result = up.doubleValue() / down.doubleValue();

		return result;
	}

	/**
	 * Test if the setS is valid.
	 * 
	 * @param setA
	 *            The setA.
	 * @param lengthOfB
	 *            The length of set B.
	 * @param setS
	 *            The set S.
	 * @return True if the probability is zero, otherwise false.
	 */
	private boolean testForZero(ProbSet setA, int lengthOfB, ProbSet setS) {
		if (setS.length > lengthOfB) {
			return true;
		}

		for (int i = 0; i < setS.size(); i++) {
			Object element = setS.getElement(i);
			int numS = setS.getElementCount(element);
			int numA = setA.getElementCount(element);
			if (numS > numA) {
				// System.out.println(element + " not valid: " + " setS_" + numS
				// + " setA_" + numA);
				return true;
			}
		}

		return false;
	}

	/**
	 * Calculate the combination: select cr from cn.
	 * 
	 * @param cn
	 *            All elements.
	 * @param cr
	 *            Number to be selected.
	 * @return The result of: select cr from cn.
	 */
	private BigInteger getCombinationNum(int cn, int cr) {
		if (cn < cr) {
			return BigInteger.ZERO;
		}

		BigInteger factCn = getFactorial(cn);
		BigInteger factCr = getFactorial(cr);
		BigInteger factDiff = getFactorial(cn - cr);

		// total = factCn/(factCr*factDiff)
		BigInteger total = factCn.divide(factCr.multiply(factDiff));

		return total;
	}

	/**
	 * Calculate the factorial of n.
	 * 
	 * @param n
	 *            the number
	 * @return Factorial of n.
	 */
	private BigInteger getFactorial(int n) {
		BigInteger fact = BigInteger.ONE;
		while (n > 1) {
			fact = fact.multiply(new BigInteger(Integer.toString(n)));
			n--;
		}
		return fact;
	}

	/**
	 * The simple test for the probability calculator.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ProbabilityCalculator prob = new ProbabilityCalculator(null);

		for (int i = 0; i < 17; i++) {
			System.out.println(i + "\t" + prob.getCombinationNum(16, i));
		}

		int[] as = { 1, 1, 1, 2, 2, 2, 2, 3, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
				13 };
		int[] bs = new int[13];
		int[] ss = { 1, 1, 2, 2 };

		ArrayList<Integer> listA = new ArrayList<Integer>();
		for (int i = 0; i < as.length; i++) {
			listA.add(as[i]);
		}
		ArrayList<Integer> listS = new ArrayList<Integer>();
		for (int i = 0; i < ss.length; i++) {
			listS.add(ss[i]);
		}
		System.out.println(listA.size() + " " + listA);
		System.out.println(bs.length);
		System.out.println(listS.size() + " " + listS);
		System.out.println("----------------------------");

		System.out.println(prob.calc(listA, bs.length, listS));

		as = new int[] { 1, 1, 1, 1, 1, 2, 2, 2, 2, 2 };
		bs = new int[2];
		ss = new int[] { 2, 1 };

		listA = new ArrayList<Integer>();
		for (int i = 0; i < as.length; i++) {
			listA.add(as[i]);
		}
		listS = new ArrayList<Integer>();
		for (int i = 0; i < ss.length; i++) {
			listS.add(ss[i]);
		}
		System.out.println(listA.size() + " " + listA);
		System.out.println(bs.length);
		System.out.println(listS.size() + " " + listS);
		System.out.println("----------------------------");

		System.out.println(prob.calc(listA, bs.length, listS));

	}

}
