package org.vicomtech.opener.bratAdaptionTools.kafHandlers;

import java.util.List;

import org.vicomtech.opener.bratAdaptionTools.model.KafDocument;
import org.vicomtech.opener.bratAdaptionTools.model.KafTokenSpan;

public interface KafEntityTokenExtractorHandler {

	public List<KafTokenSpan> handle(KafDocument kafDoc);
	
}
