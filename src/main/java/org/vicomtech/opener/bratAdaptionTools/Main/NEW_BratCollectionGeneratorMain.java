package org.vicomtech.opener.bratAdaptionTools.Main;

import java.io.File;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

public class NEW_BratCollectionGeneratorMain {

	private static Options options;

	static {
		options = new Options();
		options.addOption("h", "help", false, "Prints this message");
		options.addOption("brat-output-dir", true,
				"Path to directory that will contain the generated Brat collection");
		options.getOption("brat-output-dir").setRequired(true);
		options.addOption("kaf-input-dir", true,
				"Path to directory with the KAF files to generate the Brat collection");
		options.getOption("kaf-input-dir").setRequired(true);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(System.console()==null){
			args=new String[]{};
		}
		execute(args);
	}

	public static void execute(String[]args){
		CommandLineParser parser = new BasicParser();
		try {
			// parse the command line arguments
			CommandLine line = parser.parse(options, args);
			String bratOutputDir=line.getOptionValue("brat-output-dir");
			String kafInputDir=line.getOptionValue("kaf-input-dir");
			File kafDir=new File(kafInputDir);
			File bratDir=new File(bratOutputDir);
			
			BratCollectionGenerator bratCollectionGenerator=new BratCollectionGenerator();
			bratCollectionGenerator.generateBratCollectionFromKafDataset(kafDir, bratDir);
			
		}catch(Exception e){
			System.err.println(e.getMessage());
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("java -jar [NAME_OF_THE_JAR] [OPTION]", options);
		}
	}
	
}
