package searcher.nlp;

import java.io.IOException;

import main.Indexer;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;

import searcher.util.SearchUtils;
import utils.ResourceUtils;

/**
 * Lemmazation verb in English
 * 
 */
public class LemmazationVerb {
	/**
	 * Lemma from word
	 * 
	 * @param word
	 * @return
	 */
	public static String lemma(String word) {
		String result = null;
		String path = ResourceUtils.getClasspathBase(Indexer.class);
		try {
			IndexSearcher verbListSearcher = SearchUtils
					.createIndexSearcher(path + "/index/data/verbList");
			QueryParser queryParser = new QueryParser(Version.LUCENE_40,
					"verb", new WhitespaceAnalyzer(Version.LUCENE_40));
			StringBuilder wordBuilder = new StringBuilder(QueryParser.escape(word));
			while (true) {
				
				Query q = queryParser.parse(wordBuilder + "*");
				TopDocs topDocs = verbListSearcher.search(q, 1);
				if(topDocs.totalHits == 0){
					int lastIndex = wordBuilder.length() - 1;
					wordBuilder.deleteCharAt(lastIndex);
					continue;
				}
				for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
					result = verbListSearcher.doc(scoreDoc.doc).get("verb");
					return result;
				}
			}

		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return word;

	}

	public static void main(String[] args) {
		System.out.println(LemmazationVerb.lemma("removes!"));
		System.out.println(LemmazationVerb.lemma("Deletes,"));
	}

}
