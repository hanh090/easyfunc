package element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
/**
 * Abstract class for all api element
 */
public abstract class ApiElement {
	private Document document;
	protected Elements elements;
	protected String selector;
	protected List<ApiElement> children;
	
	public ApiElement() {
		this.children = new ArrayList<ApiElement>();
	}
	public void addToChilren(ApiElement...child){
		this.children = Arrays.asList(child);
	}
	
	
	
	
	@Override
	public String toString() {
		String name = this.getClass().getName().toLowerCase();
		String result = "<" + name + ">\n\t";
		for (ApiElement abstractElement : this.children) {
			result += abstractElement.toString();
		}
		
		result += "</" + name + ">\n";
		
		return result;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public void doParse() {
		
	}
	
	/**
	 * Parse the content of this element
	 * @param content list element which returned when select
	 */
	public abstract void parse(Elements content);
		
	
}
