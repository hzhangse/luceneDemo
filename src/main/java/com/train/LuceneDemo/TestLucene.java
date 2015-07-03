package com.train.LuceneDemo;



import java.io.File;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class TestLucene {
    // 保存路径
    private static String INDEX_DIR = "luceneIndex";
    private static Analyzer analyzer = null;
    private static Directory directory = null;
    private static IndexWriter indexWriter = null;

    public static void main(String[] args) {
        try {
            index();
            search("man");
            insert();
            search("man");
            delete("text5");
            search("man");
            update();
            search("man");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 更新索引
     * 
     * @throws Exception
     */
    public static void update() throws Exception {
        String text1 = "update,hello,man!";
        Date date1 = new Date();
         analyzer = new StandardAnalyzer();
         directory = FSDirectory.open(new File(INDEX_DIR).toPath());

         IndexWriterConfig config = new IndexWriterConfig(
                 analyzer);
         indexWriter = new IndexWriter(directory, config);
         
         Document doc1 = new Document();
        doc1.add(new TextField("filename", "text1", Store.YES));
        doc1.add(new TextField("content", text1, Store.YES));
        
        indexWriter.updateDocument(new Term("filename","text1"), doc1);
        
         indexWriter.close();
         
         Date date2 = new Date();
         System.out.println("更新索引耗时：" + (date2.getTime() - date1.getTime()) + "ms\n");
    }
    /**
     * 删除索引
     * 
     * @param str 删除的关键字
     * @throws Exception
     */
    public static void delete(String str) throws Exception {
        Date date1 = new Date();
        analyzer = new StandardAnalyzer();
        directory = FSDirectory.open(new File(INDEX_DIR).toPath());

        IndexWriterConfig config = new IndexWriterConfig(
                analyzer);
        indexWriter = new IndexWriter(directory, config);
        
        indexWriter.deleteDocuments(new Term("filename",str));  
        
        indexWriter.close();
        
        Date date2 = new Date();
        System.out.println("删除索引耗时：" + (date2.getTime() - date1.getTime()) + "ms\n");
    }
    /**
     * 增加索引
     * 
     * @throws Exception
     */
    public static void insert() throws Exception {
        String text5 = "hello,goodbye,man,woman";
        Date date1 = new Date();
        analyzer = new StandardAnalyzer();
        directory = FSDirectory.open(new File(INDEX_DIR).toPath());

        IndexWriterConfig config = new IndexWriterConfig(
                analyzer);
        indexWriter = new IndexWriter(directory, config);

        Document doc1 = new Document();
        doc1.add(new TextField("filename", "text5", Store.YES));
        doc1.add(new TextField("content", text5, Store.YES));
        indexWriter.addDocument(doc1);

        indexWriter.commit();
        indexWriter.close();

        Date date2 = new Date();
        System.out.println("增加索引耗时：" + (date2.getTime() - date1.getTime()) + "ms\n");
    }
    /**
     * 建立索引
     * 
     * @param args
     */
    public static void index() throws Exception {
        
        String text1 = "hello,man!";
        String text2 = "goodbye,man!";
        String text3 = "hello,woman!";
        String text4 = "goodbye,woman!";
        
        Date date1 = new Date();
        analyzer = new StandardAnalyzer();
       
        directory = FSDirectory.open(new File(INDEX_DIR).toPath());

        IndexWriterConfig config = new IndexWriterConfig(
                 analyzer);
        indexWriter = new IndexWriter(directory, config);

        Document doc1 = new Document();
        doc1.add(new TextField("filename", "text1", Store.YES));
        doc1.add(new TextField("content", text1, Store.YES));
        indexWriter.addDocument(doc1);
        
        Document doc2 = new Document();
        doc2.add(new TextField("filename", "text2", Store.YES));
        doc2.add(new TextField("content", text2, Store.YES));
        indexWriter.addDocument(doc2);
        
        Document doc3 = new Document();
        doc3.add(new TextField("filename", "text3", Store.YES));
        doc3.add(new TextField("content", text3, Store.YES));
        indexWriter.addDocument(doc3);
        
        Document doc4 = new Document();
        doc4.add(new TextField("filename", "text4", Store.YES));
        doc4.add(new TextField("content", text4, Store.YES));
        indexWriter.addDocument(doc4);
        
        indexWriter.commit();
        indexWriter.close();

        Date date2 = new Date();
        System.out.println("创建索引耗时：" + (date2.getTime() - date1.getTime()) + "ms\n");
    }

    /**
     * 关键字查询
     * 
     * @param str
     * @throws Exception
     */
    public static void search(String str) throws Exception {
        directory = FSDirectory.open(new File(INDEX_DIR).toPath());
        analyzer = new StandardAnalyzer();
        DirectoryReader ireader = DirectoryReader.open(directory);
        IndexSearcher isearcher = new IndexSearcher(ireader);

        QueryParser parser = new QueryParser( "content",analyzer);
        Query query = parser.parse(str);

        ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
        for (int i = 0; i < hits.length; i++) {
            Document hitDoc = isearcher.doc(hits[i].doc);
            System.out.println(hitDoc.get("filename"));
            System.out.println(hitDoc.get("content"));
            System.out.println(hits[i].score);
        }
        ireader.close();
        directory.close();
    }
} 