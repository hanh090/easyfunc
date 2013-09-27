package searcher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.SimilarityBase;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import algorithm.TypeWeight;
import algorithm.TypeWeight.WeightAlgorithm;

public class Searcher {
	protected IndexSearcher indexSearcher;
	private Query query;
	
	
	public enum DocumentKind {
		TYPE, METHOD
	}
	
	public static void main(String[] args){
		Searcher searcher = new Searcher("helllo abc", DocumentKind.METHOD);
	}

	public Searcher(String query, DocumentKind kind) {
		try {
			setIndexSearcher(kind);
			setQuery(query);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}

	public Searcher(Query query, DocumentKind kind) {
		try {
			setIndexSearcher(kind);
			this.query = query; 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	

	/**
	 * @return
	 * 
	 */
	public TopDocs doSearch() {
		try {
			SimilarityBase similarity = computeSimilarity();
			indexSearcher.setSimilarity(similarity);
			TopDocs top = indexSearcher.search(query, 20);
			return top;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	public SimilarityBase computeSimilarity() {
		double typeWeight = computeTypeWeight();
		double methodWeight = computeMethodWeight();
		return null;
	}

	

	private double computeTypeWeight() {
		HitSearcher hitSearcher = new HitSearcher(query,DocumentKind.METHOD);
		
		TopDocs topDocs = hitSearcher.doSearch();
		
		List<Document> docs = convertToDoc(topDocs); 
		TypeWeight typeWeight = TypeWeight.create(WeightAlgorithm.BNS, docs);
		
		double score = typeWeight.computeScore();
		return score;
	}
	
	private List<Document> convertToDoc(TopDocs topDocs) {
		List<Document> result = new ArrayList<Document>();
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			try {
				result.add(indexSearcher.doc(scoreDoc.doc));
			} catch (CorruptIndexException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private double computeMethodWeight() {
		
		return 0;
	}

	

	public IndexSearcher getIndexSearcher() {
		return indexSearcher;
	}

	public void setIndexSearcher(DocumentKind kind) throws IOException {
		switch (kind) {
		case TYPE:
			indexSearcher = new IndexSearcher(
					DirectoryReader
							.open(new SimpleFSDirectory(
									new File(
											"../easyfunc-indexing/src/main/resources/index/type/"))));
			break;
		case METHOD:
			indexSearcher = new IndexSearcher(
					DirectoryReader
							.open(new SimpleFSDirectory(
									new File(
											"../easyfunc-indexing/src/main/resources/index/method/"))));
			break;
		default:
			break;
		}
	}

	public Query getQuery() {
		return query;
	}

	public void setQuery(String query) throws ParseException {
		QueryParser queryParser = new QueryParser(Version.LUCENE_40, "method",
				new EnglishAnalyzer(Version.LUCENE_40));

		Query userQuery = queryParser.parse("\"" + query + "\"");
		
		this.query = userQuery;
		if(this.query instanceof PhraseQuery){
			((PhraseQuery) this.query).setSlop(100);
		}
	}

}
