

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public class Pipe {

    static final Logger logger = LogManager.getLogger(Pipe.class);

    static String answer="";

    /**
     * This method Verify the input number with head and tail commands.
     */
    public static boolean numberChecker(String storedValue, int num)
    {
        return num == 0 || storedValue.equals("") || storedValue.equals("Illegal Input");
    }

    /**
     * This method Verify the String with all other commands except head and tail.
     */

    public static boolean stringChecker(String storedValue)
    {
        if(storedValue.equals("") || storedValue.equals("Illegal Input")) {
            logger.error("INPUT ERROR");
            return true;
        }
        return false;
    }


    /**
     * Previous answer becomes input for head with -n.
     */

    public static String head(String storedValue, int num)
    {
        logger.info("input - "+ storedValue);
        logger.info("head count - "+num);

        if(numberChecker(storedValue, num)) {
            logger.error("HEAD INPUT ERROR");
            return "Illegal Input";
        }
        StringBuilder data = new StringBuilder();
        String[] arr = storedValue.split("\n\r|\n|\r");

        if(num>arr.length)
            num= arr.length;
        for (int j = 0; j < num; j++) {
            data.append(arr[j]).append("\n");
            logger.trace("Head command - DATA - "+arr[j]);
        }
        logger.debug("");
        logger.info("Head method executed");
        return data.toString();
    }

    /**
     * Previous answer becomes input for tail with -n.
     */

    public static String tail(String storedValue, int num)
    {
        logger.info("input - "+ storedValue);
        logger.info("tail count - "+num);

        if(numberChecker(storedValue, num)) {
            logger.error("TAIL INPUT ERROR");
            return "Illegal Input";
        }
        StringBuilder data= new StringBuilder();
        String[] reverse= storedValue.split("\n\r|\n|\r");
        int length=reverse.length;

        if(num>reverse.length)
            num= reverse.length;
        for(int j=length-num; j<length; j++) {
            data.append(reverse[j]).append("\n");
            logger.debug("Tail Data - "+reverse[j]);
        }
        logger.info("Tail method executed");
        return data.toString();
    }

    /**
     * Search file in pwd and return contents in form of String.
     */

    public static String cat(String filename) throws IOException {
        String data;
        try {
            String path;
            if(filename == null) {
                path = System.getProperty("user.dir");
            }else {
                path = filename;
            }
            logger.info("File Path- " + path);
            data = new String(Files.readAllBytes(Path.of(path)));
            logger.debug("DATA -" + data);
            logger.info("Cat executed");
        } catch (IOException e) {
            logger.error("CAT INPUT ERROR - "+e);
            throw new IOException("Cat Input error");
        }
        return data;
    }

    /**
     * List files in pwd.
     */

    public static String ls()
    {
        String path=System.getProperty("user.dir");
        logger.warn("System Default Path - "+path);
        File f = new File(path);
        String[] arr =f.list();

        StringBuilder data= new StringBuilder();
        for(int i = 0; i< Objects.requireNonNull(arr).length; i++) {
            if(arr[i].charAt(0)!='.')
                data.append(arr[i]).append("\n");
            logger.debug("LS -DATA - "+arr[i]);
        }
        logger.info("LS executed");
        return data.toString();
    }

    /**
     * Filter searches for a particular input, and displays all lines that contain that pattern.
     */

    public static String grep(String storedValue,String str)
    {
        logger.info("input -"+storedValue);
        if(stringChecker(storedValue)) {
            logger.error("grep input error");
            return "Illegal Input";
        }
        StringBuilder data = new StringBuilder();
        String[] arr = storedValue.split("\n\r|\n|\r");

        for (String s : arr) {
            if (s.contains(str))
                data.append(s).append("\n");
            logger.debug("grep append - "+s);
        }
        logger.info("grep executed");
        return data.toString();
    }

    /**
     * Filters out the repeated lines in answer variable.
     */

    public static String uniq(String storedValue)
    {
        logger.info("input- "+storedValue);
        if(stringChecker(storedValue)) {
            logger.error("uniq - input error");
            return "Illegal Input";
        }
        StringBuilder data = new StringBuilder();
        String[] arr = storedValue.split("\n\r|\n|\r");

        data.append(arr[0]).append("\n");
        for(int i=1;i<arr.length-1;i++) {
            if(arr[i].equals(arr[i-1]))
                continue;
            else
                data.append(arr[i]).append("\n");
            logger.debug("uniq append - "+arr[i]);
        }
        logger.info("uniq executed");
        return data.toString();
    }

    /**
     * Number of lines, word count, byte/characters count of answer variable.
     */

    public static String wc(String storedValue)
    {
        logger.info("input - "+storedValue);
        if(stringChecker(storedValue)) {
            logger.error("wc input error");
            return "Illegal Input";
        }
        long wordCount,lineCount,characters;
        characters=0;
        String[] arr = storedValue.split("\n");
        lineCount=arr.length;

        arr=storedValue.split(" ");
        wordCount=arr.length;

        for(int i = 0; i < storedValue.length(); i++) {
            characters++;
            logger.debug("wc - data - "+characters);
        }
        String data="";
        data=data+lineCount+" "+wordCount+" "+characters;

        logger.info("wc -executed");
        return data;
    }

    /**
     * Sorts the contents of answer variable, line by line.
     */

    public static String mySort(String storedValue)
    {
        logger.info("input - "+storedValue);
        if(stringChecker(storedValue)) {
            logger.error("sort input error");
            return "Illegal Input";
        }
        StringBuilder data = new StringBuilder();
        String[] arr = storedValue.split("\n\r|\n|\r");

        Arrays.sort(arr, Comparator.comparingInt(str -> str.charAt(0)));

        for (String s : arr) {
            data.append(s).append("\n");
            logger.debug("sort - "+s);
        }
        logger.info("sort executed");
        return data.toString();
    }

    /**
     * Select is used to call respective methods.
     */

    public static void select(String str) throws Exception {


        String[] myList = str.split("\\s");
        logger.debug("Command - "+ Arrays.toString(myList));

        switch(myList[0])
        {
            case "cat": {  logger.info("cat method invoked"); answer="";
                answer += cat(myList[1]);
                logger.debug("cat result: "+answer);
            }
            break;
            case "head":{ logger.info("head method invoked");  String storedValue =answer;
                int num=Character.getNumericValue(myList[1].charAt(1));
                answer="";
                answer+=head(storedValue,num);
                logger.debug("head result: "+answer);
            }
            break;
            case "tail": {  logger.info("tail method invoked"); String storedValue =answer;
                int num=Character.getNumericValue(myList[1].charAt(1));
                answer="";
                answer+=tail(storedValue,num);
                logger.debug("tail result: "+answer);
            }
            break;
            case "ls": {  logger.info("ls method invoked");  answer="";
                answer+=ls();
                logger.debug("ls result: "+answer);
            }
            break;
            case "wc": { logger.info("wc method invoked");   String storedValue=answer;
                answer="";
                answer+=wc(storedValue);
                logger.debug("wc result: "+answer);
            }
            break;
            case "sort": { logger.info("sort method invoked");  String storedValue=answer;
                answer="";
                answer+= mySort(storedValue);
                logger.debug("sort result: "+answer);
            }
            break;
            case "uniq": { logger.info("uniq method invoked");  String storedValue=answer;
                answer="";
                answer+=uniq(storedValue);
                logger.debug("uniq result: "+answer);
            }
            break;
            case "grep":{ logger.info("grep method invoked");  String storedValue=answer;
                answer="";
                answer += grep(storedValue,myList[1]);
                logger.debug("grep result: "+answer);
            }
            break;
            default:
                System.out.println("No such command exists");
                logger.error("command not matched");
        }

    }


    public static void main(String[] args)  {

        try {
            logger.info("Pipe program invoked");
            logger.info("Command input from CLI");
            String[] command = args[0].split("\\|");
            ArrayList<String> myList = new ArrayList<>();
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
                logger.debug("Pipe Command - "+center);
            }
            logger.debug("Method parameters - "+myList);
            // Evaluate all commands one by one.
            logger.info("Invoking switch case and passing parameter");
            int i = 0;
            while (i < myList.size()) {
                select(myList.get(i));
                i++;
                logger.debug("Executing -PIPE - "+myList.get(i));
            }


        }
        catch (Exception e){
            logger.error("INPUT ERROR - main - "+e.getMessage());
        }
        System.out.println(answer);
        logger.debug("Result - "+answer);
        logger.info("Pipe executed");
    }
}