package org.vicomtech.opener.bratAdaptionTools.Main.adhocStuff;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
//import java.util.Set;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;

import javax.xml.ws.BindingProvider;

import org.apache.commons.io.FileUtils;

import org.vicomtech.opener.bratAdaptionTools.ws.client.OpenerService;
import org.vicomtech.opener.bratAdaptionTools.ws.client.OpenerServiceImplService;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
//import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class FrenchAdhocReviewsRetriever {

//	public static final String DIR_FOR_ID_FILES="reviewIDs";
	public static final String DIR_FOR_KAF_DOCS="KAF_DOCS";
	public static final String DIR_WITH_REVIEW_IDS = "attractionReviews";
//	public static final String PATH_TO_DIR_WITH_REVIEW_IDS=DIR_FOR_ID_FILES+File.separator+DIR_WITH_REVIEW_IDS;
	public static final String DIR_WITH_REVIEWS_KAF = DIR_FOR_KAF_DOCS+File.separator+DIR_WITH_REVIEW_IDS+"_KAF_20140203";
	public static final String COLLECTION_CONTAINING_THE_REVIEWS="attractions";
//	public static final String[] languages = new String[] { "dutch", "english",
//			"french", "spanish", "german", "italian" };

	public static final String REVIEW_ID_PATTERN_STRING = ".*\\Qreview_id\":\"\\E(\\p{Alnum}+)\";\\s\\s1";
//	private static final Pattern REVIEW_ID_PATTERN = Pattern
//			.compile(REVIEW_ID_PATTERN_STRING);

	private MongoClient mongoClient;
	private DB db;

	private static OpenerService openerService=getOpenerService();
	private Map<String,String>langNameMap;
	//private Set<String>desiredLanguages;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FrenchAdhocReviewsRetriever frenchAdhocReviewsRetriever = new FrenchAdhocReviewsRetriever();
		frenchAdhocReviewsRetriever.analyzeReviewsForAnnotation();
	}

	public FrenchAdhocReviewsRetriever() {
		try {
			mongoClient = new MongoClient("localhost", 27017);
			db = mongoClient.getDB("opener-annotation-task");
			langNameMap=Maps.newHashMap();
			langNameMap.put("french", "fr");
			langNameMap.put("spanish", "es");
			langNameMap.put("english", "en");
			langNameMap.put("german", "de");
			langNameMap.put("dutch", "nl");
			langNameMap.put("italian", "it");
		//	desiredLanguages=Sets.newHashSet();
		//	desiredLanguages.add("dutch");
		//	desiredLanguages.add("german");
		//	desiredLanguages.add("french");
		//	desiredLanguages.add("spanish");
		//	desiredLanguages.add("italian");
		//	desiredLanguages.add("english");
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void analyzeReviewsForAnnotation(){
		Map<String, List<ReviewInfo>> reviewIdsPerLanguage = getReviewPerLanguage();
		File dirForKaf=new File(DIR_WITH_REVIEWS_KAF);
		if(!dirForKaf.exists()){
			dirForKaf.mkdirs();
		}
		for(String language:reviewIdsPerLanguage.keySet()){
			//////
//			if(!language.equalsIgnoreCase("english")){
//				continue;
//			}
			//////
			System.out.println("Starting with language: "+language);
			File dirForThisLanguage=new File(dirForKaf.getAbsolutePath()+File.separator+language);
			if(!dirForThisLanguage.exists()){
				dirForThisLanguage.mkdirs();
			}
			System.out.println("Directory for this language: "+dirForThisLanguage.getAbsolutePath());
			List<ReviewInfo> reviewIdsForThisLanguage=reviewIdsPerLanguage.get(language);
			int count=0;
			for(ReviewInfo reviewInfo:reviewIdsForThisLanguage){
				try{
					System.out.println("Analyzing review with id: "+reviewInfo.getReview_id()+" (count: "+(++count)+")");
					//ReviewInfo reviewInfo = getReviewInfo(reviewId);
					String title=reviewInfo.getTitle()!=null?reviewInfo.getTitle():"";
					String comment=reviewInfo.getComment()!=null?reviewInfo.getComment():"";
					String analyzableContent=title+"\n"+comment;
					analyzableContent=analyzableContent.trim();
					String[]analyzableContentLength=analyzableContent.split(" |\n");
					if(analyzableContentLength.length<30){
						continue;
					}
					String kafFileName=language+FileNumberingManager.obtainNumberForAbsolutePath(dirForThisLanguage.getAbsolutePath())+"_"+reviewInfo.getReview_id()+".kaf";
					String kaf=analyzeReviewWithOpeNER(analyzableContent, langNameMap.get(language));
					File kafFile=new File(dirForThisLanguage.getAbsolutePath()+File.separator+kafFileName);
					System.out.println("Going to write the kaf file to: "+kafFile.getAbsolutePath());
					try {
						FileUtils.write(kafFile, kaf, "UTF-8");
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	

	public Map<String, List<ReviewInfo>> getReviewPerLanguage() {
		Map<String, List<ReviewInfo>> reviewsPerLanguage = Maps.newHashMap();
		String[]languages={"french"};
		for(String language:languages){
			System.out.println("Adding reviews for: "+language);
			List<ReviewInfo> reviewsForLanguage = getReviewsForLang(language);
			reviewsPerLanguage.put(language, reviewsForLanguage);
		}
		return reviewsPerLanguage;
	}

	public List<ReviewInfo> getReviewsForLang(String language) {
		DBCollection collection = db.getCollection(COLLECTION_CONTAINING_THE_REVIEWS);
		BasicDBObject query = new BasicDBObject("language", language);
		//DBObject dbObject = collection.findOne(query);
		DBCursor dbCursor = collection.find(query);
		List<ReviewInfo>reviewsForLanguage=Lists.newArrayList();
		while(dbCursor.hasNext()){
			DBObject dbObject=dbCursor.next();
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			ReviewInfo reviewInfo = gson.fromJson(dbObject.toString(),
				ReviewInfo.class);
			reviewsForLanguage.add(reviewInfo);
		}
		return reviewsForLanguage;
	}
	
	public String analyzeReviewWithOpeNER(String reviewContent, String expectedLang){
		String kaf=openerService.tokenize(reviewContent, expectedLang);
		kaf=openerService.postag(kaf, expectedLang);
		kaf=openerService.nerc(kaf, expectedLang);
		
		String kafConstit = openerService.parseConstituents(kaf, expectedLang);
		if (kafConstit!=null && !kafConstit.trim().equalsIgnoreCase("")) {
			kaf=kafConstit;
			String kafCoref = openerService.corefDetect(kafConstit,
					expectedLang);
			if (!kafCoref.trim().equalsIgnoreCase("")) {
				kaf = kafCoref;
			}
		}
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
		bp.getRequestContext().put("com.sun.xml.internal.ws.request.timeout", 60000);
		return service;
	}

	/**
	 * Internal encapsulation class for review basic inf (id+title+content)
	 * @author agarciap
	 *
	 */
	public static class ReviewInfo {
		private String review_id;
		private String title;
		private String comment;

		public ReviewInfo() {
			super();
		}

		public String getReview_id() {
			return review_id;
		}

		public void setReview_id(String review_id) {
			this.review_id = review_id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getComment() {
			return comment;
		}

		public void setComment(String comment) {
			this.comment = comment;
		}
	}
	
	public static class FileNumberingManager{
		
		private static Map<String,Integer>numberingMap=Maps.newHashMap();
		
		public static synchronized String obtainNumberForAbsolutePath(String absolutePath){
			int numberOfDigits=5;
			Integer number=numberingMap.get(absolutePath);
			String formattedNumber="";
			if(number==null){
				number=1;
				numberingMap.put(absolutePath, 1);
			}else{
				numberingMap.put(absolutePath, ++number);
			}
			String numberString=""+number;
			for(int i=0;i<numberOfDigits-numberString.length();i++){
				formattedNumber+=0;
			}
			return formattedNumber+number;
		}
	}

}
