package aarongis.geotools;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import org.geotools.data.postgis.PostgisNGDataStoreFactory;

public class ReadPropertiesFile {

	public static void main(String[] args) {
		
		ReadPropertiesFile app = new ReadPropertiesFile();
		
		app.printSystemInfo();
		app.printAll("config.properties");
		System.out.println(PostgisNGDataStoreFactory.PASSWD.key);
		
	}
	private void printSystemInfo() {
		// TODO Auto-generated method stub
	     Properties pps = System.getProperties();
	     pps.list(System.out);
		
	}
	private void printAll(String filename) {

	        try (InputStream input = getClass().getClassLoader().getResourceAsStream(filename)) {

	            Properties prop = new Properties();

	            if (input == null) {
	                System.out.println("Sorry, unable to find " + filename);
	                return;
	            }

	            prop.load(input);

	            // Java 8 , print key and values
	            prop.forEach((key, value) -> System.out.println("Key : " + key + ", Value : " + value));

	            // Get all keys
	            prop.keySet().forEach(x -> System.out.println(x));

	            Set<Object> objects = prop.keySet();

	            /*Enumeration e = prop.propertyNames();
	            while (e.hasMoreElements()) {
	                String key = (String) e.nextElement();
	                String value = prop.getProperty(key);
	                System.out.println("Key : " + key + ", Value : " + value);
	            }*/

	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
	 }
}
