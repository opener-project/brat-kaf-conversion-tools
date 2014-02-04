package org.vicomtech.opener.bratAdaptionTools.Main;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.xml.ws.BindingProvider;

import org.apache.commons.io.FileUtils;
import org.vicomtech.opener.bratAdaptionTools.ws.client.OpenerService;
import org.vicomtech.opener.bratAdaptionTools.ws.client.OpenerServiceImplService;

import com.google.common.collect.Sets;

public class NewsForAnnotationAnalysis {

	public static final String NEWS_DATASET_ROOT_FOLDER="NEWS_FOR_ANNOTATION";
	public static final String PROCESSED_NEWS_OUTPUT_FOLDER="PROCESSED_NEWS_DATASET_20140204";
	
	public static Set<String>languagesToProcess=Sets.newHashSet();;
	
	private static OpenerService openerService=getOpenerService();
	
	static{
		languagesToProcess=Sets.newHashSet();
		languagesToProcess.add("fr");
//		languagesToProcess.add("es");
//		languagesToProcess.add("en");
//		languagesToProcess.add("it");
//		languagesToProcess.add("nl");
//		languagesToProcess.add("de");
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		NewsForAnnotationAnalysis newsForAnnotationAnalysis=new NewsForAnnotationAnalysis();
		newsForAnnotationAnalysis.processNewsFiles();
		
	}

	protected void processNewsFiles(){
		File rootDir=new File(NEWS_DATASET_ROOT_FOLDER);
		for(File f:rootDir.listFiles()){
			processNewsFile(f, new File(PROCESSED_NEWS_OUTPUT_FOLDER+File.separator+f.getName()), null);
		}
		
	}
	
	protected void processNewsFile(File currentFile, File outputFile, String expectedLang){
		System.out.println("Entering processNewFile with params: "+currentFile.getPath()+" "+outputFile.getPath()+" "+expectedLang);
		if(currentFile.isDirectory()){
			System.out.println("Processing directory: "+currentFile.getPath());
			for(File subFile:currentFile.listFiles()){
				File newOutputFile= new File(outputFile.getPath()+File.separator+subFile.getName());
				System.out.println("Setting output path to: "+newOutputFile.getPath());
				expectedLang=expectedLang==null?checkIfIsLanguageDirectory(currentFile.getName()):expectedLang;
				//FILTER LANGUAGES, ONLY THE REQUESTED ONES
				if(!languagesToProcess.isEmpty() && !languagesToProcess.contains(expectedLang)){
					continue;
				}
				///////////////////////////////////////////
				processNewsFile(subFile,newOutputFile, expectedLang);
			}
			
		} else {
			System.out.println(">>>Processing file: "+currentFile.getPath());
			System.out.println(">>>Assigned output file: "+outputFile.getPath());
			try {
				String reviewContent = FileUtils.readFileToString(currentFile);
				String resultingKAF = analyzeReviewWithOpeNER(reviewContent,
						expectedLang, currentFile.getName().endsWith(".kaf"));
				String outputFilePath = outputFile.getPath();
				if (outputFilePath.endsWith(".txt")
						&& !outputFilePath.endsWith(".kaf")) {
					outputFilePath = outputFilePath.substring(0,
							outputFilePath.length() - ".txt".length())
							+ ".kaf";
				}
				File fOut = new File(outputFilePath);
				if (!fOut.getParentFile().exists()) {
					fOut.getParentFile().mkdirs();
				}

				FileUtils.write(fOut, resultingKAF);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
	}
	
	protected String checkIfIsLanguageDirectory(String dirName){
		if(dirName.equalsIgnoreCase("en")){
			return "en";
		}else if(dirName.equalsIgnoreCase("es")){
			return "es";
		}else if(dirName.equalsIgnoreCase("fr")){
			return "fr";
		}else if(dirName.equalsIgnoreCase("it")){
			return "it";
		}else if(dirName.equalsIgnoreCase("nl")){
			return "nl";
		}else if(dirName.equalsIgnoreCase("de")){
			return "de";
		}else{
			return null;
		}
	}
	
	public String analyzeReviewWithOpeNER(String reviewContent, String expectedLang, boolean alreadyTokenized){
		//In this case we are expecting kaf with token layer (processed by Andoni)
		//if we go back to process raw text, uncomment the tokenizer call
		String kaf = "";
		if (alreadyTokenized) {
			kaf = reviewContent;
		} else {
			kaf = openerService.tokenize(reviewContent, expectedLang);
		}
		kaf = openerService.postag(kaf, expectedLang);
		kaf = openerService.nerc(kaf, expectedLang);
		
//		String kafConstit = openerService.parseConstituents(kaf, expectedLang);
//		if (kafConstit!=null && !kafConstit.trim().equalsIgnoreCase("")) {
//			kaf=kafConstit;
//			String kafCoref = openerService.corefDetect(kafConstit,
//					expectedLang);
//			if (!kafCoref.trim().equalsIgnoreCase("")) {
//				kaf = kafCoref;
//			}
//		}
		return kaf;
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
		//bp.getRequestContext().put("com.sun.xml.internal.ws.request.timeout", 60000);
		return service;
	}
	
}
