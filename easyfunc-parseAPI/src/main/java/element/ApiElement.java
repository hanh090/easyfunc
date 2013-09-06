package element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.select.Elements;

/**
 * Abstract class for all api element
 */
public abstract class ApiElement {
	private String selector;
	private List<Object> children;
	/**
	 * Content which didn't parse
	 */
	private Elements rawContent;

	public ApiElement() {
		children = new ArrayList<Object>();
		this.selector = buildSelector();
	}
	
	public void doParse(Elements parentContent) {
		if(this.selector != null)
			this.rawContent = parentContent.select(this.selector);
		else if(rawContent == null){
			System.out.println("Null");
			return;
		}
		
		buildChildren();
		parse();
	
		
	}

	/**
	 * Parse the content of this element
	 * 
	 * @param parentContent
	 *            content of parent element
	 */
	protected void parse() {
		
		Iterator<Object> iterator = this.children.iterator();
		while (iterator.hasNext()) {

			Object child = iterator.next();
	
			if (child instanceof ApiElement) {
				((ApiElement) child).doParse(rawContent);
			}
			else if(child instanceof String){
			
				String text = rawContent.text().replaceAll("\\s", " ");
				if(text == null || text.isEmpty())
					text = "null";
				this.children.set(this.children.indexOf(child),text);
			}
			else{
				System.out.println("null\n\n");
			}

		};

	}

	@Override
	public String toString() {
		String name = this.getClass().getSimpleName().toLowerCase();
		String result = "<" + name + ">\n\t";
		for (Object apiElement : this.children) {
			result += apiElement.toString().replaceAll("\n", "\n\t");
		}

		result += "\n</" + name + ">\n";

		return result;
	}

	/**
	 * Get selector of element
	 * 
	 * @return selector of element
	 */
	public String getSelector(){
		return selector;
	}

	/**
	 * Set selector of element
	 * 
	 */
	public void setSelector(String selector) {
		this.selector = selector;
	}


	/**
	 * Get list children
	 * 
	 * @return list of children
	 */
	public List<Object> getChildren(){
		return children;
	}

	public void setChildren(List<? extends Object> children) {
		for (Object object : children) {
			this.children.add(object);
		}
	}
	
	public void setChildren(Object... children) {
		this.children = Arrays.asList(children);
	}
	
	public Elements getRawContent() {
		return rawContent;
	}
	
	public void setRawContent(Elements rawContent) {
		this.rawContent = rawContent;
	}
	
	public abstract String buildSelector();
	public abstract void buildChildren();

	
}
