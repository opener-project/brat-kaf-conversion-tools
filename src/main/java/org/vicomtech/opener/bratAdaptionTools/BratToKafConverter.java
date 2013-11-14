package org.vicomtech.opener.bratAdaptionTools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.vicomtech.opener.bratAdaptionTools.bratToKafHandlers.BratToKafHandler;
import org.vicomtech.opener.bratAdaptionTools.bratToKafHandlers.BratToKafHandlersDispatcher;
import org.vicomtech.opener.bratAdaptionTools.model.BratAnnotation;
import org.vicomtech.opener.bratAdaptionTools.model.KafDocument;

public class BratToKafConverter {

	public String convertBratToKaf(String pathToTxtFile, String pathToKafFile){
		try{
			InputStream txtIs=getInputStream(pathToTxtFile);
			String pathToAnnFile=pathToTxtFile.replace(".txt", ".ann");
			InputStream annIs=getInputStream(pathToAnnFile);
			InputStream kafFileInputStream=getInputStream(pathToKafFile);
			String kafString=convertBratToKaf(txtIs,annIs,kafFileInputStream);
			txtIs.close();
			annIs.close();
			kafFileInputStream.close();
			return kafString;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public String convertBratToKaf(InputStream bratTxtFileInputStream, InputStream annFileInputStream, InputStream kafFileInputStream){
		BratAnnotationFileParser bratAnnotationFileParser=new BratAnnotationFileParser();
		KafDocument kafDoc=KafDocument.parseKafDocument(kafFileInputStream);
		kafDoc.removeAllExceptTokensAndTermLayers();
		List<BratAnnotation> bratAnnotations = bratAnnotationFileParser.processFiles(bratTxtFileInputStream, annFileInputStream);
		BratToKafHandlersDispatcher bratToKafHandlersDispatcher=BratToKafHandlersDispatcher.getBratToKafHandlersDispatcher();
		for(BratAnnotation bratAnnotation:bratAnnotations){
			BratToKafHandler bratToKafHandler=bratToKafHandlersDispatcher.getHandler(bratAnnotation);
			bratToKafHandler.handleConversion(kafDoc, bratAnnotation, bratAnnotations);
		}
		String kafString=kafDoc.getKafAsString();
		return kafString;
	}
	
	private InputStream getInputStream(String pathToFile){
		try {
			return new FileInputStream(new File(pathToFile));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
}
