package document;

import org.jsoup.select.Elements;

public class MethodDocument extends ApiDocumentFactory{

	public MethodDocument(Elements elements) {
		super(elements);
	}

	@Override
	public ApiDocument createDocument() {
		return null;
	}
	
}