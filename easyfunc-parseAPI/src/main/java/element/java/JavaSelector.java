package element.java;

import org.jsoup.select.Selector;

/**
 * Selector for all element of Java API. To more precisely, all element shoud be contain 2 level
 * @see Selector
 */
public final class JavaSelector {
	/**
	 * Java selector
	 */
	public static final String JAVA_CONTENT_SELECTOR = "body";
	/**
	 * Package selector
	 */
	public static final String PACKAGE_SELECTOR = ".header > .subTitle";
	/**
	 * Inheritance selector
	 */
	public static final String INHERITANCE_SELECTOR = "ul.inheritance";
	/**
	 * Implement selector
	 */
	public static final String IMPLEMENT_SELECTOR = null;
	/**
	 * Type selector
	 */
	public static final String TYPE_SELECTOR = ".description > .blockList";
	/**
	 * Method list selector
	 */
	public static final String METHODS_SELECTOR = "li.blockList > a[name]";
	/**
	 * Method selector
	 */
	public static final String METHOD_SELECTOR = "ul.blockList > li.blockList";
	/**
	 * Annotation list selector
	 */
	public static final String ANNOTATIONS_SELECTOR = ".blockList > dl";
	/**
	 * Annotation selector
	 */
	public static final String ANNOTATION_SELECTOR = null;
	/**
	 * Description selector
	 */
	public static final String DESCRIPTION_SELECTOR = ".blockList .block, dl dd";
	/**
	 * Name  selector
	 */
	public static final String NAME_SELECTOR = ".blockList pre, dl dt";
	
	

}
