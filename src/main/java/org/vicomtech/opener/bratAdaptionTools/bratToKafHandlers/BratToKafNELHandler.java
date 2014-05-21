package org.vicomtech.opener.bratAdaptionTools.bratToKafHandlers;

import ixa.kaflib.Entity;
import ixa.kaflib.ExternalRef;
import java.util.List;

import org.apache.log4j.Logger;
import org.vicomtech.opener.bratAdaptionTools.model.BratAnnotation;
import org.vicomtech.opener.bratAdaptionTools.model.BratAnnotation.Reference;
import org.vicomtech.opener.bratAdaptionTools.model.KafDocument;

public class BratToKafNELHandler implements BratToKafHandler {

	private static Logger log = Logger.getLogger(BratToKafNEHandler.class);

	@Override
	public void handleConversion(KafDocument kafDoc, BratAnnotation bratAnnotation,
			List<BratAnnotation> otherBratAnnotations) {
		try {
			Reference ref = bratAnnotation.getReference();
			BratAnnotation pointedEntityBratAnnotation = BratAnnotation.selectBratAnnotationById(ref.getEntity(),
					otherBratAnnotations);
			Entity entity = kafDoc.getEntityForATokenSpan(pointedEntityBratAnnotation.getKafTokenSpan());
			ExternalRef externalRef = kafDoc.createExternalRef(bratAnnotation);

			entity.addExternalRef(externalRef);
		} catch (Exception e) {
			// System.err.println(kafDoc.getKafAsString());
			// System.err.println("pointedEntityBratAnnotation.getKafTokenSpan() = "+
			// Arrays.toString(pointedEntityBratAnnotation.getKafTokenSpan()));
			// throw new RuntimeException(e);
			log.warn("Some error in NE handler: " + e.getMessage());
		}
	}

}
