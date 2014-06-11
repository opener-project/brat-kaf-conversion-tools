package org.vicomtech.opener.bratAdaptionTools.Main;

import java.util.Arrays;

public class CLI {

	public static final String BRAT2KAF="BRAT2KAF";
	public static final String KAF2BRAT="KAF2BRAT";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length==0){
			printErrMessage();
		}else if(args[0].equalsIgnoreCase(BRAT2KAF)){
			String[] args2 = Arrays.copyOfRange(args, 1, args.length);
			NEW_BratToKafConverterMain.execute(args2);
		}else if(args[0].equalsIgnoreCase(KAF2BRAT)){
			String[] args2 = Arrays.copyOfRange(args, 1, args.length);
			NEW_BratCollectionGeneratorMain.execute(args2);
		}else{
			printErrMessage();
		}
	}

	
	public static void printErrMessage(){
		System.err.println("Invalid main parameter: "+KAF2BRAT+" | "+BRAT2KAF);
	}
}
