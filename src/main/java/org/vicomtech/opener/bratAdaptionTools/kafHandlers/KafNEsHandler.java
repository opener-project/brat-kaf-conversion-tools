package org.vicomtech.opener.bratAdaptionTools.kafHandlers;

import java.util.List;

import org.vicomtech.opener.bratAdaptionTools.model.KafDocument;
import org.vicomtech.opener.bratAdaptionTools.model.KafTokenSpan;

import com.google.common.collect.Lists;

import eu.openerproject.kaf.layers.KafEntity;
import eu.openerproject.kaf.layers.KafTarget;
import eu.openerproject.kaf.layers.KafTerm;
import eu.openerproject.kaf.layers.KafWordForm;

public class KafNEsHandler implements KafEntityTokenExtractorHandler{

	@Override
	public List<KafTokenSpan> handle(KafDocument kafDoc) {
		List<KafTokenSpan>kafTokenSpans=Lists.newArrayList();
		List<KafEntity>kafEntities=getKafNamedEntities(kafDoc);
		for(KafEntity kafEntity:kafEntities){
			List<KafTerm> terms = getEntityTerms(kafEntity, kafDoc);
			KafTokenSpan kafTokenSpan=getTokenSpan(terms, kafDoc);
			kafTokenSpans.add(kafTokenSpan);
		}
		return kafTokenSpans;
	}
	
	protected List<KafEntity> getKafNamedEntities(KafDocument kafDoc){
		return kafDoc.getEntityList();
	}
	
	protected List<KafTerm>getEntityTerms(KafEntity kafEntity, KafDocument kafDoc){
		List<KafTerm>terms=Lists.newArrayList();
		List<KafTarget> targets = kafEntity.getSpans().get(0);
		for(KafTarget target:targets){
			String termId=target.getId();
			KafTerm term=kafDoc.getTerm(termId);
			terms.add(term);
		}
		return terms;
	}
	
	protected KafTokenSpan getTokenSpan(List<KafTerm>terms,KafDocument kafDoc){
		KafTerm firstTerm=terms.get(0);
		KafWordForm firstWord=getFirstWordFormForATerm(firstTerm, kafDoc);
		KafTerm lastTerm=terms.get(terms.size()-1);
		KafWordForm lastWord=getLastWordFormForATerm(lastTerm, kafDoc);
		KafTokenSpan kafTokenSpan=new KafTokenSpan(getIdNumberFromId(firstWord.getWid())-1, getIdNumberFromId(lastWord.getWid())-1);
		return kafTokenSpan;
	}
	
	protected KafWordForm getFirstWordFormForATerm(KafTerm term, KafDocument kafDoc){
		List<KafTarget> targets = term.getSpan();
		KafWordForm first=kafDoc.getWordform(targets.get(0).getId());
		return first;
	}
	
	protected KafWordForm getLastWordFormForATerm(KafTerm term, KafDocument kafDoc){
		List<KafTarget> targets = term.getSpan();
		KafWordForm last=kafDoc.getWordform(targets.get(targets.size()-1).getId());
		return last;
	}
	
	protected int getIdNumberFromId(String id){
		return Integer.parseInt(id.substring(1));
	}

}
