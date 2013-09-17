import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

public class Searcher {
	private IndexSearcher indexSearcher;
	private QueryParser queryParser;

	public Searcher() {
		try {
			indexSearcher = new IndexSearcher(
					DirectoryReader
							.open(new SimpleFSDirectory(
									new File(
											"../easyfunc-indexing/src/main/resources/index/type/"))));

			queryParser = new QueryParser(Version.LUCENE_40, "method",
					new StandardAnalyzer(Version.LUCENE_40));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Searcher().doSearch();
	}

	public void doSearch() {
		try {
			Query userQuery = queryParser.parse("Appends the specified element");
			PhraseQuery phraseQuery = new PhraseQuery();
			phraseQuery.setSlop(100);
			Set<Term> terms = new HashSet<Term>();
			userQuery.extractTerms(terms);
			for (Term term : terms) {
				phraseQuery.add(term);
			}
			TopDocs top = indexSearcher.search(phraseQuery, 10);
			for (ScoreDoc docMatch : top.scoreDocs) {
				int i = docMatch.doc;
				System.out.println("Docid" + i);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
