package element.java;

import java.util.ArrayList;
import java.util.List;

import element.ApiElement;
import element.ChildrenType;

public class Name extends ApiElement {
	@Override
	public String getSelector() {
		
		return JavaSelector.NAME_SELECTOR;
	}

	@Override
	public ChildrenType getChildrenType() {
		
		return ChildrenType.NO_CHILD;
	}

	@Override
	public List<Object> getChildren() {
		return new ArrayList<Object>();
	}

}
