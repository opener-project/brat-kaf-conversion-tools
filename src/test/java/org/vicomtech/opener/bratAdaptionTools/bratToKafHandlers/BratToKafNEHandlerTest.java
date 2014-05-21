package org.vicomtech.opener.bratAdaptionTools.bratToKafHandlers;

import static org.junit.Assert.*;
import ixa.kaflib.Entity;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.vicomtech.opener.bratAdaptionTools.model.BratAnnotation;
import org.vicomtech.opener.bratAdaptionTools.model.KafDocument;

public class BratToKafNEHandlerTest {
	
	public static final String KAF_DOC_PATH="/kaf-example2.xml";
	public static final String KAF_DOC_WITH_OTHER_ENTITY_TYPES="/kaf-example_with_other_entity_types.kaf";
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
	
	@Test
	public void testHandleOtherEntityTypes(){
		BratAnnotation bratAnnotation=new BratAnnotation();
		bratAnnotation.setId("T1");
		bratAnnotation.setKafTokenSpan(new int[]{7,8});
		bratAnnotation.setType("Attraction");
		assertEquals(1, kafDocument.getEntityList().size());
		bratToKafHandler.handleConversion(kafDocument, bratAnnotation,null);
		System.out.println(kafDocument.getKafAsString());
				Entity entity = kafDocument.getEntityList().get(1);
		System.out.println("Entity: "+entity.getStr()+" | "+entity.getType());
		assertEquals(2, kafDocument.getEntityList().size());
		assertEquals("but it", kafDocument.getEntityList().get(1).getStr());
		assertEquals("Attraction",kafDocument.getEntityList().get(1).getType());

	}

}
