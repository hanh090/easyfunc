package document.stat;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.math3.stat.StatUtils;
import org.apache.lucene.index.IndexReader;

public class NormalDistribution extends TermStats {

	private double mean;
	private double sd;

	public NormalDistribution() {
		super();
	}

	

	private double[] convertToDouble(Collection<Integer> values) {
		double[] result = new double[values.size()];
		int i = -1;
		for (Integer value : values) {
			result[++i] = value;
		}
		return result;
	}

	@Override
	protected void computeStatProperties(IndexReader reader,Map<String, Integer> termTable) {
		double[] values = convertToDouble(termTable.values());
		this.mean = computeMean(values);
		this.sd = computeSd(values);
	}

	private double computeMean(double[] values) {

		return StatUtils.mean(values);
	}

	private double computeSd(double[] values) {
		return StatUtils.variance(values);
	}

	
	public double getMean() {
		return mean;
	}

	public double getSd() {
		return sd;
	}

}
