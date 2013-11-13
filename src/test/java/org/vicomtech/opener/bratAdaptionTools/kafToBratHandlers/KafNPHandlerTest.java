package org.vicomtech.opener.bratAdaptionTools.kafToBratHandlers;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.vicomtech.opener.bratAdaptionTools.kafToBratHandlers.KafNPHandler;
import org.vicomtech.opener.bratAdaptionTools.model.KafDocument;
import org.vicomtech.opener.bratAdaptionTools.model.KafTokenSpan;

public class KafNPHandlerTest {

	public static final String KAF_DOC_PATH="/kaf-example-with-constituents.kaf";
	
	private KafDocument kafDocument;
	private KafNPHandler kafNPHandler;
	
	@Before
	public void setUp() throws Exception {
		InputStream is=Class.class.getResourceAsStream(KAF_DOC_PATH);
		kafDocument=KafDocument.parseKafDocument(is);
		kafNPHandler=new KafNPHandler();
	}

	@Test
	public void testHandle() {
		List<KafTokenSpan> kafTokenSpans = kafNPHandler.handle(kafDocument);
		assertNotNull(kafTokenSpans);
		for(KafTokenSpan kafTokenSpan:kafTokenSpans){
			System.out.println(kafTokenSpan);
		}
	}

}
