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
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

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
    static int k = 0;

    /**
     * This method provide xml configuration
     * @param jsonString json data
     * @param root xml tag
     * @return xml format from json
     * @throws JSONException thrown when json is not in correct format
     */
    private static String convertToXML(String jsonString, String root) throws JSONException {

        JSONObject jsonObject = new JSONObject(jsonString);
        System.out.println(jsonObject);
        if (k == 0) {
            return "<?xml version=\"1.0\" encoding=\"ISO-8859-15\"?>\n<roots>\n\t<" + root + ">" + XML.toString(jsonObject) + "\t</" + root + ">";
        } else {
            return "\t\t" + XML.toString(jsonObject);
        }
    }

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

                writer.write(line + "\n");
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    /**
     * This method convert csv file to pdf format file
     *
     * @param path museum.csv file path
     * @throws IOException thrown when path of file incorrect
     */
    @Override
    public void csvToPdf(String path) throws IOException {
        CSVReader reader;
        try {
            reader = new CSVReader(new FileReader(path));//reading csv file from given path
        } catch (FileNotFoundException e) {

            throw new RuntimeException(e);
        }
        String[] nextLine;
        Document my_pdf_data = new Document();

        Rectangle rc = new Rectangle(8300f, 8000f);
        my_pdf_data.setPageSize(rc);

        try {
            PdfWriter.getInstance(my_pdf_data, new FileOutputStream("/Users/azuga/Desktop/museum2.pdf"));//writing pdf file to given path
        } catch (DocumentException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        my_pdf_data.open();
        PdfPTable my_first_table = new PdfPTable(78);
        PdfPCell table_cell;
        while ((nextLine = reader.readNext()) != null) {
            int i = 0;

            while (i <= 71) {
                table_cell = new PdfPCell(new Phrase(nextLine[i]));
                my_first_table.addCell(table_cell);
                i++;
            }
        }
        try {
            my_pdf_data.add(my_first_table);
        } catch (DocumentException e) {

            throw new RuntimeException(e);
        }
        my_pdf_data.close();
    }

    /**
     * This method convert json to XML
     *
     * @param path museum.json file path
     */
    @Override
    public void jsonToXML(String path) {


        String result;
        try {
            result = new String(Files.readAllBytes(Path.of(path)));
            String json = result.replaceAll("}\\{", "}},{{");
            String[] loopObj = json.split("},\\{");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 6; i++, k++) {
                String xmlValue = convertToXML(loopObj[i], "root");
                sb.append(xmlValue.replaceAll("><", ">\n<"));
            }
            sb.append("\n</roots>");
            String res = sb.toString();
            FileWriter fileWriter = new FileWriter("/Users/azuga/Desktop/Json2Xml.xml");
            fileWriter.write(res);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
