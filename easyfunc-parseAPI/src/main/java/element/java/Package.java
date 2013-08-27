package element.java;

import org.jsoup.select.Elements;

import element.ApiElement;
import element.WrongTemplateException;

public class Package extends ApiElement {
	
	String content;
	public Package() {
		this.selector = JavaSelector.PACKAGE_SELECTOR;
		this.addToChilren();
	}

	/**
	 * 
	 */

	@Override
	public void parse(Elements content) {
		Elements childrenContent = content.select(this.selector);
		try {
			if(childrenContent.size() > 1)
				throw new WrongTemplateException("package");
			this.content = childrenContent.text();
		} catch (WrongTemplateException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void doParse() {
		// TODO Auto-generated method stub
		
	}


}
