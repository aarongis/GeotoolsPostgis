package aarongis.geotools;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.swing.data.JFileDataStoreChooser;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.PropertyDescriptor;

public class Shp2postgis {
	
    private static Logger logger = Logger.getLogger(Object.class);
    private static DataStore pgDataStore = null;
    //private static JSONArray jsonArray = null;
    
    
    
	public static void main(String[] args) {
		
		pgDataStore=PostgisDataStore.getInstance();
		
		File file = JFileDataStoreChooser.showOpenFile("shp", null);
        if (file == null) {
            return;
        }


        File pfile = file.getParentFile();
        
        FileFilter myFileFilter = new FileFilter(){  
            
            @Override  
            public boolean accept(File pathname) {  
                String filename = pathname.getName().toLowerCase();  
                if(filename.endsWith(".shp")){  
                    return true;  
                }else{  
                    return false;  
                }  
            } 
        };
            
        File[] fs = pfile.listFiles(myFileFilter);
		for(File f:fs){
			String fn= f.getPath();
			String tableName=fn.substring(fn.lastIndexOf(File.separator)+1,fn.lastIndexOf("."));
			System.out.println(f.getParent()+"///"+ tableName);
			try {
				uploadShp(f,tableName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		pgDataStore.dispose();

	}
	
	public static boolean uploadShp(File shpfile, String tablename) throws IOException {

        ShapefileDataStore shpDataStore = new ShapefileDataStore(shpfile.toURI().toURL()); ;

//        shapefileDataStore.setCharset(Charset.forName("utf-8"));
        FeatureSource featureSource = shpDataStore.getFeatureSource();
        FeatureCollection featureCollection = featureSource.getFeatures();
        SimpleFeatureType shpfeaturetype = shpDataStore.getSchema();
        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        typeBuilder.init(shpfeaturetype);
        typeBuilder.setName(tablename);
        SimpleFeatureType newtype = typeBuilder.buildFeatureType();
        pgDataStore.createSchema(newtype);
        logger.info("\npostgis创建数据表成功");

        FeatureIterator iterator = featureCollection.features();
        FeatureWriter<SimpleFeatureType, SimpleFeature> featureWriter = pgDataStore.getFeatureWriterAppend(tablename, Transaction.AUTO_COMMIT);
        Map<String, Object> fields = new HashMap<>();
        Collection<PropertyDescriptor> collection = shpfeaturetype.getDescriptors();
        Iterator<PropertyDescriptor> propertyDescriptoriterator = collection.iterator();
        while (propertyDescriptoriterator.hasNext()) {
            PropertyDescriptor descriptor = propertyDescriptoriterator.next();
            fields.put(descriptor.getName().toString(), descriptor.getType().getBinding().getSimpleName());
        }

        while (iterator.hasNext()) {
            Feature feature = iterator.next();
            SimpleFeature simpleFeature = featureWriter.next();
            Collection<Property> properties = feature.getProperties();
            Iterator<Property> propertyIterator = properties.iterator();
            while (propertyIterator.hasNext()) {
                Property property = propertyIterator.next();
                simpleFeature.setAttribute(property.getName().toString(), property.getValue());
            }
            featureWriter.write();
        }
        iterator.close();
        featureWriter.close();
        shpDataStore.dispose();
        //pgDatastore.dispose();
        logger.info("\nshp导入postgis成功");
        return true;
    }

}
