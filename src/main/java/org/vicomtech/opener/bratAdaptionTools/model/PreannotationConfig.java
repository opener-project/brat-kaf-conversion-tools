package org.vicomtech.opener.bratAdaptionTools.model;

import java.util.List;

import org.vicomtech.opener.bratAdaptionTools.kafToBratHandlers.KafNEsHandler;
import org.vicomtech.opener.bratAdaptionTools.kafToBratHandlers.KafNPHandler;
import org.vicomtech.opener.bratAdaptionTools.kafToBratHandlers.KafPronounHandler;
import org.vicomtech.opener.bratAdaptionTools.kafToBratHandlers.KafToBratHandler;

import com.google.common.collect.Lists;

public class PreannotationConfig {

	
	private List<KafToBratHandler>preannotationHandlers;
	
	private PreannotationConfig(boolean fill){
		preannotationHandlers=Lists.newArrayList();
		if(fill){
			//Hardcoded for now, but this could be somewhat read from an external configuration
			preannotationHandlers.add(new KafPronounHandler());
			preannotationHandlers.add(new KafNEsHandler());
			preannotationHandlers.add(new KafNPHandler());
		}
	}
	
	public static PreannotationConfig getEmptyPreannotationConfig(){
		return new PreannotationConfig(false);
	}
	
	public static PreannotationConfig getPreannotationConfig(){
		return new PreannotationConfig(true);
	}
	
	public List<KafToBratHandler> getPreannotationHandlers(){
		return preannotationHandlers;
	}
	
}
