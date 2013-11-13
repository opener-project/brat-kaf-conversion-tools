package org.vicomtech.opener.bratAdaptionTools.kafToBratHandlers;

import ixa.kaflib.Term;
import ixa.kaflib.WF;

import java.util.List;

import org.vicomtech.opener.bratAdaptionTools.model.KafDocument;
import org.vicomtech.opener.bratAdaptionTools.model.KafTokenSpan;

import com.google.common.collect.Lists;

//import eu.openerproject.kaf.layers.KafTarget;
//import eu.openerproject.kaf.layers.KafTerm;
//import eu.openerproject.kaf.layers.KafWordForm;

public class KafPronounHandler implements KafToBratHandler{

	private String postagCode="Q";
	
	public KafPronounHandler(){
		super();
	}
	
	public KafPronounHandler(String postagCode){
		super();
		this.postagCode=postagCode;
	}
	
	@Override
	public List<KafTokenSpan> handle(KafDocument kafDoc) {
		List<KafTokenSpan>kafTokenSpans=Lists.newArrayList();
		List<Term>terms=getTermsWithPostag(kafDoc, postagCode);
		for(Term term:terms){
			List<WF> kafWordForms = getWordFormSpanForKafTerm(term, kafDoc);
			KafTokenSpan tokenSpan = KafTokenSpan.getKafTokenSpan(kafWordForms);
			kafTokenSpans.add(tokenSpan);
		}
		return kafTokenSpans;
	}

	protected List<Term>getTermsWithPostag(KafDocument kafDoc,String postTag){
		List<Term>selectedTerms=Lists.newArrayList();
		List<Term>terms=kafDoc.getTermList();
		for(Term term:terms){
			if(term.getPos().startsWith(postTag)){
				selectedTerms.add(term);
			}
		}
		return selectedTerms;
	}
	
	protected List<WF> getWordFormSpanForKafTerm(Term term, KafDocument kafDoc){
//		List<KafTarget> targets = term.getSpan();
//		List<KafWordForm>kafWordFormsForTheTerm=Lists.newArrayList();
//		for(KafTarget target:targets){
//			String kafWordFormId=target.getId();
//			WF kafWordForm = kafDoc.getWordform(kafWordFormId);
//			kafWordFormsForTheTerm.add(kafWordForm);
//		}
		return term.getWFs();
	//	return kafWordFormsForTheTerm;
	}

	
	
	
}
