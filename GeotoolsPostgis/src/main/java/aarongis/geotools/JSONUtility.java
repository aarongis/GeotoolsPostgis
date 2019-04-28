package aarongis.geotools;

import java.io.IOException;
import java.io.InputStream;

import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureIterator;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class JSONUtility {
	
	public static SimpleFeatureSource geoJSON2Shp(InputStream input,
	        SimpleFeatureType schema, ShapefileDataStore shpDataStore)
	        throws IOException {

	    FeatureJSON fjson = new FeatureJSON(new GeometryJSON(15));

	    SimpleFeatureType featureType = schema;

	    if (featureType != null) {
	        fjson.setFeatureType(featureType);
	    }

	    FeatureIterator<SimpleFeature> jsonIt = fjson
	            .streamFeatureCollection(input);

	    if (!jsonIt.hasNext()) {
	        throw new IllegalArgumentException(
	                "Cannot create shapefile. GeoJSON stream is empty");
	    }

	    FeatureWriter<SimpleFeatureType, SimpleFeature> writer = null;

	    try {

	        // use feature type of first feature, if not supplied
	        SimpleFeature firstFeature = jsonIt.next();
	        if (featureType == null) {
	            featureType = firstFeature.getFeatureType();
	        }

	        shpDataStore.createSchema(featureType);

	        writer = shpDataStore.getFeatureWriterAppend(
	                shpDataStore.getTypeNames()[0], Transaction.AUTO_COMMIT);

	        addFeature(firstFeature, writer);

	        while (jsonIt.hasNext()) {
	            SimpleFeature feature = jsonIt.next();
	            addFeature(feature, writer);                
	        }
	    } finally {
	        if (writer != null) {
	            writer.close();
	        }
	    }

	    return shpDataStore.getFeatureSource(shpDataStore.getTypeNames()[0]);
	}


	private static void addFeature(SimpleFeature feature,
	        FeatureWriter<SimpleFeatureType, SimpleFeature> writer)
	        throws IOException {

	    SimpleFeature toWrite = writer.next();
	    for (int i = 0; i < toWrite.getType().getAttributeCount(); i++) {
	        String name = toWrite.getType().getDescriptor(i).getLocalName();
	        toWrite.setAttribute(name, feature.getAttribute(name));
	    }

	    // copy over the user data
	    if (feature.getUserData().size() > 0) {
	        toWrite.getUserData().putAll(feature.getUserData());
	    }

	    // perform the write
	    writer.write();
	}  

}
