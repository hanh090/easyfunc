package searcher.nlp;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.model.BaseModel;

public class EFChunker extends NatureLanguageProcesser {

	private String results[];

	public EFChunker(String originalSentence,BaseModel model) {
		super(originalSentence,model);
	}

	public EFChunker(String originalSentence, Map<String, String> token_tag) {
		super(originalSentence, token_tag);
	}

	@Override
	protected void processing() {
		// If did'd POS-tag then do it
		if (token_tag == null || token_tag.isEmpty()) {
			NatureLanguageProcesser nlp = new EFPosTagger(originalSentence,model);
			nlp.process();
		}
		
		try {
			@SuppressWarnings("fix later")
			ChunkerModel cModel = new ChunkerModel(new File("src/main/resources/model/english/chunker/EnglishChunk.bin"));
			ChunkerME chunkerME = new ChunkerME(cModel);
			results = chunkerME.chunk(this.getToken(), this.getTag());
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	public String[] getResults() {
		return results;
	}

	@Override
	public String toString() {
		
		return results.toString();
	}

}
