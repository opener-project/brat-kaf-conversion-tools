package org.vicomtech.opener.bratAdaptionTools.bratAnnHandlers;


import org.vicomtech.opener.bratAdaptionTools.model.BratAnnotation;

public class EntityHandler implements AnnotationHandler{

	@Override
	public BratAnnotation handleAnnotation(String annotationLine) {
		return parseBratAnnotation(annotationLine);
	}

	protected BratAnnotation parseBratAnnotation(String bratAnnotationLine){
		String[]tabSeparatedColumns=bratAnnotationLine.trim().split("\t+");
		String annotationId=tabSeparatedColumns[0];
		String annotationInfo=tabSeparatedColumns[1];
		String annotationType=getAnnotationType(annotationId, annotationInfo);
		int[]span=getSpan(annotationId, annotationInfo);
		String annotationText=tabSeparatedColumns[2];
		BratAnnotation bratAnnotation=new BratAnnotation();
		bratAnnotation.setId(annotationId);
		bratAnnotation.setOffsets(span[0], span[1]);
		bratAnnotation.setType(annotationType);
		bratAnnotation.setText(annotationText);
		return bratAnnotation;
	}
	
	protected String getAnnotationType(String annotationId,String annotationInfo){
		String[]annotationInfoComponents=annotationInfo.split(" ");
		return annotationInfoComponents[0];
	}
	
	protected int[]getSpan(String annotationId,String annotationInfo){
		String[]annotationInfoComponents=annotationInfo.split(" ");
		int[]span=new int[]{-1,-1};
		span[0]=Integer.parseInt(annotationInfoComponents[1]);
		span[1]=Integer.parseInt(annotationInfoComponents[2]);
		return span;
	}

	@Override
	public String writeAnnotation(BratAnnotation bratAnnotation) {
		StringBuffer sb=new StringBuffer();
		sb.append(bratAnnotation.getId());
		sb.append("\t");
		sb.append(bratAnnotation.getType());
		sb.append(" ");
		sb.append(bratAnnotation.getStart());
		sb.append(" ");
		sb.append(bratAnnotation.getEnd());
		sb.append("\t");
		sb.append(bratAnnotation.getText());
		return sb.toString();
	}
	
}
