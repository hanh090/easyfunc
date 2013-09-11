package element.java;

import element.ApiElement;

public class Type extends ApiElement {
	
	
	@Override
	public String selector() {
		
		return JavaSelector.TYPE_SELECTOR;
	}
	@Override
	public void buildChildren() {
		Name typeName = new Name();
		typeName.setSelector(".description " + JavaSelector.NAME_SELECTOR);
		
		Description typeDescription = new Description();
		typeDescription.setSelector(".description " + JavaSelector.DESCRIPTION_SELECTOR);
		
		this.setChildren(typeName, typeDescription ,new Methods());
	}

}
