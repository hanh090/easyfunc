package element.java;

import org.jsoup.select.Selector;

/**
 * Selector for all element of Java API. To more precisely, all element shoud be
 * contain 2 level
 * 
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
	public static final String TYPE_SELECTOR = "body > .contentContainer";
	/**
	 * Method and field list selector
	 */
	
	/**
	 * Field list selector
	 */
	public static final String FIELDS_SELECTOR = null;
	/**
	 * Field selector
	 */
	public static final String FIELD_SELECTOR = null;

	public static final String METHODS_SELECTOR = "div.details ul.blockList ul.blockList ul.blockList li.blockList, "
												+ "div.details ul.blockList ul.blockList ul.blockListLast li.blockList";
	
	/**
	 * Method selector
	 */
	public static final String METHOD_SELECTOR = null;
	/**
	 * Annotation list selector
	 */
	public static final String ANNOTATIONS_SELECTOR = null;
	/**
	 * Parameter list selector
	 */

	public static final String PARAMETERS_SELECTOR = null;
	/**
	 * Parameter selector
	 */
	public static final String PARAMETER_SELECTOR = null;
	

	public static final String ANNOTATION_SELECTOR = "dl, dl ~ dd";
	/**
	 * Description selector
	 */
	public static final String DESCRIPTION_SELECTOR = ".blockList > .block";

	/**
	 * Name selector
	 */

	public static final String NAME_SELECTOR = ".blockList > pre";
	
}
