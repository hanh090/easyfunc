package searcher.nlp;

public enum WordnetTag {
	ADJECTIVE("a"),
	ADVERB("r"),
	VERB("v"),
	NOUN("n"),
	UNKNOW("unk");
	
	
	private String tag;

	private WordnetTag(String tag) {
		this.tag = tag;
	}

	public String getTag() {
		return tag;
	}

}
