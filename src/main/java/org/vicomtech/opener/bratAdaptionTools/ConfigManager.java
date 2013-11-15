package org.vicomtech.opener.bratAdaptionTools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

public class ConfigManager {

	private static final String PROPERTIES_LOCATION = "annotation-mapping-config.properties";

	public static final String NAMED_ENTITY_TYPES_PROP="named_entity_types";
	public static final String COREF_RELATION_TYPES_PROP="coref_relation_types";
	
	private Properties properties=new Properties();

	private static ConfigManager INSTANCE;

	private ConfigManager() {
		loadProperties();
	}

	public static ConfigManager getConfigManager() {
		if (INSTANCE == null) {
			INSTANCE = new ConfigManager();
		}
		return INSTANCE;
	}

	private void loadProperties() {
		try {
			File f = new File(PROPERTIES_LOCATION);
			if (!f.exists()) {
				f.createNewFile();
				InputStream is = Class.class.getResourceAsStream("/"+ PROPERTIES_LOCATION);
				dumpDefaultFileToExternalFile(f, is);
			}
			FileInputStream fis = new FileInputStream(f);
			properties.load(fis);
			fis.close();
		} catch (IOException e) {
			System.err.println("Not able to create config file: "+ e.getMessage());
			System.err.println("WARNING: Using internal default config file");
			InputStream is = Class.class.getResourceAsStream("/"+ PROPERTIES_LOCATION);
			try {
				properties.load(is);
				is.close();
			} catch (IOException e1) {
				throw new RuntimeException(e);
			}
		}
	}

	private void dumpDefaultFileToExternalFile(File f, InputStream is) {
		try {
			FileOutputStream fos = new FileOutputStream(f);
			IOUtils.copy(is, fos);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public String[] getValuesForProperty(String property){
		String value = (String) properties.get(property);
		System.out.println("ConfigManager reading property:"+property+" value:"+value);
		String[]values=value.split(",");
		return values;
	}

}
