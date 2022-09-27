/*
 *
 *  * Copyright (c) 2022.  - All Rights Reserved
 *  *  * Unauthorized copying or redistribution of this file in source and binary forms via any medium
 *  *  * is strictly prohibited-
 *  *  * @Author -kartiks.
 *
 */

package weekone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Wc {

    public static Logger logger = LogManager.getLogger(Wc.class);

    /**
     * This method finds file.
     *
     * @param path destination path of file
     * @return file
     * @throws FileNotFoundException thrown when file not found
     * @throws IOException           thrown when path is invalid
     */
    public static File getFile(String path) throws IOException {
        File file = null;
        try {
            if (path != null) {
                file = new File(path);
            } else {
                throw new IOException("IOException: pass path of file");
            }
        } catch (IOException e) {
            logger.error("IOException: file {} null or invalid",path);
            System.err.println(e.getMessage());
        }
        return file;
    }

    /**
     * This method print line count, word count and character count of file.
     *
     * @param path -path of file
     * @throws IOException thrown when path invalid
     */
    public static void simpleWc(String path) throws IOException {

        File file = getFile(path);
        //read file content
        String content = Files.readString(Paths.get(path));
        logger.debug("File content : " + content);
        int l = 0;
        //alphabetic regular expression
        logger.info("Using Pattern and matcher to collect words from file content for number of word count");
        Pattern p = Pattern.compile("[a-zA-Z]+");
        Matcher m = p.matcher(content);
        ArrayList<String> arr = new ArrayList<>();
        while (m.find()) {
            logger.debug(m.group());
            arr.add(m.group());
        }
        logger.info("Line count in file " + file.getName());
        FileReader f = new FileReader(path);
        BufferedReader br = new BufferedReader(f);
        //counting number of lines
        while (br.readLine() != null) {
            l++;
        }
        logger.info("Character count using length of content");
        System.out.print((l == 1 ? 0 : l) + "\t" + arr.size() + "\t" + content.length() + "\t" + file.getName());
    }

    /**
     * This method print line count of file.
     *
     * @param path -path of file
     * @throws IOException thrown when path invalid
     */
    public static void wc_l(String path) throws IOException {

        File file = getFile(path);
        logger.info("Line count of text file: "+file.getName());
        FileReader f = new FileReader(path);
        BufferedReader br = new BufferedReader(f);
        int l = 0;
        while (br.readLine() != null) {
            l++;
        }
        System.out.print((l == 1 ? 0 : l) + "\t" + file.getName());
    }

    /**
     * This method print character count of file.
     *
     * @param path -path of file
     * @throws IOException thrown when path invalid
     */
    public static void wc_m(String path) throws IOException {
        File file = getFile(path);
        logger.info("Character count of file: "+file.getName());
        String content = Files.readString(Paths.get(path));
        System.out.print(content.length() + "\t" + file.getName());
    }

    /**
     * This method print word count of file.
     *
     * @param path -path of file
     * @throws IOException thrown when path invalid
     */
    public static void wc_w(String path) throws IOException {
        File file = getFile(path);
        String content = Files.readString(Paths.get(path));
        //pattern for only alphabetic words.
        logger.info("Using Pattern and matcher to collect words from file content for number of word count");
        Pattern p = Pattern.compile("[a-zA-Z]+");
        Matcher m = p.matcher(content);
        ArrayList<String> arr = new ArrayList<>();
        while (m.find()) {
            logger.debug("word "+m.group());
            arr.add(m.group());
        }
        System.out.print(arr.size() + "\t" + file.getName());
    }

    /**
     * This method print line with maximum length
     *
     * @param path -path of file
     * @throws IOException thrown when path invalid
     */
    public static void wc_L(String path) throws IOException {
        logger.info("line with maximum length");
        File file = getFile(path);
        FileReader f = new FileReader(path);
        BufferedReader br = new BufferedReader(f);
        String line;
        String maxLine = "";
        while ((line = br.readLine()) != null) {
            if (line.length() > maxLine.length()) {
                logger.debug("Line: "+line);
                maxLine = line;
            }
        }
        System.out.print(maxLine.length() + "\t" + file.getName());
    }

    public static void main(String[] args) throws IOException {
        logger.info("Input from CLI: " + args[0] + " " + args[1]);
        logger.info("Invoking method through switch case");
        logger.debug("case: " + args[0]);
        switch (args[0]) {
            case "wc":
                logger.debug("simpleWc Method parameter: " + args[1]);
                simpleWc(args[1]);
                break;
            case "-l":
                logger.debug("wc-l Method parameter: " + args[1]);
                wc_l(args[1]);
                break;
            case "-m":
                logger.debug("wc-m Method parameter: " + args[1]);
                wc_m(args[1]);
                break;
            case "-w":
                logger.debug("wc-w Method parameter: " + args[1]);
                wc_w(args[1]);
                break;
            case "-L":
                logger.debug("wc-L Method parameter: " + args[1]);
                wc_L(args[1]);
                break;
            default:
                logger.error(args[0] + " command exists");
                System.out.println("No such command exists");
        }
        System.out.println("program ended");
    }
}
