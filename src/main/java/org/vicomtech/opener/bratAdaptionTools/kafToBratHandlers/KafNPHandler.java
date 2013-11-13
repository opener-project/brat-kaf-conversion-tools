package org.vicomtech.opener.bratAdaptionTools.kafToBratHandlers;

import ixa.kaflib.NonTerminal;
import ixa.kaflib.Term;
import ixa.kaflib.Terminal;
import ixa.kaflib.Tree;
import ixa.kaflib.TreeNode;

import java.util.List;

import org.vicomtech.opener.bratAdaptionTools.model.KafDocument;
import org.vicomtech.opener.bratAdaptionTools.model.KafTokenSpan;

import com.google.common.collect.Lists;

public class KafNPHandler implements KafToBratHandler{

	@Override
	public List<KafTokenSpan> handle(KafDocument kafDoc) {
		return getNPsTokenSpans(kafDoc);
	}

	public List<KafTokenSpan> getNPsTokenSpans(KafDocument kafDoc){
		List<Tree> trees = kafDoc.getConstituents();
		List<KafTokenSpan>kafTokenSpans=Lists.newArrayList();
		if(trees==null){
			return kafTokenSpans;
		}
		for(Tree tree:trees){
			NonTerminal root=(NonTerminal)tree.getRoot();
			List<NonTerminal>nps=getNPs(root);
			for(NonTerminal np:nps){
				List<Term> terms = getWFIdsForANonTerminal(np);
				KafTokenSpan kafTokenSpan = KafTokenSpan.getKafTokenSpan(terms, kafDoc);
				kafTokenSpans.add(kafTokenSpan);
			}
		}
		return kafTokenSpans;
	}
	
	public List<NonTerminal> getNPs(NonTerminal nonTerminal){
		List<NonTerminal>nps=Lists.newArrayList();
		if(isNP(nonTerminal)){
			nps.add(nonTerminal);
			return nps;
		}else{
			for(TreeNode treeNode:nonTerminal.getChildren()){
				if(treeNode instanceof NonTerminal){
					NonTerminal childNonTerminal=(NonTerminal)treeNode;
					nps.addAll(getNPs(childNonTerminal));
				}
			}
		}
		return nps;
	}
	
	public List<Term>getWFIdsForANonTerminal(NonTerminal nonTerminal){
		List<Term>terms=Lists.newArrayList();
		for(TreeNode treeNode:nonTerminal.getChildren()){
			if(treeNode instanceof NonTerminal){
				terms.addAll(getWFIdsForANonTerminal((NonTerminal)treeNode));
			}else if(treeNode instanceof Terminal){
				Terminal terminal=(Terminal)treeNode;
				terms.add(terminal.getTerm());
			}
		}
		return terms;
	}
	
	protected boolean isNP(NonTerminal nonTerminal){
		return nonTerminal.getLabel().startsWith("NP");
	}

	
}
