package element.java;

import element.ApiElement;

public class Package extends ApiElement {

	@Override
	public void buildChildren() {
		this.setChildren(new String());
	}

	@Override
	public String selector() {
		
		return JavaSelector.PACKAGE_SELECTOR;
	}


}
