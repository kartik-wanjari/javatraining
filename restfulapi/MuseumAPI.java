/*
 *
 *  * Copyright (c) 2022.  - All Rights Reserved
 *  *  * Unauthorized copying or redistribution of this file in source and binary forms via any medium
 *  *  * is strictly prohibited-
 *  *  * @Author -kartiks.
 *
 */

package restfulapi;

import com.github.opendevl.JFlat;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * This class call request from API and convert json to csv.
 */
public class MuseumAPI {
    public static Logger logger = LogManager.getLogger(MuseumAPI.class);

    private String response(String path) {
        HttpResponse<String> response;
        try {
            if (path != null) {
                HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(path)).build();
                HttpClient client = HttpClient.newBuilder().build();
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200 && (response.body().charAt(0) == '{' || response.body().charAt(0) == '[')) {

                    return response.body();
                } else {
                    throw new IllegalArgumentException();
                }
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException ne) {
            return "NullPointerException: input parameter is null";
        } catch (IOException ioe) {
            return "IOException : " + ioe;
        } catch (InterruptedException ie) {
            return "InterruptedException : " + ie;
        } catch (IllegalArgumentException ie) {
            return "IllegalArgumentException: invalid url: " + path;
        } catch (Exception e) {
            return "UnknownException => " + e.getMessage();
        }
    }

    private JSONArray convertObjToArray(String response, String key) {
        JSONArray jsonArray = null;
        JSONObject jsonObject;
        try {
            if (response != null && response.charAt(0) == '{') {
                jsonObject = new JSONObject(response);
                if (key != null) {
                    logger.info("fetching key value: " + key);
                    jsonArray = jsonObject.getJSONArray(key);
                } else {
                    throw new NullPointerException();
                }
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException ne) {
            System.err.println("NullPointerException => " + ne);
        } catch (JSONException je) {
            System.err.println(je.getMessage());
        } catch (Exception e) {
            System.err.println("Unknown Exception => " + e);
        }
        return jsonArray;
    }

    private void jsonFlattener(String path) {
        String json = null;
        try {
            if (path != null) {
                json = new String(Files.readAllBytes(Paths.get(path)));
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            System.err.println("NullPointerException => path is null");
        } catch (Exception e) {
            System.err.println("Unknown Exception => " + e);
        }
        String newJson = null;
        try {
            if (json != null) {
                newJson = json.replaceAll("}\\{", "},{");
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            System.err.println("NullPointerException => json is null");
        } catch (Exception e) {
            System.err.println("Unknown Exception => " + e);
        }

        logger.debug(newJson);
        logger.info("parsing json object to json array");
        String s = "[" + newJson + "]";
        logger.debug("JsonArray: " + s);
        logger.info("flattening nested json");
        JFlat flatMe;
        try {
            flatMe = new JFlat(s);

            flatMe.json2Sheet().headerSeparator("_").write2csv("/Users/azuga/Documents/Azuga Training/API/museum.csv");
        } catch (Exception e) {
            System.err.println("JflatException => " + e);
        }
        logger.info("museum.csv file created");
        System.out.println("\nFile created");

    }

    private void parser(String path) throws IOException {
        logger.info("Parsing float to int");
        CSVReader reader = null;
        List<String[]> csvBody = new ArrayList<>();
        try {
            reader = new CSVReader(new FileReader(path));
            logger.info("opening museum.csv file");
            csvBody = reader.readAll();
            logger.info("reading rows of csv file");
            for (int m = 1; m < csvBody.size(); m++) {
                System.out.println(csvBody.get(m)[0]);
                logger.debug("Cell(" + m + "," + 0 + "): " + csvBody.get(m)[0]);
                String i = csvBody.get(m)[0].replaceAll(".0+$", "");
                logger.debug("Corrected cell value: " + i);
                String j = csvBody.get(m)[29].replaceAll(".0+$", "");
                logger.debug("Corrected cell value: " + j);
                String k = csvBody.get(m)[30].replaceAll(".0+$", "");
                logger.debug("Corrected cell value: " + k);
                System.out.println(i);
                logger.info("Saving changes to csv file");
                csvBody.get(m)[0] = i;
                csvBody.get(m)[29] = j;
                csvBody.get(m)[30] = k;
            }
        } catch (FileNotFoundException fe) {
            System.err.println("FileNotFoundException => " + fe);
        } catch (IOException ioe) {
            System.err.println("IOException =>" + ioe);
        } catch (Exception e) {
            System.err.println("UnknownException =>" + e);
        } finally {
            reader.close();
        }

        logger.info("museum.csv file closed");
        logger.info("Writing all the changes to museum.csv");
        CSVWriter writer = new CSVWriter(new FileWriter("/Users/azuga/Documents/Azuga Training/API/museum.csv"));
        writer.writeAll(csvBody);

        writer.close();
        logger.info("museum.csv file closed");
    }


    public static void main(String[] args) throws Exception {


        MuseumAPI museumAPI = new MuseumAPI();

        String res = museumAPI.response("https://collectionapi.metmuseum.org/public/collection/v1/objects");

        JSONArray jsonArray = museumAPI.convertObjToArray(res, "objectIDs");
        logger.info("Creating museum.json file");
        PrintWriter pw = new PrintWriter("/Users/azuga/Documents/Azuga Training/API/museum.json");
        logger.info("appending museum response to museum.json");
        logger.info("fetching data of object id: 40 to 49");
        String museumResponse;
        for (int i = 40; i < 50; i++) {
            logger.info("Sending GET request to fetch Art collection from Museum API");
            museumResponse = museumAPI.response("https://collectionapi.metmuseum.org/public/collection/v1/objects/" + jsonArray.get(i));
            StringBuilder sb = new StringBuilder();
            logger.debug("Museum art data of id["+i+"]: " + museumResponse);
            sb.append(museumResponse.replaceAll("\\[]", "null"));
            pw.write(sb.toString());
            System.out.println(sb);
        }
        pw.close();
        logger.info("museum.json file created");
        logger.info("Reading museum.json file data");
        logger.info("Flattenning");
        museumAPI.jsonFlattener("/Users/azuga/Documents/Azuga Training/API/museum.json");
        logger.info("");
        museumAPI.parser("/Users/azuga/Documents/Azuga Training/API/museum.csv");


    }
}
