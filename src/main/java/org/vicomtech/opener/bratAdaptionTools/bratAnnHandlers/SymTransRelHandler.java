package org.vicomtech.opener.bratAdaptionTools.bratAnnHandlers;

import java.util.List;

import org.vicomtech.opener.bratAdaptionTools.model.BratAnnotation;

import com.google.common.collect.Lists;

public class SymTransRelHandler implements AnnotationHandler {

	@Override
	public BratAnnotation handleAnnotation(String annotationLine) {
		return parseBratAnnotation(annotationLine);
	}

	protected BratAnnotation parseBratAnnotation(String bratAnnotationLine) {
		String[] tabSeparatedColumns = bratAnnotationLine.trim().split("\t+");
		String annotationId = tabSeparatedColumns[0];
		String annotationInfo = tabSeparatedColumns[1];
		List<String> involvedEntities = getInvolvedEntities(annotationId,
				annotationInfo);
		String annotationType = getAnnotationType(annotationId, annotationInfo);
		BratAnnotation bratAnnotation = new BratAnnotation();
		bratAnnotation.setId(annotationId);
		bratAnnotation.setInvolvedEntities(involvedEntities);
		bratAnnotation.setType(annotationType);
		return bratAnnotation;
	}

	protected List<String> getInvolvedEntities(String annotationId,
			String annotationInfo) {
		String[] annotationInfoComponents = annotationInfo.split(" ");
		List<String> involvedEntities = Lists.newArrayList();
		for (int i = 1; i < annotationInfoComponents.length; i++) {
			involvedEntities.add(annotationInfoComponents[i]);
		}
		return involvedEntities;
	}

	protected String getAnnotationType(String annotationId,
			String annotationInfo) {
		String[] annotationInfoComponents = annotationInfo.split(" ");
		return annotationInfoComponents[0];
	}

	@Override
	public String writeAnnotation(BratAnnotation bratAnnotation) {
		//FORMAT EXAMPLE:
		//*	Equiv T1 T2 T3
		StringBuffer sb=new StringBuffer();
		sb.append(bratAnnotation.getId());
		sb.append(" ");
		sb.append(bratAnnotation.getType());
		sb.append(" ");
		for(String involvedEntity:bratAnnotation.getInvolvedEntities()){
			sb.append(involvedEntity);
			sb.append(" ");
		}
		return sb.toString().trim();
	}

}
