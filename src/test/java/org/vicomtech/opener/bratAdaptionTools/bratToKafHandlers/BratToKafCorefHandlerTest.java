package org.vicomtech.opener.bratAdaptionTools.bratToKafHandlers;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.vicomtech.opener.bratAdaptionTools.model.BratAnnotation;
import org.vicomtech.opener.bratAdaptionTools.model.KafDocument;

import com.google.common.collect.ImmutableList;

public class BratToKafCorefHandlerTest {

	public static final String KAF_DOC_PATH="/kaf-example2.xml";
	private KafDocument kafDocument;
	private BratToKafCorefHandler bratToKafCorefHandler;
	
	@Before
	public void setUp() throws Exception {
		InputStream is=Class.class.getResourceAsStream(KAF_DOC_PATH);
		kafDocument=KafDocument.parseKafDocument(is);
		bratToKafCorefHandler=new BratToKafCorefHandler();
	}

	@Test
	public void testHandleConversion() {
		BratAnnotation bratAnnotation=new BratAnnotation();
		bratAnnotation.setId("T1");
		bratAnnotation.setKafTokenSpan(new int[]{3,3});
		
		BratAnnotation bratAnnotation2=new BratAnnotation();
		bratAnnotation2.setId("T2");
		bratAnnotation2.setKafTokenSpan(new int[]{8,8});
		
		BratAnnotation bratAnnotation3=new BratAnnotation();
		bratAnnotation3.setId("*");
		bratAnnotation3.setInvolvedEntities(ImmutableList.of("T1","T2"));
		
		List<BratAnnotation>otherBratAnnotations=ImmutableList.of(bratAnnotation, bratAnnotation2, bratAnnotation3);
		
		assertEquals(0, kafDocument.getCorefs().size());
		bratToKafCorefHandler.handleConversion(kafDocument, bratAnnotation3, otherBratAnnotations);
		System.out.println(kafDocument.getKafAsString());
		assertEquals(1, kafDocument.getCorefs().size());
		assertEquals(2, kafDocument.getCorefs().get(0).getSpans().size());
	}

}
