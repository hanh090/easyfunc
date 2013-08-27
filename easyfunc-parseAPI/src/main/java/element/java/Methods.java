package element.java;

import java.util.ArrayList;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import element.ApiElement;

public class Methods extends ApiElement {

	public Methods() {
		this.selector = JavaSelector.METHODS_SELECTOR;
		this.children.addAll(new ArrayList<Method>());
	}
	@Override
	public void parse(Elements content) {
		Elements childrenContent = content.select(this.selector);
		
		for (ApiElement method : this.children) {
			if(method instanceof Method){
				Element child = childrenContent.get(this.children.indexOf(method));
				method.parse(new Elements(child));
			}
		}
	}
	@Override
	public void doParse() {
		// TODO Auto-generated method stub
		
	}
	
	

}
