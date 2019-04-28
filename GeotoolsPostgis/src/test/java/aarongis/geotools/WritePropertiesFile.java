package aarongis.geotools;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class WritePropertiesFile {
    
	public static void main(String[] args) {
		
		

        //try (OutputStream output = new FileOutputStream("src/main/resources/config.properties")) {
		try (OutputStream output = new FileOutputStream("config.properties")) {

            Properties prop = new Properties();

            // set the properties value
            
                
            prop.setProperty("dbtype", "postgis");
            prop.setProperty("host", "192.168.0.16");
            prop.setProperty("port", "5432");
            prop.setProperty("schema", "public");
            prop.setProperty("database", "mynew_pgis_db");
            prop.setProperty("user", "postgres");
            prop.setProperty("password", "aaron@163.com");

            // save properties to project root folder
            prop.store(output, null);

            System.out.println(prop);

        } catch (IOException io) {
            io.printStackTrace();
        }

    }

}
