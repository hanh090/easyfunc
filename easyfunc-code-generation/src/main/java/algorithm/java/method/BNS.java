package algorithm.java.method;

import java.util.List;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.lucene.document.Document;

import algorithm.TypeWeight;

public class BNS extends TypeWeight{

	public BNS(List<Document> hitDocs) {
		super(hitDocs);
	}


	@Override
	public double computeScore(int A, int B, int C, int D) {
		NormalDistribution normalDistribution = new NormalDistribution();
		double cumulOfPositive = normalDistribution.cumulativeProbability((A/(A+C)));
		double cumulOfNegative = normalDistribution.cumulativeProbability((B/(B+D)));
		return cumulOfPositive - cumulOfNegative;
	}

	

}
