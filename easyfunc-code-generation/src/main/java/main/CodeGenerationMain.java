package main;

import java.util.List;

import org.apache.lucene.document.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import searcher.Searcher;
import code.generation.CodeGeneration;

public class CodeGenerationMain {
	private Searcher searcher;
	private CodeGeneration codeGeneration;
	
	private static final Logger slf4jLogger = LoggerFactory.getLogger(CodeGenerationMain.class);
	public static final String EASYFUNC_CODE_GENERATION = "easyfunc-code-generation";
	public static void main(String[] args) {
		CodeGenerationMain codeGenerationMain = new CodeGenerationMain();
		String query = "find a shortest path";
		slf4jLogger.info("query {}",query);
		codeGenerationMain.generate(query);
		
	}
	
	
	/**
	 * Generate code from {@code fromQuery}.
	 * @param fromQuery user query which inputed in view module
	 * @return
	 */
	public List<String> generate(String fromQuery) {
		searcher = new Searcher(fromQuery);
		List<Document>docs = searcher.doSearch();
		return null;
	}
	
	/**
	 * Perform search action 
	 * @param fromQuery user query which inputed in view module
	 * @return result document list  
	 */
	public List<Document> search(String fromQuery) {
		searcher = new Searcher(fromQuery);
		return searcher.doSearch();
		
	}
}
