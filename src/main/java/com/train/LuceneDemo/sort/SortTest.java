package com.train.LuceneDemo.sort;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;

import com.train.LuceneDemo.index.Indexer;
import com.train.LuceneDemo.index.LuceneConstants;
import com.train.LuceneDemo.search.Searcher;

public class SortTest {

	String indexDir = "index";
	String dataDir = "data";
	Indexer indexer;
	Searcher searcher;

	public static void main(String[] args) {
		SortTest tester;
		try {
			tester = new SortTest();
			//tester.sortUsingRelevance("ramesh");
			tester.sortUsingIndex("ramesh");
			tester.searchUsingFuzzyQuery("record.txt");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private void sortUsingRelevance(String searchQuery) throws IOException,
			ParseException {
		searcher = new Searcher(indexDir);
		long startTime = System.currentTimeMillis();
		// create a term to search file name
		Term term = new Term(LuceneConstants.CONTENTS, searchQuery);
		// create the term query object
		Query query = new FuzzyQuery(term);

		// searcher.setDefaultFieldSortScoring(true, false);
		// do the search
		TopDocs hits = searcher.search(query, Sort.RELEVANCE);
		long endTime = System.currentTimeMillis();

		System.out.println(hits.totalHits + " documents found. Time :"
				+ (endTime - startTime) + "ms");
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			System.out.print("score: " + scoreDoc.score);
			Document doc = searcher.getDocument(scoreDoc);
			System.out.println("File: " + doc.get(LuceneConstants.FILE_NAME));
		}
		searcher.close();
	}

	private void sortUsingIndex(String searchQuery) throws IOException,
			ParseException {
		searcher = new Searcher(indexDir);
		long startTime = System.currentTimeMillis();
		// create a term to search file name
		Term term = new Term(LuceneConstants.CONTENTS, searchQuery);
		// create the term query object
		Query query = new FuzzyQuery(term);

		// do the search
		TopDocs hits = searcher.search(query, Sort.INDEXORDER);
		long endTime = System.currentTimeMillis();

		System.out.println(hits.totalHits + " documents found. Time :"
				+ (endTime - startTime) + "ms");
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			System.out.print("score: " + scoreDoc.score);
			Document doc = searcher.getDocument(scoreDoc);
			System.out.println("File: " + doc.get(LuceneConstants.FILE_NAME));
		}
		searcher.close();
	}

	private void searchUsingFuzzyQuery(String searchQuery) throws IOException,
			ParseException {
		searcher = new Searcher(indexDir);
		long startTime = System.currentTimeMillis();
		// create a term to search file name
		Term term = new Term(LuceneConstants.FILE_NAME, searchQuery);
		// create the term query object
		Query query = new FuzzyQuery(term, 2, 0);
		// do the search
		TopDocs hits = searcher.search(query,Sort.INDEXORDER);
		long endTime = System.currentTimeMillis();

		System.out.println(hits.totalHits + " documents found. Time :"
				+ (endTime - startTime) + "ms");
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = searcher.getDocument(scoreDoc);
			
			System.out.print("Score: " + scoreDoc.score + " doc id:"+scoreDoc.doc+" ");
			System.out.println("File: " + doc.get(LuceneConstants.FILE_PATH));
		}
		searcher.close();
	}

}