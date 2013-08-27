package resource;

import java.io.File;
import java.util.List;

/**
 * Specify all need resouce for parser
 */
public interface ApiResource {
	
	/**
	 * Get all api file. Notice that: Api resource which set above can contain some non-API file
	 * @return
	 */
	List<File> getApiFiles();
	
	void setInputPath();
	
	/**
	 * Get output path which will use for write result of parse process
	 * @return
	 */
	String getOutput();
}
