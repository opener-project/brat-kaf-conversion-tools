package org.vicomtech.opener.bratAdaptionTools;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import eu.openerproject.kaf.layers.KafWordForm;

public class KafDocumentTest {

	private KafDocument kafDocument;
	private KafWordForm exampleKafWordForm;
	
	@Before
	public void setUp() throws Exception {
		kafDocument=new KafDocument();
		exampleKafWordForm=new KafWordForm();
		exampleKafWordForm.setWid("w1");
		exampleKafWordForm.setSent(1);
		exampleKafWordForm.setLength(5);
		exampleKafWordForm.setOffset(17);
		exampleKafWordForm.setWordform("TEST");
	}

	@Test
	public void testKafDocument() {
		assertEquals(kafDocument.getWordList().size(),0);
	}

	@Test
	public void testKafDocumentInputStream() {
		InputStream is=Class.class.getResourceAsStream("/kaf-example.xml");
		kafDocument=new KafDocument(is);
		String kafString=kafDocument.getKafAsString();
		assertNotNull(kafString);
		System.out.println(kafString);
	}

	@Test
	public void testInitEmptyKaf() {
		kafDocument.initEmptyKaf();
		assertEquals(kafDocument.getWordList().size(),0);
	}

	@Test
	public void testAddWordForm() {
		kafDocument.addWordForm(exampleKafWordForm);
		kafDocument.addWordForm(exampleKafWordForm);
		kafDocument.addWordForm(exampleKafWordForm);
		assertEquals(kafDocument.getWordList().size(), 3);
	}

	@Test
	public void testGetKafAsString() {
		kafDocument.addWordForm(exampleKafWordForm);
		kafDocument.addWordForm(exampleKafWordForm);
		kafDocument.addWordForm(exampleKafWordForm);
		String kafString=kafDocument.getKafAsString();
		assertNotNull(kafString);
		System.out.println(kafString);
	}

}
