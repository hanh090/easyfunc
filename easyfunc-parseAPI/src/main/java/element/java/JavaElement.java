package element.java;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import element.ApiElement;
import element.ChildrenType;
import element.WrongTemplateException;

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
