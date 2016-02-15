package com.train.LuceneDemo.analyzer;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.cfg.DefaultConfig;
import org.wltea.analyzer.dic.Dictionary;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.train.LuceneDemo.index.LuceneConstants;

public class AnalyzerTest {
	private static void displayTokenUsingSimpleAnalyzer() throws IOException {
		String text = "Lucene is simple yet powerful java based search library.";
		Analyzer analyzer = new SimpleAnalyzer();
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

	private static void displayTokenUsingIK5Analyzer() throws IOException {
		// 检索内容
		String text = "据说WWDC要推出iPhone6要出了？与iPhone5s土豪金相比怎样呢？@2014巴西世界杯 test中文";
		List<String> list = new ArrayList<String>();
		list.add("test中文");
		Dictionary.initial(DefaultConfig.getInstance());
		Dictionary.getSingleton().addWords(list);

		Analyzer analyzer = new IKAnalyzer(true);
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
		analyzer.close();
	}

	private static void displayTokenUsingStopAnalyzer() throws IOException {
		String text = "Lucene is simple yet powerful java based search library.";
		//Analyzer analyzer = new StopAnalyzer();
		Analyzer analyzer = new StandardAnalyzer();
		
		TokenStream tokenStream = analyzer.tokenStream(
				LuceneConstants.CONTENTS, new StringReader(text));
		CharTermAttribute term = tokenStream.addAttribute(CharTermAttribute.class);
		tokenStream.reset();
		while (tokenStream.incrementToken()) {
			System.out.print("[" + term.toString() + "] ");
		}
	}

	public static void main(String[] args) throws Exception {
		// displayTokenUsingSimpleAnalyzer();
		//displayTokenUsingIK5Analyzer();
		displayTokenUsingStopAnalyzer();
	}
}
