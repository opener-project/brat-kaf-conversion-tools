package org.vicomtech.opener.bratAdaptionTools;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import eu.openerproject.kaf.layers.KafMetadata;
import eu.openerproject.kaf.layers.KafWordForm;
import eu.openerproject.kaf.reader.KafSaxParser;

public class KafDocument extends KafSaxParser{

	public KafDocument(){
		super();
		initEmptyKaf();
	}
	
	public KafDocument(InputStream is){
		super.parseFile(is);
	}
	
	protected void initEmptyKaf(){
		super.init();
		KafMetadata metadata=new KafMetadata();
		metadata.addLayer("layer", "layer", "layer");
		metadata.setVersion("1.0");
		metadata.setLanguage("LANG");
		super.setMetadata(metadata);
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
