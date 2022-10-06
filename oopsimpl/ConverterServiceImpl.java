/*
 *
 *  * Copyright (c) 2022.  - All Rights Reserved
 *  *  * Unauthorized copying or redistribution of this file in source and binary forms via any medium
 *  *  * is strictly prohibited-
 *  *  * @Author -kartiks.
 *
 */

package oopsimpl;


import au.com.bytecode.opencsv.CSVReader;
import com.github.underscore.U;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import java.util.List;

/**
 *
 */
public class ConverterServiceImpl implements ConverterService {
    public static Logger logger = LogManager.getLogger(ChartsServiceImpl.class);

    /**
     * This method convert csv formatted file to html format
     * @param file csv file
     */
    @Override
    public void csvToHtml(File file) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {//reading the file here
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String s2 = currentLine.replaceAll("\"\"", "null");
                String s3 = s2.replaceAll("\"", "");
                String s = s3.replaceAll("https://images.metmuseum.org/CRDImages", "<img src=https\\://images.metmuseum.org/CRDImages");
                String s1 = s.replaceAll("jpg", "jpg style=\"width:100px;height:100px;\" >");
                lines.add(s1);
            }
        } catch (IOException e) {
            logger.error("IOException - "+e);
        }
        for (int i = 0; i < lines.size(); i++) {
            lines.set(i, "<tr><td>" + lines.get(i) + "</td></tr>");
            lines.set(i, lines.get(i).replaceAll(",", "</td><td>"));
        }
        lines.set(0, "<table border>" + lines.get(0));
        lines.set(lines.size() - 1, lines.get(lines.size() - 1) + "</table>");

        try (FileWriter writer = new FileWriter("/Users/azuga/Desktop/museum3.html")) {//writing String to html file
            for (String line : lines) {

                writer.write(line + "\n");
            }
        } catch (IOException e) {
            logger.error("IOException - "+e);
        }
    }

    /**
     * This method convert csv file to pdf format file
     *
     * @param path museum.csv file path
     */
    @Override
    public void csvToPdf(String path) {
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(path));                   //reading csv file from given path
        } catch (FileNotFoundException e) {
            logger.error("FileNotFoundException - "+e);
        }
        String[] nextLine;
        Document pdfData = new Document();

        Rectangle rc = new Rectangle(8300f, 8000f);
        pdfData.setPageSize(rc);

        try {
            PdfWriter.getInstance(pdfData, new FileOutputStream("/Users/azuga/Desktop/museum2.pdf"));       //writing pdf file to given path
        } catch (DocumentException e) {
            logger.error("DocumentException - "+e);
        }catch (FileNotFoundException e){
            logger.error("FileNotFoundException - "+e);
        }
        pdfData.open();
        PdfPTable table = new PdfPTable(72);
        PdfPCell tableCell;
        try {
            while ((nextLine = reader != null ? reader.readNext() : null) != null) {
                int i = 0;

                while (i <= 71) {
                    tableCell = new PdfPCell(new Phrase(nextLine[i]));
                    table.addCell(tableCell);
                    i++;
                }
            }
        }catch (IOException e){
            logger.error("IOException -"+e);
        }
        try {
            pdfData.add(table);
        } catch (DocumentException e) {
            logger.error("DocumentException -"+e);
        }
        pdfData.close();
    }

    /**
     * This method convert json to XML
     *
     * @param path museum.json file path
     */
    @Override
    public void jsonToXML(String path) {
        String json = null;
        try {
            logger.info("Reading json from museum.json");
            json = Files.readString(Path.of(path));
        }catch (IOException e){
            logger.error("IOException -"+e);
        }
        logger.info("Converting json to xml");
        String xml = U.jsonToXml(json);
        FileWriter file;
        try {
            logger.info("Writing xml into museum.xml");
            file = new FileWriter("/Users/azuga/Documents/Azuga Training/API/Json2XML.xml");
            file.write(xml);
            file.flush();
            file.close();
        }catch (IOException e){
            logger.error("IOException - "+e);
        }
    }
}
