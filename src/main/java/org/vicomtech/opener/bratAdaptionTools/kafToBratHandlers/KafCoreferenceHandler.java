package org.vicomtech.opener.bratAdaptionTools.kafToBratHandlers;

import java.util.List;

import org.vicomtech.opener.bratAdaptionTools.model.KafDocument;
import org.vicomtech.opener.bratAdaptionTools.model.KafTokenSpan;

public class KafCoreferenceHandler implements KafToBratHandler{

	@Override
	public List<KafTokenSpan> handle(KafDocument kafDoc) {
		// TODO Auto-generated method stub
		//WAITING FOR RODRIGO'S STUFF REGARDING COREFERENCE SINGLETONS IN KAF, ETC.
		throw new RuntimeException("WAITING FOR RODRIGO'S STUFF REGARDING COREFERENCE SINGLETONS IN KAF, ETC.");
	}

}
