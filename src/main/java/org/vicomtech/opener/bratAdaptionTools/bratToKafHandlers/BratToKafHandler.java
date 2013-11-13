package org.vicomtech.opener.bratAdaptionTools.bratToKafHandlers;

import java.util.List;

import org.vicomtech.opener.bratAdaptionTools.model.BratAnnotation;
import org.vicomtech.opener.bratAdaptionTools.model.KafDocument;

public interface BratToKafHandler {

	public void handleConversion(KafDocument kafDoc, BratAnnotation bratAnnotation, List<BratAnnotation>otherBratAnnotations);
	
}
