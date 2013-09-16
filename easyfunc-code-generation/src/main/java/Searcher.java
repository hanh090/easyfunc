import java.awt.geom.QuadCurve2D;
import java.io.File;
import java.io.IOException;







import java.lang.ref.PhantomReference;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.Token;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import util.IndexerUtil;

public class Searcher {
	private IndexSearcher indexSearcher;
	private QueryParser queryParser;
	public Searcher() {
		try {
			indexSearcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File(
					IndexerUtil.getOutput("method")))));
			queryParser = new QueryParser(Version.LUCENE_40, "method", new StandardAnalyzer(Version.LUCENE_40));
		} catch (CorruptIndexException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Searcher().doSearch();
	}

	public void doSearch() {
		
		
		try {
			PhraseQuery q  = new PhraseQuery();
			q.setSlop(10);
			Query query = queryParser.parse("the image on the screen");
			Set<Term> terms = new HashSet<Term>();
			query.extractTerms(terms);
			for (Term term : terms) {
				q.add(term);
			}

			TopDocs docs = indexSearcher.search(q, 10);
			for (ScoreDoc scoreDoc : docs.scoreDocs) {
				Document document = indexSearcher.doc(scoreDoc.doc);
				
				System.out.println(document.get("type"));
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
	}
}
