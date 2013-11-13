package org.vicomtech.opener.bratAdaptionTools.bratToKafHandlers;

import ixa.kaflib.Term;

import java.util.List;

import org.vicomtech.opener.bratAdaptionTools.model.BratAnnotation;
import org.vicomtech.opener.bratAdaptionTools.model.KafDocument;


public class BratToKafNEHandler implements BratToKafHandler{

	@Override
	public void handleConversion(KafDocument kafDoc, BratAnnotation bratAnnotation, List<BratAnnotation>otherBratAnnotations) {
		List<Term> terms = kafDoc.getTermsForATokenSpan(bratAnnotation.getKafTokenSpan());
		kafDoc.addEntity(bratAnnotation.getType(), terms);
	}

}
