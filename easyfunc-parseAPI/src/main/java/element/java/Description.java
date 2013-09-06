package element.java;

import element.ApiElement;

public class Description extends ApiElement {
	
	@Override
	public void buildChildren() {
		this.setChildren(new String());

		
	}

	@Override
	public String buildSelector() {
		
		return JavaSelector.DESCRIPTION_SELECTOR;
	}

}
