package algorithm.java.method;

import java.util.List;

import org.apache.lucene.document.Document;

import algorithm.TypeWeight;

public class ChiSquare extends TypeWeight {

	public ChiSquare(List<Document> hitDocs) {
		super(hitDocs);
	}


	@Override
	public double computeScore(int A, int B, int C, int D) {
		//tuSo
		double numerator = (A + B + C + D)*Math.pow((A*D - B*C), 2);
		//mauSo
		double denominator = (A+C)*(A+B)*(B + D)*(C+D);
		return Math.log(numerator/denominator);
	}

}
