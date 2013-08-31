package parser;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import api.Api;
import api.JavaApi;
import element.ApiElement;

public class EasyFuncParser {
	private Api api;
	
	public static void main(String[] args) {
		new EasyFuncParser(new JavaApi()).parse();
	}
	
	public EasyFuncParser(Api api) {
		this.api = api;
	}
	
	
	/**
	 * Parse all file from {@code pathAPI}
	 * 
	 * @param pathAPI Path of API
	 * @exception
	 * 
	 * 
	 */
	public void parse() {

		for (File file : this.api.getApiResource().getApiFiles()) {
			try {
				Document document = Jsoup.parse(file, "UTF-8");
				ApiElement abstractElement = api.getApiElement();
				abstractElement.doParse(document.children());
				//writeOutput(elements);
			} catch (IOException e) {
				e.printStackTrace();
			}


		} 
	}

	

	private void writeOutput(Elements elements) {
		
	}

}
