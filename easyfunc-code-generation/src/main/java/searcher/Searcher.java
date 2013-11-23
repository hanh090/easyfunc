package searcher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import main.CodeGenerationMain;
import opennlp.tools.postag.POSModel;
import opennlp.tools.util.InvalidFormatException;

import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import searcher.nlp.EFPosTagger;
import searcher.nlp.RelateSentence;
import searcher.nlp.RelateSentence.RelateAlgorithm;
import searcher.nlp.WordnetTag;
import searcher.util.SearchUtils;
import utils.ResourceUtils;
import algorithm.MethodWeight;
import algorithm.MethodWeight.MethodWeightAlg;
import algorithm.TypeWeight;
import algorithm.TypeWeight.TypeWeightAlg;
import document.wrapper.FieldName;
import document.wrapper.IndexKind;

public class Searcher {
	// private static final boolean DEBUG = true;
	private static final long TIME_OUT = 30000;
	private static final int MAX_DOCS = 10;

	private String originalQuery;
	private Query query;

	public Searcher(String query) {
		this.originalQuery = query;
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
		Logger logger = LoggerFactory.getLogger("search");
		logger.debug("\n\n\nOriginal query:{}" + originalQuery);
		List<Document> result = new ArrayList<Document>();
		try {
			Map<String, Double> bestDocument = chooseBestType(
					MethodWeightAlg.BNS, TypeWeightAlg.TF_IDF, MAX_DOCS);

			Map<String, Double> methodWeightTable = chooseBestMethod(
					bestDocument, MAX_DOCS);
			int i = 0;
			logger.debug("Result size={}",  methodWeightTable.size());
			for (Entry<String, Double> entry : methodWeightTable.entrySet()) {
				IndexSearcher methodSearcher = SearchUtils
						.createIndexSearcher(IndexKind.METHOD);
				Document doc = methodSearcher.doc(Integer.parseInt(entry
						.getKey()));
				
				logger.debug("Result {}:methodName={}:typeId={}:score={}", ++i,
						doc.get(FieldName.METHOD_NAME.getName()), doc.get(FieldName.TYPE_ID.getName()), entry.getValue());
				result.add(doc);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;

	}

	/**
	 * Choose best type document relative with query based on method weight
	 * algorithm and type weight algorithm
	 * 
	 * @param malg
	 *            algorithm which use to compute method-based score
	 * @param max
	 *            maximum number of document will return
	 * @return SortedMap by score with key is type-id and value is score
	 * @see MethodWeightAlg
	 * @throws IOException
	 * @throws CorruptIndexException
	 * 
	 */
	public Map<String, Double> chooseBestType(MethodWeightAlg malg,
			TypeWeightAlg talg, int max) throws CorruptIndexException,
			IOException {

		long start = System.currentTimeMillis();

		Map<String, Double> result = new HashMap<String, Double>();

		Set<Term> terms = new HashSet<Term>();
		// query.extractTerms(terms);
		if (query instanceof BooleanQuery) {

			List<BooleanClause> clauses = ((BooleanQuery) query).clauses();
			for (BooleanClause term : clauses) {
				String[] parseClause = term.getQuery().toString().split(":");
				terms.add(new Term(parseClause[0], parseClause[1]));
			}
			;
		}

		for (Term term : terms) {
			Map<String, Double> methodScoreTable = MethodWeight.create(malg,
					term).computeScoreTable();
			Map<String, Double> typeScoreTable = TypeWeight.create(talg, term)
					.computeScoreTable();

			for (String key : typeScoreTable.keySet()) {
				double typeScore = typeScoreTable.get(key);
				double methodScore = methodScoreTable.get(key);
				// TODO:Modifield in here :)
				double finalScore = 1 * typeScore + 10 * methodScore;
				if (result.containsKey(key)) {
					double preTermScore = result.get(key);
					finalScore += preTermScore;
				}

				result.put(key, finalScore);
			}

		}

		result = sortByValue(result);

		Map<String, Double> topTypeWeight = new HashMap<String, Double>();
		int i = 0;
		for (Entry<String, Double> entry : result.entrySet()) {
			topTypeWeight.put(entry.getKey(), entry.getValue());
			i++;
			if (i == max)
				break;
		}

		System.out.println("Time to compute type weight is "
				+ String.valueOf(System.currentTimeMillis() - start));
		return topTypeWeight;
	}

	private Map<String, Double> sortByValue(Map<String, Double> map) {
		TreeMap<String, Double> sortedMap = new TreeMap<String, Double>(
				new ScoreComparator(map));
		sortedMap.putAll(map);
		return sortedMap;
	}

	/**
	 * Choose best method relative with query by nature language processing
	 * 
	 * @param bestType
	 *            list id of type document
	 * @param maxDocs
	 * @return sorted map by score with key is the ordinary number of document
	 *         in index reader of method-doc and value is score.Note that: when
	 *         index become larger, we need information of index reader
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public Map<String, Double> chooseBestMethod(Map<String, Double> bestType,
			int maxDocs) throws InvalidFormatException, IOException {

		long start = System.currentTimeMillis();
		Map<String, Double> result = new HashMap<String, Double>();

		RelateSentence relateSentence = new RelateSentence(RelateAlgorithm.WUP);

		String codeGenPath = ResourceUtils
				.getPath(CodeGenerationMain.EASYFUNC_CODE_GENERATION);
		POSModel model = new POSModel(new File(codeGenPath
				+ "/src/main/resources/model/english/postag/en-pos-maxent.bin"));

		EFPosTagger posTagQuery = (EFPosTagger) new EFPosTagger(originalQuery,
				model).process();
		String[] verbsInQuery = posTagQuery.filter(WordnetTag.VERB);
		for (Entry<String, Double> typeEntry : bestType.entrySet()) {
			Map<Integer, Document> methodDocs = findMethodDoc(typeEntry
					.getKey());
			for (Entry<Integer, Document> methodEntry : methodDocs.entrySet()) {
				Document methodDoc = methodEntry.getValue();
				System.out.println("Document: "
						+ methodDoc.get(FieldName.TYPE_ID.getName())
						+ "--------------------------");
				String[] sentences = methodDoc.get(
						FieldName.METHOD_DESC.getName()).split("\\.");

				String wornetTaggedQuery = posTagQuery.toWordnetFormat();
				String wordnetTaggedSentence = ((EFPosTagger) (new EFPosTagger(
						"It " + sentences[0], model).process()))
						.toWordnetFormat();

				double sentenceSim = relateSentence.computeSimilarity(
						wordnetTaggedSentence, wornetTaggedQuery, model);

				double nameSim = computeNameSimilarity(methodDoc, verbsInQuery);

				double finalSim = nameSim + sentenceSim;
				result.put(String.valueOf(methodEntry.getKey()), finalSim);
			}
		}
		result = sortByValue(result);
		System.out.println("Time to compute method weight is "
				+ String.valueOf(System.currentTimeMillis() - start));
		return result;
	}

	/**
	 * Get method-document from {@code docID}
	 * 
	 * @param query
	 * @return
	 */
	private Map<Integer, Document> findMethodDoc(String query) {
		Map<Integer, Document> result = null;
		try {
			QueryParser queryParser = new QueryParser(Version.LUCENE_40,
					FieldName.TYPE_ID.getName(), new EnglishAnalyzer(
							Version.LUCENE_40));

			Query q = queryParser.parse(query);

			result = findMethodDoc(q);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Get method-document from query {@code q}
	 * 
	 * @param q
	 * @return
	 */
	private Map<Integer, Document> findMethodDoc(Query q) {
		Map<Integer, Document> result = new HashMap<Integer, Document>();
		try {
			IndexSearcher searchTypeDoc = SearchUtils
					.createIndexSearcher(IndexKind.METHOD);

			HitDocCollector hitDoc = new HitDocCollector();
			searchTypeDoc.search(q, hitDoc);
			for (Integer hitDocId : hitDoc.getHitDocs()) {
				result.put(hitDocId, searchTypeDoc.doc(hitDocId));
			}

		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Compute similarity of name method of {@code document} from
	 * {@code verbsInQuery}
	 * 
	 * @param document
	 * @param verbsInQuery
	 *            verb from query
	 * @return
	 */
	@SuppressWarnings("Fix later")
	private double computeNameSimilarity(Document document,
			String[] verbsInQuery) {
		String methodName = document.get(FieldName.METHOD_NAME.getName());
		for (String verb : verbsInQuery) {
			if (methodName.contains(verb))
				return 0.5;
		}
		return 0;
	}

	public Query getQuery() {
		return query;
	}

	public String getOriginalQuery() {
		return originalQuery;
	}

}
