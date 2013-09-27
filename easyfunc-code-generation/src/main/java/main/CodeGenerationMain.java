package main;

import code.generation.CodeGeneration;
import searcher.Searcher;
import searcher.Searcher.DocumentKind;

public class CodeGenerationMain {
	private Searcher searcher;
	private CodeGeneration codeGeneration;
	
	public static void main(String[] args) {
		CodeGenerationMain codeGenerationMain = new CodeGenerationMain();
		codeGenerationMain.generate(args[0]);
	}
	
	
	/**
	 * Generate code from {@code fromQuery}.
	 * @param fromQuery user query which inputed in view module
	 * @return
	 */
	public String generate(String fromQuery) {
		searcher = new Searcher(fromQuery, DocumentKind.METHOD);
		searcher.doSearch();
		return null;
	}
}
