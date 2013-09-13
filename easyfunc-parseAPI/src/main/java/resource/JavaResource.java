package resource;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaResource implements ApiResource {
	private static final String OUTPUT = "src/main/resources/output/1.Result-JavaAPI-v7/";
	private static final String INPUT = "src/main/resources/input/1.JavaAPI-v7/docs/api";
	/**
	 * Just for test. Assign to null when test done
	 */
	@SuppressWarnings("Assign null when done")
	private static final String API_FILE_PATTERN = "[A-Z].*\\.html";
	
	FilenameFilter javaApiFilter = new FilenameFilter() {
		
		public boolean accept(File dir, String name) {
			boolean includeFolder = !Pattern.matches("class-use", dir.getName());
			return includeFolder;
		}
	};
	public JavaResource() {
		
	}
	
	public List<File> getApiFiles() {
		return getApiFilesFromDir(INPUT);
	}
	
	private List<File> getApiFilesFromDir(String directoryPath) {
		List<File> result = new ArrayList<File>();
		
		File folder = new File(directoryPath);
		for (File file : folder.listFiles(javaApiFilter)) {
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
