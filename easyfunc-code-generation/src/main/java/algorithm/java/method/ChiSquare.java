package algorithm.java.method;

import algorithm.TypeWeight;

public class ChiSquare extends TypeWeight {

	@Override
	public double computeScore() {
		// tuSo
		double numerator = (double) (A + B + C + D)
				* Math.pow((double) (A * D - B * C), 2);
		// mauSo
		double denominator = (double) (A + C) * (A + B) * (B + D) * (C + D);
		return Math.log(numerator / denominator);
	}

}
