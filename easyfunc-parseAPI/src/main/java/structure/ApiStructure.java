package structure;

import java.util.Map;

import org.jsoup.select.Selector;

/**
 * Specify structure of api docs by syntax of css like jsoup
 * @see Jsoup Selector{@link Selector}
 */
public interface ApiStructure {
	Map<String, String> getStructure();
}
