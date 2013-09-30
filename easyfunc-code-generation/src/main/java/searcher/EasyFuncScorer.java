package searcher;

import java.io.IOException;

import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.Weight;

public class EasyFuncScorer extends Scorer {

	protected EasyFuncScorer(Weight weight) {
		super(weight);
	}

	@Override
	public float score() throws IOException {

		return 0;
	}

	@Override
	public int docID() {

		return 0;
	}

	@Override
	public int nextDoc() throws IOException {

		return 0;
	}

	@Override
	public int advance(int target) throws IOException {

		return 0;
	}

}
