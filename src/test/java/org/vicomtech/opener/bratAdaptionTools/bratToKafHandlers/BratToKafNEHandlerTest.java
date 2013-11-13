package org.vicomtech.opener.bratAdaptionTools.bratToKafHandlers;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.vicomtech.opener.bratAdaptionTools.model.BratAnnotation;
import org.vicomtech.opener.bratAdaptionTools.model.KafDocument;

public class BratToKafNEHandlerTest {
	
	public static final String KAF_DOC_PATH="/kaf-example2.xml";
	private BratToKafNEHandler bratToKafHandler;
	
	private KafDocument kafDocument;
	
	@Before
	public void setUp() throws Exception {
		InputStream is=Class.class.getResourceAsStream(KAF_DOC_PATH);
		kafDocument=KafDocument.parseKafDocument(is);
		bratToKafHandler=new BratToKafNEHandler();
	}

	@Test
	public void testHandleConversion() {
		BratAnnotation bratAnnotation=new BratAnnotation();
		bratAnnotation.setId("T1");
		bratAnnotation.setKafTokenSpan(new int[]{7,8});
		bratAnnotation.setType("Miscellaneous");
		assertEquals(1, kafDocument.getEntityList().size());
		bratToKafHandler.handleConversion(kafDocument, bratAnnotation,null);
		System.out.println(kafDocument.getKafAsString());
		assertEquals(2, kafDocument.getEntityList().size());
		assertEquals("but it", kafDocument.getEntityList().get(1).getStr());
	}

}
