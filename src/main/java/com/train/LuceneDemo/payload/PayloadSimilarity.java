package com.train.LuceneDemo.payload;

import org.apache.lucene.analysis.payloads.PayloadHelper;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.util.BytesRef;

public class PayloadSimilarity extends DefaultSimilarity {


	@Override
	public float scorePayload(int doc, int start, int end, BytesRef payload) {
		if (payload != null) {
			Float pscore = PayloadHelper.decodeFloat(payload.bytes, payload.offset);
			//Float pscore = PayloadHelper.decodeFloat(payload.bytes); 
            System.out.println("doc:"+doc+" payload : " + payload.toString() + ", payload bytes: " + payload.bytes.toString() + ", decoded value is " + pscore); 
            return pscore; 
		}
		return 1.0f;
	}


}