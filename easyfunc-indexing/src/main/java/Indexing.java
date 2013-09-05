import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;


public class Indexing {
	private Directory documents;
	private IndexWriter indexWriter;
	
	
	public static void main(){
		new Indexing().doIndex();
	}
	
	public Indexing() {
//		this.indexWriter = new IndexWriter(d, conf)
	}
	public void doIndex(){
		buildDocument();
		indexing();
	}

	private void buildDocument() {
		
	}
	
	private void indexing() {
	
	}
}
