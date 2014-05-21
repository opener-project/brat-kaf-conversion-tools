package org.vicomtech.opener.bratAdaptionTools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.vicomtech.opener.bratAdaptionTools.bratAnnHandlers.AnnotationHandler;
import org.vicomtech.opener.bratAdaptionTools.bratAnnHandlers.AnnotationHandlerDispatcher;
import org.vicomtech.opener.bratAdaptionTools.model.BratAnnotation;
import org.vicomtech.opener.bratAdaptionTools.model.WhitespaceToken;

import com.google.common.collect.Lists;

public class BratAnnotationFileParser {

	private Logger log=Logger.getLogger(BratAnnotationFileParser.class);
	
	private AnnotationHandlerDispatcher annotationHandlerDispatcher;
	
	public BratAnnotationFileParser(){
		annotationHandlerDispatcher=AnnotationHandlerDispatcher.getAnnotationHandlerDispatcher();
	}

	public void setAnnotationHandlerDispatcher(
			AnnotationHandlerDispatcher annotationHandlerDispatcher) {
		this.annotationHandlerDispatcher = annotationHandlerDispatcher;
	}
	
	public void processFiles(String fullPath){
		try {
			InputStream bratTxtDocIs=new FileInputStream(new File(fullPath));
			InputStream bratAnnDocIs=new FileInputStream(new File(fullPath.replace(".txt", ".ann")));
			processFiles(bratTxtDocIs, bratAnnDocIs);
			bratTxtDocIs.close();
			bratAnnDocIs.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<BratAnnotation> processFiles(InputStream bratTxtDocIs,InputStream bratAnnDocIs){

		String bratTxtDoc=readFile(bratTxtDocIs);
		String bratAnnDoc=readFile(bratAnnDocIs);
		List<WhitespaceToken>whitespaceTokenList=WhitespaceToken.parseText(bratTxtDoc.trim());
		List<BratAnnotation> bratAnnotations = parseBratAnnFile(bratAnnDoc.trim());
		mapAnnotationsToTokens(bratAnnotations, whitespaceTokenList);
		return bratAnnotations;
	}
	
	protected String readFile(InputStream is){
		try {
			return IOUtils.toString(is, "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected List<BratAnnotation> parseBratAnnFile(String bratAnnDoc){
		List<BratAnnotation>bratAnnotations=Lists.newArrayList();
		if(bratAnnDoc.trim().length()==0){
			return bratAnnotations;
		}
		String[]bratAnnFileLines=bratAnnDoc.split("\n");
		for(String bratAnnFileLine:bratAnnFileLines){
			try{
				BratAnnotation bratAnnotation=parseBratAnnotation(bratAnnFileLine.trim());
				bratAnnotations.add(bratAnnotation);
			}catch(Exception e){
				log.warn("Some error parsing a brat annotation: "+bratAnnFileLine.trim()+" ; Skipping...");
			}
		}
		return bratAnnotations;
	}
	
	protected BratAnnotation parseBratAnnotation(String bratAnnotationLine){
		AnnotationHandler annHandler = annotationHandlerDispatcher.getAnnotationHandler(bratAnnotationLine);
		return annHandler.handleAnnotation(bratAnnotationLine);
	}
	
	protected void mapAnnotationsToTokens(List<BratAnnotation>bratAnnotations, List<WhitespaceToken>whitespaceTokenList){
		for(BratAnnotation bratAnnotation:bratAnnotations){
			if(bratAnnotation.isEntity()){
				//System.out.println(bratAnnotation);
				int[] kafTokenSpan = getKafTokenSpan(bratAnnotation, whitespaceTokenList);
				bratAnnotation.setKafTokenSpan(kafTokenSpan);
			}
		}
	}
	
	protected int[] getKafTokenSpan(BratAnnotation bratAnnotation, List<WhitespaceToken>whitespaceTokenList){
		int[] kafTokenSpan=new int[]{-1,-1};
		for(int i=0;i<whitespaceTokenList.size();i++){
			WhitespaceToken tokenInfo=whitespaceTokenList.get(i);
			//Continue iterating
			if(tokenInfo.getStart()<bratAnnotation.getStart()){
				continue;
			}
			//this is the first token of the span
			if(bratAnnotation.getStart()==tokenInfo.getStart()){
				kafTokenSpan[0]=i;
			}
			//looking for the end of the span
			if(bratAnnotation.getEnd()==tokenInfo.getEnd()){
				kafTokenSpan[1]=i;
				return kafTokenSpan;
			}
		}
		//we should never get here
		throw new RuntimeException("Error mapping tokens, no mapping found");
	}
}
