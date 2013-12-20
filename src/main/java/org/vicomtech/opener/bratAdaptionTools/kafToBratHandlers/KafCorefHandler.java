package org.vicomtech.opener.bratAdaptionTools.kafToBratHandlers;

import ixa.kaflib.Coref;
import ixa.kaflib.Target;
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
			List<List<Term>> targetsTerms = getAllCorefTerms(coref);
			for(List<Term>terms:targetsTerms){
				KafTokenSpan kafTokenSpan=KafTokenSpan.getKafTokenSpan(terms, kafDoc);
				kafTokenSpans.add(kafTokenSpan);
			}			
		}
		return kafTokenSpans;
	}
	
	protected List<List<Term>>getAllCorefTerms(Coref coref){
		List<List<Term>>targetsTerms=Lists.newArrayList();
		for(List<Target>targets:coref.getReferences()){
			List<Term>terms=Lists.newArrayList();
			for(Target target:targets){
				terms.add(target.getTerm());
			}
			targetsTerms.add(terms);
		}
		return targetsTerms;
	}
	
}
