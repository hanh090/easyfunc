package algorithm;

import algorithm.java.method.BNS;
import algorithm.java.method.ChiSquare;

public abstract class TypeWeight {
	/**
	 * A
	 */
	protected int A;
	/**
	 * B
	 */
	protected int B;
	/**
	 * C
	 */
	protected int C;
	/**
	 * D
	 */
	protected int D;

	public enum WeightAlgorithm {
		BNS, CHI_SQUARE, ODDS_RATIO
	}

	protected TypeWeight() {
	}

	/**
	 * Create type weight algorithm
	 * 
	 * @param algorithm
	 *            name of algorithm
	 * @return
	 */
	public static TypeWeight create(WeightAlgorithm algorithm) {
		switch (algorithm) {
		case BNS:
			return new BNS();
			
		case CHI_SQUARE:
			return new ChiSquare();

		default:
			break;
		}
		return null;

	}

	

	public void setFactors(int A, int B, int C, int D) {
		this.A = A;
		this.B = B;
		this.C = C;
		this.D = D;
	}
	
	public abstract double computeScore();

}
