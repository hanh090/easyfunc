package api;

import element.java.JavaElement;
import resource.JavaResource;
import structure.JavaStructure;

public class JavaApi extends Api {
	public JavaApi() {
		this.apiResource = new JavaResource();
		this.apiElement = new JavaElement();
	}
}
