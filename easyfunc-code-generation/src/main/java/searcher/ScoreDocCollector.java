package searcher;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Scorer;

import document.wrapper.FieldName;
/**
 * Collector for store hit document and its score
 *
 */
public class ScoreDocCollector extends Collector {
	private IndexReader reader;
	private Map<String, Double> scoreDoc;
	private Scorer scorer;

	public ScoreDocCollector(IndexReader reader) {
		this.reader = reader;
		scoreDoc = new HashMap<String, Double>();
	}

	@Override
	public void setScorer(Scorer scorer) throws IOException {
		this.scorer = scorer;
	}

	@Override
	public void collect(int doc) throws IOException {
		scoreDoc.put(reader.document(doc).get(FieldName.TYPE_ID.getName()),
				(double) scorer.score());
	}

	@Override
	public void setNextReader(AtomicReaderContext context) throws IOException {
	}

	@Override
	public boolean acceptsDocsOutOfOrder() {

		return false;
	}

	public Map<String, Double> getScoreDoc() {
		return scoreDoc;
	}

}
