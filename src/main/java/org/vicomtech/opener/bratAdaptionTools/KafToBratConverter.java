package org.vicomtech.opener.bratAdaptionTools;

import org.vicomtech.opener.bratAdaptionTools.model.KafDocument;
import org.vicomtech.opener.bratAdaptionTools.model.PreannotationConfig;

public class KafToBratConverter {
	
	public void bratAnnotationFromKaf(){
		
		
	}
	
	public String generateBratAnnotation(KafDocument kafDocument, PreannotationConfig preAnnotationConfig){
		
		return null;
	}
	
	//MAPPING TOKEN BETWEEN KAF AND WHITESPACE TOKENIZED FILES FOR BRAT
	//The spans are actually only used to generate the brat annotation (i.e. colorize stuff)
	//between KAF and WhitespaceTokens there should be a 1:1 mapping
	//KAF_TOKENS[i] = WhitespaceTokens[i]
	
	//Maybe what we need is a map KAF_TOKEN-->WhitespaceToken (which contains the spans)
	//The we can play with the annotations ANN1 --> WSToken1.start .. WSTokenN.end
}
