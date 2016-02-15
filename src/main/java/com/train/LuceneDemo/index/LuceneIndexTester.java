package com.train.LuceneDemo.index;

import java.io.IOException;

public class LuceneIndexTester {

	String indexDir = "Index";
	String dataDir = "Data";
	Indexer indexer;

	public static void main(String[] args) {
		LuceneIndexTester tester;
		try {
			tester = new LuceneIndexTester();
			tester.createIndex();
			// update doc with empty content
			//tester.updateIndex();
			//tester.deleteIndex();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createIndex() throws IOException {
		indexer = new Indexer(indexDir);
		int numIndexed;
		long startTime = System.currentTimeMillis();
		numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
		long endTime = System.currentTimeMillis();
		indexer.close();
		System.out.println(numIndexed + " File indexed, time taken: "
				+ (endTime - startTime) + " ms");
	}

	private void updateIndex() throws IOException {
		indexer = new Indexer(indexDir);
		int numIndexed;
		long startTime = System.currentTimeMillis();
		numIndexed = indexer.updateIndex(dataDir, new TextFileFilter());
		long endTime = System.currentTimeMillis();
		indexer.close();
		System.out.println(numIndexed + " File indexed updated, time taken: "
				+ (endTime - startTime) + " ms");
	}

	
	 private void deleteIndex() throws IOException{
	      indexer = new Indexer(indexDir);
	      int numIndexed;
	      long startTime = System.currentTimeMillis();	
	      numIndexed = indexer.deleteIndex(dataDir, new TextFileFilter());
	      long endTime = System.currentTimeMillis();
	      indexer.close();
	      System.out.println(numIndexed + " File indexed delete, time taken: "
					+ (endTime - startTime) + " ms");
	   }

}