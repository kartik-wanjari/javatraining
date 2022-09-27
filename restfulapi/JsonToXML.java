
/*
 *
 *  * Copyright (c) 2022.  - All Rights Reserved
 *  *  * Unauthorized copying or redistribution of this file in source and binary forms via any medium
 *  *  * is strictly prohibited-
 *  *  * @Author -kartiks.
 *
 */

package restfulapi;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.*;


/**
 * This class creates XML file by taking input as Json File
 */
public class JsonToXML {

    private static Logger logger = LogManager.getLogger(JsonToXML.class);
    /**
     * This method convert Json data to Xml format.
     */
    static int k=0;
    private static String convertToXML(String jsonString, String root) throws JSONException {
        logger.info("collecting json: "+jsonString);
        JSONObject jsonObject = new JSONObject(jsonString);
        System.out.println(jsonObject);
        if(k==0) {
            logger.info("xml declaration");
            return "<?xml version=\"1.0\" encoding=\"ISO-8859-15\"?>\n<roots>\n\t<" + root + ">" + XML.toString(jsonObject) + "\t</" + root + ">";
        }
        else {
            logger.debug("Json to xml: "+XML.toString(jsonObject));
            return "\t\t" + XML.toString(jsonObject);
        }
    }

    public void jsonToXML() throws JSONException {

        String path = "/Users/azuga/Documents/Azuga Training/API/museum.json";
        logger.info("JSON to XML program initiated");
        logger.debug("Path of museum.json file: "+path);
        String result;
        try {

            result = new String(Files.readAllBytes(Paths.get(path)));
            logger.debug("");
            String json = result.replaceAll("}\\{","}},{{");
            String[] loopObj = json.split("},\\{");

            StringBuilder sb = new StringBuilder();
            for(int i = 0 ; i<6;i++,k++){

                String xmlValue = convertToXML(loopObj[i], "root");
                sb.append(xmlValue.replaceAll("><",">\n<"));
            }
            sb.append("\n</roots>");
            String res = sb.toString();

            FileWriter file = new FileWriter("/Users/azuga/Documents/Azuga Training/API/Json2Xml.xml");
            file.write(res);
            file.flush();
            file.close();

        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}