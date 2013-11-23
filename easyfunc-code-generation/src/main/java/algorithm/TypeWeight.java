package algorithm;

import java.io.IOException;
import java.util.Map;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;

import document.wrapper.IndexKind;
import searcher.ScoreDocCollector;
import searcher.util.SearchUtils;
import algorithm.java.type.BM25;
import algorithm.java.type.TfIdf;
/**
 * Class to compute score base on type document
 *
 */
public abstract class TypeWeight implements Weight {
	public enum TypeWeightAlg {
		TF_IDF, BM25;
	}
	protected Term t;
	protected TypeWeight(Term t) {
		this.t = t;
	}
	
	public static TypeWeight create(TypeWeightAlg talg,Term t){
		switch (talg) {
		case TF_IDF:
			return new TfIdf(t);
		case BM25:
			return new BM25(t);
		default:
			break;
		}
		return null;
	}
	
	@Override
	public Map<String, Double> computeScoreTable() {
		try {
			IndexSearcher defaultSearch = SearchUtils.createIndexSearcher(IndexKind.TYPE);
			ScoreDocCollector scoreDocCollector = new ScoreDocCollector(
					defaultSearch.getIndexReader());
			setSimilarity(defaultSearch);
			defaultSearch.search(new TermQuery(t), scoreDocCollector);

			return scoreDocCollector.getScoreDoc();

		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected abstract void setSimilarity(IndexSearcher searcher);
	
}
