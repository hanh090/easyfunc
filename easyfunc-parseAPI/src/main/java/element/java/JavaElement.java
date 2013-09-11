package element.java;

import element.ApiElement;

/**
 * Unit of java api file
 */
public class JavaElement extends ApiElement {

	@Override
	public String selector() {
		return JavaSelector.JAVA_CONTENT_SELECTOR;
	}

	@Override
	public void buildChildren() {
		this.setChildren(new Package(), new Type());
	}

	
}
