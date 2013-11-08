package org.vicomtech.opener.bratAdaptionTools.model;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import eu.openerproject.kaf.layers.KafMetadata;
import eu.openerproject.kaf.layers.KafWordForm;
import eu.openerproject.kaf.reader.KafSaxParser;

public class KafDocument extends KafSaxParser{

	private KafDocument(){
		super();
	}
	
	private KafDocument(InputStream is){
		super.parseFile(is);
	}
	
	public static KafDocument getEmptyKafDocument(){
		KafDocument kafDocument=new KafDocument();
		kafDocument.init();
		KafMetadata metadata=new KafMetadata();
		metadata.addLayer("layer", "layer", "layer");
		metadata.setVersion("1.0");
		metadata.setLanguage("LANG");
		kafDocument.setMetadata(metadata);
		return kafDocument;
	}
	
	public static KafDocument parseKafDocument(InputStream is){
		return new KafDocument(is);
	}
	
	public void addWordForm(KafWordForm kafWordForm){
		this.getWordList().add(kafWordForm);
	}
	
	public String getKafAsString(){
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		super.writeKafToStream(bos, false);
		return bos.toString();
	}
	
}
