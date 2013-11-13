package org.vicomtech.opener.bratAdaptionTools.model;

import ixa.kaflib.Coref;
import ixa.kaflib.Entity;
import ixa.kaflib.KAFDocument;
import ixa.kaflib.Target;
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

import com.google.common.collect.Lists;
//import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


public class KafDocument{

	private KAFDocument kaf;
	private Map<String,Term>termMap;
	private Map<String,WF>wordformMap;
	private Map<String,Entity>entityMap;

	
	private KafDocument(InputStream is){
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
		throw new RuntimeException("Not implemented for the IXA KAF parser");
	}
	
	public static KafDocument parseKafDocument(InputStream is){
		return new KafDocument(is);
	}
	
	public String getKafAsString(){
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
	
	public List<Coref>getCorefs(){
		return kaf.getCorefs();
	}
	
	public List<Term>getTermsForATokenSpan(int[] kafTokenSpan){
		List<WF>allWordForms=this.getWordList();
		List<WF>requiredWordForms=Lists.newArrayList();
		for(int i=kafTokenSpan[0];i<=kafTokenSpan[1];i++){
			requiredWordForms.add(allWordForms.get(i));
		}
		List<Term>terms=this.getTermPointingToWordForm(requiredWordForms);
		return terms;
	}
	
	public List<Term> getTermPointingToWordForm(List<WF> wordForms){
		return kaf.getTermsByWFs(wordForms);
	}
	
	public void addEntity(String type,List<Term>terms){
		List<List<Term>>references=Lists.newArrayList();
		references.add(terms);
//		int currentMaxIdNumber=Integer.parseInt(kaf.getEntities().get(kaf.getEntities().size()-1).getId().substring(1));
//		currentMaxIdNumber++;
		String newId="e"+getNextId(kaf.getEntities());
		kaf.createEntity(newId, type, references);
	}
	
	public void addCoref(List<List<Term>>corefTermClusters){
		List<List<Target>>corefTargetsList=Lists.newArrayList();
		for(List<Term>clusterTerms:corefTermClusters){
			List<Target>targets=Lists.newArrayList();
			for(Term clusterTerm:clusterTerms){
				targets.add(KAFDocument.createTarget(clusterTerm));
			}
			corefTargetsList.add(targets);
		}
//		int currentMaxIdNumber=Integer.parseInt(kaf.getCorefs().get(Math.max(kaf.getCorefs().size()-1),0).getId().substring(1));
//		currentMaxIdNumber++;
		String newId="c"+getNextId(kaf.getCorefs());
		kaf.createCoref(newId, corefTargetsList);
	}
	
	@SuppressWarnings("rawtypes")
	protected int getNextId(List list){
		String id="";
		if(list.isEmpty()){
			return 1;
		}else{
			Object lastObj=list.get(list.size()-1);
			if(lastObj instanceof Entity){
				id=((Entity)lastObj).getId();
			}else if(lastObj instanceof Coref){
				id=((Coref)lastObj).getId();
			}else{
				throw new RuntimeException("getNextId not implemented for this kind of objects");
			}
			int idNum=Integer.parseInt(id.substring(1));
			return idNum++;
		}
	}
	
//	private void playingAround(){
//		//IdManager idman;
//		kaf.createEntity("", "", null);
//	}
	
}
