package document;

import org.apache.lucene.document.Field;

public class MethodDocument extends AbstractDocument {

	@Override
	public boolean add(Field field) {
		
		return false;
	}

	@Override
	public boolean update(String id, Field field) {
		
		return false;
	}

	@Override
	public boolean delete(Field field) {
		
		return false;
	}

}
