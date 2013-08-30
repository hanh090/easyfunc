package element.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.select.Elements;

import element.ApiElement;
import element.ChildrenType;

public class Annotations extends ApiElement {

	@Override
	public String getSelector() {
		return JavaSelector.ANNOTATIONS_SELECTOR;
	}

	@Override
	public ChildrenType getChildrenType() {
		return ChildrenType.DICRETE;
	}

	@Override
	public List<Object> getChildren() {
		return this.buildChilren(new Parameter(),new Implement());
	}

	

}
