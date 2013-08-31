package element.java;

import java.util.List;

import element.ApiElement;
import element.ChildrenType;
/**
 * Inheritance tree 
 */
public class Inheritance extends ApiElement {

	@Override
	public String getSelector() {
		
		return JavaSelector.INHERITANCE_SELECTOR;
	}

	@Override
	public ChildrenType getChildrenType() {
		
		return ChildrenType.LIST;
	}

	@Override
	public List<Object> getChildren() {
		
		return null;
	}
	
	

}
