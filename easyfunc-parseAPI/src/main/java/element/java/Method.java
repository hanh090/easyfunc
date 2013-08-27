package element.java;

import org.jsoup.select.Elements;

import element.ApiElement;
import element.WrongTemplateException;

public class Method extends ApiElement {
	
	
	public Method(String selector, ApiElement... elements) {
		this.selector = JavaSelector.METHOD_SELECTOR;
		this.addToChilren(new Name(), new Description(),new Annotations());
	}
	@Override
	public void parse(Elements content) {
		Elements childrenContent = content.select(this.selector);
		try {
			if(childrenContent.size() > 1)
				throw new WrongTemplateException("method");
			else{
				for (ApiElement child : this.children) {
					child.parse(childrenContent);
				}
			}
		} catch (WrongTemplateException e) {
			e.printStackTrace();
		}
		
	}
	@Override
	public void doParse() {
		// TODO Auto-generated method stub
		
	}

}
