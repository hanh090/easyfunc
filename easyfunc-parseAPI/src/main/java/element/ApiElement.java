package element;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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

	public boolean doParse(Elements content) {
		if (content == null || content.size() == 0) {
			System.out.println("Content null! "
					+ this.getClass().getSimpleName());
			return false;
		}

		parse(content);
		return true;
	}

	/**
	 * Parse the content of this element
	 * 
	 * @param content
	 *            content of parent element
	 */
	protected void parse(Elements content) {
		Elements childrenContent = content.select(this.selector);
		Iterator<Object> iterator = this.children.iterator();
		do {
			
			Object child = null;
			try {
				child = iterator.next();
			} catch(Exception e)  {
				
			}
			
			switch (childrenType) {
			case DICRETE:

				if (child instanceof ApiElement) {
					boolean result = ((ApiElement) child)
							.doParse(childrenContent);

					if (!result)
						return;
				}
				break;

			case LIST:

				Element childContent = childrenContent.get(this.children
						.indexOf(child));
				if (child instanceof ApiElement)
					((ApiElement) child).doParse(new Elements(childContent));
				break;

			case NO_CHILD:
				this.children.add(childrenContent.text());
				return;
			default:
				break;
			}

		} while (iterator.hasNext());

	}

	@Override
	public String toString() {
		String name = this.getClass().getSimpleName().toLowerCase();
		String result = "<" + name + ">\n";
		for (Object apiElement : this.children) {
			result += "\t" + apiElement.toString();
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
