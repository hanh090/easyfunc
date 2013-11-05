package document.stat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.BytesRef;

import document.java.TypeDocument.FieldName;

public abstract class TermStats {
	/**
	 * Table about frequency of term across document context
	 */
	protected Map<String, Integer> termTable;
	
	public TermStats() {
		termTable = new HashMap<String, Integer>();
		
	}
	/**
	 * Compute statistic about term and its frequency
	 * @param reader
	 * @throws IOException
	 */
	public void computeTermTable(IndexReader reader) throws IOException {
		//MultiFields.getFields(reader).terms(field)
		int numDocs = reader.numDocs();
		for (int docID =0 ; docID < numDocs; docID++) {
			Terms termVector = reader.getTermVector(docID, FieldName.METHOD_DESC.getName());
			if(termVector == null)
				continue;
			TermsEnum iterator = termVector.iterator(null);
			BytesRef next = iterator.next();
			while (next != null) {
				int docFreq =  reader.docFreq(FieldName.METHOD_DESC.getName(), next);
				
				termTable.put(next.utf8ToString(), docFreq);
				next = iterator.next();
			}
			
		}
		
		computeStatProperties(reader,termTable);
	}
	/**
	 * Compute some addition properties in specify statistic
	 * @param reader 
	 * @param termTable
	 */
	protected abstract void computeStatProperties(IndexReader reader, Map<String, Integer> termTable) throws IOException;
	
	

	public Map<String, Integer> getTermTable() {
		return termTable;
	}


}
