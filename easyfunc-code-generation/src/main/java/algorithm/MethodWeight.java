package algorithm;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Term;

import searcher.util.SearchUtils;
import algorithm.java.method.BNS;
import algorithm.java.method.ChiSquare;
import document.wrapper.FieldName;
import document.wrapper.IndexKind;
/**
 * Class to compute score base on method index but final result base on type document
 *
 */
public abstract class MethodWeight implements Weight {
	/**
	 * Map with key is id of class and value is 4 sub part of result set: true-positive, false-positive, true-negative, false-negative
	 */
	protected Map<String,Integer[]> termQuarter;

	public enum MethodWeightAlg {
		BNS, CHI_SQUARE, ODDS_RATIO, INFOMATION_GAIN
	}

	protected MethodWeight(Map<String,Integer[]> termQuater) {
		this.termQuarter = termQuater;
	}

	/**
	 * Create weight algorithm base method index
	 * 
	 * @param algorithm
	 *            name of algorithm
	 * @return
	 */
	public static MethodWeight create(MethodWeightAlg algorithm,Map<String,Integer[]> termQuarter) {
		switch (algorithm) {
		case BNS:
			return new BNS(termQuarter);

		case CHI_SQUARE:
			return new ChiSquare(termQuarter);

		default:
			break;
		}
		return null;

	}
	
	/**
	 * Compute score for specify document responsible id
	 * @return
	 */
	public abstract double computeScore(String id);
	
	@Override
	public Map<String,Double> computeScoreTable(){
		Map<String, Double> result  = new HashMap<String, Double>();
		for (String key : this.termQuarter.keySet()) {
			result.put(key, computeScore(key));
		}
		
		return result;
	}

	public static MethodWeight create(MethodWeightAlg malg, Term term) {
		try {
			Map<String,Integer[]> termQuarter = computeTermQuarter(term);
			return create(malg, termQuarter);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Compute term frequency table with key is id of document and value is 4 value of result set: true-positive, false-positive, true-negative, false-negative
	 * 
	 * @param term
	 * @return
	 * @throws CorruptIndexException
	 * @throws IOException
	 */
	private static Map<String, Integer[]> computeTermQuarter(Term term)
			throws CorruptIndexException, IOException {
		Map<String, Integer[]> result = new HashMap<String, Integer[]>();
		IndexReader reader = SearchUtils.createIndexSearcher(IndexKind.METHOD).getIndexReader();

		DocsEnum docsEnum = MultiFields.getTermDocsEnum(
				reader,
				MultiFields.getLiveDocs(reader),
				term.field(), term.bytes(), true);

		int docId = -1;
		while ((docId = docsEnum.nextDoc()) != DocsEnum.NO_MORE_DOCS) {
			String id = reader.document(docId).get(FieldName.TYPE_ID.getName());
			//Values store by serialize: truePositive, falsePositive, trueNegative, falseNegative
			Integer[] values = new Integer[4];
			if (result.containsKey(id)) {
				values = result.get(id);
				values[0]++;
			} else {
				//A-True-positive
				values[0] = 1;
				//B-False-Positive
				values[1] = Integer.parseInt(reader.document(docId).get(
						FieldName.METHOD_SIZE.getName()));
				values[2] = 0;
				values[3] = reader.numDocs();
			}
			result.put(id, values);
		}
		// Calculate sum of  term frequent: truePositive + trueNegative
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
	
	

}
