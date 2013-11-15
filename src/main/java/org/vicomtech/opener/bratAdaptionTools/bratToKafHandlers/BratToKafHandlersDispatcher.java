package org.vicomtech.opener.bratAdaptionTools.bratToKafHandlers;

import java.util.Map;

import org.vicomtech.opener.bratAdaptionTools.ConfigManager;
import org.vicomtech.opener.bratAdaptionTools.model.BratAnnotation;

import com.google.common.collect.Maps;

public class BratToKafHandlersDispatcher {

	public static final String NE_KEY="NE";
	public static final String COREF_KEY="COREF";
	public static final String DUMMY_KEY="DUMMY";
	
	private static BratToKafHandlersDispatcher INSTANCE;
	private static ConfigManager configManager=ConfigManager.getConfigManager();
	
	private Map<String,BratToKafHandler>handlers;
	
	private BratToKafHandlersDispatcher(){
		loadHandlers();
	}
	
	public static BratToKafHandlersDispatcher getBratToKafHandlersDispatcher(){
		if(INSTANCE==null){
			INSTANCE=new BratToKafHandlersDispatcher();
		}
		return INSTANCE;
	}
	
	private void loadHandlers(){
		handlers=Maps.newHashMap();
		handlers.put(NE_KEY, new BratToKafNEHandler());
		handlers.put(COREF_KEY, new BratToKafCorefHandler());
		handlers.put(DUMMY_KEY, new BratToKafDummyHandler());
	}
	
	public BratToKafHandler getHandler(BratAnnotation bratAnnotation){
		String key=inferKey(bratAnnotation);
		if(!handlers.containsKey(key)){
			throw new RuntimeException("No handler available for key: "+key);
		}
		return handlers.get(key);
		
	}
	
	protected String inferKey(BratAnnotation bratAnnotation){
		//TODO Configure something... regarding annotation type "PERSON", etc., (to avoid adding "markables" or other stuff)
		//this must return one of the above difined keys (NE_KEY, COREF_KEY...)
		String[] neTypes=configManager.getValuesForProperty(ConfigManager.NAMED_ENTITY_TYPES_PROP);
		String[] corefTypes=configManager.getValuesForProperty(ConfigManager.COREF_RELATION_TYPES_PROP);
		String annotationType=bratAnnotation.getType();
		for(String neType:neTypes){
			if(neType.equalsIgnoreCase(annotationType)){
				return NE_KEY;
			}
		}
		for(String corefType:corefTypes){
			if(corefType.equalsIgnoreCase(annotationType)){
				return COREF_KEY;
			}
		}
		return DUMMY_KEY;
	}
	
}
