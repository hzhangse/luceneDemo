package com.train.LuceneDemo.payload;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.payloads.FloatEncoder;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;

public class PayloadIndexing {

	private IndexWriter indexWriter = null;
	private final Analyzer analyzer = new PayloadAnalyzer(new FloatEncoder()); // 使用PayloadAnalyzer，并指定Encoder
	private final Similarity similarity = new PayloadSimilarity(); // 实例化一个PayloadSimilarity
	private IndexWriterConfig config = null;

	public PayloadIndexing(String indexPath) throws CorruptIndexException,
			LockObtainFailedException, IOException {
		File indexFile = new File(indexPath);
		config = new IndexWriterConfig(analyzer);
		config.setOpenMode(OpenMode.CREATE).setSimilarity(similarity); // 设置计算得分的Similarity
		indexWriter = new IndexWriter(FSDirectory.open(indexFile.toPath()),
				config);
	}

	public void index() throws CorruptIndexException, IOException {
		Document doc2 = new Document();
		doc2.add(new Field("category", "foods|0.357 shopping|0.791",
				Field.Store.YES, Field.Index.ANALYZED));
		doc2.add(new Field("content", "egg book potato bread", Field.Store.YES,
				Field.Index.ANALYZED));
		indexWriter.addDocument(doc2);
		
		Document doc1 = new Document();
		doc1.add(new Field("category", "foods|0.984 shopping|0.503",
				Field.Store.YES, Field.Index.ANALYZED));
		doc1.add(new Field("content", "egg tomato potato bread",
				Field.Store.YES, Field.Index.ANALYZED));
		indexWriter.addDocument(doc1);

	

		indexWriter.close();
	}

	
	public static void main(String[] args) throws CorruptIndexException,
			IOException {
		new PayloadIndexing("payloadindex").index();
	}
}