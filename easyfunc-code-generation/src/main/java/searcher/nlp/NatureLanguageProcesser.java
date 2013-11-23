package searcher.nlp;

import java.util.LinkedHashMap;
import java.util.Map;

import opennlp.tools.util.model.BaseModel;

/**
 * Process some action relate to nature language i.e: POS-Tag, Chunk, Parse....
 * of a sentence
 * 
 */
public abstract class NatureLanguageProcesser {

	protected String originalSentence;
	protected BaseModel model;
	/**
	 * Mapping between a token with a PENN tag
	 */
	protected Map<String, String> token_tag;

	public NatureLanguageProcesser(String originalSentence, BaseModel model) {
		this.originalSentence = originalSentence;
		this.model = model;
		token_tag = new LinkedHashMap<String, String>();
	}
	
	public NatureLanguageProcesser(String originalSentence,Map<String, String> token_tag) {
		this.originalSentence = originalSentence;
		this.token_tag = token_tag;
	}

	
	public NatureLanguageProcesser process(){
		processing();
		return this;
	}
	
	protected abstract void processing();

	public Map<String, String> getToken_Tag() {
		return token_tag;
	}

	public String[] getToken(){
		return token_tag.keySet().toArray(new String[token_tag.size()]);
	}
	
	public String[] getTag(){
		return token_tag.values().toArray(new String[token_tag.size()]);
	}
	

}
