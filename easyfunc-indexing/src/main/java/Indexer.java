import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
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
//			indexer.setUp();
//			indexer.getHitCount("contents", "pleas");
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
			typeDocument.add("method", methodEle.text(), Store.NO);
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
				methodDocument.add("method", element.text(), Store.NO);
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
//	  protected String[] ids = {"1", "2"};
//	  protected String[] unindexed = {"Netherlands", "Italy"};
//	  protected String[] unstored = {"A class that implements an empty border with no size. This provides a convenient base class from which other border classes can be easily derived. Warning: Serialized objects of this class will not be compatible with future Swing releases. The current serialization support is appropriate for short term storage or RMI between applications running the same version of Swing. As of 1.4, support for long term storage of all JavaBeansTM has been added to the java.beans package. Please see XMLEncoder.",
//	                                 "Venice has lots of canals"};
//	  protected String[] text = {"Amsterdam", "Venice"};
//
//	  private Directory directory;
//
//	  protected void setUp() throws Exception {     //1
//	    directory =  FSDirectory.open(new File("src/main/resources/index/method/"));
//
//	    IndexWriter writer = getWriter();           //2
//	    
//	    writer.deleteAll();
//	    for (int i = 0; i < ids.length; i++) {      //3
//	      Document doc = new Document();
//	      doc.add(new Field("id", ids[i],
//	                        Field.Store.YES,
//	                        Field.Index.NOT_ANALYZED));
//	      doc.add(new Field("country", unindexed[i],
//	                        Field.Store.YES,
//	                        Field.Index.NO));
//	      doc.add(new Field("contents", unstored[i],
//	                        Field.Store.NO,
//	                        Field.Index.ANALYZED));
//	      doc.add(new Field("city", text[i],
//	                        Field.Store.YES,
//	                        Field.Index.ANALYZED));
//	      writer.addDocument(doc);
//	     // writer.commit();
//	    }
//	  
//	    writer.close();
//	  }
//
//	  private IndexWriter getWriter() throws IOException {            // 2
//	    return new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_40, new EnglishAnalyzer(Version.LUCENE_40))); // 2
//	  }
//
//	  protected int getHitCount(String fieldName, String searchString)
//	    throws IOException {
//	    IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(directory)); //4
//	    Term t = new Term(fieldName, searchString);
//	    Query query = new TermQuery(t);    
//	    //5
//	    int hitCount = searcher.search(query, 10).totalHits;     //6
//	    System.out.println(hitCount);
//	    return hitCount;
//	  }

	  

}
