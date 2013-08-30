package element.java;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.select.Elements;

import element.ApiElement;
import element.ChildrenType;

public class Implement extends ApiElement {

	@Override
	public String getSelector() {
		
		return JavaSelector.IMPLEMENT_SELECTOR;
	}

	@Override
	public ChildrenType getChildrenType() {
		
		return ChildrenType.LIST;
	}

	@Override
	public List<Object> getChildren() {
		
		List<Object> result = new ArrayList<Object>();
		return result ;
	}



}
