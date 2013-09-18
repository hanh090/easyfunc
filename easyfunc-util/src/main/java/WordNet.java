import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.wordnet.SynLookup;
import org.apache.lucene.wordnet.Syns2Index;

/**
 * WorNet index
 * 
 * @see Syns2Index
 */
public final class WordNet {
	public final static boolean DEBUG = true;
	/**
	 * Where locate wordnet database
	 */
	public static final String RESOURCES_LIB_WORDNET = "../resources/lib/WordNet-3.0/wn_s.pl";
	/**
	 * Where store index of wordnet
	 */
	public static final String RESOURCES_INDEX_WORDNET = "../easyfunc-indexing/src/main/resources/index/wordnet";

	final static class CountingCollector extends Collector {
		public int numHits = 0;

		@Override
		public void setScorer(Scorer scorer) throws IOException {
		}

		@Override
		public void collect(int doc) throws IOException {
			numHits++;
		}

		@Override
		public void setNextReader(IndexReader reader, int docBase) {
		}

		@Override
		public boolean acceptsDocsOutOfOrder() {
			return true;
		}
	}

	/**
	 * Location which path to {@code wn_s.pl} file
	 */
	private String pathToPrologFile;
	/**
	 * Location which store wordnet index. Note: location must not exist(so fun)
	 */
	private String pathToStoredIndex;

	public static void main(String[] args) {
		WordNet wordNet = new WordNet(RESOURCES_LIB_WORDNET,
				RESOURCES_INDEX_WORDNET);
		try {
			//wordNet.doWornetIndex();
			wordNet.doLookupSynonym("search");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Throwable e) {

			e.printStackTrace();
		}

	}

	public WordNet(String pathToPrologFile, String pathToStoredIndex) {
		this.pathToPrologFile = pathToPrologFile;
		this.pathToStoredIndex = pathToStoredIndex;
	}

	/**
	 * Convert WordNet database to Lucene index
	 * 
	 * @throws Throwable
	 */
	public void doWornetIndex() throws Throwable {
		while (!testPath()) {
			System.out.println(deleteStoredFolder());
		}

		Syns2Index.main(new String[] { pathToPrologFile, pathToStoredIndex });

	}

	public boolean deleteStoredFolder() throws IOException {
		File file = new File(pathToStoredIndex);
		FileUtils.forceDelete(file);
		return file.delete();
	}

	/**
	 * Test path of resource
	 * 
	 * @return
	 */
	private boolean testPath() {
		File test1 = new File(pathToPrologFile);
		File test2 = new File(pathToStoredIndex);
		return test1.exists() && !test2.exists();

	}

	/**
	 * Perform synonym expansion on a query.
	 * 
	 * @param query
	 * @param searcher
	 * @param a
	 * @param field
	 * @param boost
	 * @return
	 * @throws IOException
	 */
	public Query doExpandQuery(String query, IndexSearcher searcher, Analyzer a,
			String field, float boost) throws IOException {
		return SynLookup.expand(query, searcher, a, field, boost);
	}
	/**
	 * Lookup synonyms list
	 * @param word
	 * @throws IOException
	 */
	public void doLookupSynonym(String word)
			throws IOException {

		FSDirectory directory = FSDirectory.open(new File(pathToStoredIndex));
		IndexSearcher searcher = new IndexSearcher(directory, true);

		Query query = new TermQuery(new Term(Syns2Index.F_WORD, word));
		CountingCollector countingCollector = new CountingCollector();
		searcher.search(query, countingCollector);

		if (countingCollector.numHits == 0) {
			System.out.println("No synonyms found for " + word);
		} else {
			System.out.println("Synonyms found for \"" + word + "\":");
		}

		ScoreDoc[] hits = searcher.search(query, countingCollector.numHits).scoreDocs;

		for (int i = 0; i < hits.length; i++) {
			Document doc = searcher.doc(hits[i].doc);

			String[] values = doc.getValues(Syns2Index.F_SYN);

			for (int j = 0; j < values.length; j++) {
				System.out.println(values[j]);
			}
		}

		searcher.close();
		directory.close();
	}
}
