package org.vicomtech.opener.bratAdaptionTools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
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
	}
	
	private String bratTxtDoc;
	private String bratAnnDoc;
	
	private List<WhitespaceTokenInfo>whitespaceTokenList;
	private List<BratAnnotation>bratAnnotations;

	public BratToKafParser(){
		whitespaceTokenList=Lists.newArrayList();
		bratAnnotations=Lists.newArrayList();
	}
	
	public void loadFiles(String fullPath){
		try {
			InputStream bratTxtDocIs=new FileInputStream(new File(fullPath));
			InputStream bratAnnDocIs=new FileInputStream(new File(fullPath.replace(".txt", ".ann")));
			loadFiles(bratTxtDocIs, bratAnnDocIs);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void loadFiles(InputStream bratTxtDocIs,InputStream bratAnnDocIs){
		readFiles(bratTxtDocIs, bratAnnDocIs);
	}
	
	protected void readFiles(InputStream bratTxtDocIs,InputStream bratAnnDocIs){
		StringWriter writer = new StringWriter();
		try {
			IOUtils.copy(bratTxtDocIs, writer, "UTF-8");
			bratTxtDoc = writer.toString();
			IOUtils.copy(bratAnnDocIs, writer, "UTF-8");
			bratAnnDoc = writer.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected void feedWhitespaceTokenList(){
		String[]whiteSpaceTokens=bratTxtDoc.split(" |\n");
		int offsetCount=0;
		for(String token:whiteSpaceTokens){
			WhitespaceTokenInfo whitespaceTokenInfo=new WhitespaceTokenInfo(offsetCount, token.length(), token);
			offsetCount+=token.length()+1;
			whitespaceTokenList.add(whitespaceTokenInfo);
		}
	}
	
	protected void parseBratAnnFile(){
		String[]bratAnnFileLines=bratAnnDoc.split("\n");
		for(String bratAnnFileLine:bratAnnFileLines){
			BratAnnotation bratAnnotation=parseBratAnnotation(bratAnnFileLine);
			bratAnnotations.add(bratAnnotation);
		}
	}
	
	protected BratAnnotation parseBratAnnotation(String bratAnnotationLine){
		String[]tabSeparatedColumns=bratAnnotationLine.split("\t+");
		String annotationId=tabSeparatedColumns[0];
		String annotationInfo=tabSeparatedColumns[1];
		List<String>involvedEntities=getInvolvedEntities(annotationId, annotationInfo);
		String annotationType=getAnnotationType(annotationId, annotationInfo);
		int[]span=getSpan(annotationId, annotationInfo);
		String annotationText=tabSeparatedColumns[2];
		Reference reference=getReference(annotationId, annotationInfo);
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
	
	protected void mapAnnotationsToTokens(){
		for(BratAnnotation bratAnnotation:bratAnnotations){
			if(bratAnnotation.isEntity()){
				
			}
		}
	}
	
	protected int[] getKafTokenSpan(BratAnnotation bratAnnotation){
		int[] kafTokenSpan=new int[]{-1,-1};
		for(int i=0;i<whitespaceTokenList.size();i++){
			WhitespaceTokenInfo tokenInfo=whitespaceTokenList.get(i);
			//Continue iterating
			if(bratAnnotation.getStart()<tokenInfo.offset){
				continue;
			}
			//this is the first token of the span
			else if(bratAnnotation.getStart()==tokenInfo.offset){
				kafTokenSpan[0]=i;
				if(bratAnnotation.getEnd()==tokenInfo.offset+tokenInfo.length){
					//and it is also the last (so only one)
					kafTokenSpan[1]=i;
					return kafTokenSpan;
				}
			}
			//looking for the end of the span
			else if(bratAnnotation.getEnd()==tokenInfo.offset+tokenInfo.length){
				kafTokenSpan[1]=i;
				return kafTokenSpan;
			}
		}
		//we should never get here
		throw new RuntimeException("Error mapping tokens, no mapping found");
	}
	
}
