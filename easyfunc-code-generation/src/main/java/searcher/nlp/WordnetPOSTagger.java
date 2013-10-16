package searcher.nlp;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.ExecutionException;

import org.omg.CORBA.portable.ApplicationException;

import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import searcher.Searcher;

/**
 * Class for POS Tagging document base on Wordnet format: word#[verb noun,
 * adjective adverb]#sense
 * 
 * @author 51000_000
 * 
 */
public class WordnetPOSTagger {
	/**
	 * POS Tagging the {@code sentence} base Wordnet format
	 * 
	 * @param sentence
	 * @throws IOException
	 * @return String POS-Tagged base Wordnet format
	 * @throws ExecutionException 
	 */
	public String posTag(String sentence) throws IOException, ExecutionException {
		String result = null;
		@SuppressWarnings("fix later")
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

			String[] wordnetTags = toWornetTags(tags);
			
			result = mix(whitespaceTokenizerLine,"#",wordnetTags, " ");

			perfMon.incrementCounter();
		}
		perfMon.stopAndPrintFinalResult();

		return result;
	}

	public String[] toWornetTags(String[] tags) {
		String[] result = new String[tags.length];
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
	protected String toWornetTags(String tag) {
		if (tag.equals(PennTag.ADJECTIVE.getTag()))
			return WordnetTag.ADJECTIVE.getTag();
		else if (tag.equals(PennTag.VERB.getTag())
				|| tag.equals(PennTag.VERB_MODAL.getTag())
				|| tag.equals(PennTag.VERB_PARTICIPLE_PAST.getTag())
				|| tag.equals(PennTag.VERB_PAST_TENSE.getTag())
				|| tag.equals(PennTag.VERB_SINGULAR_PRESENT_NONTHIRD_PERSON.getTag())
				|| tag.equals(PennTag.VERB_SINGULAR_PRESENT_THIRD_PERSON.getTag())) {
			return WordnetTag.VERB.getTag();
	

		}
		
		else if(tag.equals(PennTag.NOUN.getTag())
				|| tag.equals(PennTag.NOUN_PLURAL.getTag())
				|| tag.equals(PennTag.NOUN_PROPER_PLURAL.getTag())
				|| tag.equals(PennTag.NOUN_PROPER_SINGULAR.getTag())){
			return WordnetTag.NOUN.getTag();
		}
		else if(tag.equals(PennTag.ADVERB.getTag())
				|| tag.equals(PennTag.ADVERB_COMPARATIVE.getTag())
				|| tag.equals(PennTag.ADVERB_SUPERLATIVE.getTag())
				|| tag.equals(PennTag.ADVERB_WH.getTag())){
			return WordnetTag.ADVERB.getTag();
		}
		return WordnetTag.UNKNOW.getTag();
	}

	/**
	 * Mix to array start with order element
	 * 
	 * @param source
	 *            source array will be mix
	 * @param adding
	 *            adding array will be add to source
	 * @param option
	 *            to choose separate between two words
	 * @return
	 * @throws ExecutionException
	 */
	private String mix(String[] source,String sense, String[] adding, String separate)
			throws ExecutionException {
		if (source.length != adding.length)
			throw new ExecutionException("Difference lenght", null);
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < source.length; i++) {
			result.append(source[i] + sense + adding[i] + separate);
		}

		return result.toString();
	}

	public static void main(String[] args) throws IOException, ExecutionException {
		WordnetPOSTagger wPosTagger = new WordnetPOSTagger();
		System.out.println(wPosTagger.posTag("how to insert element into a list <a>"));
	}

}
