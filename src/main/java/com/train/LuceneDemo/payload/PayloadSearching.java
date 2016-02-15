package com.train.LuceneDemo.payload;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.payloads.AveragePayloadFunction;
import org.apache.lucene.search.payloads.PayloadTermQuery;
import org.apache.lucene.store.FSDirectory;


public class PayloadSearching {
	
	private IndexReader indexReader;
	private IndexSearcher searcher;
	
	public PayloadSearching(String indexPath) throws CorruptIndexException, IOException {
		
		
		indexReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath,
				new String[0])));
		searcher = new IndexSearcher(indexReader);
		searcher.setSimilarity(new PayloadSimilarity()); // 设置自定义的PayloadSimilarity
	}
	
	public ScoreDoc[] search(String qsr) throws ParseException, IOException {
		int hitsPerPage = 10;
		BooleanQuery bq = new BooleanQuery();
		for(String q : qsr.split(" ")) {
			bq.add(createPayloadTermQuery(q), Occur.MUST);
		}
		TopScoreDocCollector collector = TopScoreDocCollector.create(5 * hitsPerPage, null);
		searcher.search(bq, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		for (int i = 0; i < hits.length; i++) {
			int docId = hits[i].doc; // 文档编号
			Explanation  explanation  = searcher.explain(bq, docId);
			System.out.println(explanation.toString());
		}
		return hits;
	}
	
	public void display(ScoreDoc[] hits, int start, int end) throws CorruptIndexException, IOException {
		end = Math.min(hits.length, end);
		for (int i = start; i < end; i++) {
			Document doc = searcher.doc(hits[i].doc);
			int docId = hits[i].doc; // 文档编号
			float score = hits[i].score; // 文档得分
			System.out.println(docId + "\t" + score + "\t" + doc + "\t");
		}
	}
	
	public void close() throws IOException {
		
		indexReader.close();
	}
	
	private PayloadTermQuery createPayloadTermQuery(String item) {
		PayloadTermQuery ptq = null;
		if(item.indexOf("^")!=-1) {
			String[] a = item.split("\\^");
			String field = a[0].split(":")[0];
			String token = a[0].split(":")[1];
			ptq = new PayloadTermQuery(new Term(field, token), new AveragePayloadFunction());
			ptq.setBoost(Float.parseFloat(a[1].trim()));
		} else {
			String field = item.split(":")[0];
			String token = item.split(":")[1];
			ptq = new PayloadTermQuery(new Term(field, token), new AveragePayloadFunction());
		}
		return ptq;
	}
	
	public static void main(String[] args) throws ParseException, IOException {
		int start = 0, end = 10;	
//		String queries = "category:foods^123.0 content:bread^987.0";
		String queries = "category:foods content:egg";
		PayloadSearching payloadSearcher = new PayloadSearching("payloadindex");
		payloadSearcher.display(payloadSearcher.search(queries), start, end);
		payloadSearcher.close();
	}

}