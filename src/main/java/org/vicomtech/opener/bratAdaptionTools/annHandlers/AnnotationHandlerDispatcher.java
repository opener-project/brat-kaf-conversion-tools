package org.vicomtech.opener.bratAdaptionTools.annHandlers;

import java.util.Map;

import org.vicomtech.opener.bratAdaptionTools.model.BratAnnotation;

import com.google.common.collect.Maps;

public class AnnotationHandlerDispatcher {

	private static AnnotationHandlerDispatcher INSTANCE;
	
	private Map<String,AnnotationHandler>handlers;
	
	private AnnotationHandlerDispatcher(){
			loadHandlers();
	}
	
	public static AnnotationHandlerDispatcher getAnnotationHandlerDispatcher(){
		if(INSTANCE==null){
			INSTANCE=new AnnotationHandlerDispatcher();
		}
		return INSTANCE;
	}
	
	private void loadHandlers(){
		handlers=Maps.newHashMap();
		handlers.put("T", new EntityHandler());
		handlers.put("R", new RelationHandler());
		handlers.put("N", new NormalizationHandler());
		handlers.put("*", new SymTransRelHandler());
	}
	
	public AnnotationHandler getAnnotationHandler(String bratAnnotationLine){
		String key=bratAnnotationLine.substring(0,1);
		return getAnnotationHandlerByKey(key);
	}
	
	public AnnotationHandler getAnnotationHandler(BratAnnotation bratAnnotation){
		String key=bratAnnotation.getId().substring(0,1);
		return getAnnotationHandlerByKey(key);
	}
	
	protected AnnotationHandler getAnnotationHandlerByKey(String key){
		if(!handlers.containsKey(key)){
			throw new RuntimeException("No annotation handler configured for key: "+key);
		}
		return handlers.get(key);
	}
	
}
