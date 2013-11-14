package org.vicomtech.opener.bratAdaptionTools;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.vicomtech.opener.bratAdaptionTools.model.KafDocument;
import org.vicomtech.opener.bratAdaptionTools.model.PreannotationConfig;

public class KafToBratConverterTest {

	public static final String KAF_DOC_PATH="/kaf-example2.xml";
	public static final String KAF_DOC_PATH2="/kaf-example.xml";
		
		private KafToBratConverter kafToBratConverter;
		private KafDocument kafDocument;
		private KafDocument kafDocument2;
		
		@Before
		public void setUp() throws Exception {
			InputStream is=Class.class.getResourceAsStream(KAF_DOC_PATH);
			kafDocument=KafDocument.parseKafDocument(is);
			InputStream is2=Class.class.getResourceAsStream(KAF_DOC_PATH2);
			kafDocument2=KafDocument.parseKafDocument(is2);
			kafToBratConverter=new KafToBratConverter();
		}

//	@Test
//	public void testBratAnnotationFromKaf() {
//		fail("Not yet implemented");
//	}

	@Test
	public void testGenerateBratAnnotation() {
		PreannotationConfig preAnnotationConfig=PreannotationConfig.getPreannotationConfig();
		String bratAnnotationFileString=kafToBratConverter.generateBratAnnotation(kafDocument, preAnnotationConfig);
		assertNotNull(bratAnnotationFileString);
		System.out.println(bratAnnotationFileString);
		String expected="T1	markable 39 41	it\nT2	markable 12 21	Amsterdam";
		assertEquals(expected, bratAnnotationFileString);
		System.out.println("===============");
		String bratAnnotationFileString2=kafToBratConverter.generateBratAnnotation(kafDocument2, preAnnotationConfig);
		assertNotNull(bratAnnotationFileString2);
		System.out.println(bratAnnotationFileString2);
		String expected2="T1	markable 12 16	Pisa";
		assertEquals(expected2, bratAnnotationFileString2);
		
	}

}
