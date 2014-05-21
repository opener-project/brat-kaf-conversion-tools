package org.vicomtech.opener.bratAdaptionTools;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import ixa.kaflib.KAFDocument;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

public class MiscellaneousTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() throws IOException {
		String path="D:\\stuff_from_the_laptop_itself\\BRAT_ANNOTATED_FILES_TO_KAF\\HOTEL_SET_1_KAF\\english";//\\english00001_0123ff23e0d0dc0177f9b71a1928b674.kaf";
		for(File f:new File(path).listFiles()){
			System.out.println(f.getName() +" "+f.getUsableSpace());
			String kaf=FileUtils.readFileToString(f, "UTF-8");
			System.out.println(kaf);
		}
		
		String path2="D:\\stuff_from_the_laptop_itself\\BRAT_ANNOTATED_FILES_TO_KAF\\HOTEL_SET_1_KAF\\english\\english00001_0123ff23e0d0dc0177f9b71a1928b674.kaf";
		
		String kaf=FileUtils.readFileToString(new File(path2), "UTF-8");
		System.out.println("THIS ONE:\n"+kaf);
		KAFDocument kafdoc=KAFDocument.createFromStream(new StringReader(kaf));
		System.out.println(kafdoc.getTerms().size());
	}

}
