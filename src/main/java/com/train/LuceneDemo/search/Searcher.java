package com.train.LuceneDemo.search;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.train.LuceneDemo.index.LuceneConstants;

public class Searcher {

	IndexSearcher indexSearcher;
	QueryParser queryParser;
	Query query;

	public Searcher(String indexDirectoryPath) throws IOException {
		Directory indexDirectory = FSDirectory
				.open(new File(indexDirectoryPath).toPath());
		DirectoryReader ireader = DirectoryReader.open(indexDirectory);
		indexSearcher = new IndexSearcher(ireader);
		queryParser = new QueryParser(LuceneConstants.CONTENTS,
				new StandardAnalyzer());
	}

	public TopDocs search(String searchQuery) throws IOException,
			ParseException {
		query = queryParser.parse(searchQuery);
		return indexSearcher.search(query, LuceneConstants.MAX_SEARCH);
	}

	public TopDocs search(Query query) throws IOException, ParseException {
		return indexSearcher.search(query, LuceneConstants.MAX_SEARCH);
	}

	public TopDocs search(Query query, Sort sort) throws IOException,
			ParseException {
		return indexSearcher.search(query, LuceneConstants.MAX_SEARCH, sort,true,true);
	}



	public Document getDocument(ScoreDoc scoreDoc)
			throws CorruptIndexException, IOException {
		return indexSearcher.doc(scoreDoc.doc);
	}

	public void close() throws IOException {

	}
}