package org.vicomtech.opener.bratAdaptionTools.kafHandlers;

import static org.junit.Assert.*;

import ixa.kaflib.Term;

import java.io.InputStream;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.vicomtech.opener.bratAdaptionTools.model.KafDocument;
import org.vicomtech.opener.bratAdaptionTools.model.KafTokenSpan;

//import eu.openerproject.kaf.layers.KafTerm;

public class KafPronounHandlerTest {

	public static final String KAF_DOC_PATH="/kaf-example2.xml";
	
	private KafPronounHandler kafPronounHandler;
	private KafDocument kafDocument;
	
	@Before
	public void setUp() throws Exception {
		kafPronounHandler=new KafPronounHandler();
		InputStream is=Class.class.getResourceAsStream(KAF_DOC_PATH);
		kafDocument=KafDocument.parseKafDocument(is);
	}

	@Test
	public void testHandle() {
		List<KafTokenSpan> tokenSpan = kafPronounHandler.handle(kafDocument);
		assertEquals(1,tokenSpan.size());
		assertEquals(8,tokenSpan.get(0).getInitialToken());
		assertEquals(8, tokenSpan.get(0).getFinalToken());
	}

	@Test
	public void testGetTermsWithPostag() {
		List<Term> terms = kafPronounHandler.getTermsWithPostag(kafDocument, "G");
		assertEquals(2,terms.size());
		assertEquals("t7",terms.get(0).getId());
		assertEquals("t12",terms.get(1).getId());
	}

//	@Test
//	public void testGetWordFormSpanForKafTerm() {
//		
//		
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetKafTokenSpan() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetIdNumberFromId() {
//		fail("Not yet implemented");
//	}

}
