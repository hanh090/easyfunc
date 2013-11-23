package algorithm.java.type;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.similarities.BM25Similarity;

import algorithm.TypeWeight;

public class BM25 extends TypeWeight {

	public BM25(Term t) {
		super(t);
	}

	@Override
	protected void setSimilarity(IndexSearcher searcher) {
		searcher.setSimilarity(new BM25Similarity());
	}

}
