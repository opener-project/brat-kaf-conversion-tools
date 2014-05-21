package org.vicomtech.opener.bratAdaptionTools.Main;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.vicomtech.opener.bratAdaptionTools.BratToKafConverter;

import com.google.common.collect.Lists;

public class NEW_BratToKafConverterMain {

	private static Logger log=Logger.getLogger(NEW_BratToKafConverterMain.class);
	
	public static final String LANGUAGE="spanish";
	public static final String REVIEW_SET="HOTEL_SET_1";
	
	public static final String BRAT_FILES_DIR = "D:\\stuff_from_the_laptop_itself\\BRAT_ANNOTATED_FILES_TO_KAF\\"+REVIEW_SET+"_BRAT\\"+LANGUAGE;
	public static final String KAF_FILES_DIR = "D:\\stuff_from_the_laptop_itself\\BRAT_ANNOTATED_FILES_TO_KAF\\"+REVIEW_SET+"_KAF\\"+LANGUAGE;
	public static final String OUTPUT_DIR = "D:\\stuff_from_the_laptop_itself\\BRAT_ANNOTATED_FILES_TO_KAF\\"+REVIEW_SET+"_KAF\\"+LANGUAGE+"\\DUMPED";

	private static Options options;

	static {
		options = new Options();
		options.addOption("h", "help", false, "Prints this message");
		options.addOption("brat", "brat-files-directory", true,
				"Path to directory containing Brat files with the annotations");
		options.getOption("brat").setRequired(true);
		options.addOption("kaf", "kaf-files-directory", true,
				"Path to directory with the pairing KAF files of the annotated Brat files");
		options.getOption("kaf").setRequired(true);
		options.addOption("output", "output-kaf-directory", true,
				"Path to the output directory to which the resulting annotated KAF files will be written");
		options.getOption("output").setRequired(true);
	}

	public static void main(String[] args) {
		if (System.console() == null) {
			args = new String[] { "-brat", BRAT_FILES_DIR, "-kaf", KAF_FILES_DIR, "-output", OUTPUT_DIR };
		}
		execute(args);
	}

	public static void execute(String[] args) {
		CommandLineParser parser = new BasicParser();
		try {
			// parse the command line arguments
			CommandLine line = parser.parse(options, args);

			if (line.hasOption("brat") && line.hasOption("kaf") && line.hasOption("output")) {
				String pathToBratFiles = line.getOptionValue("brat");
				String pathToKafFiles = line.getOptionValue("kaf");
				String outputPath = line.getOptionValue("output");
				log.info("Path to brat files: "+pathToBratFiles);
				log.info("Path to kaf files: "+pathToKafFiles);
				log.info("Path to output files: "+outputPath);
				NEW_BratToKafConverterMain new_BratToKafConverterMain=new NEW_BratToKafConverterMain();
				new_BratToKafConverterMain.performConversion(pathToBratFiles, pathToKafFiles, outputPath);
			} else {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("java -jar [NAME_OF_THE_JAR] [OPTION]", options);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void performConversion(String pathToBratFiles,String pathToKafFiles,String outputPath){
		List<File> bratTextFiles = getFileListFromDirectory(pathToBratFiles, ".txt");
		BratToKafConverter bratToKafConverter=new BratToKafConverter();
		for(File bratTxtFile:bratTextFiles){
			log.info("Processing brat file: "+bratTxtFile.getName());
			//File bratAnnFile=getPairingBratAnnFile(bratTxtFile);
			File pairingKafFile=getKafPairingFile(bratTxtFile, pathToKafFiles);
			String kaf=bratToKafConverter.convertBratToKaf(bratTxtFile.getAbsolutePath(), pairingKafFile.getAbsolutePath());
			String outKafFileName=FilenameUtils.concat(outputPath, pairingKafFile.getName());
			File outputKafFile=new File(outKafFileName);
			try {
				FileUtils.write(outputKafFile, kaf,"UTF-8");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected List<File> getFileListFromDirectory(String pathToDirectory, String extensionFilter) {
		File dir = getDirectoryFromPath(pathToDirectory);
		List<File> resultingFiles = Lists.newArrayList();
		for (File file : dir.listFiles()) {
			if (file.getName().endsWith(extensionFilter)) {
				resultingFiles.add(file);
			}
		}
		return resultingFiles;
	}
	protected File getPairingBratAnnFile(File bratTxtFile){
		File bratAnnFile=new File(bratTxtFile.getAbsolutePath().replace(".txt", ".ann"));
		return bratAnnFile;
	}

	protected File getKafPairingFile(File bratTextFile, String pathToKafFilesDirectory) {
		File kafDir = getDirectoryFromPath(pathToKafFilesDirectory);
		String bratTextFileName = bratTextFile.getName();
		String expectedKafName = bratTextFileName.replace(".txt", ".kaf");
		String pairingKafFilePath = FilenameUtils.concat(kafDir.getAbsolutePath(), expectedKafName);
		File pairingKafFile = new File(pairingKafFilePath);
		return pairingKafFile;
	}

	protected File getDirectoryFromPath(String path) {
		File dir = new File(path);
		if (!dir.exists() || !dir.isDirectory()) {
			throw new RuntimeException(dir.getAbsolutePath() + " does NOT exist or is NOT a directory");
		} else {
			return dir;
		}
	}

}
