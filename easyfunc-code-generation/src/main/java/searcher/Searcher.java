package searcher;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import searcher.nlp.RelateSentence;
import searcher.nlp.RelateSentence.RelateAlgorithm;
import algorithm.TypeWeight;
import algorithm.TypeWeight.WeightAlgorithm;
import document.java.TypeDocument.FieldName;

public class Searcher {
	private static final boolean DEBUG = true;
	private Query query;

	public Searcher(String query) {

		this.query = parseQuery(query);

	}

	private Query parseQuery(String query) {
		QueryParser queryParser = new QueryParser(Version.LUCENE_40,
				FieldName.METHOD_DESC.getName(), new EnglishAnalyzer(
						Version.LUCENE_40));

		Query userQuery = null;
		try {
			userQuery = queryParser.parse(query);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return userQuery;
	}

	/**
	 * Perform search action
	 * 
	 * @return sorted by score of method-document
	 * 
	 */
	public List<Document> doSearch() {
		List<Document> result = new ArrayList<Document>();
		try {
			Map<String, Double> typeWeightTable = computeTypeWeight();

			Map<String, Double> methodWeightTable = computeMethodWeight(typeWeightTable);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;

	}

	/**
	 * Compute type weight for specify term
	 * 
	 * @return Map with key is type id and value is
	 * @throws IOException
	 * @throws CorruptIndexException
	 */
	private Map<String, Double> computeTypeWeight()
			throws CorruptIndexException, IOException {

		long start = System.currentTimeMillis();

		Map<String, Double> typeWeightTable = new HashMap<String, Double>();

		Set<Term> terms = new HashSet<Term>();
		query.extractTerms(terms);

		for (Term term : terms) {

			Map<String, Integer[]> termFreqTable = termFreqTable(term);

			Map<String, Double> methodBasedScoreTable = scoreTable(
					WeightAlgorithm.BNS, termFreqTable);
			Map<String, Double> typeBasedScoreTable = scoreTable(term);

			// TF-IDF same factor like chisquare
			for (String key : typeBasedScoreTable.keySet()) {
				double typeBasedScore = typeBasedScoreTable.get(key);
				double methodBasedScore = methodBasedScoreTable.get(key);
				double termScore = typeBasedScore + methodBasedScore;
				if (typeWeightTable.containsKey(key)) {
					double preTermScore = typeWeightTable.get(key);
					termScore += preTermScore;
				}

				typeWeightTable.put(key, termScore);
			}

		}

		System.out.println("Time to compute type weight is "
				+ String.valueOf(System.currentTimeMillis() - start));
		return typeWeightTable;
	}

	/**
	 * Create term frequency table with key is id of document
	 * 
	 * @param term
	 * @return
	 * @throws CorruptIndexException
	 * @throws IOException
	 */
	private Map<String, Integer[]> termFreqTable(Term term)
			throws CorruptIndexException, IOException {
		Map<String, Integer[]> result = new HashMap<String, Integer[]>();
		IndexSearcher indexSearcher = createIndexSearcher(IndexKind.METHOD);

		DocsEnum docsEnum = MultiFields.getTermDocsEnum(
				indexSearcher.getIndexReader(),
				MultiFields.getLiveDocs(indexSearcher.getIndexReader()),
				term.field(), term.bytes(), true);

		int docId = -1;
		while ((docId = docsEnum.nextDoc()) != DocsEnum.NO_MORE_DOCS) {
			String id = indexSearcher.doc(docId).get(FieldName.ID.getName());
			Integer[] values = new Integer[4];

			if (result.containsKey(id)) {
				values = result.get(id);
				values[0]++;
			} else {
				values[0] = 1;
				values[1] = Integer.parseInt(indexSearcher.doc(docId).get(
						FieldName.METHOD_SIZE.getName()));
				values[2] = 0;
				values[3] = indexSearcher.getIndexReader().numDocs();
			}
			result.put(id, values);
		}
		// Calculate sum of term frequent
		int sumOfTermFreq = 0;
		for (Integer[] values : result.values()) {
			sumOfTermFreq += values[0];
		}
		// Update
		for (String key : result.keySet()) {
			Integer[] values = result.get(key);
			values[2] = sumOfTermFreq;
			result.put(key, values);
		}
		return result;
	}

	/**
	 * Score table for method-index kind
	 * 
	 * @param algorithm
	 * @param termFreqTable
	 * @return
	 */
	private Map<String, Double> scoreTable(WeightAlgorithm algorithm,
			Map<String, Integer[]> termFreqTable) {
		Map<String, Double> result = new HashMap<String, Double>();

		TypeWeight typeWeight = null;
		switch (algorithm) {

		case CHI_SQUARE:
			typeWeight = TypeWeight.create(WeightAlgorithm.CHI_SQUARE);

			break;
		case BNS:
			typeWeight = TypeWeight.create(WeightAlgorithm.BNS);
		default:
			break;
		}

		for (String sDocId : termFreqTable.keySet()) {
			Integer[] values = termFreqTable.get(sDocId);
			int A = values[0];
			int B = values[1] - A;
			int C = values[2] - A;
			int D = values[3] - A - B - C;
			typeWeight.setFactors(A, B, C, D);
			double score = typeWeight.computeScore();
			result.put(sDocId, score);
		}
		return result;

	}

	/**
	 * Score table for type-index kind
	 * 
	 * @param term
	 * @param termFreqTable
	 * @return
	 */
	private Map<String, Double> scoreTable(Term term) {
		try {
			IndexSearcher defaultSearch = createIndexSearcher(IndexKind.TYPE);
			ScoreDocCollector scoreDocCollector = new ScoreDocCollector(
					defaultSearch.getIndexReader());
			defaultSearch.search(new TermQuery(term), scoreDocCollector);

			return scoreDocCollector.getScoreDoc();

		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Compute score table for method
	 * 
	 * @param typeWeightTable
	 *            .keySet() list id of type document
	 * @return
	 */
	private Map<String, Double> computeMethodWeight(
			Map<String, Double> typeWeightTable) {

		Map<String, Double> result = new HashMap<String, Double>();
		String queryAfterAnalyze = getQueryAfterAnalyze();

		RelateSentence relateSentence = new RelateSentence(RelateAlgorithm.WUP);

		for (String typeId : typeWeightTable.keySet()) {
			List<Document> typeDocs = getDoc(typeId);
			for (Document document : typeDocs) {
				String[] sentences = document.get(
						FieldName.METHOD_DESC.getName()).split("\\.");
				for (String sentence : sentences) {

					double[][] matrixSim = relateSentence.computeSynonymMatrix(
							sentence, queryAfterAnalyze);

				}
			}
		}

		return result;
	}

	private String getQueryAfterAnalyze() {
		String result = "";
		Set<Term> terms = new HashSet<Term>();
		query.extractTerms(terms);
		for (Term term : terms) {
			result = result + term.text() + " ";
		}
		return result.trim();
	}

	/**
	 * Get document from {@code docID}
	 * 
	 * @param docID
	 * @return
	 */
	private List<Document> getDoc(String docID) {
		List<Document> result = new ArrayList<Document>();
		try {
			IndexSearcher searchTypeDoc = new IndexSearcher(
					DirectoryReader
							.open(new SimpleFSDirectory(
									new File(
											"../easyfunc-indexing/src/main/resources/index/method/"))));

			QueryParser queryParser = new QueryParser(Version.LUCENE_40,
					FieldName.ID.getName(), new EnglishAnalyzer(
							Version.LUCENE_40));
			Query q = queryParser.parse(docID);

			HitDocCollector hitDoc = new HitDocCollector();
			searchTypeDoc.search(q, hitDoc);
			for (Integer hitDocId : hitDoc.getHitDocs()) {
				result.add(searchTypeDoc.doc(hitDocId));
			}

		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	public IndexSearcher createIndexSearcher(IndexKind kind) throws IOException {
		IndexSearcher indexSearcher = null;
		switch (kind) {
		case TYPE:
			indexSearcher = new IndexSearcher(
					DirectoryReader
							.open(new SimpleFSDirectory(
									new File(
											"../easyfunc-indexing/src/main/resources/index/type/"))));
			break;
		case METHOD:
			indexSearcher = new IndexSearcher(
					DirectoryReader
							.open(new SimpleFSDirectory(
									new File(
											"../easyfunc-indexing/src/main/resources/index/method/"))));
			break;
		default:
			break;
		}

		return indexSearcher;
	}

	public Query getQuery() {
		return query;
	}

	/**
	 * POS Tagging the {@code sentence}
	 * 
	 * @param sentence
	 * @throws IOException
	 */
	public void posTag(String sentence) throws IOException {
		POSModel model = new POSModelLoader().load(new File(
				"src/main/resources/model/english/postag/en-pos-maxent.bin"));
		PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
		POSTaggerME tagger = new POSTaggerME(model);

		ObjectStream<String> lineStream = new PlainTextByLineStream(
				new StringReader(sentence));

		perfMon.start();
		String line;
		while ((line = lineStream.read()) != null) {

			String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE
					.tokenize(line);
			String[] tags = tagger.tag(whitespaceTokenizerLine);

			POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
			System.out.println(sample.toString());

			perfMon.incrementCounter();
		}
		perfMon.stopAndPrintFinalResult();
	}

	public static void main(String[] args) throws IOException {
		Searcher searcher = new Searcher("how to insert element into a list");
		searcher.posTag("how to insert element into a list");
	}

}
