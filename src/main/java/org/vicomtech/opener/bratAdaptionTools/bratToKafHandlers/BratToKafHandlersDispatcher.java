package org.vicomtech.opener.bratAdaptionTools.bratToKafHandlers;

import java.util.Map;

import org.vicomtech.opener.bratAdaptionTools.model.BratAnnotation;

import com.google.common.collect.Maps;

public class BratToKafHandlersDispatcher {

	public static final String NE_KEY="";
	public static final String COREF_KEY="";
	
	private static BratToKafHandlersDispatcher INSTANCE;
	
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
		return "";
	}
	
}
