package org.vicomtech.opener.bratAdaptionTools.bratToKafHandlers;

import ixa.kaflib.Term;

import java.util.List;

import org.vicomtech.opener.bratAdaptionTools.model.BratAnnotation;
import org.vicomtech.opener.bratAdaptionTools.model.KafDocument;

import com.google.common.collect.Lists;

public class BratToKafCorefHandler implements BratToKafHandler{

	@Override
	public void handleConversion(KafDocument kafDoc, BratAnnotation bratAnnotation, List<BratAnnotation>otherBratAnnotations) {
		// TODO Auto-generated method stub
		List<List<Term>>corefClusters=Lists.newArrayList();
		List<String> involvedEntities = bratAnnotation.getInvolvedEntities();
		for(String involvedEntity:involvedEntities){
			BratAnnotation pointedBratAnnotation=BratAnnotation.selectBratAnnotationById(involvedEntity, otherBratAnnotations);
			List<Term>mentionTerms=kafDoc.getTermsForATokenSpan(pointedBratAnnotation.getKafTokenSpan());
			corefClusters.add(mentionTerms);
		}
		kafDoc.addCoref(corefClusters);
	}


	
}
