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
			if(!new File(pathToKafFile).exists()){
				throw new RuntimeException("PATH TO KAF FILE: "+pathToKafFile+" DOES NOT EXIST!");
			}else{
				System.err.println("PATH TO KAF FILE: "+pathToKafFile+" DOES EXIST!");
			}
			InputStream txtIs=getInputStream(pathToTxtFile);
			String pathToAnnFile=pathToTxtFile.replace(".txt", ".ann");
			InputStream annIs=getInputStream(pathToAnnFile);
			InputStream kafFileInputStream=getInputStream(pathToKafFile);
			////////////
//			kafFileInputStream= FileUtils.openInputStream(new File(pathToKafFile));
//			System.err.println("FILE CONTENT: "+FileUtils.readLines(new File(pathToKafFile),"UTF-8"));
//			String path2="D:\\stuff_from_the_laptop_itself\\BRAT_ANNOTATED_FILES_TO_KAF\\HOTEL_SET_1_KAF\\english\\english00001_0123ff23e0d0dc0177f9b71a1928b674.kaf";
//			if(path2.equals(pathToKafFile)){
//				System.err.println("PATHS ARE EQUAL!");
//			}else{
//				System.err.println("PATHS ARE NOT EQUAL!:\n"+path2+"\n"+pathToKafFile);
//			}
//			String kaf=FileUtils.readFileToString(new File(path2), "UTF-8");
//			System.out.println("THIS ONE:\n"+kaf);
			///////////
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
