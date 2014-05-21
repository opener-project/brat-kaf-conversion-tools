package org.vicomtech.opener.bratAdaptionTools.bratToKafHandlers;

import java.util.List;

import org.apache.log4j.Logger;
import org.vicomtech.opener.bratAdaptionTools.model.BratAnnotation;
import org.vicomtech.opener.bratAdaptionTools.model.KafDocument;

public class BratToKafDummyHandler implements BratToKafHandler{

	private static Logger log=Logger.getLogger(BratToKafDummyHandler.class);
	
	@Override
	public void handleConversion(KafDocument kafDoc,
			BratAnnotation bratAnnotation,
			List<BratAnnotation> otherBratAnnotations) {
		log.warn("Dummy brat-to-kaf handler (NO-OP) for "+bratAnnotation);
	}

}
