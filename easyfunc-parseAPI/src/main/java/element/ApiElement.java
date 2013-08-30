package element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Abstract class for all api element
 */
public abstract class ApiElement {
	private String selector;
	private List<Object> children;
	private ChildrenType childrenType;

	public ApiElement() {
		this.selector = getSelector();
		this.children = getChildren();
		this.childrenType = getChildrenType();
	}

	public void doParse(Elements content) {
		if (content == null || content.size() == 0)
			return;
		parse(content);
	}

	/**
	 * Parse the content of this element
	 * 
	 * @param content
	 *            content of parent element
	 */
	private void parse(Elements content) {
		Elements childrenContent = content.select(this.selector);
		
		switch (childrenType) {
		case DICRETE:
		case NO_CHILD:
			for (Object child : this.children) {
				if (child instanceof ApiElement)
					((ApiElement) child).doParse(childrenContent);
			}
			break;

		case LIST:
			for (Object child : this.children) {
				Element childContent = childrenContent.get(this.children
						.indexOf(child));
				if (child instanceof ApiElement)
					((ApiElement) child).doParse(new Elements(childContent));
				;
			}
		default:
			break;
		}
	}
	
	
	@Override
	public String toString() {
		String name = this.getClass().getName().toLowerCase();
		String result = "<" + name + ">\n\t";
		for (Object abstractElement : this.children) {
			result += abstractElement.toString();
		}

		result += "</" + name + ">\n";

		return result;
	}

	/**
	 * Get selector of element
	 * 
	 * @return selector of element
	 */
	public abstract String getSelector();

	/**
	 * Get {@link ChildrenType} of element
	 * 
	 * @return children type of element
	 */
	public abstract ChildrenType getChildrenType();

	/**
	 * Get list children
	 * 
	 * @return list of children
	 */
	public abstract List<Object> getChildren();
	
	public List<Object> buildChilren(Object... child) {
		return Arrays.asList(child);
	}

}
