package element.java;

import java.util.ArrayList;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import element.ApiElement;

public class Fields extends ApiElement {
	
	public Fields() {
		this.selector =JavaSelector.FIELDS_SELECTOR;
		this.children.addAll(new ArrayList<Field>());
	}
	
	@Override
	public void parse(Elements content) {
		Elements childrenContent = content.select(this.selector);
		
		for (ApiElement field : this.children) {
			if(field instanceof Field){
				Element child = childrenContent.get(this.children.indexOf(field));
				field.parse(new Elements(child));
			}
		}
	}

	@Override
	public void doParse() {
		// TODO Auto-generated method stub
		
	}

}
