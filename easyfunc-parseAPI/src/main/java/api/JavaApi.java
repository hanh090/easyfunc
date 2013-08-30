package api;

import resource.JavaResource;
import element.java.JavaElement;

public class JavaApi extends Api {
	public JavaApi() {
		this.apiResource = new JavaResource();
		this.apiElement = new JavaElement();
	}
}
