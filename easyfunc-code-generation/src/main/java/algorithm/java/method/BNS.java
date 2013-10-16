package algorithm.java.method;

import org.apache.commons.math3.distribution.NormalDistribution;

import util.ParserUtil;
import algorithm.TypeWeight;

/**
 * Bi-Normal Separation
 * 
 * @see article in project's docs directory
 * 
 */
public class BNS extends TypeWeight {
	// Normal Distribution for True Positive Rate
	private NormalDistribution normalDist;
	

	public BNS() {
		super();
		double mean = Double.valueOf( ParserUtil.getText("../easyfunc-indexing/src/main/resources/stat/termStat.xml", "mean"));
		double sd = Double.valueOf(ParserUtil.getText("../easyfunc-indexing/src/main/resources/stat/termStat.xml", "sd"));
		normalDist = new NormalDistribution(mean, sd);
	}


	@Override
	public double computeScore() {
		double p = calcProb(((double)A)/(double)(A+C));
		double e = calcProb(((double)B)/(double)(B+D));
		double inverseCumulativeProbability = normalDist.inverseCumulativeProbability(p);
		double inverseCumulativeProbability2 = normalDist.inverseCumulativeProbability(e);
		double result  = inverseCumulativeProbability - inverseCumulativeProbability2;
		return result;
	}

	/**
	 * Calculate probability in range [0.0005- (1-0.0005)]
	 * @param d
	 * @return
	 */
	private double calcProb(double d) {
		if(d < 0.0005)
			return 0.0005;
		else if(d > (1-0.0005))
			return (1-0.0005);
		else
			return d;
	}

}
