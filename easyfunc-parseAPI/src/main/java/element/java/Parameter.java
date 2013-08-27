package element.java;

import org.jsoup.select.Elements;

import element.ApiElement;
import element.WrongTemplateException;

public class Parameter extends ApiElement {

	public Parameter() {
		this.selector = JavaSelector.PARAMETER_SELECTOR;
		this.addToChilren(new Name(), new Description());
	}

	@Override
	public void parse(Elements content) {
		Elements childrenContent = content.select(this.selector);
		try {
			if(content.size() > 1)
				throw new WrongTemplateException("parameter");
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
