package element.java;

import element.ApiElement;

public class Method extends ApiElement {
	
	
	
	public void buildChildren() {
		this.setChildren(new Name(), new Description());

	}

	@Override
	public String buildSelector() {
		
		return JavaSelector.METHOD_SELECTOR;
	}

}
