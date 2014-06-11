package org.vicomtech.opener.bratAdaptionTools.Main;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
//import java.util.Map;
//import java.io.InputStream;


import javax.xml.ws.BindingProvider;

import org.apache.commons.io.FileUtils;
import org.vicomtech.opener.bratAdaptionTools.BratToKafConverter;
import org.vicomtech.opener.bratAdaptionTools.ws.client.OpenerService;
import org.vicomtech.opener.bratAdaptionTools.ws.client.OpenerServiceImplService;

import com.google.common.collect.Lists;

//import com.google.common.collect.Maps;


import static org.vicomtech.opener.bratAdaptionTools.Main.BratAnnotationGenerationTestingMain.*;

@Deprecated
public class BratToKafReconversionTestMain {

	public static final String BRAT_DOCUMENTS_DIR="/D:/stuff_from_the_laptop_itself/BRAT_ANNOTATED_FILES_TO_KAF/HOTEL_SET_1_BRAT/english";
	public static final String KAF_DOCUMENT_DIR="/D:/stuff_from_the_laptop_itself/BRAT_ANNOTATED_FILES_TO_KAF/HOTEL_SET_1_KAF/english";
	
	private static OpenerService openerService=getOpenerService();
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
//		Map<String,String>params=Maps.newHashMap();
//		for(String arg:args){
//			
//		}
		if(System.console()==null){
			System.out.println("Creating our own args");
			args=new String[]{"-d-brat",BRAT_DOCUMENTS_DIR,"-d-kaf",KAF_DOCUMENT_DIR};
		}
		System.out.println("Executing with: "+Arrays.toString(args));
		execute(args);
	}
	public static void execute(String[]args) throws IOException{
		
		if(args.length==0){
			testingExecution();
		}else{
			String pathToBratDocs="";
			String pathToKafDocs="";
			String helpString="Parameter syntax: [-h] -d-brat PATH_TO_BRAT_COLLECTIONS -d-kaf PATH_TO_KAF_OUTPUT_FOLDER\n" +
					"-h      --> Displays this help message\n" +
					"-d-brat --> Path to the root directory containing the convertible brat files\n" +
					"-d-kaf  --> Path to the root directory to write the KAF conversions (the input hierarchy will be maintained)";
			if(args.length==1 && args[0].equals("-h")){
				System.out.println(helpString);
				return;
			}else if(args.length==4){
				if(args[0].equalsIgnoreCase("-d-brat")){
					pathToBratDocs=args[1];
				}
				if(args[2].equalsIgnoreCase("-d-kaf")){
					pathToKafDocs=args[3];
				}
			}
			if(pathToBratDocs.trim().length()==0 || pathToKafDocs.trim().length()==0){
				System.out.println("ERROR: Incorrect parameters");
				System.out.println(helpString);
				return;
			}else{
				actualExecution(pathToBratDocs,pathToKafDocs);
			}
		}
		
		
	}
	
	protected static void actualExecution(String pathForBratDocs,String pathForKafDocs){
		BratToKafConverter bratToKafConverter=new BratToKafConverter();
		File bratConvertibleCollectionsRoot=new File(pathForBratDocs);
		List<String>pathsToBratTxtFile=getAllBratTxtFilePathsRecursively(bratConvertibleCollectionsRoot);
		for(String pathToBratTxtFile:pathsToBratTxtFile){
			String pathToCorrespondingKafFile=pathToBratTxtFile.replace(pathForBratDocs, pathForKafDocs).replace(".txt", ".kaf");
			String pathToCorrespondingAnnFile=pathToBratTxtFile.replace(".txt", ".ann");
			File kafFile=retrieveOrCreateFileAndTheRequiredDirs(pathToCorrespondingKafFile);
			File annFile=retrieveOrCreateFileAndTheRequiredDirs(pathToCorrespondingAnnFile);
			if (annFile.lastModified() > kafFile.lastModified()) {
				System.out.println("Going to convert BRAT to KAF: bratTxtPath:"
						+ pathToBratTxtFile + " pathToKaf: "
						+ pathToCorrespondingKafFile);
				String kafString = bratToKafConverter.convertBratToKaf(
						pathToBratTxtFile, pathToCorrespondingKafFile);
				// File
				// kafFile=retrieveOrCreateFileAndTheRequiredDirs(pathToCorrespondingKafFile);
				try {
					FileUtils.write(kafFile, kafString);
				} catch (IOException e) {
					System.err.println("ERROR writing KAF file: "
							+ e.getMessage());
				}
			}else{
				System.out.println("KAF ("+kafFile.getPath()+") is up to date");
			}
			
		}
		
	}
	
	protected static List<String>getAllBratTxtFilePathsRecursively(File originDir){
		List<String>result=Lists.newArrayList();
		File[]files=originDir.listFiles();
		for(File f:files){
			if(f.isDirectory()){
				result.addAll(getAllBratTxtFilePathsRecursively(f));
			}else if(f.getName().endsWith(".txt")){
				result.add(f.getAbsolutePath());
			}
		}
		return result;
	}
	
	protected static File retrieveOrCreateFileAndTheRequiredDirs(String filePath) {
		try {
			File f = new File(filePath);
			if (f.exists()) {
				return f;
			} else {
				System.out.println("FILEPATH:" +filePath);
				File fDir = new File(filePath.substring(0,filePath.lastIndexOf(File.separator)));
				fDir.mkdirs();

				f.createNewFile();
				return f;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected static void testingExecution() throws IOException{
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
