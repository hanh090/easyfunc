package algorithm.java.type;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.similarities.DefaultSimilarity;

import algorithm.TypeWeight;

public class TfIdf extends TypeWeight {

	public TfIdf(Term t) {
		super(t);
	}

	@Override
	protected void setSimilarity(IndexSearcher searcher) {
		searcher.setSimilarity(new DefaultSimilarity());
	}


}
