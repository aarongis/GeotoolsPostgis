package aarongis.geotools;

import java.util.Map;

import org.geotools.data.DataStore;

public class PostgisDataStoreTest {
	
	public static void main(String[] args) {
		
		PropertiesUtility p = PropertiesUtility.getInstance();
		Map<String, String> maps = p.getPostgisdbsParams();
		
		maps.forEach((key, value) -> System.out.println("Key : " + key + ", Value : " + value));
	
	}

}
