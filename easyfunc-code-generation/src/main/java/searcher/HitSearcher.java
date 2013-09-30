package searcher;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.search.Query;

public class HitSearcher extends Searcher {

	
	public HitSearcher(Query query, DocumentKind kind) {
		super(query, kind);
	}
	
	public List<Integer> countHitDoc() {
		try {
			TotalHitDocCollector hitDocCollector = new TotalHitDocCollector();
			indexSearcher.search(this.getQuery(), hitDocCollector);
			
			return hitDocCollector.getHitDocIds();
				
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
