package searcher.nlp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import opennlp.tools.postag.POSModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.cmu.lti.jawjaw.pobj.POS;
import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.JiangConrath;
import edu.cmu.lti.ws4j.impl.WuPalmer;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;

/**
 * Evaluate two sentence in nature language
 * 
 */
public class RelateSentence {

	/**
	 * Factor for similarity between 2 verb
	 */
	public static final double FACTOR_VERB_SIM = 3;

	/**
	 * Factor for similarity between 2 verb
	 */
	public static final double FACTOR_NOUN_SIM = 1;

	/**
	 * Stored score and it's information of two word
	 * 
	 */
	private class RelatedUnit {
		int pos1;
		int pos2;
		POS tag;
		double score;

		/**
		 * Constructor related unit with position of list word, tag of two word
		 * and score
		 * 
		 * @param pos1
		 *            position in list word 1
		 * @param pos2
		 *            position in list word 2
		 * @param tagOfPair
		 *            tag of two word
		 * @param score
		 *            score
		 */
		public RelatedUnit(int pos1, int pos2, POS tagOfPair, double score) {
			this.pos1 = pos1;
			this.pos2 = pos2;
			this.tag = tagOfPair;
			this.score = score;
		}

		@Override
		public String toString() {

			return "score[" + this.pos1 + "][" + this.pos2 + "]= " + score;
		}

	}

	/**
	 * Maximum comparator
	 * 
	 */
	private class RelatedComparator implements Comparator<RelatedUnit> {

		@Override
		public int compare(RelatedUnit o1, RelatedUnit o2) {
			if (o1.score >= o2.score)
				return -1;
			else
				return 1;
		}

	}

	public enum RelateAlgorithm {
		WUP, JCN, LCH, LIN
	}

	private ILexicalDatabase db;
	private RelatednessCalculator algorithm;

	public RelateSentence(RelateAlgorithm algorithm) {

		db = new NictWordNet();
		WS4JConfiguration ws4j = WS4JConfiguration.getInstance();
		ws4j.setMFS(true);
		ws4j.setTrace(false);
		ws4j.setCache(true);
		switch (algorithm) {
		case WUP:
			this.algorithm = new WuPalmer(db);
			break;
		case JCN:
			this.algorithm = new JiangConrath(db);
			break;
		default:
			break;
		}
	}

	/**
	 * Compute similarity score between Wordnet POS-Tagged sentence
	 * 
	 * @param sentence1
	 *            sentence extract from
	 * @param query
	 *            Wordnet POS-Tagged sentence
	 * @return
	 */
	public double computeSimilarity(String sentence1, String query,
			POSModel model) {
		Logger logger = LoggerFactory.getLogger("similarity");
		logger.debug(
				"\n\n\nCalculate similarity score between query={} and sentence1={}",
				query, sentence1);
		double result = 0;

		String[] listWords1 = filterSupportPOS(sentence1);
		String[] listWords2 = filterSupportPOS(query);
		List<RelatedUnit> computeSynonymMatrix = computeSynonymMatrix(
				listWords1, listWords2, true);
		for (RelatedUnit unit : computeSynonymMatrix) {
			logger.debug("Optimized score between word {} and {} is score={}",
					listWords1[unit.pos1], listWords2[unit.pos2], unit.score);
			result += unit.score / (listWords1.length + listWords2.length);
		}
		return result;

	}

	/**
	 * Filter word which supported in your configure, some words which don't
	 * support will be remove
	 * 
	 * @param sentence
	 * @return
	 */
	private String[] filterSupportPOS(String sentence) {

		String[] listWords = tokenizer(sentence);
		String[] result = new String[listWords.length];
		int i = -1;
		for (String word : listWords) {
			POS wordTag = tagOf(word);
			List<POS> supportTags = getPOSList(this.algorithm.getPOSPairs());
			if (supportTags.contains(wordTag)) {
				result[++i] = word;
			}
		}

		return result;
	}

	/**
	 * Get POS tag list from POS pair which depend your configure. Default
	 * posPair contain [n,n] [v,v]
	 * 
	 * @param posPairs
	 * @return
	 */
	private List<POS> getPOSList(List<POS[]> posPairs) {
		List<POS> posList = new ArrayList<POS>();
		for (POS[] posPair : posPairs) {
			for (int i = 0; i < posPair.length; i++) {
				POS pos = posPair[i];
				if (!posList.contains(pos))
					posList.add(pos);

			}
		}
		return posList;
	}

	/**
	 * Return synonym matrix with option optimize or not.When optimize is true,
	 * result will be sort descending by score and remove all element which has
	 * score = 0
	 * 
	 * @param sentence1
	 * @param sentence2
	 * @param optimize
	 * @return
	 */
	public List<RelatedUnit> computeSynonymMatrix(String[] sentence1,
			String[] sentence2, boolean optimize) {
		List<RelatedUnit> result = new ArrayList<RelateSentence.RelatedUnit>();
		long start = System.currentTimeMillis();
		for (int i = 0; i < sentence1.length; i++) {
			for (int j = 0; j < sentence2.length; j++) {
				if (sentence1[i] != null && sentence2[j] != null) {
					POS tagOfPair = tagOf(sentence1[i]);
					double simScore = normolizeScore(
							this.algorithm.calcRelatednessOfWords(sentence1[i],
									sentence2[j]), tagOfPair);
					result.add(new RelatedUnit(i, j, tagOfPair, simScore));
				}
			}
		}

		// Get max score of two sentence using local optimize
		if (optimize) {
			return optimizeMatrix(result);
		}

		System.out.println("Time to process finding synonym is"
				+ (System.currentTimeMillis() - start));
		return result;
	}

	/**
	 * Returns Wordnet tag of word
	 * 
	 * @param word
	 *            word with Wordnet format
	 * @return
	 */
	private POS tagOf(String word) {
		int indexOfSharp = word.lastIndexOf("#");
		String pos = word.substring(indexOfSharp + 1);
		if (pos.equalsIgnoreCase(POS.n.toString()))
			return POS.n;
		else if (pos.equalsIgnoreCase(POS.v.toString()))
			return POS.v;
		else if (pos.equalsIgnoreCase(POS.a.toString()))
			return POS.a;
		else
			return null;
	}

	/**
	 * Normalize similarity score and depend POS of word and responsible factor
	 * similarity of POS
	 * 
	 * @param score
	 * @param pos
	 * @see {@link #FACTOR_VERB_SIM}, {@link #FACTOR_NOUN_SIM}
	 * @return
	 */
	private double normolizeScore(double score, POS pos) {
		if (score >= 1)
			score = 1;
		if (pos.equals(POS.v))
			score = score * FACTOR_VERB_SIM;
		if (pos.equals(POS.n))
			score = score * FACTOR_NOUN_SIM;
		return score;
	}

	/**
	 * Check tuple of two work was existed in list or not
	 * 
	 * @param optimizedList
	 * @param head
	 */
	private boolean isExist(List<RelatedUnit> optimizedList, RelatedUnit head) {
		for (RelatedUnit optimizedUnit : optimizedList) {
			if (optimizedUnit.pos1 == head.pos1
					|| optimizedUnit.pos2 == head.pos2)
				return true;

		}

		return false;
	}

	/**
	 * Optimize matrix result using local optimize algorithm
	 * 
	 * @param matrix
	 * @return
	 */
	private List<RelatedUnit> optimizeMatrix(List<RelatedUnit> matrix) {
		Collections.sort(matrix, new RelatedComparator());

		List<RelatedUnit> optimizedList = new ArrayList<RelateSentence.RelatedUnit>();

		while (!matrix.isEmpty()) {
			RelatedUnit head = matrix.remove(0);
			if (head == null)
				continue;
			else if (head.score == 0)
				break;
			if (!isExist(optimizedList, head))
				optimizedList.add(head);
		}

		return optimizedList;
	}

	private String[] tokenizer(String sentence) {
		return WhitespaceTokenizer.INSTANCE.tokenize(sentence);
	}

}
