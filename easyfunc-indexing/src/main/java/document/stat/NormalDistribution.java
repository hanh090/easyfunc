package document.stat;

import java.util.Collection;

import org.apache.commons.math3.stat.StatUtils;
import org.apache.lucene.index.IndexReader;

public class NormalDistribution extends TermStats {

	private double mean;
	private double sd;
	
	public NormalDistribution(IndexReader reader) {
		super(reader);
		double[] values = convertToDouble(termTable.values());
		this.mean = StatUtils.mean(values);
		this.sd = StatUtils.variance(values );
	}
	private double[] convertToDouble(Collection<Integer> values) {
		double[] result = new double[values.size()];
		int i = -1;
		for (Integer value : values) {
			result[++i] = value;
		}
		return result;
	}

	public double getMean() {
		return mean;
	}

	public double getSd() {
		return sd;
	}

}
