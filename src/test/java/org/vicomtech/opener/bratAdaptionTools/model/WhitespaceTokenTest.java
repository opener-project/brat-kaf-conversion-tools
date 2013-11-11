package org.vicomtech.opener.bratAdaptionTools.model;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class WhitespaceTokenTest {

	public static final String KAF_DOC_PATH="/kaf-example2.xml";
	public static final String KAF_DOC_PATH2="/kaf-example.xml";
		
		private KafDocument kafDocument;
		private KafDocument kafDocument2;
		
		@Before
		public void setUp() throws Exception {
			InputStream is=Class.class.getResourceAsStream(KAF_DOC_PATH);
			kafDocument=KafDocument.parseKafDocument(is);
			InputStream is2=Class.class.getResourceAsStream(KAF_DOC_PATH2);
			kafDocument2=KafDocument.parseKafDocument(is2);
		}

	@Test
	public void testParseText() {
		String example="This is a test .\nSecond sentence .";
		List<WhitespaceToken> whitespaceTokenList = WhitespaceToken.parseText(example);
		assertEquals(8,whitespaceTokenList.size());
		assertEquals(15, whitespaceTokenList.get(4).getStart());
		assertEquals(16, whitespaceTokenList.get(4).getEnd());
		assertEquals(17, whitespaceTokenList.get(5).getStart());
		assertEquals(23, whitespaceTokenList.get(5).getEnd());
	}

	@Test
	public void testGenerateWhiteSpaceTokenizedText() {
		String actual1=WhitespaceToken.generateWhiteSpaceTokenizedText(kafDocument);
		String expected1="The city of Amsterdam is very nice but it is not small .";
		assertEquals(expected1, actual1);
		String actual2=WhitespaceToken.generateWhiteSpaceTokenizedText(kafDocument2);
		String expected2="The city of Pisa is beautiful .";
		assertEquals(expected2, actual2);
	}
	
	@Test
	public void testGetEnclosingText(){
		String example="This is a test .\nSecond sentence .";
		List<WhitespaceToken> whitespaceTokenList = WhitespaceToken.parseText(example);
		String enclosingText=WhitespaceToken.getEnclosingText(whitespaceTokenList, 3, 5);
		assertEquals("test . Second", enclosingText);
	}

}
