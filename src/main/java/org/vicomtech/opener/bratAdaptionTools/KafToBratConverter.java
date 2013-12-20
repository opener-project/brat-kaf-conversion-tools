package org.vicomtech.opener.bratAdaptionTools;

import java.util.ArrayList;
import java.util.List;

import org.vicomtech.opener.bratAdaptionTools.BratAnnotationsManager.AnnotationIdType;
import org.vicomtech.opener.bratAdaptionTools.bratAnnHandlers.AnnotationHandler;
import org.vicomtech.opener.bratAdaptionTools.bratAnnHandlers.AnnotationHandlerDispatcher;
import org.vicomtech.opener.bratAdaptionTools.kafToBratHandlers.KafToBratHandler;
import org.vicomtech.opener.bratAdaptionTools.model.BratAnnotation;
import org.vicomtech.opener.bratAdaptionTools.model.KafDocument;
import org.vicomtech.opener.bratAdaptionTools.model.KafTokenSpan;
import org.vicomtech.opener.bratAdaptionTools.model.PreannotationConfig;
import org.vicomtech.opener.bratAdaptionTools.model.WhitespaceToken;

import com.google.common.collect.Lists;

public class KafToBratConverter {
	
	public void bratAnnotationFromKaf(){
		
		
	}
	
	public String generateBratAnnotation(KafDocument kafDocument, PreannotationConfig preAnnotationConfig){
		
		List<KafTokenSpan>kafTokenSpans=Lists.newArrayList();
		for(KafToBratHandler handler:preAnnotationConfig.getPreannotationHandlers()){
			kafTokenSpans.addAll(handler.handle(kafDocument));
		}
		//The following line has been commented to avoid the merging of spans (required for markables)
		//kafTokenSpans=KafTokenSpan.mergeKafTokenSpans(kafTokenSpans);
		String whiteSpaceTokenizedText=WhitespaceToken.generateWhiteSpaceTokenizedText(kafDocument);
		List<WhitespaceToken> whitespaceTokenList = WhitespaceToken.parseText(whiteSpaceTokenizedText);
		BratAnnotationsManager bratAnnotationsManager=new BratAnnotationsManager();
		AnnotationIdType annotationIdType=AnnotationIdType.ENTITY;
		String type="Markable";
		for(KafTokenSpan kafTokenSpan:kafTokenSpans){
			int start=whitespaceTokenList.get(kafTokenSpan.getInitialToken()).getStart();
			int end=whitespaceTokenList.get(kafTokenSpan.getFinalToken()).getEnd();
			String text=WhitespaceToken.getEnclosingText(whitespaceTokenList, kafTokenSpan.getInitialToken(), kafTokenSpan.getFinalToken());
			bratAnnotationsManager.addAnnotation(annotationIdType, type, start, end, new ArrayList<String>(), text);
		}
		List<BratAnnotation> bratAnnotations = bratAnnotationsManager.getAnnotations();
		AnnotationHandlerDispatcher annotationHandlerDispatcher=AnnotationHandlerDispatcher.getAnnotationHandlerDispatcher();
		StringBuffer sb=new StringBuffer();
		for(BratAnnotation bratAnnotation:bratAnnotations){
			AnnotationHandler annotationHandler=annotationHandlerDispatcher.getAnnotationHandler(bratAnnotation);
			String annotationString=annotationHandler.writeAnnotation(bratAnnotation);
			sb.append(annotationString);
			sb.append("\n");
		}
		return sb.toString().trim();
	}
	
	//MAPPING TOKEN BETWEEN KAF AND WHITESPACE TOKENIZED FILES FOR BRAT
	//The spans are actually only used to generate the brat annotation (i.e. colorize stuff)
	//between KAF and WhitespaceTokens there should be a 1:1 mapping
	//KAF_TOKENS[i] = WhitespaceTokens[i]
	
	//Maybe what we need is a map KAF_TOKEN-->WhitespaceToken (which contains the spans)
	//The we can play with the annotations ANN1 --> WSToken1.start .. WSTokenN.end

}
