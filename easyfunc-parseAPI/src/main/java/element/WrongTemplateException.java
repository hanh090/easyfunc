package element;

public class WrongTemplateException extends Exception {

	public WrongTemplateException(String string) {
		super("Wrong " + string + " template!");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
