package algorithm.java.method;

import java.util.Map;

import algorithm.MethodWeight;

public class ChiSquare extends MethodWeight {

	public ChiSquare(Map<String, Integer[]> termQuater) {
		super(termQuater);
	}

	@Override
	public double computeScore(String id) {
		// tuSo
		Integer[]quarters = this.termQuarter.get(id);
		int truePositive = quarters[0];
		int falsePositive = quarters[1];
		int trueNegative = quarters[2];
		int falseNegative = quarters[3];
		double numerator = (double) (truePositive + falsePositive + trueNegative + falseNegative)
				* Math.pow((double) (truePositive * falseNegative - falsePositive * trueNegative), 2);
		// mauSo
		double denominator = (double) (truePositive + trueNegative) * (truePositive + falsePositive) * (falsePositive + falseNegative) * (trueNegative + falseNegative);
		return Math.log(numerator / denominator);
	}

}
