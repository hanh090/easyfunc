package algorithm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;

import algorithm.java.method.BNS;
import document.java.TypeDocument.FieldName;
import util.ParserUtil;


public abstract class TypeWeight {
	private List<Document> hitDocs;
	
	public enum WeightAlgorithm{
		BNS,
		CHI_SQUARE,
		ODDS_RATIO
	}
	
	public TypeWeight(List<Document> hitDocs) {
		this.hitDocs = hitDocs;
	}
	public static TypeWeight create(WeightAlgorithm algorithm, List<Document> hitDocs){
		switch (algorithm) {
		case BNS:
			
			return new BNS(hitDocs);

		default:
			break;
		}
		return null;
		
	}
	
	public double computeScore() {
		Integer[] factor = calculateFactor();
		double score = computeScore(factor[0], factor[1], factor[2], factor[3]);
		return score;
	}
	
	
	private Integer[] calculateFactor(){
		int A = compute_A_Factor();
		int C = this.hitDocs.size() - A;
		return null;
	}
	
	private int compute_A_Factor() {
		int result = 0;
		for (Document hitDoc : hitDocs) {
			Integer[] value = new Integer[4];
			// Store A+B at value[1] and fix it when computed A
			value[1] = Integer.parseInt(hitDoc.get(FieldName.METHOD_SIZE
					.getName()));
		}

		return 0;
	}
	/**
	 * Calculate 4 factor by order to positive_key, positive_nokey,
	 * negative_key, negative_nokey
	 * 
	 * @param keySize
	 *            sum of {@link #negativeKey_Factor} and
	 *            {@link #positiveKey_Factor}
	 * @param positiveSize
	 *            sum of {@link #positiveNoKey_Factor} and
	 *            {@link #positiveKey_Factor}
	 * @param domainSize
	 *            sum of 4 factor
	 * @return Map with type name is key and array of 4 factor is value
	 */
	public Map<String, Integer[]> calculateFactorMap(){
		Map<String, Integer[]> result = computePositiveTermFactor(hitDocs);
		
		int domainSize = ParserUtil.countAllElement("method");
		for (String key : result.keySet()) {
			//A
			int positive_term = result.get(key)[0];
			//B
			int positive_noTerm = result.get(key)[1] - positive_term;
			//C
			int negative_term = hitDocs.size() - positive_term;
			//D
			int negative_noTerm = domainSize - positive_term - positive_noTerm - negative_term;
			result.put(key, new Integer[]{positive_term,positive_noTerm,negative_term,negative_noTerm});
		}
		return result;
	}

	private Map<String, Integer[]> computePositiveTermFactor(
			List<Document> hitDocs) {
		Map<String, Integer[]> result = new HashMap<String, Integer[]>();
		for (Document hitDoc : hitDocs) {
			Integer[] value = new Integer[4];
			// Store A+B at value[1] and fix it when computed A
			value[1] = Integer.parseInt(hitDoc.get(FieldName.METHOD_SIZE
					.getName()));
			String id = hitDoc.get("id");
			if (result.containsKey(id)) {
				value[0] = result.get(id)[0]++;
			}
			result.put(id, value);
		}

		return result;
	}

	/**
	 * Return score responsible algorithm
	 * 
	 * @return
	 */
	protected abstract double computeScore(int A, int B, int C, int D);
	
}
