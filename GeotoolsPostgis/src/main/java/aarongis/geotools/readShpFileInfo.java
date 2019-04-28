package aarongis.geotools;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.ServiceInfo;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.swing.data.JFileDataStoreChooser;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class readShpFileInfo {
	
	public static void main(String[] args) {
		
		 File file = JFileDataStoreChooser.showOpenFile("shp", null);
	        if (file == null) {
	            return;
	        }

	        FileDataStore store;
			try {
				store = FileDataStoreFinder.getDataStore(file);
				ServiceInfo info = store.getInfo();
				System.out.println(info);
				String typeName = store.getTypeNames()[0];
				System.out.println(typeName);
				
				SimpleFeatureType pgfeaturetype = store.getSchema();
				
				CoordinateReferenceSystem coordRef = pgfeaturetype.getGeometryDescriptor().getCoordinateReferenceSystem();
				System.out.println(coordRef.toString());
				
				Map<Object, Object> userDatas = pgfeaturetype.getGeometryDescriptor().getUserData();
				
				for(Map.Entry<Object, Object> entry : userDatas.entrySet()){
					Object mapKey = entry.getKey();
					Object mapValue = entry.getValue();
				    System.out.println(mapKey+":"+mapValue);
				}
				//System.out.println(srid);
				
		        SimpleFeatureSource featureSource = store.getFeatureSource();
		        SimpleFeatureCollection simpleFeatureCollection = featureSource.getFeatures();
		        
		        System.out.println("Feature numbers in shape file:"+simpleFeatureCollection.size());
		        /**
		        try ( SimpleFeatureIterator iterator = simpleFeatureCollection.features() ){
		            while( iterator.hasNext() ){
		                 SimpleFeature feature = iterator.next();
		                 System.out.println( feature.getID() );
		                 Iterator<Property> props = feature.getProperties().iterator();
		                 while(props.hasNext()) {
		                	 Property prop = props.next();
		                	 System.out.println(prop.getName()+"=="+prop.getValue());
		                 }
		                 //ListIterator<Object> attributes = feature.getAttributes().listIterator();
		                 //while(attributes.hasNext()) {
		                //	 Object attribute = attributes.next();
		                //	 System.out.println(attribute);
		                 //}
		            }
		        }
		        **/
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	        
		
	}

}
