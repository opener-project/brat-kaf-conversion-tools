package org.vicomtech.opener.bratAdaptionTools.bratToKafHandlers;

import java.util.List;

import org.vicomtech.opener.bratAdaptionTools.model.BratAnnotation;
import org.vicomtech.opener.bratAdaptionTools.model.KafDocument;

public class BratToKafDummyHandler implements BratToKafHandler{

	@Override
	public void handleConversion(KafDocument kafDoc,
			BratAnnotation bratAnnotation,
			List<BratAnnotation> otherBratAnnotations) {
		System.out.println("WARNING: Dummy brat-to-kaf handler (NO-OP) for "+bratAnnotation);
	}

}
