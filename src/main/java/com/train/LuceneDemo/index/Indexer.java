package com.train.LuceneDemo.index;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Indexer {

	private IndexWriter writer;

	public Indexer(String indexDirectoryPath) throws IOException {
		// this directory will contain the indexes
		Directory indexDirectory = FSDirectory
				.open(new File(indexDirectoryPath).toPath());

		IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
		writer = new IndexWriter(indexDirectory, config);

	}

	public void close() throws CorruptIndexException, IOException {
		writer.close();
	}

	private Document getDocument(File file) throws IOException {
		Document document = new Document();

		// index file contents
		Field contentField = new Field(LuceneConstants.CONTENTS,
				new FileReader(file));
		// index file name
		Field fileNameField = new Field(LuceneConstants.FILE_NAME,
				file.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED);
		// index file path
		Field filePathField = new Field(LuceneConstants.FILE_PATH,
				file.getCanonicalPath(), Field.Store.YES,
				Field.Index.NOT_ANALYZED);

		document.add(contentField);
		document.add(fileNameField);
		document.add(filePathField);

		return document;
	}

	private void indexFile(File file) throws IOException {
		System.out.println("Indexing " + file.getCanonicalPath());
		Document document = getDocument(file);
		writer.addDocument(document);
	}

	private void updateIndexFile(File file) throws IOException {
		System.out.println("Updating index: " + file.getCanonicalPath());
		updateDocument(file);
	}

	private void deleteDocument(File file) throws IOException {
		// delete indexes for a file
		writer.deleteDocuments(new Term(LuceneConstants.FILE_NAME, file
				.getName()));
		writer.commit();
		System.out.println("index contains deleted files: "
				+ writer.hasDeletions());
		System.out.println("index contains documents: " + writer.maxDoc());
		System.out.println("index contains deleted documents: "
				+ writer.numDocs());
	}

	private void updateDocument(File file) throws IOException {
		Document document = new Document();

		// update indexes for file contents
		writer.updateDocument(
				new Term(LuceneConstants.FILE_NAME, file.getName()), document);
		// writer.close();
	}

	public int createIndex(String dataDirPath, FileFilter filter)
			throws IOException {
		// get all files in the data directory
		File[] files = new File(dataDirPath).listFiles();

		for (File file : files) {
			if (!file.isDirectory() && !file.isHidden() && file.exists()
					&& file.canRead() && filter.accept(file)) {
				indexFile(file);
			}
		}
		return writer.numDocs();
	}

	public int updateIndex(String dataDirPath, FileFilter filter)
			throws IOException {
		// get all files in the data directory
		File[] files = new File(dataDirPath).listFiles();

		for (File file : files) {
			if (!file.isDirectory() && !file.isHidden() && file.exists()
					&& file.canRead() && filter.accept(file)) {
				updateIndexFile(file);
			}
		}
		return writer.numDocs();
	}

	private void deleteFile(File file) throws IOException {
		System.out.println("Deleting index for " + file.getCanonicalPath());
		deleteDocument(file);
	}
	
	public int deleteIndex(String dataDirPath, FileFilter filter)
			throws IOException {
		// get all files in the data directory
		File[] files = new File(dataDirPath).listFiles();

		for (File file : files) {
			if (!file.isDirectory() && !file.isHidden() && file.exists()
					&& file.canRead() && filter.accept(file)) {
				deleteFile(file);
			}
		}
		return writer.numDocs();
	}
}