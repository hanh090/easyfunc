package document.stat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.BytesRef;

import document.java.TypeDocument.FieldName;

public abstract class TermStats {
	/**
	 * Table about frequency of term
	 */
	protected Map<String, Integer> termTable;
	
	public TermStats(IndexReader reader) {
		try {
			termTable = new HashMap<String, Integer>();
			computeTermTable(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void computeTermTable(IndexReader reader) throws IOException {
		//MultiFields.getFields(reader).terms(field)
		int numDocs = reader.numDocs();
		for (int docID =0 ; docID < numDocs; docID++) {
			Terms termVector = reader.getTermVector(docID, FieldName.METHOD_DESC.getName());
			if(termVector == null)
				continue;
			TermsEnum iterator = termVector.iterator(null);
			BytesRef next = iterator.next();
			while (next != null) {
				termTable.put(next.utf8ToString(), reader.docFreq(FieldName.METHOD_DESC.getName(), next));
				next = iterator.next();
			}
		}
		
	}

	public Map<String, Integer> getTermTable() {
		return termTable;
	}

	public void setTermTable(Map<String, Integer> termTable) {
		this.termTable = termTable;
	}

}
