package structure;

import java.util.HashMap;
import java.util.Map;

/**
 * Specify structure of Java API docs
 */
public class JavaStructure implements ApiStructure {
	private Map<String, String> map;
	
	class Package{
		
	}
	public JavaStructure() {
		map = new HashMap<String, String>();
		
//		map.put("package", "");
//		map.put("type","");
//		map.put()
	}
	
	@Override
	public Map<String, String> getStructure() {
		return map;
	}

}
