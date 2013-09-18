package main;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.synonym.WordnetSynonymParser;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import parser.Parser;
import api.JavaApi;
import document.MethodDocument;
import document.TypeDocument;
import util.ParserUtil;

public class Indexer {

	private IndexWriter indexWriterForType;
	Directory directoryType ;
	
	private IndexWriter indexWriterForMethod;
	Directory directoryMethod ;
	
	private static final String OUTPUT_PATH = "";

	public static void main(String[] args) {
		Indexer indexer = new Indexer();
		try {
			indexer.doIndex();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Indexer() {
		try {
			directoryType = FSDirectory.open(new File(
					"src/main/resources/index/type/"));
			this.indexWriterForType = new IndexWriter(
					directoryType,
					new IndexWriterConfig(Version.LUCENE_40,
							new StandardAnalyzer(Version.LUCENE_40)));
		
			directoryMethod = FSDirectory.open(new File(
					"src/main/resources/index/method/"));
			this.indexWriterForMethod =  new IndexWriter(
					directoryMethod,
					new IndexWriterConfig(Version.LUCENE_40,
							new EnglishAnalyzer(Version.LUCENE_40)));

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void doIndex() {
		File inputPath = new File(ParserUtil.getOuputLocation());
		try {
			indexWriterForMethod.deleteAll();
			indexWriterForType.deleteAll();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		for (File file : inputPath.listFiles()) {
			Elements packageEle = ParserUtil.getElement(file, "package");
			Elements typeEle = ParserUtil.getElement(file, "type");
			Elements methodEle = ParserUtil.getElement(file, "method");

			TypeDocument typeDocument = new TypeDocument();
			typeDocument.add("package", packageEle.text(), Store.YES);
			typeDocument.add("type", typeEle.text(), Store.YES);
			typeDocument.add("method", methodEle.text(), Store.YES);
			try {
				indexWriterForType.addDocument(typeDocument.getDocument());
				
				System.out.println("Indexed file" + file.getName());
			} catch (IOException e) {
				e.printStackTrace();
			}

			for (Element element : methodEle) {
				MethodDocument methodDocument = new MethodDocument();
				methodDocument.add("package", packageEle.text(), Store.YES);
				methodDocument.add("type", typeEle.select("name").text(),
						Store.YES);
				methodDocument.add("method", element.text(), Store.YES);
				try {
					indexWriterForMethod.addDocument(methodDocument
							.getDocument());
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		try {
			
			indexWriterForMethod.commit();
			indexWriterForMethod.close();
			directoryMethod.close();
			
			indexWriterForType.commit();
			indexWriterForType.close();
			directoryType.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
