package org.vicomtech.opener.bratAdaptionTools.Main;

import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
import java.io.IOException;
//import java.io.InputStream;

import javax.xml.ws.BindingProvider;

import org.apache.commons.io.FileUtils;
import org.vicomtech.opener.bratAdaptionTools.BratToKafConverter;
import org.vicomtech.opener.bratAdaptionTools.ws.client.OpenerService;
import org.vicomtech.opener.bratAdaptionTools.ws.client.OpenerServiceImplService;
import static org.vicomtech.opener.bratAdaptionTools.Main.BratAnnotationGenerationTestingMain.*;

public class BratToKafReconversionTestMain {

	private static OpenerService openerService=getOpenerService();
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		BratToKafConverter bratToKafConverter=new BratToKafConverter();
		File dirWithTexts=new File(DIR_FOR_BRAT_RESULTS);
		for(File rawTextFile:dirWithTexts.listFiles()){
			if(rawTextFile.isDirectory()){
				continue;
			}
			if(rawTextFile.getName().endsWith(".txt")){
				File kafFile=new File(DIR_FOR_KAFS+"/"+rawTextFile.getName().substring(0, rawTextFile.getName().length()-4)+".kaf");
				//InputStream kafIs=new FileInputStream(kafFile);
				System.out.println(rawTextFile.getAbsolutePath()+"\n"+kafFile.getAbsolutePath());
				String kafString=bratToKafConverter.convertBratToKaf(rawTextFile.getAbsolutePath(), kafFile.getAbsolutePath());
				FileUtils.write(new File(DIR_FOR_KAFS+"/result_"+kafFile.getName()), kafString);
			}
			
		}
		
		
		//InputStream bratTxtIs=new FileInputStream(new File());
		
	//	bratToKafConverter.convertBratToKaf(bratTxtFileInputStream, annFileInputStream, kafFileInputStream);
		
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
		kaf=openerService.parseConstituents(kaf, lang);
		return kaf;
	}

}
