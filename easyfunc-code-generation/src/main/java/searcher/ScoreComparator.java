package searcher;

import java.util.Comparator;
import java.util.Map;
/**
 * Comparator for score value
 * @param <T>
 * @param <V>
 *
 */
/**
 * Comparator for score value
 *
 */
public class ScoreComparator implements Comparator<String> {
	Map<String, Double> map;

	public ScoreComparator(Map<String, Double> map) {
		this.map = map;
	}

	@Override
	public int compare(String o1, String o2) {
		if (map.get(o1) >= map.get(o2)) {
			return -1;
		} else {
			return 1;
		} // returning 0 would merge keys
	}
}