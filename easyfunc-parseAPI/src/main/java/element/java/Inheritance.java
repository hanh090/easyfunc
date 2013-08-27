package element.java;

import org.jsoup.select.Elements;

import element.ApiElement;
/**
 * Inheritance tree 
 */
public class Inheritance extends ApiElement {
	
	public Inheritance() {
		this.selector = JavaSelector.INHERITANCE_SELECTOR;
		this.addToChilren(new Inheritance());
	}
	@Override
	public void doParse() {
		// TODO Auto-generated method stub

	}

	@Override
	public void parse(Elements content) {
		// TODO Auto-generated method stub

	}

}
