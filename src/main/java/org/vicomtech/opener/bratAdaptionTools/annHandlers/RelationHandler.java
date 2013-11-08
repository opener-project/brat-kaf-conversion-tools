package org.vicomtech.opener.bratAdaptionTools.annHandlers;

import java.util.List;

import org.vicomtech.opener.bratAdaptionTools.model.BratAnnotation;

import com.google.common.collect.Lists;

public class RelationHandler implements AnnotationHandler{

	@Override
	public BratAnnotation handleAnnotation(String annotationLine) {
		return parseBratAnnotation(annotationLine);
	}

	protected BratAnnotation parseBratAnnotation(String bratAnnotationLine){
		String[]tabSeparatedColumns=bratAnnotationLine.trim().split("\t+");
		String annotationId=tabSeparatedColumns[0];
		String annotationInfo=tabSeparatedColumns[1];
		List<String>involvedEntities=getInvolvedEntities(annotationId, annotationInfo);
		String annotationType=getAnnotationType(annotationId, annotationInfo);
		String annotationText=tabSeparatedColumns[2];
		BratAnnotation bratAnnotation=new BratAnnotation();
		bratAnnotation.setId(annotationId);
		bratAnnotation.setInvolvedEntities(involvedEntities);
		bratAnnotation.setType(annotationType);
		bratAnnotation.setText(annotationText);
		return bratAnnotation;
	}
	
	protected List<String>getInvolvedEntities(String annotationId,String annotationInfo){
		String[] annotationInfoComponents = annotationInfo.split(" ");
		List<String> involvedEntities = Lists.newArrayList();

		for (int i = 1; i < annotationInfoComponents.length; i++) {
			String currentComponent = annotationInfoComponents[i];
			involvedEntities.add(currentComponent.substring(currentComponent
					.indexOf(":") + 1));
		}

		return involvedEntities;
	}
	
	protected String getAnnotationType(String annotationId,String annotationInfo){
		String[]annotationInfoComponents=annotationInfo.split(" ");
		return annotationInfoComponents[0];
	}

	@Override
	public String writeAnnotation(BratAnnotation bratAnnotation) {
		//FORMAT EXAMPLE:
		//R1	Origin Arg1:T3 Arg2:T4
		StringBuffer sb=new StringBuffer();
		sb.append(bratAnnotation.getId());
		sb.append("\t");
		sb.append(bratAnnotation.getType());
		sb.append(" ");
		for(int i=0;i<bratAnnotation.getInvolvedEntities().size();i++){
			String involvedEntity=bratAnnotation.getInvolvedEntities().get(i);
			sb.append("Arg");
			sb.append(i);
			sb.append(":");
			sb.append(involvedEntity);
			sb.append(" ");
		}
		return sb.toString().trim();
	}
	
}
