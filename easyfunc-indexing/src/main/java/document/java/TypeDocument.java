package document.java;

import java.util.List;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import config.ApiField;
import document.ApiDocument;

/**
 * A document represent the unit in Java i.e Class, Interface and Enum. It
 * contain id which is full path of class and all methods in type Example:
 * <table border="1">
 * <tr>
 * <th>Field</th>
 * <th>Content</th>
 * </tr>
 * <tr>
 * <td>id</td>
 * <td>org.xml.sax.XMLReader.html</td>
 * </tr>
 * <tr>
 * <td>method</td>
 * <td>callSomething()...</td>
 * </tr>
 * </table>
 */
public class TypeDocument extends ApiDocument {

	public TypeDocument(String id) {
		super(id);
	}

	public enum FieldName {
		ID("id"), TYPE_DESCRIPTION("typeDesc"), METHOD_NAME("methodName"), METHOD_DESC(
				"methodDesc"), METHOD_SIZE("methodSize");

		private String nameField;

		FieldName(String nameField) {
			this.nameField = nameField;
		}

		public String getName() {
			return this.nameField;
		}

	}

	@Override
	public List<Field> index() {

		return null;
	}

	@Override
	public boolean add(String id, String value, Store store) {
		this.document.add(new ApiField(id, value, store));
		return true;
	}

	@Override
	public boolean update(String id, String value) {

		return false;
	}

	@Override
	public boolean delete(String id, String value) {

		return false;
	}

	/**
	 * @param fromContent
	 *            type element from .xml file
	 */
	@Override
	public ApiDocument toApiDocument(Elements fromContent) {
		this.add(FieldName.TYPE_DESCRIPTION.getName(),
				fromContent.select("type > description").text(), Store.YES);

		Elements methods = fromContent.select("method");
		this.add(FieldName.METHOD_SIZE.getName(),
				String.valueOf(methods.size()), Store.YES);
		for (Element methodField : fromContent.select("method")) {
			this.add(FieldName.METHOD_NAME.getName(), methodField
					.select("name").text(), Store.YES);
			this.add(FieldName.METHOD_DESC.getName(), methodField
					.select("description").text(), Store.NO);
		}
		return this;
	}

}
