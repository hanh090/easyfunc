package api;

import element.ApiElement;
import resource.ApiResource;
import structure.ApiStructure;

public abstract class Api {
	ApiResource apiResource;
	ApiElement apiElement;
	
	public ApiResource getApiResource() {
		return apiResource;
	}

	public ApiElement getApiElement() {
		return apiElement;
	}	
	
	
}
