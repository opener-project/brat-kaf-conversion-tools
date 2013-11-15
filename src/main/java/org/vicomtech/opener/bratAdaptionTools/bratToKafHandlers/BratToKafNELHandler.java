package org.vicomtech.opener.bratAdaptionTools.bratToKafHandlers;

import ixa.kaflib.Entity;
import ixa.kaflib.ExternalRef;

import java.util.List;

import org.vicomtech.opener.bratAdaptionTools.model.BratAnnotation;
import org.vicomtech.opener.bratAdaptionTools.model.BratAnnotation.Reference;
import org.vicomtech.opener.bratAdaptionTools.model.KafDocument;

public class BratToKafNELHandler implements BratToKafHandler{

	@Override
	public void handleConversion(KafDocument kafDoc,
			BratAnnotation bratAnnotation,
			List<BratAnnotation> otherBratAnnotations) {
		Reference ref=bratAnnotation.getReference();
		BratAnnotation pointedEntityBratAnnotation=BratAnnotation.selectBratAnnotationById(ref.getEntity(), otherBratAnnotations);
		Entity entity=kafDoc.getEntityForATokenSpan(pointedEntityBratAnnotation.getKafTokenSpan());
		ExternalRef externalRef=kafDoc.createExternalRef(bratAnnotation);
		entity.addExternalRef(externalRef);
	}

	

}
