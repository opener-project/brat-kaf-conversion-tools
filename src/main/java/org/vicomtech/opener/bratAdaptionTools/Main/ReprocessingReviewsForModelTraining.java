package org.vicomtech.opener.bratAdaptionTools.Main;

import java.io.File;
//import java.io.IOException;
import java.util.Set;

import javax.xml.ws.BindingProvider;

import org.apache.commons.io.FileUtils;
import org.vicomtech.opener.bratAdaptionTools.ws.client.OpenerService;
import org.vicomtech.opener.bratAdaptionTools.ws.client.OpenerServiceImplService;

import com.google.common.collect.Sets;

public class ReprocessingReviewsForModelTraining {

	public static final String KAF_DOC_FOLDER="KAF_DOCS";
	public static final String COLLECTION_FOLDER="attractionReviews_all_KAF_20140210";
	public static final String INPUT_REVIEWS_ROOT_FOLDER=KAF_DOC_FOLDER+File.separator+COLLECTION_FOLDER;
	
	public static final String REPROCESSED_REVIEWS_OUTPUT_FOLDER="KAF_DOCS"+File.separator+"REPROCESSED_KAFS"+File.separator+COLLECTION_FOLDER;
	
	public static Set<String>languagesToProcess=Sets.newHashSet();
	
	private static OpenerService openerService=getOpenerService();
	
	static{
		languagesToProcess=Sets.newHashSet();
//		languagesToProcess.add("fr");
//		languagesToProcess.add("es");
//		languagesToProcess.add("en");
//		languagesToProcess.add("it");
		languagesToProcess.add("nl");
//		languagesToProcess.add("de");
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ReprocessingReviewsForModelTraining newsForAnnotationAnalysis=new ReprocessingReviewsForModelTraining();
		newsForAnnotationAnalysis.processNewsFiles();
		
	}

	protected void processNewsFiles(){
		File rootDir=new File(INPUT_REVIEWS_ROOT_FOLDER);
		for(File f:rootDir.listFiles()){
			processNewsFile(f, new File(REPROCESSED_REVIEWS_OUTPUT_FOLDER+File.separator+f.getName()), null);
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

				FileUtils.write(fOut, resultingKAF,"UTF-8");
			} catch (Exception e) {
				System.err.println("ERROR IN FILE: "+currentFile.getPath());
				//throw new RuntimeException(e);
				e.printStackTrace();
			}
		}
		
	}
	
	protected String checkIfIsLanguageDirectory(String dirName){
		if(dirName.equalsIgnoreCase("english")){
			return "en";
		}else if(dirName.equalsIgnoreCase("spanish")){
			return "es";
		}else if(dirName.equalsIgnoreCase("french")){
			return "fr";
		}else if(dirName.equalsIgnoreCase("italian")){
			return "it";
		}else if(dirName.equalsIgnoreCase("dutch")){
			return "nl";
		}else if(dirName.equalsIgnoreCase("german")){
			return "de";
		}else{
			return null;
		}
	}
	
//	public String analyzeReviewWithOpeNER(String reviewContent, String expectedLang, boolean alreadyTokenized){
//		//In this case we are expecting kaf with token layer (processed by Andoni)
//		//if we go back to process raw text, uncomment the tokenizer call
//		String kaf = "";
//		if (alreadyTokenized) {
//			kaf = reviewContent;
//		} else {
//			kaf = openerService.tokenize(reviewContent, expectedLang);
//		}
//		kaf = openerService.polarityTag(kaf, expectedLang);
//		kaf = openerService.postag(kaf, expectedLang);
//		kaf = openerService.nerc(kaf, expectedLang);
//		
////		String kafConstit = openerService.parseConstituents(kaf, expectedLang);
//		kaf = openerService.parseConstituents(kaf, expectedLang);
////		if (kafConstit!=null && !kafConstit.trim().equalsIgnoreCase("")) {
////			kaf=kafConstit;
////			String kafCoref = openerService.corefDetect(kafConstit,
////					expectedLang);
////			if (!kafCoref.trim().equalsIgnoreCase("")) {
////				kaf = kafCoref;
////			}
////		}
//		return kaf;
//	}
	
	public String analyzeReviewWithOpeNER(String kaf, String expectedLang, boolean alreadyTokenized){
		//In this case we are expecting kaf with token layer (processed by Andoni)
		//if we go back to process raw text, uncomment the tokenizer call
//		String kaf = "";
//		if (alreadyTokenized) {
//			kaf = reviewContent;
//		} else {
//			kaf = openerService.tokenize(reviewContent, expectedLang);
//		}
		//kaf = openerService.postag(kaf, expectedLang);
		//kaf = openerService.polarityTag(kaf, expectedLang);
		kaf = openerService.nerc(kaf, expectedLang);
		
//		String kafConstit = openerService.parseConstituents(kaf, expectedLang);
	//	kaf = openerService.parseConstituents(kaf, expectedLang);
//		if (kafConstit!=null && !kafConstit.trim().equalsIgnoreCase("")) {
//			kaf=kafConstit;
//			String kafCoref = openerService.corefDetect(kafConstit,
//					expectedLang);
//			if (!kafCoref.trim().equalsIgnoreCase("")) {
//				kaf = kafCoref;
//			}
//		}
		kaf = openerService.corefDetect(kaf, expectedLang);
		return kaf;
	}
	
	protected static OpenerService getOpenerService(){
		OpenerServiceImplService serviceImpl = new OpenerServiceImplService();
		OpenerService service = serviceImpl.getOpenerServiceImplPort();
		// La URL que quieras, esto es lo que deberías obtener mediante
		// configuración externa
//		String endpointURL = "http://192.168.17.128:9999/ws/opener?wsdl";
		String endpointURL = "http://100.68.0.19:9999/ws/opener?wsdl";
		BindingProvider bp = (BindingProvider) service;
		bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				endpointURL);
		//bp.getRequestContext().put("com.sun.xml.internal.ws.request.timeout", 60000);
		return service;
	}
	
}
