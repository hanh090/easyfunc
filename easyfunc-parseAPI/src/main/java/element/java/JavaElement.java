package element.java;

import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import element.ApiElement;
import element.WrongTemplateException;

/**
 * Unit of java api file
 */
public class JavaElement extends ApiElement {
	
	
	public JavaElement() {
		this.selector = JavaSelector.JAVA_CONTENT_SELECTOR;
		this.children.add(new Package());
		this.children.add(new Type());
	}
	
	public JavaElement(Document document) {
		this.selector = JavaSelector.JAVA_CONTENT_SELECTOR;
		this.setDocument(document);
		this.children.add(new Package());
		this.children.add(new Type());
	}
	

	@Override
	public void parse(Elements content) {	
	}
	
	
	@Override
	public void doParse() {
		Elements childrenContent = getDocument().select(this.selector);
		try {
			if(childrenContent.size() > 1)
				throw new WrongTemplateException("java");
			else{
				for (ApiElement child : this.children) {
					child.parse(childrenContent);
				}
			}
		} catch (WrongTemplateException e) {
			e.printStackTrace();
		}
	}


	
}
