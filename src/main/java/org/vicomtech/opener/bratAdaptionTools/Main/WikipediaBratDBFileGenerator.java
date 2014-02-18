package org.vicomtech.opener.bratAdaptionTools.Main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.Lists;

public class WikipediaBratDBFileGenerator {

	private static Connection conn;
	
	static{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/fr_wikipedia", "root", "root"); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		   
	}
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		List<String> bratNormDBEntries = generateBratNormDBEntries();
		File f=new File("fr-wikipedia-brat-norm-db.txt");
		FileOutputStream fos=new FileOutputStream(f);
		//for(String line:bratNormDBEntries){
			IOUtils.writeLines(bratNormDBEntries,"\n", fos);
		//}
		
	}

	protected static List<String>generateBratNormDBEntries(){
		try{
			List<String>bratNormDBEntries=Lists.newArrayList();
			String sql="select page_id, CAST(page_title as CHAR(100)) page_title from page where page.page_is_redirect=0";
			PreparedStatement pst=conn.prepareStatement(sql);
			ResultSet rs=pst.executeQuery();
			while(rs.next()){
				int pageId=rs.getInt("page_id");
				String pageTitle=rs.getString("page_title");
				StringBuffer sb=new StringBuffer();
				sb.append(pageId);
				sb.append("\t");
				sb.append("name:Page Title:");
				sb.append(pageTitle);
				sb.append("\t");
				sb.append("attr:Name:");
				sb.append(pageTitle.replace("_", " "));
				bratNormDBEntries.add(sb.toString().trim());
				System.out.println("Processing... pageId: "+pageId+"   pageTitle: "+pageTitle);
			}
			return bratNormDBEntries;
		
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
}
