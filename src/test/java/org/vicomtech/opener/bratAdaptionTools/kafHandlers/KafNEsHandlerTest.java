package org.vicomtech.opener.bratAdaptionTools.kafHandlers;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.vicomtech.opener.bratAdaptionTools.model.KafDocument;
import org.vicomtech.opener.bratAdaptionTools.model.KafTokenSpan;

public class KafNEsHandlerTest {

public static final String KAF_DOC_PATH="/kaf-example2.xml";
public static final String KAF_DOC_PATH2="/kaf-example.xml";
	
	private KafNEsHandler kafNEsHandler;
	private KafDocument kafDocument;
	private KafDocument kafDocument2;
	
	@Before
	public void setUp() throws Exception {
		kafNEsHandler=new KafNEsHandler();
		InputStream is=Class.class.getResourceAsStream(KAF_DOC_PATH);
		kafDocument=KafDocument.parseKafDocument(is);
		InputStream is2=Class.class.getResourceAsStream(KAF_DOC_PATH2);
		kafDocument2=KafDocument.parseKafDocument(is2);
	}

	@Test
	public void testHandle() {
		List<KafTokenSpan> tokenSpans = kafNEsHandler.handle(kafDocument);
		assertEquals(1,tokenSpans.size());
		assertEquals(3, tokenSpans.get(0).getInitialToken());
		assertEquals(3, tokenSpans.get(0).getFinalToken());
		
		tokenSpans = kafNEsHandler.handle(kafDocument2);
		assertEquals(1,tokenSpans.size());
		assertEquals(3, tokenSpans.get(0).getInitialToken());
		assertEquals(3, tokenSpans.get(0).getFinalToken());
	}

}
