package element.java;

import org.jsoup.select.Elements;

import element.ApiElement;
import element.WrongTemplateException;

public class Description extends ApiElement {
	String content;
	
	
	public Description() {
		this.selector = JavaSelector.DESCRIPTION_SELECTOR;
	}

	public void parse(Elements content) {
		Elements chidren = content.select(this.selector);

		try {
			if (chidren.size() > 1)
				throw new WrongTemplateException(this.getClass().toString());
			this.content = chidren.text();
		} catch (WrongTemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void doParse() {
		// TODO Auto-generated method stub
		
	}

}
