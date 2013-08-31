package api;

import element.java.JavaElement;
import resource.JavaResource;

public class JavaApi extends Api {
	public JavaApi() {
		this.apiResource = new JavaResource();
		this.apiElement = new JavaElement();
	}
}
