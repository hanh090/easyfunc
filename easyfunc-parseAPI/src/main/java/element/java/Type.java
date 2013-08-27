package element.java;

import org.jsoup.select.Elements;

import element.ApiElement;
import element.WrongTemplateException;

public class Type extends ApiElement {
	
	
	public Type() {
		this.selector = JavaSelector.TYPE_SELECTOR;
		this.addToChilren(new Name(), new Description(), new Methods(), new Fields());
	}
	@Override
	public void parse(Elements content) {
		Elements childrenContent = content.select(this.selector);
		try {
			if(childrenContent == null || childrenContent.size() == 0)
				return;
			if(childrenContent.size() > 1)
				throw new WrongTemplateException("type");
			else{
				for (ApiElement child : this.children) {
					child.parse(childrenContent);
				}
			}
		} catch (WrongTemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}
	@Override
	public void doParse() {
		// TODO Auto-generated method stub
		
	}

}
