package org.vicomtech.opener.bratAdaptionTools.kafToBratHandlers;

import java.util.List;

import org.vicomtech.opener.bratAdaptionTools.model.KafDocument;
import org.vicomtech.opener.bratAdaptionTools.model.KafTokenSpan;

public interface KafToBratHandler {

	public List<KafTokenSpan> handle(KafDocument kafDoc);
	
}
