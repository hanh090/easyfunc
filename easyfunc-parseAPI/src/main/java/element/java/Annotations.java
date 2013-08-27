package element.java;

import org.jsoup.select.Elements;

import element.ApiElement;

public class Annotations extends ApiElement {

	
	public Annotations() {
		this.selector = JavaSelector.ANNOTATIONS_SELECTOR;
		this.addToChilren(new Parameters(), new Implement());
	}

	@Override
	public void parse(Elements content) {
		
	}

	@Override
	public void doParse() {
		// TODO Auto-generated method stub
		
	}

}
