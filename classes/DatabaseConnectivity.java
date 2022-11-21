/*
 *
 *  * Copyright (c) 2022.  - All Rights Reserved
 *  *  * Unauthorized copying or redistribution of this file in source and binary forms via any medium
 *  *  * is strictly prohibited-
 *  *  * @Author -kartiks.
 *
 */

package database.classes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static java.sql.Types.NULL;

/**
 * This class fetch response from RestAPI (Museum API) and enter it to MySql Database
 */
public class DatabaseConnectivity {
    public static Logger logger = LogManager.getLogger(DatabaseConnectivity.class);

    /**
     * This method fetch response from museum api
     *
     * @param path of museum api to fetch data
     * @return response from api
     */
    String response(String path) {
        logger.info("Sending request to museum API");
        HttpResponse<String> response;
        try {
            if (path != null) {
                // Creating request
                HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(path)).build();
                // Creating client to send request
                HttpClient client = HttpClient.newBuilder().build();
                // Sending request to fetch response from MuseumApi
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200 && (response.body().charAt(0) == '{' || response.body().charAt(0) == '[')) {
                    logger.debug("Response Status -" + response.statusCode() + "\n" + "Response from museum api - " + response.body());
                    return response.body();
                } else {
                    logger.error("uri is invalid");
                    return "invalid uri, request not processed";
                }
            } else {
                logger.error("uri is null");
                return "uri path is null";
            }
        } catch (IOException e) {
            logger.error("IOException : " + e);
            return "IOException : " + e;
        } catch (InterruptedException e) {
            logger.error("InterruptedException : " + e);
            return "InterruptedException : " + e;
        } catch (Exception e) {
            logger.error("UnknownException : " + e);
            return "UnknownException => " + e.getMessage();
        }
    }

    /**
     * This method parse json data into map
     *
     * @param json response from api
     * @return map with key (String) and value (Object)
     */
    public Map<String, Object> mapper(String json) {
        logger.info("Parsing json to map");
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = null;
        try {
            // Object mapper mapping key value pair into Map
            map = objectMapper.readValue(json, Map.class);
            System.out.println("Map is " + map);
            System.out.println("Map Size is " + map.size());

        } catch (JsonMappingException e) {
            logger.error("JsonMappingException" + e);
            System.err.println("JsonMappingException" + e);
        } catch (JsonProcessingException e) {
            logger.error("JsonProcessingException" + e);
            System.err.println("JsonProcessingException" + e);
        }
        logger.info("json to Map executed");
        return map;
    }


    public void insert(String tableName) {
        logger.info("DatabaseConnectivity program invoked");
        DatabaseConnectivity db = new DatabaseConnectivity();

        // Fetching response from museum API for object id 1 to 5
        for (int i = 1; i <= 5; i++) {
            logger.info("fetching response for object id - " + i);
            String json = db.response("https://collectionapi.metmuseum.org/public/collection/v1/objects/" + i);
            Map<String, Object> map = db.mapper(json);
            logger.info("checking null value in map");
            if (map.get("measurements") == null) {
                map.replace("measurements", "null");
            }
            if (map.get("tags") == null) {
                map.replace("tags", "null");
            }
            if (map.get("constituents") == null) {
                map.replace("constituents", "null");
            }
            logger.info("Connecting to MySql database");
            Connection connect = null;
            PreparedStatement preparedStatement;
            try {
                // Load the MySQL driver, each DB has its own driver
                Class.forName("com.mysql.jdbc.Driver");

                // DB connection setup
                connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/normal", "root", "Sql2000!");
                logger.info("Preparing insert query to enter data to museum table");
                // PreparedStatements
                preparedStatement = connect
                        .prepareStatement("insert into  "+tableName+" values (?, ?, ?, ?,?, ?, ?, ?,?, ?, ?, ?,?, ?, ?, ?,?, ?, ?, ?,?, ?, ?, ?,?, ?, ?, ?,?, ?, ?, ?,?, ?, ?, ?,?, ?, ?, ?,?, ?, ?, ?,?, ?, ?, ?,?, ?, ?, ?,?, ?, ?, ?,? )");
                int iterator = 1;
                logger.info("Iterating Map key and value to set into query");
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    if (entry.getValue().getClass().getSimpleName().equals("Integer")) {
                        logger.debug(Integer.parseInt(entry.getValue().toString()));
                        preparedStatement.setInt(iterator, Integer.parseInt(entry.getValue().toString()));
                    } else if (entry.getValue().getClass().getSimpleName().equals("String")) {
                        logger.debug(entry.getValue().toString());
                        preparedStatement.setString(iterator, entry.getValue().toString());
                    } else if (entry.getValue().getClass().getSimpleName().equals("Boolean")) {
                        boolean boo = Boolean.getBoolean(entry.getValue().toString());
                        logger.debug(boo);
                        preparedStatement.setBoolean(iterator, boo);
                    } else if (entry.getValue().getClass().getSimpleName().equals("Float")) {
                        logger.debug(Float.parseFloat(entry.getValue().toString()));
                        preparedStatement.setFloat(iterator, Float.parseFloat(entry.getValue().toString()));
                    } else if (entry.getValue().getClass().getSimpleName().equals("ArrayList")) {
                        logger.debug(entry.getValue());
                        preparedStatement.setString(iterator, new ArrayList<>(Arrays.asList(entry.getValue())).toString());
                    } else if (entry.getValue() == "null") {
                        logger.debug(entry.getValue());
                        preparedStatement.setNull(iterator, NULL);
                    } else {
                        logger.error("Parsing data type not matched with table data type");
                    }
                    iterator++;
                }
                preparedStatement.executeUpdate();
                logger.info("query executed");
            } catch (SQLException | ClassNotFoundException e) {
                logger.error("Exception - " + e);
                System.err.println(e.getMessage());
            } finally {
                try {
                    if (connect != null) {
                        logger.info("Connection closed");
                        connect.close();
                    }

                } catch (Exception e) {
                    logger.error("Exception - " + e);
                    System.err.println(e.getMessage());
                }
            }

        }
    }
}

