

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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pipe {
    String answer = null;
    static final Logger logger = LogManager.getLogger(Pipe.class);

    /**
     * This method Verify the input number with head and tail commands.
     */
    private static boolean numberChecker(String storedValue, int num) {
        return num == 0 || storedValue.equals("") || storedValue.equals("Illegal Input");
    }

    /**
     * This method Verify the String with all other commands except head and tail.
     */

    private static boolean stringChecker(String storedValue) {
        if (storedValue.equals("") || storedValue.equals("Illegal Input")) {
            logger.error("INPUT ERROR");
            return true;
        }
        return false;
    }


    /**
     * Previous answer becomes input for head with -n.
     */

    private static String head(String storedValue, int num) {
        logger.info("input - " + storedValue);
        logger.info("head count - " + num);

        if (numberChecker(storedValue, num)) {
            logger.error("HEAD INPUT ERROR");
            return "Illegal Input";
        }
        StringBuilder data = new StringBuilder();
        String[] arr = storedValue.split("\n\r|\n|\r");

        if (num > arr.length)
            num = arr.length;
        for (int j = 0; j < num; j++) {
            if (j < num - 1) {
                data.append(arr[j]).append("\n");
                logger.trace("Head command - DATA - " + arr[j]);
            } else {
                data.append(arr[j]);
            }
        }
        logger.info("Head method executed");
        return data.toString();
    }

    /**
     * Previous answer becomes input for tail with -n.
     */

    private static String tail(String storedValue, int num) {
        logger.info("input - " + storedValue);
        logger.info("tail count - " + num);

        if (numberChecker(storedValue, num)) {
            logger.error("TAIL INPUT ERROR");
            return "Illegal Input";
        }
        StringBuilder data = new StringBuilder();
        String[] reverse = storedValue.split("\n\r|\n|\r");
        int length = reverse.length;

        if (num > reverse.length)
            num = reverse.length;
        for (int j = length - num; j < length; j++) {
            if (j < length - 1) {
                data.append(reverse[j]).append("\n");
                logger.debug("Tail Data - " + reverse[j]);
            } else {
                data.append(reverse[j]);
            }
        }
        logger.info("Tail method executed");
        return data.toString();
    }

    /**
     * Search file in pwd and return contents in form of String.
     */

    private static String cat(String path) throws IOException {
        String data;

        try {
            if (path != null) {
                File file = new File(path);
                if (file.exists()) {
                    logger.info("File Path- " + path);
                    data = Files.readString(Path.of(path));
                    logger.debug("DATA -" + data);
                }
                else {
                    return "File does not exist";
                }
            } else {
                logger.error("Path is null");
                return "Path is null";
            }
            logger.info("Cat executed");
        } catch (IOException e) {
            logger.error("IOException - " + e);
            return e.getMessage();
        }
        return data;
    }

    /**
     * List files in pwd.
     */
    private static String ls() {
        String path = System.getProperty("user.dir");
        logger.warn("System Default Path - " + path);
        File f = new File(path);
        String[] arr = f.list();

        StringBuilder data = new StringBuilder();
        for (int i = 0; i < Objects.requireNonNull(arr).length; i++) {
            if (arr[i].charAt(0) != '.')
                data.append(arr[i]).append("\n");
            logger.debug("LS -DATA - " + arr[i]);
        }
        logger.info("LS executed");
        return data.toString();
    }

    /**
     * Filter searches for a particular input, and displays all lines that contain that pattern.
     */

    private static String grep(String storedValue, String str) {
        logger.info("input -" + storedValue);
        if (stringChecker(storedValue)) {
            logger.error("grep input error");
            return "Illegal Input";
        }
        StringBuilder data = new StringBuilder();
        String[] arr = storedValue.split("\n\r|\n|\r");

        for (String s : arr) {
            if (s.contains(str))
                data.append(s).append("\n");
            logger.debug("grep append - " + s);
        }
        logger.info("grep executed");
        return data.toString();
    }

    /**
     * Filters out the repeated lines in answer variable.
     */
    private static String uniq(String storedValue) {
        logger.info("input- " + storedValue);
        String[] arr = storedValue.split("\n");
        Set<String> a = new TreeSet<>();                       //creating a set to get unique values in sorted order
        Collections.addAll(a, arr);
        StringBuilder s = new StringBuilder();
        for (String i : a) {
            if (!i.equals(arr[arr.length - 1])) {
                s.append(i).append("\n");
            } else {
                s.append(i);
            }

        }
        return s.toString();
    }

    /**
     * Number of lines, word count, byte/characters count of answer variable.
     */

    private static String wc(String storedValue) {
        logger.info("input - " + storedValue);
        if (stringChecker(storedValue)) {
            logger.error("wc input error");
            return "Illegal Input";
        }
        long wordCount, lineCount;
        String[] arr = storedValue.split("\n\r|\n|\r");
        lineCount = arr.length;

        Pattern p = Pattern.compile("[a-zA-Z0-9]+");
        Matcher m = p.matcher(storedValue);
        ArrayList<String> array = new ArrayList<>();
        while (m.find()) {
            logger.debug(m.group());
            array.add(m.group());
        }
        wordCount = array.size();

        String data = (lineCount == 0 ? 1 : lineCount - 1) + "\t" + wordCount + "\t" + storedValue.length();


        logger.info("wc -executed");
        return data;
    }

    /**
     * Sorts the contents of answer variable, line by line.
     */

    private static String mySort(String storedValue) {
        logger.info("input - " + storedValue);
        String[] arr = storedValue.split("\n");                             //split String by new lines.
        Arrays.sort(arr);                                               //sort array in alphabetic manner
        StringBuilder s = new StringBuilder();
        for (String i : arr) {
            if (!i.equals(arr[arr.length - 1])) {
                s.append(i).append("\n");
            } else {
                s.append(i);
            }

        }
        return s.toString();

    }

    /**
     * Select is used to call respective methods.
     */

    private String select(String str) throws IOException {


        String[] myList = str.split("\\s");
        logger.debug("Command - " + Arrays.toString(myList));

        switch (myList[0]) {
            case "cat": {
                logger.info("cat method invoked");
                answer = cat(myList[1]);
                logger.debug("cat result: " + answer);
            }
            break;
            case "head": {
                logger.info("head method invoked");
                String storedValue = answer;
                int num = Character.getNumericValue(myList[1].charAt(1));

                answer = head(storedValue, num);
                logger.debug("head result: " + answer);
            }
            break;
            case "tail": {
                logger.info("tail method invoked");
                String storedValue = answer;
                int num = Character.getNumericValue(myList[1].charAt(1));

                answer = tail(storedValue, num);
                logger.debug("tail result: " + answer);
            }
            break;
            case "ls": {
                logger.info("ls method invoked");
                answer = ls();
                logger.debug("ls result: " + answer);
            }
            break;
            case "wc": {
                logger.info("wc method invoked");
                String storedValue = answer;

                answer = wc(storedValue);
                logger.debug("wc result: " + answer);
            }
            break;
            case "sort": {
                logger.info("sort method invoked");
                String storedValue = answer;

                answer = mySort(storedValue);
                logger.debug("sort result: " + answer);
            }
            break;
            case "uniq": {
                logger.info("uniq method invoked");
                String storedValue = answer;

                answer = uniq(storedValue);
                logger.debug("uniq result: " + answer);
            }
            break;
            case "grep": {
                logger.info("grep method invoked");
                String storedValue = answer;

                answer = grep(storedValue, myList[1]);
                logger.debug("grep result: " + answer);
            }
            break;
            default:
                logger.error("command not matched");
                return "No such command exists";
        }
        return answer;

    }

    public String pipe(String args) {
        String result = null;
        Pipe pipe = new Pipe();
        try {
            logger.info("Pipe program invoked");
            logger.info("Command input from CLI");
            String[] command;
            ArrayList<String> myList;
            if(args != null) {
                command = args.split("\\|");
                myList = new ArrayList<>();
            }else{
                logger.error("CLI is null");
                return null;
            }
        /* First: Split the input from pipe " | " .
           leftTrim: Remove all the left spaces.
           rightTrim: Remove all right spaces.
           Center: Replace all middle spaces by single space.
        */
            logger.info("CLI input string manipulation");
            for (String s : command) {
                String leftTrim = s.replaceAll("^\\s+", "");
                String rightTrim = leftTrim.replaceAll("\\s+$", "");
                String center = rightTrim.replaceAll("\\s+", " ");
                myList.add(center);
                logger.debug("Pipe Command - " + center);
            }
            logger.debug("Method parameters - " + myList);
            // Evaluate all commands one by one.
            logger.info("Invoking switch case and passing parameter");
            int i = 0;
            while (i < myList.size()) {
                result = pipe.select(myList.get(i));
                i++;
            }
        } catch (IOException e) {
            logger.error("IOException - " + e.getMessage());
        }

        logger.debug("Result - " + result);
        logger.info("Pipe executed");
        return result;
    }

}