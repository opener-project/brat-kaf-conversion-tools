package org.vicomtech.opener.bratAdaptionTools;

import java.io.InputStream;

import org.vicomtech.opener.bratAdaptionTools.kafToBratHandlers.KafToBratEntityHandlerDispatcher;

public class BratToKafConverter {

	public void convertBratToKaf(String pathToTxtFile){
		try{
			InputStream is=Class.class.getResourceAsStream(pathToTxtFile);
			convertBratToKaf(is);
			is.close();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public void convertBratToKaf(InputStream txtFileInputStream){
		BratAnnotationFileParser bratAnnotationFileParser=new BratAnnotationFileParser();
		//KafToBratEntityHandlerDispatcher kafToBratHandler KafToBratEntityHandlerDispatcher.getKafToBratEntityHandlerDispatcher();
		
		
	}
	
}
