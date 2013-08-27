package element.java;

import java.util.ArrayList;

import org.jsoup.select.Elements;

import element.ApiElement;

public class Parameters extends ApiElement {

	public Parameters() {
		this.selector = JavaSelector.PARAMETERS_SELECTOR;
		this.children.addAll(new ArrayList<Parameter>());
		
	}

	@Override
	public void parse(Elements content) {
		

	}

	@Override
	public void doParse() {
		// TODO Auto-generated method stub
		
	}

}
