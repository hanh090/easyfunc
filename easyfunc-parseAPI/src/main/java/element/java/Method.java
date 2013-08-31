package element.java;

import java.util.List;

import element.ApiElement;
import element.ChildrenType;

public class Method extends ApiElement {
	@Override
	public String getSelector() {
		
		return JavaSelector.METHOD_SELECTOR;
	}

	@Override
	public ChildrenType getChildrenType() {
		
		return ChildrenType.DICRETE;
	}

	@Override
	public List<Object> getChildren() {
		return this.buildChilren(new Name(), new Description(), new Annotations());
	}
	

}
