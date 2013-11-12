package org.vicomtech.opener.bratAdaptionTools.model;

import ixa.kaflib.Term;
import ixa.kaflib.WF;

import java.util.List;

import com.google.common.collect.Lists;

public class KafTokenSpan {

	private int initialToken;
	private int finalToken;

	public KafTokenSpan(int initialToken, int finalToken) {
		super();
		this.initialToken = initialToken;
		this.finalToken = finalToken;
	}

	public int getInitialToken() {
		return initialToken;
	}

	public void setInitialToken(int initialToken) {
		this.initialToken = initialToken;
	}

	public int getFinalToken() {
		return finalToken;
	}

	public void setFinalToken(int finalToken) {
		this.finalToken = finalToken;
	}
	
	public boolean isOverlappingSpans(KafTokenSpan otherKafTokenSpan){
		int thisInitToken=this.initialToken;
		int thisFinalToken=this.finalToken;		
		int otherInitToken=otherKafTokenSpan.getInitialToken();
		int otherFinalToken=otherKafTokenSpan.getFinalToken();
		
		if(thisFinalToken>=otherInitToken && thisFinalToken<=otherFinalToken){
			return true;	
		}else if(thisInitToken>=otherInitToken && thisInitToken<=otherFinalToken){
			return true;
		}else{
			return false;
		}
	}
	
	public KafTokenSpan returnMergedSpan(KafTokenSpan otherKafTokenSpan){
		int newInitToken=Math.min(this.initialToken, otherKafTokenSpan.getInitialToken());
		int newFinalToken=Math.max(this.getFinalToken(), otherKafTokenSpan.getFinalToken());
		return new KafTokenSpan(newInitToken,newFinalToken);
	}
	
	/**
	 * Quite a weird implementation... need testing before trusting it...
	 * @param kafTokenSpans
	 * @return
	 */
	public static List<KafTokenSpan>mergeKafTokenSpans(List<KafTokenSpan>kafTokenSpans){
		List<KafTokenSpan>mergedList=Lists.newArrayList();
		mergedList.addAll(kafTokenSpans);
		for(int i=0;i<mergedList.size();i++){
			KafTokenSpan kaftokenSpan=mergedList.get(i);
			for(int j=i+1;j<mergedList.size();j++){
				KafTokenSpan otherKafTokenSpan=mergedList.get(j);
				//System.out.println("Comparing spans (item:"+i+" and "+j+"): "+kaftokenSpan+";"+otherKafTokenSpan);
				if(kaftokenSpan.isOverlappingSpans(otherKafTokenSpan)){
					
					mergedList.set(i, kaftokenSpan.returnMergedSpan(otherKafTokenSpan));
					//System.out.println("MERGING item "+i+" now is "+mergedList.get(i));
					mergedList.remove(j);
					i=-1;// i has to be increased yet, so it need to be -1 to become 0 after the i++
					break;
				}
			}
		}
		return mergedList;
	}

	@Override
	public boolean equals(Object obj) {
		KafTokenSpan otherKafTokenSpan=(KafTokenSpan)obj;
		return (this.initialToken==otherKafTokenSpan.initialToken && this.finalToken==otherKafTokenSpan.finalToken);
	}
	
	@Override
	public String toString() {
		return "[KafTokenSpan:"+initialToken+","+finalToken+"]";
	}
	
	public static KafTokenSpan getKafTokenSpan(List<Term> terms, KafDocument kafDoc) {
		List<WF>wordForms=Lists.newArrayList();
		for(Term term:terms){
			wordForms.addAll(term.getWFs());
		}
		return getKafTokenSpan(wordForms);
	}
	
	public static KafTokenSpan getKafTokenSpan(List<WF>kafWordForms){
		WF first=kafWordForms.get(0);
		WF last=kafWordForms.get(kafWordForms.size()-1);
		System.out.println("Getting KafTokenSpan for WF:[id:"+first.getId()+":"+first.getForm()+"] and [id:"+last.getId()+":"+last.getForm()+"]");
		return new KafTokenSpan(getIdNumberFromId(first.getId())-1, getIdNumberFromId(last.getId())-1);
	}
	
	
	
	protected static int getIdNumberFromId(String id){
		return Integer.parseInt(id.substring(1));
	}
}
