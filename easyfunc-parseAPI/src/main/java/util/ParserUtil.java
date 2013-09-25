package util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import resource.JavaResource;


public class ParserUtil {
	
	/**
	 * Get text from file with specify selector
	 * @param filePath path to file which will use for get text
	 * @param selector selector for get text.Selector has syntax of {@link org.jsoup.select.Selector}
	 * @return plain text from selector
	 */
	public static String getText(String filePath,String selector) {
		Document document = Jsoup.parse(filePath);
		String text = document.select(selector).text();
		return text;
	}
	
	/**
	 * Get text from all file in a directory with specify selector
	 * @param directoryPath name of file which will use for get text
	 * @param selector selector for get text.Selector has syntax of {@link org.jsoup.select.Selector}
	 * @return plain text from selector
	 */
	public static List<String> getAllText(String directoryPath,String selector) {
		List<String> result = new ArrayList<String>();
		File file = new File(directoryPath);
		if(!file.isDirectory()){
			System.out.println("Must be directory");
			return null;
		}
		
		for (File f : file.listFiles()) {
			try {
				Document document = Jsoup.parse(f,"UTF-8");
				result.add(document.select(selector).text());
				System.out.println(result);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		return result;
	}
	
	/**
	 * Get element from file with specify selector
	 * @param filePath path to file which will use for get text
	 * @param selector selector for get text.Selector has syntax of {@link org.jsoup.select.Selector}
	 * @return {@link Elements} from selector
	 */
	public static Elements getElement(File file,String selector) {
		try {
			Document document = Jsoup.parse(file,"UTF-8");
			Elements elements = document.select(selector);
			return elements;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Get all elements from all file in a directory with specify selector
	 * @param directoryPath name of file which will use for
	 * @param selector selector for get text.Selector has syntax of {@link org.jsoup.select.Selector}
	 * @return plain text from selector
	 */
	public static Elements getAllElement(String selector) {
		Elements result = new Elements();
		File file = new File(getOuputLocation());
		if(!file.isDirectory()){
			System.out.println("Must be directory");
			return null;
		}
		
		for (File f : file.listFiles()) {
			try {
				Document document = Jsoup.parse(f,"UTF-8");
				result.addAll(document.select(selector));
				System.out.println(result);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		return result;
	}
	/**
	 * Get the location of parsed result
	 * @return location of parsed result 
	 */
	public static String getOuputLocation(){
		return "../easyfunc-parseAPI/" + new JavaResource().getOutput();
	}
	/**
	 * Count element in specify file
	 * @param selector
	 * @param file
	 * @return
	 */
	public static int countElement(String selector, File file){
		try {
			Document document = Jsoup.parse(file, "UTF-8");
			return document.select(selector).size();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
		
	}
	/**
	 * Count all element in output directory
	 * @param selector
	 * @return
	 */
	public static int countAllElement(String selector){
		int result = 0;
		File file = new File(getOuputLocation());
		
		for (File f : file.listFiles()) {
			try {
				Document document = Jsoup.parse(f,"UTF-8");
				result += document.select(selector).size();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		return result;
		
	}
}
