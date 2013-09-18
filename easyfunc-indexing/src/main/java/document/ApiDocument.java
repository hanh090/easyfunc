package document;

import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;

/**
 * Wrapper class of {@link Document} for extend some function
 *
 */
public abstract class ApiDocument {
	protected Document document;
	
	public ApiDocument() {
		document = new Document();
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
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
}
