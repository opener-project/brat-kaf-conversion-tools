package org.vicomtech.opener.annotationReviews;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.vicomtech.opener.bratAdaptionTools.KafToBratConverter;
import org.vicomtech.opener.bratAdaptionTools.model.KafDocument;
import org.vicomtech.opener.bratAdaptionTools.model.PreannotationConfig;
import org.vicomtech.opener.bratAdaptionTools.model.WhitespaceToken;

import com.google.common.collect.Maps;

public class BratCollectionGenerator {

	public static final String KAF_DATASET_ROOT="reviewsForAnnotations_KAF_2";
	public static final String BRAT_COLLECTIONS_ROOT="bratCollectionsForAnnotation_2";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BratCollectionGenerator bratCollectionGenerator=new BratCollectionGenerator();
		File kafDir=new File(KAF_DATASET_ROOT);
		File bratDir=new File(BRAT_COLLECTIONS_ROOT);
		bratCollectionGenerator.generateBratCollectionFromKafDataset(kafDir, bratDir);
	}
	
	public void generateBratCollectionFromKafDataset(File kafDir, File bratDir){
		//File rootDirFile=new File(pathToDatasetRoot);
		File[] files=kafDir.listFiles();
		if(!bratDir.exists()){
			bratDir.mkdirs();
		}			
		for(File file:files){
			if(file.isDirectory()){
				generateBratCollectionFromKafDataset(file, appendToPath(bratDir, file.getName()));
			}else if(file.getName().endsWith(".kaf")){
				generateBratDocsFromKaf(file, bratDir);
			}
		}
	}

	public void generateBratDocsFromKaf(File kafFile, File bratDir){
		try{
//			if(!bratDir.exists()){
//				bratDir.mkdirs();
//			}	
			System.out.println("Generating Brat docs for "+kafFile.getName());
			KafToBratConverter kafToBratConverter=new KafToBratConverter();
			KafDocument kafDocument=KafDocument.parseKafDocument(new FileInputStream(kafFile));
			String bratAnn=kafToBratConverter.generateBratAnnotation(kafDocument, PreannotationConfig.getEmptyPreannotationConfig());
			String whiteSpaceTokenizedText=WhitespaceToken.generateWhiteSpaceTokenizedText(kafDocument);
			
			String[] bratFileNames=getBratFilesNames(kafFile.getName(), bratDir);
			File whiteSpaceTokenizedTextFile=appendToPath(bratDir, bratFileNames[0]);
			File bratAnnFile=appendToPath(bratDir, bratFileNames[1]);
			FileUtils.write(whiteSpaceTokenizedTextFile, whiteSpaceTokenizedText);
			FileUtils.write(bratAnnFile, bratAnn);
			
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	protected File appendToPath(File dir,String name){
		return new File(dir.getAbsolutePath()+File.separator+name);
	}
	
	protected String[] getBratFilesNames(String kafFileName, File dir){
		//String kafFileNameWithoutExtension=kafFileName.substring(0,kafFileName.lastIndexOf("."));
		String numbering=FileNumberingManager.obtainNumberForAbsolutePath(dir.getAbsolutePath());
		String bratTxtFileName=kafFileName.substring(0,kafFileName.lastIndexOf("."))+".txt";
		bratTxtFileName=bratTxtFileName.replace("_",numbering+"_");
		String bratAnnFileName=kafFileName.substring(0,kafFileName.lastIndexOf("."))+".ann";
		bratAnnFileName=bratAnnFileName.replace("_", numbering+"_");
		return new String[]{bratTxtFileName,bratAnnFileName};
	}
	
	public static class FileNumberingManager{
		
		private static Map<String,Integer>numberingMap=Maps.newHashMap();;
		
//		public static FileNumberingManager getInstance(){
//			return new FileNumberingManager();
//		}
//		
//		private FileNumberingManager(){
//			numberingMap=Maps.newHashMap();
//		}
		
		public static synchronized String obtainNumberForAbsolutePath(String absolutePath){
			int numberOfDigits=5;
			Integer number=numberingMap.get(absolutePath);
			String formattedNumber="";
			if(number==null){
				number=1;
				numberingMap.put(absolutePath, 1);
			}else{
				numberingMap.put(absolutePath, ++number);
			}
			String numberString=""+number;
			for(int i=0;i<numberOfDigits-numberString.length();i++){
				formattedNumber+=0;
			}
			return formattedNumber+number;
		}
	}
	
}
