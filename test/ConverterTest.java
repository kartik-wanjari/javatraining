/*
 *
 *  * Copyright (c) 2022.  - All Rights Reserved
 *  *  * Unauthorized copying or redistribution of this file in source and binary forms via any medium
 *  *  * is strictly prohibited-
 *  *  * @Author -kartiks.
 *
 */

package test;

import com.pdfunit.AssertThat;
import oopsimpl.ConverterService;
import oopsimpl.ConverterServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class test csv to html and pdf and json to xml method.
 */
public class ConverterTest {
    public static Logger logger = LogManager.getLogger(ConverterTest.class);
    ConverterService cs = new ConverterServiceImpl();

    /**
     * This method test existence of pdf file.
     */
    @BeforeEach
    @Test
    void testPdfExistence() {
        logger.info("Invoking pdf existence method");
        cs.csvToPdf("/Users/azuga/Documents/Azuga Training/API/museum.csv");
        File file = new File("/Users/azuga/Documents/Azuga Training/API/museum.pdf");
        assertTrue(file.exists());
        logger.info("test case passes - museum.pdf exist");
    }

    /**
     * This method test number of pages of pdf file.
     */
    @Test
    void testPdfForPages() {
        logger.info("Invoking test pdf for pages");
        AssertThat.document("/Users/azuga/Documents/Azuga Training/API/museum.pdf").hasNumberOfPages(1);
        logger.info("Test case passed - pdf pages");
    }

    /**
     * This method test content of pdf files.
     */
    @Test
    void testPdfForContent() {
        logger.info("Invoking test pdf for content");
        cs.csvToPdf("/Users/azuga/Documents/Azuga Training/API/museum.csv");
        AssertThat.document("/Users/azuga/Documents/Azuga Training/API/museum.pdf").and("/Users/azuga/Desktop/Golden/museum.pdf").haveSameText();
        logger.info("Test case passed - pdf content matched");
    }

    /**
     * This method test existence of html file.
     */
    @BeforeEach
    @Test
    void testHtmlExist() {
        logger.info("Invoking test pdf for pages");
        cs.csvToHtml(new File("/Users/azuga/Documents/Azuga Training/API/museum.csv"));
        File file = new File("/Users/azuga/Documents/Azuga Training/API/CSV2HTML.html");
        assertTrue(file.exists());
        logger.info("test case passes - CSV2HTML.html exist");
    }

    /**
     * This method test existence of xml file.
     */
    @BeforeEach
    @Test
    void testXmlExist() {
        logger.info("Invoking test xml existence");
        cs.jsonToXML("/Users/azuga/Documents/Azuga Training/API/museum for xml.json");
        File file = new File("/Users/azuga/Documents/Azuga Training/API/Json2XML.xml");
        assertTrue(file.exists());
        logger.info("test case passes - Json2XML.xml exist");
    }

    /**
     * This method test html content of museum csv to html file.
     */
    @Test
    void testCsvToHtml() {
        logger.info("Invoking test csv to html");
        cs.csvToHtml(new File("/Users/azuga/Documents/Azuga Training/API/museum.csv"));
        CheckSum sum = new CheckSum();
        try {
            logger.info("Checking excepted and actual html files using CheckSum");
            String expected = sum.checkSum("/Users/azuga/Desktop/Golden/CSV2HTML.html");
            String actual = sum.checkSum("/Users/azuga/Documents/Azuga Training/API/CSV2HTML.html");
            assertEquals(expected, actual);
            logger.info("Test case passed - html file matched");
        } catch (NoSuchAlgorithmException e) {
            logger.error("NoSuchAlgorithmException - " + e);
        } catch (IOException e) {
            logger.error("IOException - " + e);
        }

    }

    /**
     * This method test html content of museum csv to xml file.
     */
    @Test
    void testJsonToXml() {
        cs.jsonToXML("/Users/azuga/Documents/Azuga Training/API/museum for xml.json");
        CheckSum sum = new CheckSum();
        try {
            logger.info("Checking excepted and actual xml files using CheckSum");
            String expected = sum.checkSum("/Users/azuga/Desktop/Golden/Json2XML.xml");
            String actual = sum.checkSum("/Users/azuga/Documents/Azuga Training/API/Json2XML.xml");
            assertEquals(expected, actual);
            logger.info("Test case passed - xml file matched");
        } catch (NoSuchAlgorithmException e) {
            logger.error("NoSuchAlgorithmException - " + e);
        } catch (IOException e) {
            logger.error("IOException - " + e);
        }
    }
}
