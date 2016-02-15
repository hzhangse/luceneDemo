package com.train.LuceneDemo;

import java.io.IOException;

import junit.framework.TestCase;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;

import com.train.LuceneDemo.index.LuceneConstants;
import com.train.LuceneDemo.search.Searcher;

public class PhraseQueryTest extends TestCase {

	private IndexSearcher searcher;

	protected void setUp() throws IOException {

		RAMDirectory directory = new RAMDirectory();

		Analyzer analyzer = new StandardAnalyzer();
		// directory = FSDirectory.open(new File().toPath());

		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter writer = new IndexWriter(directory, config);

		// IndexWriter writer=new IndexWriter(directory,new
		// WhitespaceAnalyzer(),true);

		Document doc = new Document();
		Field field = new Field("field",
				"the quick brown fox jumped over the lazy dog", Field.Store.YES, Field.Index.ANALYZED_NO_NORMS);
//		doc.add(new TextField("field",
//				"the quick brown fox jumped over the lazy dog", Store.YES));
        doc.add(field);
		writer.addDocument(doc);

		writer.close();

		DirectoryReader ireader = DirectoryReader.open(directory);
		searcher = new IndexSearcher(ireader);

	}

	private void matched(String[] phrase, int slop) throws Exception {
		PhraseQuery query = new PhraseQuery();

		query.setSlop(slop);

		for (int i = 0; i < phrase.length; i++) {

			query.add(new Term("field", phrase[i]));

		}

		TopDocs hits = searcher.search(query, LuceneConstants.MAX_SEARCH);
		

		
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = searcher.doc(scoreDoc.doc);
			System.out.println("File: " + doc.get("field"));
		}
		
	}
	
	public void testMatch() throws Exception{
		String[] phrase=new String[]{"fox","brown","lazy"};

		matched(phrase,6) ;
	}
}