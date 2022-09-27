/*
 *
 *  * Copyright (c) 2022.  - All Rights Reserved
 *  *  * Unauthorized copying or redistribution of this file in source and binary forms via any medium
 *  *  * is strictly prohibited-
 *  *  * @Author -kartiks.
 *
 */

package restfulapi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class converts csv file to html file
 */
public class CsvToHtml {

    private static final Logger logger = LogManager.getLogger(CsvToHtml.class.getName());
    public void csvToHtml() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("/Users/azuga/Desktop/museum1.csv"))) {//reading the file here
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {

                logger.debug("Data before manipulation"+currentLine);
                String s2 = currentLine.replaceAll("\"\"","null");
                String s3 = s2.replaceAll("\"","");
                String s=s3.replaceAll("https://images.metmuseum.org/CRDImages","<img src=https\\://images.metmuseum.org/CRDImages");
                String s1=s.replaceAll("jpg","jpg style=\"width:100px;height:100px;\" >");
                logger.debug("Data after manipulation"+s1);
                lines.add(s1);
            }
        } catch (IOException e) {
            logger.error("Exception "+e.getMessage());
            e.printStackTrace();
        }
        for (int i = 0; i < lines.size(); i++) {
            lines.set(i, "<tr><td>" + lines.get(i) + "</td></tr>");
            lines.set(i, lines.get(i).replaceAll(",", "</td><td>"));
        }
        lines.set(0, "<table border>" + lines.get(0));
        lines.set(lines.size() - 1, lines.get(lines.size() - 1) + "</table>");

        try (FileWriter writer = new FileWriter("/Users/azuga/Desktop/museum3.html")) {//writing String to html file
            for (String line : lines) {
                logger.debug("data which is converted to html form "+line);
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            logger.error("Exception "+e.getMessage());
            e.printStackTrace();
        }
    }
}