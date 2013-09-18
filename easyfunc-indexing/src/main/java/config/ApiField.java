package config;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;

/**
 * A field for API doc that is indexed and tokenized, and term
 *  vectors. 
 *
 */
public final class ApiField extends Field {
	/** Indexed, tokenized, not stored. */
	  public static final FieldType TYPE_NOT_STORED = new FieldType();

	  /** Indexed, tokenized, stored. */
	  public static final FieldType TYPE_STORED = new FieldType();

	  static {
	    TYPE_NOT_STORED.setIndexed(true);
	    TYPE_NOT_STORED.setTokenized(true);
	    TYPE_NOT_STORED.setStoreTermVectors(true);
	    TYPE_NOT_STORED.setStoreTermVectorPositions(true);
	    TYPE_NOT_STORED.setStoreTermVectorOffsets(true);
	    TYPE_NOT_STORED.freeze();

	    TYPE_STORED.setIndexed(true);
	    TYPE_STORED.setTokenized(true);
	    TYPE_STORED.setStoreTermVectors(true);
	    TYPE_STORED.setStoreTermVectorPositions(true);
	    TYPE_STORED.setStoreTermVectorOffsets(true);
	    TYPE_STORED.setStored(true);
	    TYPE_STORED.freeze();
	  }
	  /** Creates a new ApiField with String value. 
	   * @param name field name
	   * @param value string value
	   * @param store Store.YES if the content should also be stored
	   * @throws IllegalArgumentException if the field name or value is null.
	   */
	  public ApiField(String name, String value, Store store) {
	    super(name, value, store == Store.YES ? TYPE_STORED : TYPE_NOT_STORED);
	  }

	
}
