package org.vicomtech.opener.bratAdaptionTools.Main;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.xml.ws.BindingProvider;

import org.apache.commons.io.FileUtils;
import org.vicomtech.opener.bratAdaptionTools.KafToBratConverter;
import org.vicomtech.opener.bratAdaptionTools.model.KafDocument;
import org.vicomtech.opener.bratAdaptionTools.model.PreannotationConfig;
import org.vicomtech.opener.bratAdaptionTools.model.WhitespaceToken;
import org.vicomtech.opener.bratAdaptionTools.ws.client.OpenerService;
import org.vicomtech.opener.bratAdaptionTools.ws.client.OpenerServiceImplService;

public class BratAnnotationGenerationTestingMain {

	public static final String DIR_WITH_RAW_FILES="KAF_TO_BRAT_TESTS";
	public static final String DIR_FOR_RESULTS="KAF_TO_BRAT_TESTS/RESULTS";
	
	private static OpenerService openerService=getOpenerService();
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		File dirWithTexts=new File(DIR_WITH_RAW_FILES);
//		if(!dirWithTexts.exists()){
//			dirWithTexts.mkdir();
//		}
		KafToBratConverter kafToBratConverter=new KafToBratConverter();
		for(File rawTextFile:dirWithTexts.listFiles()){
			String textContent=FileUtils.readFileToString(rawTextFile, "UTF-8");
			String kaf=analyzeRawText(textContent);
			KafDocument kafDocument=KafDocument.parseKafDocument(new ByteArrayInputStream(kaf.getBytes()));
			String bratAnn=kafToBratConverter.generateBratAnnotation(kafDocument, new PreannotationConfig());
			String whiteSpaceTokenizedText=WhitespaceToken.generateWhiteSpaceTokenizedText(kafDocument);
			File whiteSpaceTextResultFile=new File(DIR_FOR_RESULTS+"/"+rawTextFile.getName()+".txt");
			File bratAnnResultFile=new File(DIR_FOR_RESULTS+"/"+rawTextFile.getName()+".ann");
			FileUtils.write(whiteSpaceTextResultFile, whiteSpaceTokenizedText);
			FileUtils.write(bratAnnResultFile, bratAnn);
		}

	}
	
	protected static OpenerService getOpenerService(){
		OpenerServiceImplService serviceImpl = new OpenerServiceImplService();
		OpenerService service = serviceImpl.getOpenerServiceImplPort();
		// La URL que quieras, esto es lo que deberías obtener mediante
		// configuración externa
		String endpointURL = "http://192.168.17.128:9999/ws/opener?wsdl";
		BindingProvider bp = (BindingProvider) service;
		bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				endpointURL);
		return service;
	}
	
	protected static String analyzeRawText(String text){
		String lang=openerService.identifyLanguage(text);
		String kaf=openerService.tokenize(text, lang);
		kaf=openerService.postag(kaf, lang);
		kaf=openerService.nerc(kaf, lang);
		return kaf;
	}

}