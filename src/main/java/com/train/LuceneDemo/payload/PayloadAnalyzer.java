package com.train.LuceneDemo.payload;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.payloads.DelimitedPayloadTokenFilter;
import org.apache.lucene.analysis.payloads.FloatEncoder;
import org.apache.lucene.analysis.payloads.PayloadEncoder;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import com.train.LuceneDemo.index.LuceneConstants;

public class PayloadAnalyzer extends Analyzer {
	private PayloadEncoder encoder;

	PayloadAnalyzer(PayloadEncoder encoder) {
		this.encoder = encoder;
	}



	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		Tokenizer source = new WhitespaceTokenizer(); // 用来解析空格分隔的各个类别
		TokenStream result = new DelimitedPayloadTokenFilter(source, '|', encoder); // 在上面分词的基础上，在进行Payload数据解析
		TokenStreamComponents com = new TokenStreamComponents(source,result);
		
		return com;
	}
	
	private static void displayTokenUsingSimpleAnalyzer() throws IOException {
	//String text = "foods|0.984 shopping|0.503";
		String text = "egg tomato potato bread";
		Analyzer analyzer = new PayloadAnalyzer(new FloatEncoder()); 
		TokenStream tokenStream = analyzer.tokenStream(
				LuceneConstants.CONTENTS, new StringReader(text));
		CharTermAttribute term = tokenStream
				.addAttribute(CharTermAttribute.class);
		tokenStream.reset();
		while (tokenStream.incrementToken()) {
			// tokenStream.
			// System.out.println(tokenStream.toString());
			System.out.print("[" + term.toString() + "] ");
		}
	}
	
	public  static void main(String[] args) throws Exception{
		displayTokenUsingSimpleAnalyzer();
	}
}
