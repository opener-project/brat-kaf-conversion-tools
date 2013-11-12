package org.vicomtech.opener.bratAdaptionTools;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.vicomtech.opener.bratAdaptionTools.model.KafDocument;

//import eu.openerproject.kaf.layers.KafWordForm;

public class KafDocumentTest {

	public static final String KAF_DOC_PATH="/kaf-example2.xml";
	public static final String KAF_DOC_PATH2="/kaf-example.xml";
	
	private KafDocument kafDocument;
	//private KafDocument kafDocument2;
//	private KafWordForm exampleKafWordForm;
	
	@Before
	public void setUp() throws Exception {
		InputStream is=Class.class.getResourceAsStream(KAF_DOC_PATH);
		kafDocument=KafDocument.parseKafDocument(is);
//		InputStream is2=Class.class.getResourceAsStream(KAF_DOC_PATH2);
//		kafDocument2=KafDocument.parseKafDocument(is2);
		
//		kafDocument=KafDocument.getEmptyKafDocument();
//		exampleKafWordForm=new KafWordForm();
//		exampleKafWordForm.setWid("w1");
//		exampleKafWordForm.setSent(1);
//		exampleKafWordForm.setLength(5);
//		exampleKafWordForm.setOffset(17);
//		exampleKafWordForm.setWordform("TEST");
	}

	@Test
	public void testKafDocument() {
		//assertEquals(kafDocument.getWordList().size(),0);
		assertTrue(true);
	}

	@Test
	public void testKafDocumentInputStream() {
		InputStream is=Class.class.getResourceAsStream("/kaf-example.xml");
		kafDocument=KafDocument.parseKafDocument(is);
		String kafString=kafDocument.getKafAsString();
		assertNotNull(kafString);
		System.out.println(kafString);
	}

//	@Test
//	public void testInitEmptyKaf() {
//		kafDocument.initEmptyKaf();
//		assertEquals(kafDocument.getWordList().size(),0);
//	}

//	@Test
//	public void testAddWordForm() {
//		kafDocument.addWordForm(exampleKafWordForm);
//		kafDocument.addWordForm(exampleKafWordForm);
//		kafDocument.addWordForm(exampleKafWordForm);
//		assertEquals(kafDocument.getWordList().size(), 3);
//	}

	@Test
	public void testGetKafAsString() {
//		kafDocument.addWordForm(exampleKafWordForm);
//		kafDocument.addWordForm(exampleKafWordForm);
//		kafDocument.addWordForm(exampleKafWordForm);
		String kafString=kafDocument.getKafAsString();
		assertNotNull(kafString);
		System.out.println(kafString);
	}

}
