package org.vicomtech.opener.bratAdaptionTools.annotationReviews;

import static org.junit.Assert.*;

import java.net.UnknownHostException;

import javax.xml.ws.BindingProvider;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.vicomtech.opener.annotationReviews.ReviewsForAnnotationAnalyzer.ReviewInfo;
import org.vicomtech.opener.bratAdaptionTools.ws.client.OpenerService;
import org.vicomtech.opener.bratAdaptionTools.ws.client.OpenerServiceImplService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class CallOpenerAnalysisTest {

	private static OpenerService openerService=getOpenerService();
	private static MongoClient mongoClient;
	private static DB db;
	
	@BeforeClass
	public static void beforeClass() throws UnknownHostException{
		mongoClient = new MongoClient("localhost", 27017);
		db = mongoClient.getDB("opener-annotation-task");
	}
	
	@Before
	public void setUp() throws Exception {
		
	}

	//@Ignore
	@Test
	public void test() {
//		String reviewId="068b3a3273680d4b271a2819ff1d5b18";
//		String language="en";
		String reviewId="07e00353493e5be174b5d9eecdd1f923";
		String language="es";
		ReviewInfo reviewInfo = getReviewInfo(reviewId);
		String title=reviewInfo.getTitle()!=null?reviewInfo.getTitle():"";
		String comment=reviewInfo.getComment()!=null?reviewInfo.getComment():"";
		String analyzableContent=title+"\n"+comment;
		
	//	analyzableContent+="\nEl hotel está en el centro de Madrid es bonita. ";
		
		analyzableContent=analyzableContent.trim();
		String kaf=analyzeReviewWithOpeNER(analyzableContent,language);
		System.out.println(analyzableContent+"\n==============");
		System.out.println(kaf);
		
		assertTrue(true);
	}
	
	public String analyzeReviewWithOpeNER(String reviewContent, String expectedLang){
		String kaf=openerService.tokenize(reviewContent, expectedLang);
		kaf=openerService.postag(kaf, expectedLang);
		kaf=openerService.nerc(kaf, expectedLang);
		kaf=openerService.parseConstituents(kaf, expectedLang);
		kaf=openerService.corefDetect(kaf, expectedLang);
//		try {
//			KAFDocument kafDoc=KAFDocument.createFromStream(new InputStreamReader(new ByteArrayInputStream(kaf.getBytes())));
//			return kafDoc.toString();
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
		return kaf;
	}
	
	protected static OpenerService getOpenerService(){
		OpenerServiceImplService serviceImpl = new OpenerServiceImplService();
		OpenerService service = serviceImpl.getOpenerServiceImplPort();
		// La URL que quieras, esto es lo que deberías obtener mediante
		// configuración externa
		String endpointURL = "http://192.168.17.128:9999/ws/opener?wsdl";
		BindingProvider bp = (BindingProvider) service;
		bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				endpointURL);
		return service;
	}
	
	public ReviewInfo getReviewInfo(String reviewId) {
		DBCollection collection = db.getCollection("reviews-all");
		BasicDBObject query = new BasicDBObject("review_id", reviewId);
		DBObject dbObject = collection.findOne(query);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		ReviewInfo reviewInfo = gson.fromJson(dbObject.toString(),
				ReviewInfo.class);
		return reviewInfo;
	}

}
