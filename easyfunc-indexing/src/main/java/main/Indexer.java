package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import util.ParserUtil;
import document.ApiDocument;
import document.java.MethodDocument;
import document.java.TypeDocument;
import document.java.TypeDocument.FieldName;
import document.stat.NormalDistribution;

public class Indexer {

	private IndexWriter indexWriterForType;

	private IndexWriter indexWriterForMethod;

	public static void main(String[] args) {
		Indexer indexer = new Indexer();
		try {
			indexer.doIndex();
			indexer.doStat();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Indexer() {
		try {
			indexWriterForType = createIndexWriter(
					"src/main/resources/index/type/", Version.LUCENE_40);
			indexWriterForMethod = createIndexWriter(
					"src/main/resources/index/method/", Version.LUCENE_40);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @throws IOException
	 * @throws CorruptIndexException
	 * @throws LockObtainFailedException
	 */
	public IndexWriter createIndexWriter(String outputPath, Version version)
			throws IOException, CorruptIndexException,
			LockObtainFailedException {
		Directory directory = FSDirectory.open(new File(outputPath));
		return new IndexWriter(directory, new IndexWriterConfig(version,
				new EnglishAnalyzer(version)));
	}

	public void doIndex() throws CorruptIndexException, IOException {
		File inputPath = new File(ParserUtil.getOuputLocation());

		indexWriterForMethod.deleteAll();
		indexWriterForType.deleteAll();

		for (File file : inputPath.listFiles()) {
			Elements typeEle = ParserUtil.getElement(file, "type");

			doIndexForTypeDoc(file, typeEle);

			doIndexForMethodDoc(file, typeEle);

		}

		indexWriterForMethod.commit();
		indexWriterForType.commit();
		indexWriterForMethod.close();
		indexWriterForType.close();

	}

	private void doIndexForTypeDoc(File file, Elements typeEle)
			throws CorruptIndexException, IOException {
		ApiDocument typeDocument = new TypeDocument(file.getName())
				.toApiDocument(typeEle);

		indexWriterForType.addDocument(typeDocument.getDocument());
		System.out.println("Indexed file" + file.getName());
	}

	private void doIndexForMethodDoc(File file, Elements typeEle)
			throws CorruptIndexException, IOException {
		int methodSize = typeEle.select("method").size();

		// Add methods
		for (Element methodEle : typeEle.select("method")) {
			ApiDocument methodDocument = new MethodDocument(file.getName())
					.toApiDocument(new Elements(methodEle));

			methodDocument.add(FieldName.METHOD_SIZE.getName(),
					String.valueOf(methodSize), Store.YES);

			indexWriterForMethod.addDocument(methodDocument.getDocument());
		}

	}

	public void doStat() throws CorruptIndexException, IOException {
		NormalDistribution termStat = new NormalDistribution();
		termStat.computeTermTable(DirectoryReader.open(indexWriterForMethod.getDirectory()));
		storeTermStat(termStat);

	}

	private void storeTermStat(NormalDistribution termStat) throws IOException {
		Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("src/main/resources/stat/termStat.xml"),
				"UTF8"));

		writer.write("<mean>" + termStat.getMean() + "</mean>");
		writer.write("<sd>" + termStat.getSd() + "</sd>");

		writer.flush();
		writer.close();
	}

}
