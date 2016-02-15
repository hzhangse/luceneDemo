package com.train.LuceneDemo.search;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;

import com.train.LuceneDemo.index.LuceneConstants;

public class LuceneSearchTester {
	String indexDir = "index";
	String dataDir = "data";
	Searcher searcher;

	private void searchUsingPrefixQuery(String searchQuery) throws IOException,
			ParseException {
		searcher = new Searcher(indexDir);
		long startTime = System.currentTimeMillis();
		// create a term to search file name
		Term term = new Term(LuceneConstants.FILE_NAME, searchQuery);
		// create the term query object
		Query query = new PrefixQuery(term);
		// do the search
		TopDocs hits = searcher.search(query);
		long endTime = System.currentTimeMillis();

		System.out.println(hits.totalHits + " documents found. Time :"
				+ (endTime - startTime) + "ms");
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = searcher.getDocument(scoreDoc);
			System.out.println("File: " + doc.get(LuceneConstants.FILE_PATH));
		}
		searcher.close();
	}

	private void searchUsingTermRangeQuery(String searchQueryMin,
			String searchQueryMax) throws IOException, ParseException {
		searcher = new Searcher(indexDir);
		long startTime = System.currentTimeMillis();
		// create the term query object
		Query query = TermRangeQuery.newStringRange(LuceneConstants.FILE_NAME,
				searchQueryMin, searchQueryMax, true, false);
		// do the search
		TopDocs hits = searcher.search(query);
		long endTime = System.currentTimeMillis();

		System.out.println(hits.totalHits + " documents found. Time :"
				+ (endTime - startTime) + "ms");
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = searcher.getDocument(scoreDoc);
			System.out.println("File: " + doc.get(LuceneConstants.FILE_PATH));
		}
		searcher.close();
	}

	private void searchUsingTermQuery(String field, String value)
			throws IOException, ParseException {
		searcher = new Searcher(indexDir);
		long startTime = System.currentTimeMillis();
		// create the term query object
		Query query = new TermQuery(new Term(field, value));
		// do the search
		TopDocs hits = searcher.search(query);
		long endTime = System.currentTimeMillis();

		System.out.println(hits.totalHits + " documents found. Time :"
				+ (endTime - startTime) + "ms");
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			System.out.println("score: " + scoreDoc.score);
			Document doc = searcher.getDocument(scoreDoc);
			System.out.println("File: " + doc.get(LuceneConstants.FILE_NAME));
		}
		searcher.close();
	}

	/***
	 * 组合关系代表的意思如下: 1、MUST和MUST表示“与”的关系，即“并集”。 2、MUST和MUST_NOT前者包含后者不包含。
	 * 3、MUST_NOT和MUST_NOT没意义 4、SHOULD与MUST表示MUST，SHOULD失去意义；
	 * 5、SHOUlD与MUST_NOT相当于MUST与MUST_NOT。 6、SHOULD与SHOULD表示“或”的概念。
	 * 
	 * @param searchQuery1
	 * @param searchQuery2
	 * @throws IOException
	 * @throws ParseException
	 */
	private void searchUsingBooleanQuery(String searchQuery1,
			String searchQuery2) throws IOException, ParseException {
		searcher = new Searcher(indexDir);
		long startTime = System.currentTimeMillis();
		// create a term to search file name
		Term term1 = new Term(LuceneConstants.FILE_NAME, searchQuery1);
		// create the term query object
		Query query1 = new TermQuery(term1);

		Term term2 = new Term(LuceneConstants.FILE_NAME, searchQuery2);
		// create the term query object
		Query query2 = new PrefixQuery(term2);

		BooleanQuery query = new BooleanQuery();
		query.add(query1, BooleanClause.Occur.SHOULD);
		query.add(query2, BooleanClause.Occur.SHOULD);

		// do the search
		TopDocs hits = searcher.search(query);
		long endTime = System.currentTimeMillis();

		System.out.println(hits.totalHits + " documents found. Time :"
				+ (endTime - startTime) + "ms");
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = searcher.getDocument(scoreDoc);
			System.out.println("File: " + doc.get(LuceneConstants.FILE_PATH));
		}
		searcher.close();
	}

	private void searchUsingPhraseQuery(String[] phrases) throws IOException,
			ParseException {
		searcher = new Searcher(indexDir);
		long startTime = System.currentTimeMillis();

		PhraseQuery query = new PhraseQuery();
		query.setSlop(0);

		for (String word : phrases) {
			query.add(new Term(LuceneConstants.FILE_NAME, word));
		}

		// do the search
		TopDocs hits = searcher.search(query);
		long endTime = System.currentTimeMillis();

		System.out.println(hits.totalHits + " documents found. Time :"
				+ (endTime - startTime) + "ms");
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = searcher.getDocument(scoreDoc);
			System.out.println("File: " + doc.get(LuceneConstants.FILE_PATH));
		}
		searcher.close();
	}

	private void searchUsingWildCardQuery(String searchQuery)
			throws IOException, ParseException {
		searcher = new Searcher(indexDir);
		long startTime = System.currentTimeMillis();
		// create a term to search file name
		Term term = new Term(LuceneConstants.FILE_NAME, searchQuery);
		// create the term query object
		Query query = new WildcardQuery(term);
		// do the search
		TopDocs hits = searcher.search(query);
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
		TopDocs hits = searcher.search(query);
		long endTime = System.currentTimeMillis();

		System.out.println(hits.totalHits + " documents found. Time :"
				+ (endTime - startTime) + "ms");
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = searcher.getDocument(scoreDoc);
			System.out.print("Score: " + scoreDoc.score + " ");
			System.out.println("File: " + doc.get(LuceneConstants.FILE_PATH));
		}
		searcher.close();
	}

	private void searchUsingMatchAllDocsQuery(String searchQuery)
			throws IOException, ParseException {
		searcher = new Searcher(indexDir);
		long startTime = System.currentTimeMillis();
		// create the term query object
		Query query = new MatchAllDocsQuery();
		// do the search
		TopDocs hits = searcher.search(query);
		long endTime = System.currentTimeMillis();

		System.out.println(hits.totalHits + " documents found. Time :"
				+ (endTime - startTime) + "ms");
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = searcher.getDocument(scoreDoc);
			System.out.print("Score: " + scoreDoc.score + " ");
			System.out.println("File: " + doc.get(LuceneConstants.FILE_PATH));
		}
		searcher.close();
	}

	public static void main(String[] args) {
		LuceneSearchTester tester;
		try {
			tester = new LuceneSearchTester();
			// tester.searchUsingBooleanQuery("2.txt", "1");
			// tester.searchUsingTermRangeQuery("1.txt", "3.txt");
			// tester.searchUsingTermQuery(LuceneConstants.CONTENTS, "ramesh");
			// tester.searchUsingPrefixQuery("1");
			// String[] phrases = new String[] { "record1.txt" };
			// tester.searchUsingPhraseQuery(phrases);
			// tester.searchUsingWildCardQuery("*record?.txt");
			tester.searchUsingFuzzyQuery("ecord3.txt");
			//tester.searchUsingMatchAllDocsQuery("");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

}
