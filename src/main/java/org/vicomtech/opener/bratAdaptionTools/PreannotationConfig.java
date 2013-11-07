package org.vicomtech.opener.bratAdaptionTools;

import java.util.Map;

public class PreannotationConfig {

	public static enum PreannotationElements{
		NERC,PRONOUNS,NounPhrases,COREF;		
	}
	
	private Map<PreannotationElements,Boolean> config;
	
	
	
}
