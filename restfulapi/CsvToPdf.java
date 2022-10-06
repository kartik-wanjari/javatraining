/*
 *
 *  * Copyright (c) 2022.  - All Rights Reserved
 *  *  * Unauthorized copying or redistribution of this file in source and binary forms via any medium
 *  *  * is strictly prohibited-
 *  *  * @Author -kartiks.
 *
 */

package restfulapi;

import java.io.FileOutputStream;
import java.io.*;
import au.com.bytecode.opencsv.CSVReader;
import java.io.FileReader;
import java.util.Arrays;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class convert csv file to pdf format file
 */
public class CsvToPdf {

    private static final Logger logger = LogManager.getLogger(CsvToPdf.class.getName());
    public void csvToPdf() throws IOException {
        CSVReader reader;
        try {
            reader = new CSVReader(new FileReader("/Users/azuga/Desktop/museum1.csv"));//reading csv file from given path
        } catch (FileNotFoundException e) {
            logger.error("Exception "+e.getMessage());
            throw new RuntimeException(e);
        }
        String[] nextLine;
        Document pdfData = new Document();

        Rectangle rc = new Rectangle(8300f,8000f);
        pdfData.setPageSize(rc);

        try {
            PdfWriter.getInstance(pdfData, new FileOutputStream("/Users/azuga/Desktop/museum2.pdf"));//writing pdf file to given path
        } catch (DocumentException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        pdfData.open();
        PdfPTable table = new PdfPTable(78);
        PdfPCell tableCell;
        while ((nextLine = reader.readNext()) != null) {
            int i = 0;
            logger.debug("Reading from csv file "+ Arrays.toString(nextLine));
            while (i <= 77) {
                tableCell = new PdfPCell(new Phrase(nextLine[i]));
                table.addCell(tableCell);
                i++;
            }
        }
        try {
            pdfData.add(table);
        } catch (DocumentException e) {
            logger.error("Exception "+e.getMessage());
            throw new RuntimeException(e);
        }
        pdfData.close();
    }
}
