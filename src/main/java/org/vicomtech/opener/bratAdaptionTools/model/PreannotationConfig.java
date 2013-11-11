package org.vicomtech.opener.bratAdaptionTools.model;

import java.util.List;

import org.vicomtech.opener.bratAdaptionTools.kafHandlers.KafEntityTokenExtractorHandler;
import org.vicomtech.opener.bratAdaptionTools.kafHandlers.KafNEsHandler;
import org.vicomtech.opener.bratAdaptionTools.kafHandlers.KafPronounHandler;

import com.google.common.collect.Lists;

public class PreannotationConfig {

	
	private List<KafEntityTokenExtractorHandler>preannotationHandlers;
	
	public List<KafEntityTokenExtractorHandler> getPreannotationHandlers(){
		//Hardcoded for now, but this could be somewhat read from an external configuration
		preannotationHandlers=Lists.newArrayList();
		preannotationHandlers.add(new KafPronounHandler());
		preannotationHandlers.add(new KafNEsHandler());
		return preannotationHandlers;
	}
	
}
