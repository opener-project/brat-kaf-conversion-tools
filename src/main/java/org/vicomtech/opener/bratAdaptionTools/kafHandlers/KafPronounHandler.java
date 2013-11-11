package org.vicomtech.opener.bratAdaptionTools.kafHandlers;

import java.util.List;

import org.vicomtech.opener.bratAdaptionTools.model.KafDocument;
import org.vicomtech.opener.bratAdaptionTools.model.KafTokenSpan;

import com.google.common.collect.Lists;

import eu.openerproject.kaf.layers.KafTarget;
import eu.openerproject.kaf.layers.KafTerm;
import eu.openerproject.kaf.layers.KafWordForm;

public class KafPronounHandler implements KafEntityTokenExtractorHandler{

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
		List<KafTerm>terms=getTermsWithPostag(kafDoc, postagCode);
		for(KafTerm term:terms){
			List<KafWordForm> kafWordForms = getWordFormSpanForKafTerm(term, kafDoc);
			KafTokenSpan tokenSpan = getKafTokenSpan(kafWordForms);
			kafTokenSpans.add(tokenSpan);
		}
		return kafTokenSpans;
	}

	protected List<KafTerm>getTermsWithPostag(KafDocument kafDoc,String postTag){
		List<KafTerm>selectedTerms=Lists.newArrayList();
		List<KafTerm>terms=kafDoc.getTermList();
		for(KafTerm term:terms){
			if(term.getPos().startsWith(postTag)){
				selectedTerms.add(term);
			}
		}
		return selectedTerms;
	}
	
	protected List<KafWordForm> getWordFormSpanForKafTerm(KafTerm term, KafDocument kafDoc){
		List<KafTarget> targets = term.getSpan();
		List<KafWordForm>kafWordFormsForTheTerm=Lists.newArrayList();
		for(KafTarget target:targets){
			String kafWordFormId=target.getId();
			KafWordForm kafWordForm = kafDoc.getWordform(kafWordFormId);
			kafWordFormsForTheTerm.add(kafWordForm);
		}
		return kafWordFormsForTheTerm;
	}
	
	protected KafTokenSpan getKafTokenSpan(List<KafWordForm>kafWordForms){
		KafWordForm first=kafWordForms.get(0);
		KafWordForm last=kafWordForms.get(kafWordForms.size()-1);
		return new KafTokenSpan(getIdNumberFromId(first.getWid())-1, getIdNumberFromId(last.getWid())-1);
	}
	
	protected int getIdNumberFromId(String id){
		return Integer.parseInt(id.substring(1));
	}
	
}
