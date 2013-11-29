package org.vicomtech.opener.bratAdaptionTools.Main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class ProcessTourpediaDump {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		InputStream is;
		if(args.length==0){
			URL url=new URL("http://wafi.iit.cnr.it/openervm/API/getBratForVenues.php");
			is=url.openStream();
			processTourpediaDump(is, new FileOutputStream(new File("tourpedia-fb.txt")));
		}else{
			System.err.println("Not implemented");
		}

	}
	
	public static void processTourpediaDump(InputStream is, OutputStream os){
		try {
			List<String>entries=IOUtils.readLines(is);
			for(String entry:entries){
				String processedEntry=processEntry(entry);
				IOUtils.write(processedEntry.getBytes(), os);
				IOUtils.write("\n".getBytes(), os);
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected static String processEntry(String entry){
		String[]columns=entry.split("\t");
		String id=columns[0];
		String nameCol=columns[1];
		String descriptionCol=columns[2];
		String urlCol=columns[3];
		if(urlCol.trim().endsWith(":")){
			urlCol=urlCol.trim()+"n/a";
		}
		StringBuffer sb=new StringBuffer();
		sb.append(id);
		sb.append("\t");
		sb.append(nameCol.replace("name:", "name:Name:"));
		sb.append("\t");
		sb.append(descriptionCol.replace("\t", "  ").replace("attr:", "attr:Address:"));
		sb.append("\t");
		sb.append(urlCol.replace("info:", "attr:Web page:"));
		return sb.toString();
	}

}
