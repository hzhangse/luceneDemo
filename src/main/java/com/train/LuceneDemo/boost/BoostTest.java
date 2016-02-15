package com.train.LuceneDemo.boost;

import java.io.File;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.train.LuceneDemo.index.Indexer;
import com.train.LuceneDemo.index.LuceneConstants;

public class BoostTest {
	public static void testNormsDocBoost() throws Exception {

		Directory indexDirectory = FSDirectory.open(new File("boost").toPath());

		IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
		IndexWriter writer = new IndexWriter(indexDirectory, config);

		Document doc1 = new Document();
		Field f1 = new Field("contents", "common hello hello", Field.Store.YES,
				Field.Index.ANALYZED);
		f1.setBoost(10000);
		doc1.add(f1);
		writer.addDocument(doc1);

		Document doc2 = new Document();
		Field f2 = new Field("contents", "common common hello",
				Field.Store.YES, Field.Index.ANALYZED_NO_NORMS);
		doc2.add(f2);
		writer.addDocument(doc2);

		Document doc3 = new Document();
		Field f3 = new Field("contents", "common common common",
				Field.Store.YES, Field.Index.ANALYZED_NO_NORMS);
		doc3.add(f3);
		writer.addDocument(doc3);
		writer.close();

		DirectoryReader ireader = DirectoryReader.open(indexDirectory);

		IndexSearcher searcher = new IndexSearcher(ireader);
		TopDocs docs = searcher.search(new TermQuery(new Term("contents",
				"common")), 10);
		for (ScoreDoc doc : docs.scoreDocs) {

			System.out.println("docid : " + doc.doc + " score : " + doc.score
					+ " content:" + searcher.doc(doc.doc).get("contents"));
		}
	}

	public static void testNormsFieldBoost() throws Exception {
		Directory indexDirectory = FSDirectory.open(new File("boost").toPath());

		IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
		IndexWriter writer = new IndexWriter(indexDirectory, config);

		Document doc1 = new Document();
		Field f1 = new Field("title", "common hello hello", Field.Store.YES,
				Field.Index.ANALYZED_NO_NORMS);
		// f1.setBoost(100);
		doc1.add(f1);
		writer.addDocument(doc1);
		Document doc2 = new Document();
		Field f2 = new Field("contents", "common common hello",
				Field.Store.YES, Field.Index.ANALYZED_NO_NORMS);
		doc2.add(f2);
		writer.addDocument(doc2);
		writer.close();
		DirectoryReader ireader = DirectoryReader.open(indexDirectory);

		IndexSearcher searcher = new IndexSearcher(ireader);
		QueryParser parser = new QueryParser("contents", new StandardAnalyzer());

		Query query = parser.parse("title:common contents:common");
		TopDocs docs = searcher.search(query, 10);
		for (ScoreDoc doc : docs.scoreDocs) {
			System.out.println("docid : " + doc.doc + " score : " + doc.score
					+ " content:" + searcher.doc(doc.doc).get("contents"));
		}
	}

	public static void testNormsLength() throws Exception {
		Directory indexDirectory = FSDirectory.open(new File("boost").toPath());

		IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
		IndexWriter writer = new IndexWriter(indexDirectory, config);

		Document doc1 = new Document();
		Field f1 = new Field("contents", "common hello hello", Field.Store.NO,
				Field.Index.ANALYZED_NO_NORMS);
		doc1.add(f1);
		writer.addDocument(doc1);
		Document doc2 = new Document();
		Field f2 = new Field("contents",
				"common common hello hello hello hello", Field.Store.NO,
				Field.Index.ANALYZED_NO_NORMS);
		doc2.add(f2);
		writer.addDocument(doc2);
		writer.close();
		DirectoryReader ireader = DirectoryReader.open(indexDirectory);

		IndexSearcher searcher = new IndexSearcher(ireader);
		QueryParser parser = new QueryParser("contents", new StandardAnalyzer());

		Query query = parser.parse("title:common contents:common");
		TopDocs docs = searcher.search(query, 10);
		for (ScoreDoc doc : docs.scoreDocs) {
			System.out.println("docid : " + doc.doc + " score : " + doc.score);
		}
	}

	public static void testOmitNorms() throws Exception {
		Directory indexDirectory = FSDirectory.open(new File("boost").toPath());

		IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
		IndexWriter writer = new IndexWriter(indexDirectory, config);

		Document doc1 = new Document();
		Field f1 = new Field("title", "common hello hello", Field.Store.NO,
				Field.Index.ANALYZED);
		doc1.add(f1);
		writer.addDocument(doc1);
		for (int i = 0; i < 10000; i++) {
			Document doc2 = new Document();
			Field f2 = new Field("contents",
					"common common hello hello hello hello", Field.Store.NO,
					Field.Index.ANALYZED_NO_NORMS);
			doc2.add(f2);
			writer.addDocument(doc2);
		}
		writer.close();
	}

	public static void testQueryBoost() throws Exception {
		Directory indexDirectory = FSDirectory.open(new File("boost").toPath());

		IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
		IndexWriter writer = new IndexWriter(indexDirectory, config);
		Document doc1 = new Document();
		Field f1 = new Field("contents", "common1 hello hello", Field.Store.NO,
				Field.Index.ANALYZED);
		doc1.add(f1);
		writer.addDocument(doc1);
		Document doc2 = new Document();
		Field f2 = new Field("contents", "common2 common2 hello",
				Field.Store.NO, Field.Index.ANALYZED);
		doc2.add(f2);
		writer.addDocument(doc2);
		writer.close();

		DirectoryReader ireader = DirectoryReader.open(indexDirectory);
		IndexSearcher searcher = new IndexSearcher(ireader);
		QueryParser parser = new QueryParser("contents", new StandardAnalyzer());

		// common1^100 common2
		Query query = parser.parse("common1^100 common2");
		TopDocs docs = searcher.search(query, 10);
		for (ScoreDoc doc : docs.scoreDocs) {
			System.out.println("docid : " + doc.doc + " score : " + doc.score);
		}
	}

	public static void TestCoord() throws Exception {
		MySimilarity sim = new MySimilarity();
		Directory indexDirectory = FSDirectory.open(new File("boost").toPath());

		IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
		IndexWriter writer = new IndexWriter(indexDirectory, config);
	
		Document doc1 = new Document();
		Field f1 = new Field("contents", "common hello world", Field.Store.NO,
				Field.Index.ANALYZED);
		doc1.add(f1);
		writer.addDocument(doc1);
		Document doc2 = new Document();
		Field f2 = new Field("contents", "common common common",
				Field.Store.NO, Field.Index.ANALYZED);
		doc2.add(f2);
		writer.addDocument(doc2);
		for (int i = 0; i < 10; i++) {
			Document doc3 = new Document();
			Field f3 = new Field("contents", "world", Field.Store.NO,
					Field.Index.ANALYZED);
			doc3.add(f3);
			writer.addDocument(doc3);
		}
		writer.close();
		
		DirectoryReader ireader = DirectoryReader.open(indexDirectory);
		IndexSearcher searcher = new IndexSearcher(ireader);
		QueryParser parser = new QueryParser("contents", new StandardAnalyzer());

		searcher.setSimilarity(sim);
		Query query = parser.parse("common world");
		TopDocs docs = searcher.search(query, 2);
		for (ScoreDoc doc : docs.scoreDocs) {
			System.out.println("docid : " + doc.doc + " score : " + doc.score);
		}
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		// testNormsDocBoost();
		// testNormsFieldBoost();
		// testNormsLength();
		// testOmitNorms();
		//testQueryBoost();
		TestCoord();
	}

}
