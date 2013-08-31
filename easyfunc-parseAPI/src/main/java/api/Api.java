package api;

import element.ApiElement;
import resource.ApiResource;


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
