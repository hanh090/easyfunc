package parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import api.Api;
import api.JavaApi;
import element.ApiElement;

public class Parser {
	private Api api;

	public static void main(String[] args) {
		new Parser(new JavaApi()).parse();
	}

	public Parser(Api api) {
		this.api = api;
	}

	/**
	 * Parse all file from {@code pathAPI}
	 * 
	 * @param pathAPI
	 *            Path of API
	 * @exception
	 * 
	 * 
	 */
	public void parse() {

		for (File file : this.api.getApiResource().getApiFiles()) {
			try {
				Document document = Jsoup.parse(file, "UTF-8");
				ApiElement apiElement = api.getApiElement();
				apiElement.doParse(document.children());
				writeOutput(file, apiElement.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
	
	private void writeOutput(File file, String content) {
		try {
			File f = new File(api.getApiResource().getOutput() + file.getName());
			
//			if (f.exists() && f.getParentFile() != file.getParentFile()) {
//				f = new File(api.getApiResource().getOutput() + file.getName()
//						+ file.getParent());
//			}

			BufferedWriter fWrite = new BufferedWriter(new FileWriter(f));
			fWrite.write(content);
			fWrite.flush();
			fWrite.close();
			System.out.println("Write " + file.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
