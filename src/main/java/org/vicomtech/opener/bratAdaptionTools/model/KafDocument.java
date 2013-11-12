package org.vicomtech.opener.bratAdaptionTools.model;

import ixa.kaflib.Entity;
import ixa.kaflib.KAFDocument;
//import ixa.kaflib.NonTerminal;
import ixa.kaflib.Term;
//import ixa.kaflib.Terminal;
import ixa.kaflib.Tree;
//import ixa.kaflib.TreeNode;
import ixa.kaflib.WF;

import java.io.BufferedReader;
//import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

//import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

//import eu.openerproject.kaf.layers.KafEntity;
//import eu.openerproject.kaf.layers.KafMetadata;
//import eu.openerproject.kaf.layers.KafTerm;
//import eu.openerproject.kaf.layers.KafWordForm;
//import eu.openerproject.kaf.reader.KafSaxParser;

public class KafDocument{

	private KAFDocument kaf;
	private Map<String,Term>termMap;
	private Map<String,WF>wordformMap;
	private Map<String,Entity>entityMap;
	
//	private KafDocument(){
//		super();
//	}
	
	private KafDocument(InputStream is){
		//super.parseFile(is);
		BufferedReader breader=new BufferedReader(new InputStreamReader(is));
		try {
			kaf = KAFDocument.createFromStream(breader);
			wordformMap=Maps.newHashMap();
			for(WF wf:kaf.getWFs()){
				wordformMap.put(wf.getId(), wf);
			}
			termMap=Maps.newHashMap();
			for(Term term:kaf.getTerms()){
				termMap.put(term.getId(), term);
			}
			entityMap=Maps.newHashMap();
			for(Entity entity:kaf.getEntities()){
				entityMap.put(entity.getId(), entity);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static KafDocument getEmptyKafDocument(){
//		KafDocument kafDocument=new KafDocument();
//		kafDocument.init();
//		KafMetadata metadata=new KafMetadata();
//		metadata.addLayer("layer", "layer", "layer");
//		metadata.setVersion("1.0");
//		metadata.setLanguage("LANG");
//		kafDocument.setMetadata(metadata);
//		return kafDocument;
		throw new RuntimeException("Not implemented for the IXA KAF parser");
	}
	
	public static KafDocument parseKafDocument(InputStream is){
		return new KafDocument(is);
	}
	
//	public void addWordForm(KafWordForm kafWordForm){
//		this.getWordList().add(kafWordForm);
//	}
	
	public String getKafAsString(){
//		ByteArrayOutputStream bos=new ByteArrayOutputStream();
//		super.writeKafToStream(bos, false);
//		return bos.toString();
		return kaf.toString();
	}

	public List<WF> getWordList() {
		return kaf.getWFs();
	}

	public Term getTerm(String termId) {
		return termMap.get(termId);
	}

	public List<Term> getTermList() {
		return kaf.getTerms();
	}

	public WF getWordform(String id) {
		return wordformMap.get(id);
	}

	public List<Entity> getEntityList() {
		return kaf.getEntities();
	}

	public List<Tree> getConstituents() {
		return kaf.getConstituents();
	}
	
	
	
}
