package element;

/**
 * Type of children list 
 *
 */
public enum ChildrenType {
	/**
	 * Element don't contain any other element
	 */
	NO_CHILD,
	/**
	 * Children element don't overlap each others
	 */
	DICRETE,
	/**
	 * Children element are list same elements
	 */
	LIST,
	/**
	 * Mix of {@link #DICRETE} and {@link #LIST}
	 */
	MIX
}
