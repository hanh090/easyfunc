package element.java;

import java.util.List;

import element.ApiElement;
import element.ChildrenType;

public class Type extends ApiElement {

	@Override
	public String getSelector() {
		
		return JavaSelector.TYPE_SELECTOR;
	}

	@Override
	public ChildrenType getChildrenType() {
		
		return ChildrenType.DICRETE;
	}

	@Override
	@SuppressWarnings("Missing annontoatation")
	public List<Object> getChildren() {
		return this.buildChilren(new Name(), new Description(),new Methods());
	}
	
	
	
}
