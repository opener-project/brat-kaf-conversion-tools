package org.vicomtech.opener.bratAdaptionTools.annHandlers;

import org.vicomtech.opener.bratAdaptionTools.model.BratAnnotation;

public interface AnnotationHandler {

	public BratAnnotation handleAnnotation(String annotationLine);
	
	public String writeAnnotation(BratAnnotation bratAnnotation);
	
}
