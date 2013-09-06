package element.java;

import element.ApiElement;

public class Name extends ApiElement {
	
	@Override
	public void buildChildren() {
		this.setChildren(new String());
		
	}

	@Override
	public String buildSelector() {
		
		return JavaSelector.NAME_SELECTOR;
	}
	
}
