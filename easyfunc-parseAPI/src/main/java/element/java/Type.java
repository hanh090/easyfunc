package element.java;

import java.util.List;

import org.jsoup.select.Elements;

import element.ApiElement;
import element.ChildrenType;
import element.WrongTemplateException;

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
	public List<Object> getChildren() {
		
		return this.buildChilren(new Name(), new Description(),new Annotations(),new Fields(),new Methods());
	}
	
	
	
}
