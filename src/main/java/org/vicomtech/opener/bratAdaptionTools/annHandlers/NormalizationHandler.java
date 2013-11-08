package org.vicomtech.opener.bratAdaptionTools.annHandlers;

import org.vicomtech.opener.bratAdaptionTools.model.BratAnnotation;
import org.vicomtech.opener.bratAdaptionTools.model.BratAnnotation.Reference;


public class NormalizationHandler implements AnnotationHandler{

	@Override
	public BratAnnotation handleAnnotation(String annotationLine) {
		return parseBratAnnotation(annotationLine);
	}
	
	protected BratAnnotation parseBratAnnotation(String bratAnnotationLine){
		String[]tabSeparatedColumns=bratAnnotationLine.trim().split("\t+");
		String annotationId=tabSeparatedColumns[0];
		String annotationInfo=tabSeparatedColumns[1];
		String annotationType=getAnnotationType(annotationId, annotationInfo);
		String annotationText=tabSeparatedColumns[2];
		Reference reference=getReference(annotationId, annotationInfo);
		BratAnnotation bratAnnotation=new BratAnnotation();
		bratAnnotation.setId(annotationId);
		bratAnnotation.setType(annotationType);
		bratAnnotation.setText(annotationText);
		bratAnnotation.setReference(reference);
		return bratAnnotation;
	}
	
	protected String getAnnotationType(String annotationId,String annotationInfo){
		String[]annotationInfoComponents=annotationInfo.split(" ");
		return annotationInfoComponents[0];
	}
	
	protected Reference getReference(String annotationId,String annotationInfo){
		String[]annotationInfoComponents=annotationInfo.split(" ");
		String entity=annotationInfoComponents[1];
		String knowledgeBaseName=annotationInfoComponents[2].split(":")[0];
		String resourceId=annotationInfoComponents[2].split(":")[1];
		Reference reference=new Reference(entity, knowledgeBaseName, resourceId);
		return reference;
	}

	@Override
	public String writeAnnotation(BratAnnotation bratAnnotation) {
		//FORMAT EXAMPLE:
		//N1	Reference T1 Wikipedia:534366	Barack Obama
		StringBuffer sb=new StringBuffer();
		sb.append(bratAnnotation.getId());
		sb.append("\t");
		sb.append("Reference");
		sb.append(" ");
		Reference reference=bratAnnotation.getReference();
		sb.append(reference.getEntity());
		sb.append(" ");
		sb.append(reference.getKnowledgeBaseName());
		sb.append(":");
		sb.append(reference.getResourceId());
		sb.append("\t");
		sb.append(bratAnnotation.getText());
		return sb.toString();
	}

}
