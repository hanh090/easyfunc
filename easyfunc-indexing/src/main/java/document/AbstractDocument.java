package document;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Field;


/**
 * Abstract class define some need method for document which use in lucene
 *
 */
public abstract class AbstractDocument {
	private List<Field> fields;
	public AbstractDocument() {
		fields = new ArrayList<Field>();
	}
	
	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	/**
	 * Add field to document
	 * @param field
	 * @return true if add success and otherwise
	 */
	public abstract boolean add(Field field);
	/**
	 * Update field in document
	 * @param id id of field which we want to update
	 * @param field new field value 
	 * @return true if update success and otherwise
	 */
	public abstract boolean update(String id, Field field);
	/**
	 * 
	 * Delete field in document
	 * @param field field which we want to delete
	 * @return true if delete success and otherwise
	 */
	public abstract boolean delete(Field field);

}
