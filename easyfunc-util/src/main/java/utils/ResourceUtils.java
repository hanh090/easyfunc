package utils;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;

/**
 * Utilities for resource in project
 * 
 */
public final class ResourceUtils {

	private static final String DEFAULT_BASE = "classes";
	private static final String EASYFUNC = "easyfunc";

	/**
	 * Get root with current file is path from class loader point
	 * @deprecated
	 * @return
	 */
	public static String getRoot() {
		File current = new File(System.getProperty("user.dir"));
		return getRoot(current);
	}

	/**
	 * Get root {@link #EASYFUNC} with input is current file
	 * @deprecated
	 * @param current
	 * @return
	 */
	public static String getRoot(File current) {
		if (current.getName().equals(EASYFUNC))
			return current.getAbsolutePath();
		else
			return getRoot(current.getParentFile());
	}

	/**
	 * Get class path of class c
	 * 
	 * @param c
	 * @return
	 */
	/**
	 * Get base class path contain class c.Default is {@link #DEFAULT_BASE} folder
	 */
	public static String getClasspathBase(Class c) {
		return getClasspathBase(c, DEFAULT_BASE);

	}

	public static String getClasspathBase(Class c, String baseFolderName) {
		String separator = "/";
		String classFilePath = getClasspath(c);
		String parentPath = FilenameUtils
				.getFullPathNoEndSeparator(classFilePath);

		return getClasspathBase(parentPath, baseFolderName, separator);

	}
	
	public static String getClasspath(Class c) {

		return c.getResource(c.getSimpleName() + ".class").getFile();
	}
	
	public static String getClasspathBase(String parentPath,
			String baseFolderName, String separator) {
		int lastIndexOf = parentPath.lastIndexOf(separator);
		
		String parentName = parentPath.substring(lastIndexOf+1);
		if (parentName.equals(baseFolderName))
			return parentPath;
		else
			return getClasspathBase(parentPath.substring(0, lastIndexOf),
					baseFolderName, separator);
	}

	/**
	 * Get path of resource id start from root {@link #EASYFUNC}
	 * @deprecated
	 * @param resourceId
	 * @return
	 */
	public static String getPath(String resourceId) {
		String rootPath = getRoot();
		File root = new File(rootPath);
		FileFilter filter = new RegexFileFilter("^easyfunc.*");
		return getPath(resourceId, root, root.listFiles(filter));
	}

	/**
	 * Get path of resource id from {@code start} directory
	 * @deprecated
	 * @param resourceId
	 * @param start
	 *            directory name which start finding
	 * @param domain
	 *            domain of file which traversed
	 * @return
	 */
	public static String getPath(String resourceId, File start, File[] domain) {
		for (File file : domain) {
			if (file.getName().equals(resourceId))
				return file.getAbsolutePath();
			else if (file.isDirectory()) {
				String result = getPath(resourceId, file, file.listFiles());
				if (result != null)
					return result;
			}
		}

		return null;
	}

}
