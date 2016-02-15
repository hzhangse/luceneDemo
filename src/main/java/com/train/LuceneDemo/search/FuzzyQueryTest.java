package com.train.LuceneDemo.search;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;

import com.train.LuceneDemo.index.LuceneConstants;

public class FuzzyQueryTest {

	public FuzzyQueryTest(String INDEX_STORE_PATH) {
		try {
			RAMDirectory directory = new RAMDirectory();

			Analyzer analyzer = new StandardAnalyzer();

			IndexWriterConfig config = new IndexWriterConfig(analyzer);
			IndexWriter writer = new IndexWriter(directory, config);
			
			

			// 创建3个文档
			Document doc1 = new Document();
			Document doc2 = new Document();
			Document doc3 = new Document();

			Field f1 = new Field("content", "word", Field.Store.YES,
					Field.Index.ANALYZED);
			Field f2 = new Field("content", "work", Field.Store.YES,
					Field.Index.ANALYZED);
			Field f3 = new Field("content", "world", Field.Store.YES,
					Field.Index.ANALYZED);

			doc1.add(f1);
			doc2.add(f2);
			doc3.add(f3);

			writer.addDocument(doc1);
			writer.addDocument(doc2);
			writer.addDocument(doc3);
			writer.close();

			DirectoryReader ireader = DirectoryReader.open(directory);
			IndexSearcher searcher = new IndexSearcher(ireader);

			// 构建一个Term对象，然后对其进行模糊查找
			Term t = new Term("content", "work");
			FuzzyQuery query = new FuzzyQuery(t);

			// 打印查询结构
			TopDocs hits = searcher.search(query, LuceneConstants.MAX_SEARCH);
			for (int i = 0; i < hits.scoreDocs.length; i++) {
				Document doc = searcher.doc(hits.scoreDocs[i].doc);
				System.out.println(doc.getField("content"));
				System.out.println(doc.get("content"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("即将进行模糊查找----------------->>>>>>>");
		System.out.println("正在进行模糊查找");
		FuzzyQueryTest fq = new FuzzyQueryTest("E:\\Lucene项目\\索引文件");
		System.out.println("正在进行模糊查找");
		System.out.println("查找结束----------------------->>>>>>>>");
	}

}