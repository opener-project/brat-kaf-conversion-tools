package org.vicomtech.opener.bratAdaptionTools.Main;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.vicomtech.opener.bratAdaptionTools.Main.ReviewsForAnnotationAnalyzer.ReviewInfo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class ReviewIdSelector {

	public static final String COLLECTION_CONTAINING_THE_REVIEWS = "attractions-all-langs-20140210";
	public static final String DIR_FOR_ID_FILES="reviewIDs";
	public static final String DIR_WITH_REVIEW_IDS = "attractionReviews_all_20140210";
	public static final String PATH_TO_DIR_WITH_REVIEW_IDS=DIR_FOR_ID_FILES+File.separator+DIR_WITH_REVIEW_IDS;
	
	
	private MongoClient mongoClient;
	private DB db;
	private Map<String, String> langNameMap;

	public ReviewIdSelector() {
		try {
			mongoClient = new MongoClient("localhost", 27017);
			db = mongoClient.getDB("opener-annotation-task");
			langNameMap = Maps.newHashMap();
			langNameMap.put("french", "fr");
			langNameMap.put("spanish", "es");
			langNameMap.put("english", "en");
			langNameMap.put("german", "de");
			langNameMap.put("dutch", "nl");
			langNameMap.put("italian", "it");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ReviewIdSelector reviewIdSelector=new ReviewIdSelector();
		Map<String, List<ReviewInfo>> reviewsPerLanguage = reviewIdSelector.getReviewsPerLanguage();
		for(String language:reviewsPerLanguage.keySet()){
			System.out.println("Reviews for "+language+" --> "+reviewsPerLanguage.get(language).size());
			List<ReviewInfo>reviewsForThisLang=reviewsPerLanguage.get(language);
			List<String>idLines=Lists.newArrayList();
			for(ReviewInfo reviewInfo:reviewsForThisLang){
				String id=reviewInfo.getReview_id();
				idLines.add("\"review_id\":\""+id+"\";  1");
			}
			FileUtils.writeLines(new File(PATH_TO_DIR_WITH_REVIEW_IDS+File.separator+language+"_attractions_id.txt"), idLines);
		}
	}

	public Map<String, List<ReviewInfo>> getReviewsPerLanguage() {
		DBCollection collection = db
				.getCollection(COLLECTION_CONTAINING_THE_REVIEWS);
		Map<String, List<ReviewInfo>> reviewsPerLanguage = Maps.newHashMap();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		for (String language : langNameMap.keySet()) {
			System.out.println("Retrieving reviews for "+language);
			BasicDBObject query = new BasicDBObject("language", language);
			DBCursor dbCursor = collection.find(query);
			List<ReviewInfo> reviewsForThisLang = Lists.newArrayList();
			//int count=0;
			while (dbCursor.hasNext()) {
				//System.out.println(">>>>Retrieving reviews for "+language+" ("+(++count)+")");
				DBObject dbObject = dbCursor.next();
				ReviewInfo reviewInfo = gson.fromJson(dbObject.toString(),
						ReviewInfo.class);
				reviewsForThisLang.add(reviewInfo);
			}
			reviewsForThisLang=getSelectedReviews(reviewsForThisLang,language);
			reviewsPerLanguage.put(language, reviewsForThisLang);
		}
		return reviewsPerLanguage;

	}

	public List<ReviewInfo>getSelectedReviews(List<ReviewInfo>reviews, String currentLang){
		int totalSetSize=reviews.size();
		int numSubsets=35;
		int subsetSize=totalSetSize/numSubsets;
		System.out.println("TotalSetSize: "+totalSetSize+"  subsetSize: "+subsetSize);
		int numReviewsPerSubset=100;
		if(numReviewsPerSubset>subsetSize){
			System.err.println("WARNING: subsets are too small to provide "+numReviewsPerSubset+" --> "+subsetSize);
			numReviewsPerSubset=subsetSize;
		}
		
		List<ReviewInfo>selectedReviews=Lists.newArrayList();
		
		for(int i=0;i<numSubsets;i++){
			System.out.println("Getting reviews ("+currentLang+") from element "+subsetSize*i+" to "+(subsetSize*i+numReviewsPerSubset-1));
			for(int j=subsetSize*i;j<subsetSize*i+numReviewsPerSubset;j++){
				if(j>=totalSetSize){
					System.err.println("Preventing an IndexOutOfBoundException... (j="+j+", totalSize="+totalSetSize+")");
					break;
				}
				selectedReviews.add(reviews.get(j));
			}
		}
		return selectedReviews;
	}
	
}
