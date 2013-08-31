package element.java;

import java.util.List;

import element.ApiElement;
import element.ChildrenType;

public class Annotation extends ApiElement {

	@Override
	public String getSelector() {
		return JavaSelector.ANNOTATION_SELECTOR;
	}

	@Override
	public ChildrenType getChildrenType() {
		return null;
	}

	@Override
	public List<Object> getChildren() {
		return null;
	}

}
