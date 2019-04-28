package aarongis.geotools;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtility {
	
	private static PropertiesUtility instance = null;
	
	Map<String, String> map=null; 
	
    
    public static PropertiesUtility getInstance() {
    	
    	if(instance==null) {
    		
    		instance = new PropertiesUtility();
    	}
    	
    	return instance;
    }
    
    private  Map<String, String> readPropertiesFile(String filename) {
    	
    	Properties prop = new Properties();
    	
    	try {
   	    
    		InputStream input = Class.forName("aarongis.geotools.PropertiesUtility").getClassLoader().getResourceAsStream(filename); 

            prop.load(input);
    	}catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Map<String, String> map = new HashMap<String, String>((Map) prop);
    	return map;
    }
    
    public Map<String, String> getPostgisdbsParams() {
    	
    	return readPropertiesFile("config.properties");
    }

}
