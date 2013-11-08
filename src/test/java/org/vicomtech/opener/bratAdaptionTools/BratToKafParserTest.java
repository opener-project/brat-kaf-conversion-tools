package org.vicomtech.opener.bratAdaptionTools;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
//import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.vicomtech.opener.bratAdaptionTools.model.BratAnnotation;
import org.vicomtech.opener.bratAdaptionTools.model.WhitespaceToken;

public class BratToKafParserTest {

	private String TXT_FILE="/TXT_FILE.txt";
	private String ANN_FILE="/TXT_FILE.ann";
	private BratAnnotationFileParser bratToKafParser;
	private InputStream bratTxtDocIs;
	private InputStream bratAnnDocIs;
	
	@Before
	public void setUp() throws Exception {
		bratTxtDocIs=Class.class.getResourceAsStream(TXT_FILE);
		bratAnnDocIs=Class.class.getResourceAsStream(ANN_FILE);
		bratToKafParser=new BratAnnotationFileParser();
	}
	
	@After
	public void tearDown() throws Exception{
		bratAnnDocIs.close();
		bratTxtDocIs.close();
	}
	
//	@Test
//	public void testLoadFilesString() {
//		String fullPath=Class.class.getResource(TXT_FILE).getPath();
//		
//	}

//	@Test
//	public void testLoadFilesInputStreamInputStream() {
//		String bratDoc=bratToKafParser.getBratAnnDoc();
//		assertNull(bratDoc);
//		String bratAnn=bratToKafParser.getBratAnnDoc();
//		assertNull(bratAnn);
//		bratToKafParser.loadFiles(bratTxtDocIs, bratAnnDocIs);
//		bratDoc=bratToKafParser.getBratAnnDoc();
//		assertNotNull(bratDoc);
//		bratAnn=bratToKafParser.getBratAnnDoc();
//		assertNotNull(bratAnn);
//	}

//	@Test
//	public void testReadFiles() {
//		String bratDoc=bratToKafParser.getBratAnnDoc();
//		assertNull(bratDoc);
//		String bratAnn=bratToKafParser.getBratAnnDoc();
//		assertNull(bratAnn);
//		bratToKafParser.readFiles(bratTxtDocIs, bratAnnDocIs);
//		bratDoc=bratToKafParser.getBratAnnDoc();
//		assertNotNull(bratDoc);
//		bratAnn=bratToKafParser.getBratAnnDoc();
//		assertNotNull(bratAnn);
//	}

	@Test
	public void testObtainWhitespaceTokenList() throws IOException {
		//bratToKafParser.loadFiles(bratTxtDocIs, bratAnnDocIs);
		//List<WhitespaceTokenInfo> whitespaceTokenList = bratToKafParser.getWhitespaceTokenList();
		//assertEquals(whitespaceTokenList.size(),0);
		String bratTxtDoc = IOUtils.toString(bratTxtDocIs, "UTF-8");
		List<WhitespaceToken> whitespaceTokenList=WhitespaceToken.parseText(bratTxtDoc);
		assertEquals(whitespaceTokenList.size(),13);
		//System.out.println(Arrays.toString(whitespaceTokenList.toArray()));
	}

	@Test
	public void testParseBratAnnFile() throws IOException {
		//bratToKafParser.loadFiles(bratTxtDocIs, bratAnnDocIs);
		String bratAnnDoc = IOUtils.toString(bratAnnDocIs, "UTF-8");
		List<BratAnnotation> bratAnnotations = bratToKafParser.parseBratAnnFile(bratAnnDoc);
		assertEquals(5, bratAnnotations.size());
	}

	@Test
	public void testParseBratAnnotation() throws IOException {
		String bratAnnDoc = IOUtils.toString(bratAnnDocIs, "UTF-8");
		BratAnnotation bratAnnotation = bratToKafParser.parseBratAnnotation(bratAnnDoc.split("\n")[0]);
		BratAnnotation expectedBratAnnotation=new BratAnnotation();
		// T1	Miscellaneous 0 4	This
		expectedBratAnnotation.setId("T1");
		expectedBratAnnotation.setOffsets(0, 4);
		expectedBratAnnotation.setType("Miscellaneous");
		expectedBratAnnotation.setText("This");
		compareBratAnnotations(expectedBratAnnotation, bratAnnotation);
	}

	public void compareBratAnnotations(BratAnnotation expected,BratAnnotation actual){
		assertEquals(expected.getId(), actual.getId());
		if(expected.getId().startsWith("T")){
			assertEquals(expected.getStart(), actual.getStart());
			assertEquals(expected.getEnd(), actual.getEnd());
			assertEquals(expected.getText(), actual.getText());
		}		
		assertEquals(expected.getType(), actual.getType());
		if(expected.getId().startsWith("R")||expected.getId().startsWith("*")){
			assertTrue(expected.getInvolvedEntities().size()==actual.getInvolvedEntities().size());
			for(int i=0;i<expected.getInvolvedEntities().size();i++){
				assertEquals(expected.getInvolvedEntities().get(i),actual.getInvolvedEntities().get(i));
			}
		}
//		if(!expected.getId().startsWith("*")){
//			assertEquals(expected.getType(), actual.getType());
//		}
	}
	
//	@Test
//	public void testGetInvolvedEntities() throws IOException {
//		String bratAnnDoc = IOUtils.toString(bratAnnDocIs, "UTF-8");
//		BratAnnotation bratAnnotation = bratToKafParser.parseBratAnnotation(bratAnnDoc.split("\n")[3]);
////		System.out.println(bratAnnDoc.split("\n")[3]);
////		System.out.println(bratAnnotation);
//		assertEquals(2, bratAnnotation.getInvolvedEntities().size());
//		bratAnnotation = bratToKafParser.parseBratAnnotation(bratAnnDoc.split("\n")[4]);
//		System.out.println(bratAnnDoc.split("\n")[4]);
//		System.out.println(bratAnnotation);
//		assertEquals(0, bratAnnotation.getInvolvedEntities().size());
//	}

//	@Test
//	public void testGetAnnotationType() {
//		String annotationType = bratToKafParser.getAnnotationType("T1", "PER 5 10");
//		assertEquals("PER",annotationType);
//		annotationType = bratToKafParser.getAnnotationType("R1", "SOME_REL Arg1:T4 Arg2:T6");
//		assertEquals("SOME_REL",annotationType);
//		annotationType = bratToKafParser.getAnnotationType("N1", "Reference T1 Wikipedia:534366");
//		assertEquals("Reference",annotationType);
//	}
//
//	@Test
//	public void testGetReference() {
//		Reference reference=null;
//		reference=bratToKafParser.getReference("N1", "Reference T1 Wikipedia:534366");
//		assertEquals("T1", reference.entity);
//		assertEquals("Wikipedia", reference.knowledgeBaseName);
//		assertEquals("534366", reference.resourceId);
//	}
//
//	@Test
//	public void testGetSpan() {
//		int[]span=bratToKafParser.getSpan("T1", "Organization 45 48");
//		assertEquals(45, span[0]);
//		assertEquals(48, span[1]);
//	}

	@Test
	public void testMapAnnotationsToTokens() throws IOException {
		//bratToKafParser.loadFiles(bratTxtDocIs, bratAnnDocIs);
		String bratTxtDoc = IOUtils.toString(bratTxtDocIs, "UTF-8");
		String bratAnnDoc = IOUtils.toString(bratAnnDocIs, "UTF-8");
		List<WhitespaceToken> whitespaceTokenList = WhitespaceToken.parseText(bratTxtDoc);
		List<BratAnnotation> bratAnnotations = bratToKafParser.parseBratAnnFile(bratAnnDoc);
		bratToKafParser.mapAnnotationsToTokens(bratAnnotations,whitespaceTokenList);
		assertEquals(5, bratAnnotations.size());
		for(BratAnnotation bratAnnotation:bratAnnotations){
			if(bratAnnotation.isEntity()){
				assertNotNull(bratAnnotation.getKafTokenSpan());
				assertNotEquals(-1, bratAnnotation.getKafTokenSpan()[0]);
				assertNotEquals(-1, bratAnnotation.getKafTokenSpan()[1]);
			}
		}
	}

	@Test
	public void testGetKafTokenSpan() throws IOException {
		String bratTxtDoc = IOUtils.toString(bratTxtDocIs, "UTF-8");
		String bratAnnDoc = IOUtils.toString(bratAnnDocIs, "UTF-8");
		List<WhitespaceToken> whitespaceTokenList = WhitespaceToken.parseText(bratTxtDoc);
		List<BratAnnotation> bratAnnotations = bratToKafParser.parseBratAnnFile(bratAnnDoc);
		
		
//		List<WhitespaceTokenInfo> whitespaceTokenList = Lists.newArrayList();
//		WhitespaceTokenInfo w=new WhitespaceTokenInfo(3, 3, "aaa");
//		whitespaceTokenList.add(w);
//		List<BratAnnotation> bratAnnotations = Lists.newArrayList();
//		BratAnnotation b=new BratAnnotation();
//		b.setOffsets(3, 7);
//		bratAnnotations.add(b);
		
//		System.out.println(Arrays.toString(whitespaceTokenList.toArray()));
//		System.out.println(Arrays.toString(bratAnnotations.toArray()));
//		
		
		int[]kafTokenSpan=bratToKafParser.getKafTokenSpan(bratAnnotations.get(1),whitespaceTokenList);
		//System.out.println(Arrays.toString(kafTokenSpan));
		assertEquals(2, kafTokenSpan[0]);
		assertEquals(3, kafTokenSpan[1]);
	}

}
