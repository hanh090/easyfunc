package searcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Scorer;
/**
 * Class for hit document and don't care about score
 *
 */
public class HitDocCollector extends Collector {
	private List<Integer> hitDocs;
	
	public HitDocCollector() {
		hitDocs = new ArrayList<Integer>();
	}
	@Override
	public void setScorer(Scorer scorer) throws IOException {
	}

	@Override
	public void collect(int doc) throws IOException {
		hitDocs.add(doc);
	}

	@Override
	public void setNextReader(AtomicReaderContext context) throws IOException {
	}

	@Override
	public boolean acceptsDocsOutOfOrder() {

		return false;
	}
	public List<Integer> getHitDocs() {
		return hitDocs;
	}
	
}
