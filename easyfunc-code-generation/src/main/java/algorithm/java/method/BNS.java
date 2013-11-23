package algorithm.java.method;

import java.util.Map;

import main.Indexer;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.ParserUtil;
import utils.ResourceUtils;
import algorithm.MethodWeight;

/**
 * Bi-Normal Separation
 * 
 * @see article in project's docs directory
 * 
 */
public class BNS extends MethodWeight {
	private static final double MIN_PROB = 0.0005;
	private static final double MAX_PROB = 0.9995;
	// Normal Distribution for True Positive Rate
	private NormalDistribution normalDist;
//
//	private static final Logger slf4jLogger = LoggerFactory
//			.getLogger(MethodWeight.class);

	public BNS(Map<String, Integer[]> termQuarter) {
		super(termQuarter);
		String indexingPath = ResourceUtils.getPath(Indexer.EASYFUNC_INDEXING);
		double mean = Double.valueOf(ParserUtil.getText(indexingPath
				+ "/src/main/resources/stat/termStat.xml", "mean"));
		double sd = Double.valueOf(ParserUtil.getText(indexingPath
				+ "/src/main/resources/stat/termStat.xml", "sd"));
		normalDist = new NormalDistribution(mean, sd);
	}

	@Override
	public double computeScore(String id) {
		Integer[] quarter = this.termQuarter.get(id);
		int truePositive = quarter[0];
		int falsePositive = quarter[1];
		int trueNegative = quarter[2];
		int falseNegative = quarter[3];
		double p = calcProb(((double) truePositive)
				/ (double) (truePositive + falsePositive));
		double e = calcProb(((double) trueNegative)
				/ (double) (trueNegative + falseNegative));
		double inverseCumulativeProbability = scale(normalDist
				.inverseCumulativeProbability(p));
		double inverseCumulativeProbability2 = scale(normalDist
				.inverseCumulativeProbability(e));
		double result = inverseCumulativeProbability
				- inverseCumulativeProbability2;
		// DocumentID positiveScore negativeScore
//		slf4jLogger.info("{}\t{}\t{}", id,
//				String.valueOf(inverseCumulativeProbability),
//				String.valueOf(inverseCumulativeProbability2));
		return result;
	}

	/**
	 * Scale score between 0 and 1
	 * 
	 * @param score
	 * @return
	 */
	private double scale(double score) {
		double MAX_SCORE = normalDist.inverseCumulativeProbability(MAX_PROB);
		double MIN_SCORE = normalDist.inverseCumulativeProbability(MIN_PROB);
		double factorA = 1 / (MAX_SCORE - MIN_SCORE);
		double factorB = -MIN_SCORE / (MAX_SCORE - MIN_SCORE);
		return factorA * score + factorB;
	}

	/**
	 * Calculate probability in range [0.0005- (1-0.0005)]
	 * 
	 * @param d
	 * @return
	 */
	private double calcProb(double d) {
		if (d < MIN_PROB)
			return MIN_PROB;
		else if (d > MAX_PROB)
			return MAX_PROB;
		else
			return d;
	}

}
