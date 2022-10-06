
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
import java.nio.file.Path;
import com.github.underscore.U;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class creates XML file by taking input as Json File
 */
public class JsonToXML {

    private static Logger logger = LogManager.getLogger(JsonToXML.class);
    /**
     * This method convert Json data to Xml format.
     */
    public void jsonToXML() {
        String json = null;
        try {
            logger.info("Reading json from museum.json");
            json = Files.readString(Path.of("/Users/azuga/Desktop/museum.json"));
        }catch (IOException e){
            e.printStackTrace();
        }
        logger.info("Converting json to xml");
        String xml = U.jsonToXml(json);
        FileWriter file;
        try {
            logger.info("Writing xml into museum.xml");
            file = new FileWriter("/Users/azuga/Documents/Azuga Training/API/Json2Xml.xml");
            file.write(xml);
            file.flush();
            file.close();
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
