package org.vicomtech.opener.bratAdaptionTools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.vicomtech.opener.bratAdaptionTools.BratAnnotation.Reference;

import com.google.common.collect.Lists;

public class BratToKafParser {

	public static class WhitespaceTokenInfo{
		public int offset;
		public int length;
		public String text;
		public WhitespaceTokenInfo(int offset,int length,String text){
			this.offset=offset;
			this.length=length;
			this.text=text;
		}
		@Override
		public String toString() {
			return "WhitespaceTokenInfo [offset=" + offset + ", length="
					+ length + ", text=" + text + "]";
		}
	}
	
	private String bratTxtDoc;
	private String bratAnnDoc;
	
//	public String getBratTxtDoc() {
//		return bratTxtDoc;
//	}
//
//	public void setBratTxtDoc(String bratTxtDoc) {
//		this.bratTxtDoc = bratTxtDoc;
//	}
//
//	public String getBratAnnDoc() {
//		return bratAnnDoc;
//	}
//
//	public void setBratAnnDoc(String bratAnnDoc) {
//		this.bratAnnDoc = bratAnnDoc;
//	}
	
	public void loadFiles(String fullPath){
		try {
			InputStream bratTxtDocIs=new FileInputStream(new File(fullPath));
			InputStream bratAnnDocIs=new FileInputStream(new File(fullPath.replace(".txt", ".ann")));
			loadFiles(bratTxtDocIs, bratAnnDocIs);
			bratTxtDocIs.close();
			bratAnnDocIs.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void loadFiles(InputStream bratTxtDocIs,InputStream bratAnnDocIs){
		readFiles(bratTxtDocIs, bratAnnDocIs);
	}
	
	protected void readFiles(InputStream bratTxtDocIs,InputStream bratAnnDocIs){
		try {
			bratTxtDoc = IOUtils.toString(bratTxtDocIs, "UTF-8");
			bratAnnDoc = IOUtils.toString(bratAnnDocIs, "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected List<WhitespaceTokenInfo> obtainWhitespaceTokenList(String bratTxtDoc){
		String[]whiteSpaceTokens=bratTxtDoc.split(" |\n");
		List<WhitespaceTokenInfo>whitespaceTokenList=Lists.newArrayList();
		int offsetCount=0;
		for(String token:whiteSpaceTokens){
			token=token.trim();
			WhitespaceTokenInfo whitespaceTokenInfo=new WhitespaceTokenInfo(offsetCount, token.length(), token);
			offsetCount+=token.length()+1;
			whitespaceTokenList.add(whitespaceTokenInfo);
		}
		return whitespaceTokenList;
	}
	
	protected List<BratAnnotation> parseBratAnnFile(String bratAnnDoc){
		String[]bratAnnFileLines=bratAnnDoc.split("\n");
		List<BratAnnotation>bratAnnotations=Lists.newArrayList();
		for(String bratAnnFileLine:bratAnnFileLines){
			BratAnnotation bratAnnotation=parseBratAnnotation(bratAnnFileLine.trim());
			bratAnnotations.add(bratAnnotation);
		}
		return bratAnnotations;
	}
	
	protected BratAnnotation parseBratAnnotation(String bratAnnotationLine){
		String[]tabSeparatedColumns=bratAnnotationLine.trim().split("\t+");
		String annotationId=tabSeparatedColumns[0];
		String annotationInfo=tabSeparatedColumns[1];
		List<String>involvedEntities=getInvolvedEntities(annotationId, annotationInfo);
		String annotationType=getAnnotationType(annotationId, annotationInfo);
		int[]span=getSpan(annotationId, annotationInfo);
		String annotationText=tabSeparatedColumns.length==3?tabSeparatedColumns[2]:"";
		Reference reference=annotationId.startsWith("N")?getReference(annotationId, annotationInfo):null;
		BratAnnotation bratAnnotation=new BratAnnotation();
		bratAnnotation.setId(annotationId);
		bratAnnotation.setOffsets(span[0], span[1]);
		bratAnnotation.setInvolvedEntities(involvedEntities);
		bratAnnotation.setType(annotationType);
		bratAnnotation.setText(annotationText);
		bratAnnotation.setReference(reference);
		return bratAnnotation;
	}
	
	protected List<String>getInvolvedEntities(String annotationId,String annotationInfo){
		String[]annotationInfoComponents=annotationInfo.split(" ");
		List<String>involvedEntities=Lists.newArrayList();
		if(annotationId.startsWith("*")){
			for(int i=1;i<annotationInfoComponents.length;i++){
				involvedEntities.add(annotationInfoComponents[i]);
			}
		}else if(annotationId.startsWith("R")){
			for(int i=1;i<annotationInfoComponents.length;i++){
				String currentComponent=annotationInfoComponents[i];
				involvedEntities.add(currentComponent.substring(currentComponent.indexOf(":")+1));
			}
		}
		return involvedEntities;
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
	
	protected int[]getSpan(String annotationId,String annotationInfo){
		String[]annotationInfoComponents=annotationInfo.split(" ");
		int[]span=new int[]{-1,-1};
		if(annotationId.startsWith("T")){
			span[0]=Integer.parseInt(annotationInfoComponents[1]);
			span[1]=Integer.parseInt(annotationInfoComponents[2]);
		}
		return span;
	}
	
	protected void mapAnnotationsToTokens(List<BratAnnotation>bratAnnotations, List<WhitespaceTokenInfo>whitespaceTokenList){
		for(BratAnnotation bratAnnotation:bratAnnotations){
			if(bratAnnotation.isEntity()){
				//System.out.println(bratAnnotation);
				int[] kafTokenSpan = getKafTokenSpan(bratAnnotation, whitespaceTokenList);
				bratAnnotation.setKafTokenSpan(kafTokenSpan);
			}
		}
	}
	
	protected int[] getKafTokenSpan(BratAnnotation bratAnnotation, List<WhitespaceTokenInfo>whitespaceTokenList){
		int[] kafTokenSpan=new int[]{-1,-1};
		for(int i=0;i<whitespaceTokenList.size();i++){
			WhitespaceTokenInfo tokenInfo=whitespaceTokenList.get(i);
			//System.out.println(tokenInfo+"\n"+bratAnnotation);
			//Continue iterating
			if(tokenInfo.offset<bratAnnotation.getStart()){
				continue;
			}
			//this is the first token of the span
			if(bratAnnotation.getStart()==tokenInfo.offset){
				kafTokenSpan[0]=i;
//				if(bratAnnotation.getEnd()==tokenInfo.offset+tokenInfo.length+1){
//					//and it is also the last (so only one)
//					kafTokenSpan[1]=i;
//					return kafTokenSpan;
//				}
			}
			//looking for the end of the span
			if(bratAnnotation.getEnd()==tokenInfo.offset+tokenInfo.length){
				kafTokenSpan[1]=i;
				return kafTokenSpan;
			}
		}
		//we should never get here
		throw new RuntimeException("Error mapping tokens, no mapping found");
	}
	
}
