package document.java;

import java.util.List;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.jsoup.select.Elements;

import config.ApiField;
import document.ApiDocument;
import document.java.TypeDocument.FieldName;

/**
 * A document represent the method in Java i.e Field(Global variable), Method
 * and Constructor. It contain id which is full path of class and one methods in
 * responsible type
 * 
 * @see {@link TypeDocument}
 */
public class MethodDocument extends ApiDocument {

	public MethodDocument(String id) {
		super(id);
	}

	@Override
	public List<Field> index() {

		return null;
	}

	@Override
	public boolean update(String id, String value) {

		return false;
	}

	@Override
	public boolean delete(String id, String value) {

		return false;
	}

	@Override
	public boolean add(String id, String value, Store store) {
		this.document.add(new ApiField(id, value, store));
		return false;
	}

	/**
	 * @param fromContent
	 *            Just contain one method or type description
	 */
	@Override
	public ApiDocument toApiDocument(Elements fromContent) {
		if (fromContent.select("name") == null) {// type description
			this.add(FieldName.TYPE_DESCRIPTION.getName(),
					fromContent.text(), Store.YES);
			return this;
		}
		this.add(FieldName.METHOD_NAME.getName(), fromContent
				.select("name").text(), Store.YES);
		this.add(FieldName.METHOD_DESC.getName(), fromContent
				.select("description").text(), Store.YES);

		return this;
	}

}