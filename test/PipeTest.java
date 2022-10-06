/*
 *
 *  * Copyright (c) 2022.  - All Rights Reserved
 *  *  * Unauthorized copying or redistribution of this file in source and binary forms via any medium
 *  *  * is strictly prohibited-
 *  *  * @Author -kartiks.
 *
 */

package test;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import weekone.Pipe;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class test pipe command
 */
public class PipeTest {
    public static Logger logger = LogManager.getLogger(PipeTest.class);
    Pipe pipe = new Pipe();

    /**
     * This method test cat command.
     */
    @Test
    void testCat() {
        logger.info("cat test invoked");
        try {
            String expected = Files.readString(Path.of("/Users/azuga/Desktop/robots.txt"));
            String actual = pipe.pipe("cat /Users/azuga/Desktop/robots.txt");

            assertEquals(actual, expected);
            logger.info("cat test case passed");
        } catch (IOException e) {
            logger.error("IOException - " + e);
        }
    }
    /**
     * This method test cat and sorting command.
     */
    @Test
    void testCatSort() {
        logger.info("cat and sort test invoked");
        String expected = "Bhim\nBhindu\nBindu\nGagan\nGanesh\nGanesh\nJaya\nKartik\nKartik\nRajesh\nRakesh\nRamesh\nShankar\nZoya";
        String actual = pipe.pipe("cat /Users/azuga/Desktop/sortlist.txt | sort");
        assertEquals(actual, expected);
        logger.info("cat and sort test case passed");
    }
    /**
     * This method test cat, sorting and unique command.
     */
    @Test
    void testCatSortUniq() {
        logger.info("cat, sort and unique command test invoked");
        String expected = "Bhim\nBhindu\nBindu\nGagan\nGanesh\nJaya\nKartik\nRajesh\nRakesh\nRamesh\nShankar\nZoya";
        String actual = pipe.pipe("cat /Users/azuga/Desktop/sortlist.txt | sort | uniq");
        assertEquals(actual, expected);
        logger.info("cat, sort and unique command test case passed");
    }
    /**
     * This method test cat and wc command.
     */
    @Test
    void testCatWc() {
        logger.info("cat and wc test case invoked");
        String expected = "10\t151\t960";
        String actual = pipe.pipe("cat /Users/azuga/Desktop/robots.txt | wc");

        assertEquals(actual, expected);
        logger.info("cat and wc test case passed");
    }
    /**
     * This method test cat and head command.
     */
    @Test
    void testCatHead() {
        logger.info("cat and head test case invoked");
        String expected = "Ganesh\nRakesh\nBindu\nBhim";
        String actual = pipe.pipe("cat /Users/azuga/Desktop/sortlist.txt | head -4");

        assertEquals(expected, actual);
        logger.info("cat and head test case passed");
    }
    /**
     * This method test cat and tail command.
     */
    @Test
    void testCatTail() {
        logger.info("cat and tail test case invoked");
        String expected = "Ganesh\nRajesh\nGagan\nZoya";
        String actual = pipe.pipe("cat /Users/azuga/Desktop/sortlist.txt | tail -4");

        assertEquals(expected, actual);
        logger.info("cat and tail test case passed");
    }
    /**
     * This method test cat, head and tail command.
     */
    @Test
    void testCatHeadTail() {
        logger.info("cat, head and tail case invoked");
        String expected = "Bindu\nBhim\nJaya";
        String actual = pipe.pipe("cat /Users/azuga/Desktop/sortlist.txt | head -5 | tail -3");

        assertEquals(expected, actual);
        logger.info("cat, head and tail case passed");
    }
    /**
     * This method test passing null parameter.
     */
    @Test
    void testNullPath() {
        logger.info("null path test case invoked");
        Pipe p = new Pipe();
        String actual = p.pipe(null);
        assertNull(actual);
        logger.info("null path test case passed");
    }
    /**
     * This method test invalid command input parameter.
     */
    @Test
    void testNoCommandExists() {
        logger.info("invalid command test case invoked");
        Pipe p = new Pipe();
        String actual = p.pipe("kartik w");
        assertEquals("No such command exists", actual);
        logger.info("invalid command test case passed");
    }
    /**
     * This method test file not found exception.
     */
    @Test
    void testFileNotFound() {
        logger.info("file check test case invoked");
        Pipe p = new Pipe();
        String actual = p.pipe("cat dummy.txt");
        assertEquals("File does not exist", actual);
        logger.info("file check test case passed");
    }

}
