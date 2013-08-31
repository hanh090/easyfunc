package resource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JavaResource implements ApiResource {
	private static final String OUTPUT = "D:\\easyfunc\\src\\main\\resources\\output\\1.Result-JavaAPI-v7";
	private static final String INPUT = "D:\\easyfunc\\src\\main\\resources\\input\\1.JavaAPI-v7\\docs\\api";
	/**
	 * Just for test. Assign to null when test done
	 */
	@SuppressWarnings("Assign null when done")
	private static final String API_FILE_PATTERN = "[A-Z].*";
	
	
	public JavaResource() {
		
	}
	
	public List<File> getApiFiles() {
		return getApiFilesFromDir(INPUT);
	}
	
	private List<File> getApiFilesFromDir(String directoryPath) {
		List<File> result = new ArrayList<File>();
		
		File folder = new File(directoryPath);
		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				result.addAll(getApiFilesFromDir(file.getPath()));	
				continue;
			}
			if(file.getName().matches(API_FILE_PATTERN))
				result.add(file);
		}
		
		return result;
	}
	
	public String getOutput() {
		return OUTPUT;
	}
	
	public void setInputPath() {
	}

}
