package algorithm.java.method;

import java.util.List;

import org.apache.lucene.document.Document;

import algorithm.TypeWeight;

public class OddsRatio extends TypeWeight {

	public OddsRatio(List<Document> hitDocs) {
		super(hitDocs);
	}

	@Override
	public double computeScore(int A, int B, int C, int D) {
		
		return 0;
	}


}
