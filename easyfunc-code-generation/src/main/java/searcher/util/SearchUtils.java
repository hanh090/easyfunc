package searcher.util;

import java.io.File;
import java.io.IOException;

import main.Indexer;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.SimpleFSDirectory;

import document.wrapper.IndexKind;
import utils.ResourceUtils;

public class SearchUtils {
	/**
	 * Create index search base indexed data locate in index module
	 * 
	 * @param kind
	 * @return
	 * @throws IOException
	 */
	public static IndexSearcher createIndexSearcher(IndexKind kind)
			throws IOException {
		IndexSearcher indexSearcher = null;
		String indexPath = ResourceUtils.getPath(Indexer.EASYFUNC_INDEXING);
		switch (kind) {
		case TYPE:
			indexSearcher = new IndexSearcher(
					DirectoryReader.open(new SimpleFSDirectory(new File(
							indexPath + "/src/main/resources/index/type/"))));
			break;
		case METHOD:
			indexSearcher = new IndexSearcher(
					DirectoryReader.open(new SimpleFSDirectory(new File(
							indexPath + "/src/main/resources/index/method/"))));
			break;
		default:
			break;
		}

		return indexSearcher;
	}

	public static IndexSearcher createIndexSearcher(String path) throws CorruptIndexException, IOException {
		IndexSearcher indexSearcher =  new IndexSearcher(
				DirectoryReader.open(new SimpleFSDirectory(new File(
						path))));
		
		return indexSearcher;
	}
}
