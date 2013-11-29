package org.vicomtech.opener.annotationReviews;

import java.net.UnknownHostException;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;


public class ReviewRetriever {

	public static class MyReviewInfo{
		private String review_id;
		private String title;
		private String comment;
		public MyReviewInfo() {
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
	
	/**
	 * @param args
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException {
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
		  DB db = mongoClient.getDB("opener-annotation-task");
		  Set<String> collectionNames = db.getCollectionNames();
		  for(String collectionName:collectionNames){
			  System.out.println(collectionName);
		  }
		  
		  DBCollection coll = db.getCollection("reviews-all");
		  DBCursor cursor = coll.find();
//		  while(cursor.hasNext()){
//			  DBObject dbo=cursor.next();
//			  String reviewId=(String) dbo.get("review_id");
//			  System.out.println(reviewId);
//		  }
		  cursor.close();
		  BasicDBObject query = new BasicDBObject("language","spanish");
		  cursor= coll.find(query);
		  int count=0;
		  Gson gson = new GsonBuilder().setPrettyPrinting().create();
		  while(cursor.hasNext()){
			  count++;
			  DBObject dbo=cursor.next();
			  System.out.println(dbo.toString());
			  MyReviewInfo obj = gson.fromJson(dbo.toString(), MyReviewInfo.class);
			  System.out.println("Title: "+obj.getTitle()+"\nComment:\n*********\n"+obj.getComment()+"\n**********\nReviewId: "+obj.getReview_id());
//			  String comment=(String) dbo.get("comment");
//			  String title=(String) dbo.get("title");
//			  String reviewId=(String) dbo.get("review_id");
//			  System.out.println("Title: "+title+"\nComment:\n*********\n"+comment+"\n**********\nReviewId: "+reviewId);
		  }
		  cursor.close();
		  System.out.println("Number of reviews for this criteria: "+count);
	}

}
