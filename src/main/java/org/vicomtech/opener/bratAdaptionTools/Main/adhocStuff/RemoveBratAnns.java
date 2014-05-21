package org.vicomtech.opener.bratAdaptionTools.Main.adhocStuff;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class RemoveBratAnns {

	public static final String ROOT_PATH="C:\\Users\\yo\\Desktop\\OpeNER_annotation_arrange\\OPENER_BRAT_ANNOTATION_ONLY_HOTELS\\WITHOUT_AUTOMATIC_MARKABLES";
	
	public static void main(String[] args) throws IOException {
		File rootDir=new File(ROOT_PATH);
		removeBrantAnnFileContent(rootDir);

	}

	
	public static void removeBrantAnnFileContent(File file) throws IOException{
		if(file.isDirectory()){
			File[]files=file.listFiles();
			for(File innerFile:files){
				removeBrantAnnFileContent(innerFile);
			}
		}else{
			if(file.getName().endsWith(".ann")){
				FileUtils.write(file, "");
			}
		}
	}
	
}
