package org.vicomtech.opener.bratAdaptionTools;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.vicomtech.opener.bratAdaptionTools.BratAnnotationsManager.AnnotationIdType;

public class BratAnnotationManagerTest {

	private BratAnnotationsManager bratAnnotationsManager;
	
	@Before
	public void setUp() throws Exception {
		bratAnnotationsManager=new BratAnnotationsManager();
	}

	@Test
	public void testAddAnnotation() {
		List<BratAnnotation> annotations = bratAnnotationsManager.getAnnotations();
		assertEquals(annotations.size(), 0);
		bratAnnotationsManager.addAnnotation(AnnotationIdType.ENTITY, "PERSON", 10, 15,null, "test");
		annotations = bratAnnotationsManager.getAnnotations();
		assertEquals(annotations.size(), 1);
		BratAnnotation annotation = annotations.get(0);
		String expectedId="T1";
		String expectedType="PERSON";
		int expectedStart=10;
		int expectedEnd=15;
		String expectedText="test";
		assertEquals(expectedId, annotation.getId());
		assertEquals(expectedType, annotation.getType());
		assertEquals(expectedStart, annotation.getStart());
		assertEquals(expectedEnd, annotation.getEnd());
		assertEquals(expectedText, annotation.getText());
	}

	@Test
	public void testGetAnnotationIdAndUpdate() {
		int count=bratAnnotationsManager.getAnnotationIdAndUpdate(AnnotationIdType.ENTITY);
		assertEquals(1, count);
		count=bratAnnotationsManager.getAnnotationIdAndUpdate(AnnotationIdType.ENTITY);
		assertEquals(2, count);
		count=bratAnnotationsManager.getAnnotationIdAndUpdate(AnnotationIdType.ENTITY);
		assertEquals(3, count);
		count=bratAnnotationsManager.getAnnotationIdAndUpdate(AnnotationIdType.RELATION);
		assertEquals(1, count);
		count=bratAnnotationsManager.getAnnotationIdAndUpdate(AnnotationIdType.RELATION);
		assertEquals(2, count);
	}

	@Test
	public void testComposedAnnotationId() {
		String id=bratAnnotationsManager.composedAnnotationId(AnnotationIdType.ENTITY, 4);
		assertEquals("T4", id);
		id=bratAnnotationsManager.composedAnnotationId(AnnotationIdType.RELATION, 4);
		assertEquals("R4", id);
		id=bratAnnotationsManager.composedAnnotationId(AnnotationIdType.NORMALIZATION, 4);
		assertEquals("N4", id);
		id=bratAnnotationsManager.composedAnnotationId(AnnotationIdType.NOTE, 4);
		assertEquals("#4", id);
		id=bratAnnotationsManager.composedAnnotationId(AnnotationIdType.EQUIVALENCE, 4);
		assertEquals("*", id);
		id=bratAnnotationsManager.composedAnnotationId(AnnotationIdType.EVENT, 4);
		assertEquals("E4", id);
		id=bratAnnotationsManager.composedAnnotationId(AnnotationIdType.ATTRIBUTE, 4);
		assertEquals("A4", id);
		
	}

	@Test
	public void testGetAnnotations() {
		List<BratAnnotation> annotations = bratAnnotationsManager.getAnnotations();
		assertNotNull(annotations);
		assertEquals(annotations.size(), 0);
		bratAnnotationsManager.addAnnotation(AnnotationIdType.ENTITY, "PERSON", 10, 15,null, "test");
		assertEquals(annotations.size(), 1);
		bratAnnotationsManager.addAnnotation(AnnotationIdType.ENTITY, "PERSON", 10, 15,null, "test");
		assertEquals(annotations.size(), 2);
		bratAnnotationsManager.addAnnotation(AnnotationIdType.ENTITY, "PERSON", 10, 15,null, "test");
		assertEquals(annotations.size(), 3);
	}

}
