package element.java;

import java.util.List;

import element.ApiElement;
import element.ChildrenType;

/**
 * Unit of java api file
 */
public class JavaElement extends ApiElement {
	@Override
	public String getSelector() {
		
		return JavaSelector.JAVA_CONTENT_SELECTOR;
	}

	@Override
	public ChildrenType getChildrenType() {
		
		return ChildrenType.DICRETE;
	}

	@Override
	public List<Object> getChildren() {
		return this.buildChilren(new Package(), new Type());
	}
	
	

	
}
