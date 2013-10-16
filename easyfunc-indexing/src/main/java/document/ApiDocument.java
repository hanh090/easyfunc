package document;

import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import document.java.TypeDocument.FieldName;

/**
 * Wrapper class of {@link Document} for extend some function
 *
 */
public abstract class ApiDocument {
	protected Document document;
	
	public ApiDocument(String id) {
		document = new Document();
		this.add("id", id, Store.YES);
		
	}
	public ApiDocument(Document document) {
		this.document = document;
	}
	/**
	 * 
	 * @return
	 */
	public abstract List<Field> index();
	/**
	 * 
	 * @param id
	 * @param value
	 * @return
	 */
	public abstract boolean add(String id, String value, Store store);
	/**
	 * 
	 * @param id
	 * @param value
	 * @return
	 */
	public abstract boolean update(String id, String value);
	/**
	 * 
	 * @param id
	 * @param value
	 * @return
	 */
	public abstract boolean delete(String id, String value);
	/**
	 * Convert element to api document
	 * @param id
	 * @return
	 */
	public abstract ApiDocument toApiDocument(Elements fromContent);
	
	
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
}
