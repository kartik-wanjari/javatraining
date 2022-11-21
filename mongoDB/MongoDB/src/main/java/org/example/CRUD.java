package org.example;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static com.mongodb.client.model.Filters.eq;

public class CRUD {
    String uri = "mongodb://localhost:27017";
    public static Logger logger = LogManager.getLogger(CRUD.class);

    /**
     * This method insert document into museum collection of MuseumAPI database.
     * @param json Document to be inserted
     * @return Response
     */
    public String create(String json){
        String finalResult = null;
        try (MongoClient mongo = MongoClients.create(uri)) {
            logger.info("connecting to the database");

            MongoDatabase database = mongo.getDatabase("MuseumAPI");

            // Creating a collection
            logger.info("Collection created successfully");
            // Retrieving a collection
            MongoCollection<Document> collection = database.getCollection("museum");
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(json, Map.class);
            Document query = new Document().append("_id", new ObjectId());
            Document doc = collection.find(eq("objectID", map.get("objectID"))).first();
            if(doc == null) {
                for (Map.Entry<String, Object> m : map.entrySet()) {
                    logger.debug("Key Value Pair: " + m.getKey() + ", " + m.getValue());
                    if (m.getKey().equals("objectID")) {
                        query.append(m.getKey(), Integer.parseInt(m.getValue().toString()));
                    } else {
                        query.append(m.getKey(), m.getValue());
                    }
                }
                logger.debug("Data :" + query);
            }else{
                return "Document with objectID "+map.get("objectID")+" already exist.";
            }
            try {
                InsertOneResult result = collection.insertOne(query);
                finalResult = "Data inserted";
                System.out.println("Success! Inserted document id: " + result.getInsertedId());
            } catch (MongoException me) {
                System.err.println("Unable to insert due to an error: " + me);
            }
        } catch (JsonMappingException e) {
            logger.error("JsonMappingException : " + e);
            finalResult = e.getMessage();
            return finalResult;
        } catch (JsonProcessingException e) {
            logger.error("JsonProcessingException : " + e);
            finalResult = e.getMessage();
            return finalResult;
        }
        return finalResult;
    }

    /**
     * This method get documents from MuseumAPI database
     * @return Response consisting Museum Data
     */
    public String find() {

        try (MongoClient mongo = MongoClients.create(uri)) {
            logger.info("connecting to the database");

            MongoDatabase database = mongo.getDatabase("MuseumAPI");

            // Creating a collection
            logger.info("Collection created successfully");
            // Retrieving a collection
            MongoCollection<Document> collection = database.getCollection("museum");
            Bson projectionFields = Projections.excludeId();
            logger.info("Collection museum selected successfully");
            ArrayList<String> arrayList = new ArrayList<>();
            try (MongoCursor<Document> mongoCursor = collection.find().projection(projectionFields).iterator()) {
                while (mongoCursor.hasNext()) {
                    arrayList.add(mongoCursor.next().toJson());
                }
            }
            return arrayList.toString();
        }
    }
    /**
     * This method update document into museum collection of MuseumAPI database.
     * @param json Document to be updated
     * @return Response
     */
    public String update(String json) {
        String finalResult = null;
        try (MongoClient mongo = MongoClients.create(uri)) {
            logger.info("connecting to the database");
            MongoDatabase database = mongo.getDatabase("MuseumAPI");
            // Creating a collection
            logger.info("Collection created successfully");
            // Retrieving a collection
            MongoCollection<Document> collection = database.getCollection("museum");
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(json, Map.class);
            Document query = new Document();

            Bson updates;
            for (Map.Entry<String, Object> m : map.entrySet()) {
                if (m.getKey().equals("objectID")) {
                    query.append(m.getKey(), Integer.parseInt(m.getValue().toString()));
                } else {
                    updates = Updates.combine(Updates.set(m.getKey(), m.getValue()));

                    UpdateOptions options = new UpdateOptions().upsert(true);
                    try {
                        UpdateResult result = collection.updateOne(query, updates, options);
                        finalResult = "updated";
                        logger.info("Modified document count: " + result.getModifiedCount());
                        logger.info("Upserted id: " + result.getUpsertedId()); // only contains a value when an upsert is performed
                    } catch (MongoException me) {
                        logger.error("Unable to update due to an error: " + me);
                        finalResult = me.getMessage();
                        return finalResult;
                    }
                }
            }
        } catch (JsonMappingException e) {
            logger.error("JsonMappingException : " + e);
            finalResult = e.getMessage();
            return finalResult;
        } catch (JsonProcessingException e) {
            logger.error("JsonProcessingException : " + e);
            finalResult = e.getMessage();
            return finalResult;
        }
        return finalResult;
    }
    /**
     * This method insert document into museum collection of MuseumAPI database.
     * @param objectID Document id for deletion
     * @return Response
     */
    public Response delete(String objectID){
        String finalResult;
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("MuseumAPI");
            MongoCollection<Document> collection = database.getCollection("museum");
            Bson query = eq("objectID", Integer.parseInt(objectID));
            try {
                Document doc = collection.find(eq("objectID", Integer.parseInt(objectID))).first();
                if(doc != null) {
                    DeleteResult result = collection.deleteOne(query);
                    finalResult = "Deleted document count: " + result.getDeletedCount();
                    logger.info("Deleted document count: " + result.getDeletedCount());
                }else {
                    logger.error("Document does not exist");
                    return Response.status(Response.Status.BAD_REQUEST).entity("Document does not exist").build();
                }
            } catch (MongoException me) {
                logger.error("Unable to delete due to an error: " + me);
                finalResult = me.getMessage();
                return Response.status(Response.Status.BAD_REQUEST).entity(finalResult).build();
            }
        }
        return Response.ok(finalResult).build();
    }


    public static void main(String[] args) {
        CRUD crud = new CRUD();
        String json = "{\"objectID\": 46, \"isHighlight\": true, \"accessionNumber\": \"10.125.444b\", " +
                "\"accessionYear\": \"2000\", \"isPublicDomain\": true, " +
                "\"primaryImage\": \"https://images.metmuseum.org/CRDImages/ad/original/17342.jpg\", " +
                "\"primaryImageSmall\": \"https://images.metmuseum.org/CRDImages/ad/web-large/17342.jpg\", " +
                "\"additionalImages\": null, \"constituents\": null, \"department\": \"The American Wing\", " +
                "\"objectName\": \"Kartik\", \"title\": \"Andiron\", \"culture\": \"\", \"period\": \"\", " +
                "\"dynasty\": \"\", \"reign\": \"\", \"portfolio\": \"\", \"artistRole\": \"\", " +
                "\"artistPrefix\": \"\", \"artistDisplayName\": \"\", \"artistDisplayBio\": \"\", " +
                "\"artistSuffix\": \"\", \"artistAlphaSort\": \"\", \"artistNationality\": \"\", " +
                "\"artistBeginDate\": \"\", \"artistEndDate\": \"\", \"artistGender\": \"\", " +
                "\"artistWikidata_URL\": \"\", \"artistULAN_URL\": \"\", \"objectDate\": \"ca. 1790\", " +
                "\"objectBeginDate\": 1787, \"objectEndDate\": 1790, \"medium\": \"Brass\", " +
                "\"dimensions\": \"H. 27 in. (68.6 cm)\", \"measurements\": [{\"elementName\": \"Overall\", " +
                "\"elementDescription\": null, \"elementMeasurements\": {\"Height\": 68.5801}}], \"creditLine\": " +
                "\"Gift of Mrs. Russell Sage, 1909\", \"geographyType\": \"\", \"city\": \"\", \"state\": \"\", " +
                "\"county\": \"\", \"country\": \"\", \"region\": \"\", \"subregion\": \"\", \"locale\": \"\", " +
                "\"locus\": \"\", \"excavation\": \"\", \"river\": \"\", \"classification\": \"\", " +
                "\"rightsAndReproduction\": \"\", \"linkResource\": \"\", \"metadataDate\": \"2022-08-03T04:54:59.807Z\", " +
                "\"repository\": \"Metropolitan Museum of Art, New York, NY\", " +
                "\"objectURL\": \"https://www.metmuseum.org/art/collection/search/45\", \"tags\": null, " +
                "\"objectWikidata_URL\": \"\", \"isTimelineWork\": false, \"GalleryNumber\": \"774\"}";

        String insertData = "{\"objectID\": 45, \"isHighlight\": false, \"accessionNumber\": \"10.125.444b\", " +
                "\"accessionYear\": \"2000\", \"isPublicDomain\": true}";

//        System.out.println(crud.find());
//        System.out.println(crud.update(json));
//        System.out.println(crud.create(insertData));
//        System.out.println(crud.delete("111"));
//        List<String> arrayList = crud.sort("objectID");
//        for (String i : arrayList){
//            System.out.println(i);
//        }
    }
}

