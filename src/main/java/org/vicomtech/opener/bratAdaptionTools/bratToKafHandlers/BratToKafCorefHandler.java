package org.vicomtech.opener.bratAdaptionTools.bratToKafHandlers;

import ixa.kaflib.Term;

import java.util.List;

import org.apache.log4j.Logger;
import org.vicomtech.opener.bratAdaptionTools.model.BratAnnotation;
import org.vicomtech.opener.bratAdaptionTools.model.KafDocument;

import com.google.common.collect.Lists;

public class BratToKafCorefHandler implements BratToKafHandler{

	private static Logger log=Logger.getLogger(BratToKafCorefHandler.class);
	
	@Override
	public void handleConversion(KafDocument kafDoc, BratAnnotation bratAnnotation, List<BratAnnotation>otherBratAnnotations) {
		// TODO Auto-generated method stub
		List<List<Term>>corefClusters=Lists.newArrayList();
		List<String> involvedEntities = bratAnnotation.getInvolvedEntities();
		for(String involvedEntity:involvedEntities){
			try{
				BratAnnotation pointedBratAnnotation=BratAnnotation.selectBratAnnotationById(involvedEntity, otherBratAnnotations);
				List<Term>mentionTerms=kafDoc.getTermsForATokenSpan(pointedBratAnnotation.getKafTokenSpan());
				corefClusters.add(mentionTerms);
			}catch(Exception e){
				log.warn("Some error in coref handling. Skipping...");
			}
		}
		kafDoc.addCoref(corefClusters);
	}


	
}
