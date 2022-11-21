package org.example;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;

public class Operations {
    String uri = "mongodb://localhost:27017";
    public static Logger logger = LogManager.getLogger(CRUD.class);
    /**
     * This method sort documents of MuseumAPI database according to fields.
     * @param element Field name
     * @return Response consisting Museum Data
     */
    public String sort(String element) {

        try (MongoClient mongo = MongoClients.create(uri)) {
            logger.info("connecting to the database");

            MongoDatabase database = mongo.getDatabase("MuseumAPI");

            // Creating a collection
            logger.info("Collection created successfully");
            // Retrieving a collection
            MongoCollection<Document> collection = database.getCollection("museum");
            Bson projectionFields = Projections.excludeId();
            Bson sortingElement = new Document("sort",element);
            logger.info("Collection museum selected successfully");
            ArrayList<String> arrayList = new ArrayList<>();
            try (MongoCursor<Document> mongoCursor = collection.find().sort(Sorts.ascending(element)).projection(projectionFields).iterator()) {
                while (mongoCursor.hasNext()) {
                    arrayList.add(mongoCursor.next().toJson());
                }
            }
            return arrayList.toString();
        }
    }

    /**
     * This method search for data in collection.
     * @param text data for searching in collection
     * @return documents that includes 'text' data.
     */
    public String search(String text) {

        try (MongoClient mongo = MongoClients.create(uri)) {
            logger.info("connecting to the database");

            MongoDatabase database = mongo.getDatabase("MuseumAPI");

            // Creating a collection
            logger.info("Collection created successfully");
            // Retrieving a collection
            MongoCollection<Document> collection = database.getCollection("museum");
            Bson projectionFields = Projections.excludeId();
            Bson filter = Filters.text(text);

            ArrayList<String> arrayList = new ArrayList<>();
            try (MongoCursor<Document> mongoCursor = collection.find(filter).projection(projectionFields).iterator()) {
                while (mongoCursor.hasNext()) {
                    arrayList.add(mongoCursor.next().toJson());
                }
            }
            return arrayList.toString();
        }
    }

    public static void main(String[] args) {
        Operations op = new Operations();
        //System.out.println(op.search("American"));
    }
}
