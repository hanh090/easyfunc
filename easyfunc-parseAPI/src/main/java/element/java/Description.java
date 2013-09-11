package element.java;

import element.ApiElement;

public class Description extends ApiElement {
	
	@Override
	public void buildChildren() {
		this.setChildren(new String());

		
	}

	@Override
	public String selector() {
		
		return JavaSelector.DESCRIPTION_SELECTOR;
	}

}
