package org.vicomtech.opener.bratAdaptionTools.kafToBratHandlers;

import ixa.kaflib.Coref;
import ixa.kaflib.Term;

import java.util.List;

import org.vicomtech.opener.bratAdaptionTools.model.KafDocument;
import org.vicomtech.opener.bratAdaptionTools.model.KafTokenSpan;

import com.google.common.collect.Lists;

public class KafCorefHandler implements KafToBratHandler{

	@Override
	public List<KafTokenSpan> handle(KafDocument kafDoc) {
		List<KafTokenSpan>kafTokenSpans=Lists.newArrayList();
		List<Coref>corefs=kafDoc.getCorefs();
		for(Coref coref:corefs){
			List<Term> terms = coref.getTerms();
			KafTokenSpan kafTokenSpan=KafTokenSpan.getKafTokenSpan(terms, kafDoc);
			kafTokenSpans.add(kafTokenSpan);
		}
		return kafTokenSpans;
	}
}
