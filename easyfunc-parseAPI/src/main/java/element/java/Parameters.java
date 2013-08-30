package element.java;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.select.Elements;

import element.ApiElement;
import element.ChildrenType;

public class Parameters extends ApiElement {

	@Override
	public String getSelector() {
		
		return JavaSelector.PARAMETERS_SELECTOR;
	}

	@Override
	public ChildrenType getChildrenType() {
		
		return ChildrenType.LIST;
	}

	@Override
	public List<Object> getChildren() {
		
		return this.buildChilren(new ArrayList<Parameter>());
	}

}
