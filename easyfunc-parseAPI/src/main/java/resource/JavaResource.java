package resource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JavaResource implements ApiResource {
	private static final String OUTPUT = "D:\\easyfunc\\easyfunc-parseAPI\\result";
	private static final String INPUT = "D:\\Compressed\\jdk-7u6-apidocs\\docs\\api";
	/**
	 * Just for test. Assign to null when test done
	 */
	@SuppressWarnings("Assign null when done")
	private static final String API_FILE_PATTERN = "[A-Z].*";
	
	
	public JavaResource() {
		
	}
	@Override
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
	@Override
	public String getOutput() {
		return OUTPUT;
	}
	@Override
	public void setInputPath() {
		// TODO Auto-generated method stub
		
	}

}
