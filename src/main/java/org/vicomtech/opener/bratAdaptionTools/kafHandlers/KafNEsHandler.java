package org.vicomtech.opener.bratAdaptionTools.kafHandlers;

import ixa.kaflib.Entity;
import ixa.kaflib.Term;

import java.util.List;

import org.vicomtech.opener.bratAdaptionTools.model.KafDocument;
import org.vicomtech.opener.bratAdaptionTools.model.KafTokenSpan;

import com.google.common.collect.Lists;

public class KafNEsHandler implements KafEntityTokenExtractorHandler{

	@Override
	public List<KafTokenSpan> handle(KafDocument kafDoc) {
		List<KafTokenSpan>kafTokenSpans=Lists.newArrayList();
		List<Entity>kafEntities=getKafNamedEntities(kafDoc);
		for(Entity kafEntity:kafEntities){
			List<Term> terms = kafEntity.getTerms();
			KafTokenSpan kafTokenSpan=KafTokenSpan.getKafTokenSpan(terms, kafDoc);
			kafTokenSpans.add(kafTokenSpan);
		}
		return kafTokenSpans;
	}
	
	protected List<Entity> getKafNamedEntities(KafDocument kafDoc){
		return kafDoc.getEntityList();
	}

}
