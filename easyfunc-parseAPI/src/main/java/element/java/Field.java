package element.java;

import org.jsoup.select.Elements;

import element.ApiElement;
import element.WrongTemplateException;

public class Field extends ApiElement {

	public Field() {
		this.selector = JavaSelector.FIELD_SELECTOR;
		this.addToChilren(new Name(), new Description());
	}

	@Override
	public void parse(Elements content) {
		Elements childrenContent = content.select(this.selector);
		try {
			if(childrenContent.size() > 1)
				throw new WrongTemplateException("field");
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
		
		
	}

}
