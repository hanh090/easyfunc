package searcher.nlp;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import edu.cmu.lti.ws4j.util.PorterStemmer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.model.BaseModel;

public class EFPosTagger extends NatureLanguageProcesser {

	public EFPosTagger(String originalSentence, BaseModel model) {
		super(originalSentence, model);
	}

	@Override
	protected void processing() {
		try {
			posTag();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * POS Tagging the {@code sentence} base Wordnet format
	 * 
	 * @param sentence
	 * @throws IOException
	 * @return String POS-Tagged base Wordnet format
	 * @throws ExecutionException
	 */
	private void posTag() throws IOException, ExecutionException {
		System.out.println("Sentence: " + originalSentence);
		POSTaggerME tagger = new POSTaggerME((POSModel) model);

		ObjectStream<String> lineStream = new PlainTextByLineStream(
				new StringReader(originalSentence));

		String line;
		while ((line = lineStream.read()) != null) {
			// Tags
			String[] tokens = WhitespaceTokenizer.INSTANCE.tokenize(line);
			String[] tags = tagger.tag(tokens);

			// Stemming
			PorterStemmer stemmer = new PorterStemmer();
			for (int i = 0; i < tokens.length; i++) {
				String lemmaWord = null;
				//To performent just apply for verb
				if (toWornetTags(tags[i]).equals(WordnetTag.VERB.getTag())) {
					lemmaWord = LemmazationVerb.lemma(tokens[i]);
				}
				else
					lemmaWord = tokens[i].toLowerCase();
				String stemToken = stemmer.stemWord(lemmaWord);
				this.token_tag.put(stemToken, tags[i]);
			}
		}

	}

	/**
	 * Filter words has tag is {@code tag}.Note: You must POS-tag before call
	 * this method
	 * 
	 * @param tag
	 *            Wornet tag name
	 * @return array of words
	 */
	public String[] filter(WordnetTag tag) {
		List<String> result = new ArrayList<String>();
		for (Entry<String, String> entry : this.token_tag.entrySet()) {
			if (toWornetTags(entry.getValue()).equals(WordnetTag.VERB.getTag()))
				result.add(entry.getKey());
		}

		return result.toArray(new String[0]);
	}

	/**
	 * Convert list Penn tag to Wornet tags
	 * 
	 * @param tags
	 * @return
	 */
	public String[] toWornetTags() {
		String[] tags = this.getTag();
		String[] result = new String[this.token_tag.size()];
		for (int i = 0; i < tags.length; i++) {
			result[i] = toWornetTags(tags[i]);

		}
		return result;
	}

	/**
	 * Convert Penn TreeBank Tag to Wordnet tag
	 * 
	 * @param tags
	 *            A <a href=
	 *            "http://www.ling.upenn.edu/courses/Fall_2003/ling001/penn_treebank_pos.html"
	 *            >pen tree bank tag </a>
	 * @return
	 */
	public String toWornetTags(String tag) {
		if (tag.equals(PennTag.ADJECTIVE.getTag()))
			return WordnetTag.ADJECTIVE.getTag();
		else if (tag.equals(PennTag.VERB.getTag())
				|| tag.equals(PennTag.VERB_MODAL.getTag())
				|| tag.equals(PennTag.VERB_PARTICIPLE_PAST.getTag())
				|| tag.equals(PennTag.VERB_PAST_TENSE.getTag())
				|| tag.equals(PennTag.VERB_SINGULAR_PRESENT_NONTHIRD_PERSON
						.getTag())
				|| tag.equals(PennTag.VERB_SINGULAR_PRESENT_THIRD_PERSON
						.getTag())) {
			return WordnetTag.VERB.getTag();

		}

		else if (tag.equals(PennTag.NOUN.getTag())
				|| tag.equals(PennTag.NOUN_PLURAL.getTag())
				|| tag.equals(PennTag.NOUN_PROPER_PLURAL.getTag())
				|| tag.equals(PennTag.NOUN_PROPER_SINGULAR.getTag())) {
			return WordnetTag.NOUN.getTag();
		} else if (tag.equals(PennTag.ADVERB.getTag())
				|| tag.equals(PennTag.ADVERB_COMPARATIVE.getTag())
				|| tag.equals(PennTag.ADVERB_SUPERLATIVE.getTag())
				|| tag.equals(PennTag.ADVERB_WH.getTag())) {
			return WordnetTag.ADVERB.getTag();
		}
		return WordnetTag.UNKNOW.getTag();
	}

	@Override
	public String toString() {
		return toString("_", false);
	}

	/**
	 * 
	 * @param separate
	 *            to choose separate between two words
	 * @return
	 */
	private String toString(String separate, boolean isWordnetFormat) {
		StringBuilder result = new StringBuilder();
		for (Entry<String, String> entry : this.token_tag.entrySet()) {
			if (isWordnetFormat) {
				result.append(entry.getKey() + separate
						+ toWornetTags(entry.getValue()) + " ");
			} else {
				result.append(entry + separate + token_tag.get(entry) + " ");
			}

		}
		return result.toString();
	}

	public String toWordnetFormat() {
		return toString("#", true);
	}

	public static void main(String[] args) throws InvalidFormatException,
			IOException {
		EFPosTagger test = (EFPosTagger) new EFPosTagger(
				"how to insert element into link list",
				new POSModel(
						new File(
								"src/main/resources/model/english/postag/en-pos-maxent.bin")))
				.process();

		System.out.println(test.toString());
		System.out.println(test.toWordnetFormat());
	}

}
