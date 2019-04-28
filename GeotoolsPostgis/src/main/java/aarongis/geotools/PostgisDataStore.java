package aarongis.geotools;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;

public class PostgisDataStore {
	
	private static Logger logger = Logger.getLogger(PostgisDataStore.class);
    private static DataStore postgisDataStore = null;
    
    public PostgisDataStore() {
    }


    public static DataStore getInstance() {
        if (postgisDataStore == null) {
        	Map<String, String> params = PropertiesUtility.getInstance().getPostgisdbsParams();
        	try {
                postgisDataStore = DataStoreFinder.getDataStore(params);
                logger.info("\nPostgisDataStore 初始化geotools中的 Datastore成功\n");
            } catch (IOException e) {
                logger.error("\nPostgisDataStore 初始化geotools中的 Datastore失败\n");
                logger.error(e.getMessage());
            }
        }
		return postgisDataStore;
    }

}
