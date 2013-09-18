package document;

import java.util.List;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.jsoup.select.Elements;

import config.ApiField;

public class MethodDocument extends ApiDocument{

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

	

	
	
}