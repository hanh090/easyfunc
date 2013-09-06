package element.java;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import element.ApiElement;

public class Methods extends ApiElement {

	
	@Override
	public void buildChildren() {
		
		List<Method> methods = new ArrayList<Method>();
		for (Element e : this.getRawContent()) {
			Method method = new Method();
			method.setRawContent(new Elements(e));	
			methods.add(method);
			
		}
		this.setChildren(methods);		
	}
	@Override
	public String buildSelector() {
		
		return JavaSelector.METHODS_SELECTOR;
	}


}
