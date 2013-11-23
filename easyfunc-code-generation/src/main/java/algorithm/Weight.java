package algorithm;

import java.util.Map;

public interface Weight {

	/**
	 * Compute score table for all document
	 * @return map with key is class id and value is score of responsible document
	 */
	Map<String,Double> computeScoreTable();
}
