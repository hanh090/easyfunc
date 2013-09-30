package searcher;

import java.io.IOException;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Scorer;

public class EasyFuncCollector extends Collector {

	@Override
	public void setScorer(Scorer scorer) throws IOException {
	}

	@Override
	public void collect(int doc) throws IOException {
	}

	@Override
	public void setNextReader(AtomicReaderContext context) throws IOException {
	}

	@Override
	public boolean acceptsDocsOutOfOrder() {
		
		return false;
	}

}
