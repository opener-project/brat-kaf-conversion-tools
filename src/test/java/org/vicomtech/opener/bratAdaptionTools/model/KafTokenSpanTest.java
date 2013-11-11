package org.vicomtech.opener.bratAdaptionTools.model;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class KafTokenSpanTest {

	private List<KafTokenSpan>kafTokenSpans;
	
	@Before
	public void setUp() throws Exception {
		kafTokenSpans=Lists.newArrayList();
		KafTokenSpan kafTokenSpan1=new KafTokenSpan(3, 6);
		KafTokenSpan kafTokenSpan2=new KafTokenSpan(4, 7);
		KafTokenSpan kafTokenSpan3=new KafTokenSpan(7, 8);
		KafTokenSpan kafTokenSpan4=new KafTokenSpan(9, 11);
		KafTokenSpan kafTokenSpan5=new KafTokenSpan(9, 13);
		kafTokenSpans.add(kafTokenSpan1);
		kafTokenSpans.add(kafTokenSpan2);
		kafTokenSpans.add(kafTokenSpan3);
		kafTokenSpans.add(kafTokenSpan4);
		kafTokenSpans.add(kafTokenSpan5);
	}

	@Test
	public void testIsOverlappingSpans() {
		KafTokenSpan kafTokenSpan1=new KafTokenSpan(3, 6);
		KafTokenSpan kafTokenSpan2=new KafTokenSpan(4, 7);
		KafTokenSpan kafTokenSpan3=new KafTokenSpan(7, 8);
		KafTokenSpan kafTokenSpan4=new KafTokenSpan(9, 11);
		
		assertTrue(kafTokenSpan1.isOverlappingSpans(kafTokenSpan2));
		assertTrue(kafTokenSpan2.isOverlappingSpans(kafTokenSpan3));
		assertFalse(kafTokenSpan3.isOverlappingSpans(kafTokenSpan4));
		
	}

	@Test
	public void testReturnMergedSpan() {
		KafTokenSpan kafTokenSpan1=new KafTokenSpan(3, 6);
		KafTokenSpan kafTokenSpan2=new KafTokenSpan(4, 7);
		KafTokenSpan kafTokenSpan3=new KafTokenSpan(7, 8);
		KafTokenSpan kafTokenSpan4=new KafTokenSpan(9, 11);
		
		assertEquals(new KafTokenSpan(3, 7), kafTokenSpan1.returnMergedSpan(kafTokenSpan2));
		assertEquals(new KafTokenSpan(4, 8), kafTokenSpan2.returnMergedSpan(kafTokenSpan3));
		assertEquals(new KafTokenSpan(3, 11), kafTokenSpan1.returnMergedSpan(kafTokenSpan4));
		assertEquals(new KafTokenSpan(7, 11), kafTokenSpan3.returnMergedSpan(kafTokenSpan4));
	}

	@Test
	public void testMergeKafTokenSpans() {
		List<KafTokenSpan>mergedList=KafTokenSpan.mergeKafTokenSpans(kafTokenSpans);
		System.out.println(Arrays.toString(mergedList.toArray()));
		assertNotNull(mergedList);
		assertEquals(2,mergedList.size());
		assertEquals(new KafTokenSpan(3, 8), mergedList.get(0));
		assertEquals(new KafTokenSpan(9, 13), mergedList.get(1));
		
	}

}
