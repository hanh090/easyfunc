package api;

import resource.ApiResource;
import element.ApiElement;

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
