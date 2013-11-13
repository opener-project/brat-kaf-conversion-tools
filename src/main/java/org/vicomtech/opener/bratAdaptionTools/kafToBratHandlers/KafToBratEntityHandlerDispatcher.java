package org.vicomtech.opener.bratAdaptionTools.kafToBratHandlers;

import java.util.Map;

import org.vicomtech.opener.bratAdaptionTools.model.BratAnnotation;

import com.google.common.collect.Maps;

public class KafToBratEntityHandlerDispatcher {

	private static KafToBratEntityHandlerDispatcher INSTANCE;
	private Map<String,KafToBratHandler>handlers;
	
	private KafToBratEntityHandlerDispatcher(){
		loadHandlers();
	}
	
	public static KafToBratEntityHandlerDispatcher getKafToBratEntityHandlerDispatcher(){
		if(INSTANCE==null){
			INSTANCE=new KafToBratEntityHandlerDispatcher();
		}
		return INSTANCE;
	}
	
	private void loadHandlers(){
		handlers=Maps.newHashMap();
		handlers.put("T", new KafNEsHandler());
		handlers.put("*", new KafCoreferenceHandler());
	}
	
	public KafToBratHandler getKafToBratEntityHandler(BratAnnotation bratAnnotation){
		String key="";//INFER KEY FROM BRAT ANNOTATION
		return handlers.get(key);		
	}
	
}
