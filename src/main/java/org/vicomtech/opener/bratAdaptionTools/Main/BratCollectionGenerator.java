package org.vicomtech.opener.bratAdaptionTools.Main;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.vicomtech.opener.bratAdaptionTools.KafToBratConverter;
import org.vicomtech.opener.bratAdaptionTools.model.KafDocument;
import org.vicomtech.opener.bratAdaptionTools.model.PreannotationConfig;
import org.vicomtech.opener.bratAdaptionTools.model.WhitespaceToken;

import com.google.common.collect.Maps;

/**
 * This class reads the KAF files inside a directory and obtains the brat representation.
 * The brat representation consist on .txt files with the KAF tokens separated by whitespaces and 
 * .ann files with the automatic annotation if any (e.g. automatic markables) 
 * Which automatic annotation is added is controlled by the org.vicomtech.opener.bratAdaptionTools.model.PreannotationConfig class
 * @author agarciap
 *
 */
public class BratCollectionGenerator {

	public static final String KAF_DATASET_ROOT="KAF_DOCS/hotelReviewsSet2_KAF_20140302";//"KAF_DOCS/attractionReviews_KAF_20140203";
	public static final String BRAT_COLLECTIONS_ROOT="BRAT_DOCS/hotelReviewsSet2_BRAT_20140302";//"BRAT_DOCS/attractionReviews_BRAT_20140203";
	
	private int incorrectlyProcessedFileCount=0;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BratCollectionGenerator bratCollectionGenerator=new BratCollectionGenerator();
		File kafDir=new File(KAF_DATASET_ROOT);
		File bratDir=new File(BRAT_COLLECTIONS_ROOT);
		bratCollectionGenerator.generateBratCollectionFromKafDataset(kafDir, bratDir);
	}
	
	public void generateBratCollectionFromKafDataset(File kafDir, File bratDir){
		//incorrectlyProcessedFileCount=0;
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
		System.out.println("DONE. Number of incorrectly processed files: "+incorrectlyProcessedFileCount);
	}

	public void generateBratDocsFromKaf(File kafFile, File bratDir){
		try{
//			if(!bratDir.exists()){
//				bratDir.mkdirs();
//			}	
			System.out.println("Generating Brat docs for "+kafFile.getName());
			KafToBratConverter kafToBratConverter=new KafToBratConverter();
			KafDocument kafDocument=KafDocument.parseKafDocument(new FileInputStream(kafFile));
			String bratAnn=kafToBratConverter.generateBratAnnotation(kafDocument, PreannotationConfig.getPreannotationConfig());
			String whiteSpaceTokenizedText=WhitespaceToken.generateWhiteSpaceTokenizedText(kafDocument);
			
			String[] bratFileNames=getBratFilesNames(kafFile.getName(), bratDir);
			File whiteSpaceTokenizedTextFile=appendToPath(bratDir, bratFileNames[0]);
			File bratAnnFile=appendToPath(bratDir, bratFileNames[1]);
			FileUtils.write(whiteSpaceTokenizedTextFile, whiteSpaceTokenizedText);
			FileUtils.write(bratAnnFile, bratAnn);
			
		}catch(Exception e){
			//if(e.getCause() instanceof JDOMParseException){
				System.err.println("ERROR processing "+kafFile.getName()+" - The file was probably empty");
				incorrectlyProcessedFileCount++;
			//}else{
			//	throw new RuntimeException(e);
			//}
		}
	}
	
	protected File appendToPath(File dir,String name){
		return new File(dir.getAbsolutePath()+File.separator+name);
	}
	
	protected String[] getBratFilesNames(String kafFileName, File dir){
		//String kafFileNameWithoutExtension=kafFileName.substring(0,kafFileName.lastIndexOf("."));
		//String numbering=FileNumberingManager.obtainNumberForAbsolutePath(dir.getAbsolutePath());
		String bratTxtFileName=kafFileName.substring(0,kafFileName.lastIndexOf("."))+".txt";
		//bratTxtFileName=bratTxtFileName.replace("_",numbering+"_");
		String bratAnnFileName=kafFileName.substring(0,kafFileName.lastIndexOf("."))+".ann";
		//bratAnnFileName=bratAnnFileName.replace("_", numbering+"_");
		return new String[]{bratTxtFileName,bratAnnFileName};
	}
	
	public static class FileNumberingManager{
		
		private static Map<String,Integer>numberingMap=Maps.newHashMap();
		
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
