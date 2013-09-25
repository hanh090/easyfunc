package parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;



import api.Api;
import api.JavaApi;
import element.ApiElement;
import element.java.Package;

public class Parser {
	private Api api;

	public static void main(String[] args)  {
		try {
			new Parser(new JavaApi()).parse();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Parser(Api api) {
		this.api = api;
	}

	/**
	 * @throws IOException 
	 * Parse all file from {@code pathAPI}
	 * 
	 * @param pathAPI
	 *            Path of API
	 * @exception
	 * 
	 * 
	 */
	public void parse() throws IOException {
		FileUtils.cleanDirectory(new File(api.getApiResource().getOutput()));
		for (File file : this.api.getApiResource().getApiFiles()) {
			try {
				Document document = Jsoup.parse(file, "UTF-8");
				ApiElement apiElement = api.getApiElement();
				apiElement.doParse(document.children());
				writeOutput(file, apiElement);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
	
	private void writeOutput(File file, ApiElement content) {
		try {
			String title = getPackageContent(content);
			File f = new File(api.getApiResource().getOutput() + title + "." + file.getName());
			if(f.exists()){
				System.out.println("File existed " + file.getName());
			}
			BufferedWriter fWrite = new BufferedWriter(new FileWriter(f));
			fWrite.write(content.toString());
			fWrite.flush();
			fWrite.close();
			System.out.println("Write " + f.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String getPackageContent(ApiElement content) {
		for (Object ele : content.getChildren()) {
			if(ele instanceof Package){
				return ((Package) ele).getChildren().get(0).toString();
			}
			else{
				if(ele instanceof ApiElement)
					return getPackageContent((ApiElement)ele);
			}
		}
		return null;
	}
}
