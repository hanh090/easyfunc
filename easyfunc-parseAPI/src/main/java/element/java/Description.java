package element.java;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.select.Elements;

import element.ApiElement;
import element.ChildrenType;
import element.WrongTemplateException;

public class Description extends ApiElement {

	@Override
	public String getSelector() {
		return JavaSelector.DESCRIPTION_SELECTOR;
	}

	@Override
	public ChildrenType getChildrenType() {
		return ChildrenType.NO_CHILD;
	}

	@Override
	public List<Object> getChildren() {
		return this.buildChilren(new String());
	}

}
