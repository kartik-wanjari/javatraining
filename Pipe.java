/*
 * Copyright (c) 2022.  - All Rights Reserved
 * Unauthorized copying or redistribution of this file in source and binary forms via any medium
 * is strictly prohibited-
 * @Author -kartiks@azuga.com.
 */


import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class Pipe {
    /**
     * It prints the data which is present in the given file on to the console
     * @param path -It is used to take the input file path
     * @return -returns the whole file content as a string
     */
    public static String cat(String path) throws IOException {
        return Files.readString(Path.of(path));
    }

    /**
     * This method counts line, word and character count
     * @param str -It is used to take the String  aas input
     * @return line count, word count and character count and file name
     */
    public static String wc(String str)  {
        int line = 0;
        Pattern p = Pattern.compile("[a-zA-Z]+");       //compile the regular expression into pattern
        Matcher m = p.matcher(str);                           //match the pattern with string
        ArrayList<String> arr = new ArrayList<>();
        while (m.find()) {                                    //returns boolean when pattern matches.
            arr.add(m.group());                               //adding matched output into ArrayList.

        }

        Scanner scanner = new Scanner(str);
        while (scanner.hasNextLine()) {                       //read user input
            scanner.nextLine();
            line++;

        }
        scanner.close();
        return line + "\t" + arr.size() + "\t" + str.length();
    }

    /**
     * This method is used sort the given input and prints the output to the console
     * @param str -this is used to take the input string
     */
    public static String sort(String str) {
        String[] arr = str.split("\n");                             //split String by new lines.
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
     * This method returns the single output from the file containing repeated values.
     * @param str - it is used to take the input from the user as command line argument
     */
    public static String unique(String str) {
        String[] arr = str.split("\n");
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
     * This method returns output containing word input from user
     * @param str - It is used to take the input from the user as command line argument
     * @param word - It is used to select lines containing the input string.
     */
    public static String grep(String str, String word) {
        String[] arr = str.split("\n\r|\r|\n");                     //split string by new line
        ArrayList<String> ar = new ArrayList<>(Arrays.asList(arr));         //storing string array into a array list
        StringBuilder s = new StringBuilder();
        for (String i : ar) {                                              //getting values from array list
            if (i.contains(word)) {                                     //checking if input variable in string
                s.append(i).append("\n");
            }
        }
        return s.toString();
    }

    /**
     * This method returns "i" number of lines from the top from the string.
     * @param str - it is used to take the input from the user as command line argument
     * @param i - It is used to select lines from the string from the top.
     */
    public static String head(String str, int i) {
        ArrayList<String> ar = new ArrayList<>(Arrays.asList(str.split("\n\r|\n|\r")));

        StringBuilder h = new StringBuilder();
        for(int a = 0 ; a<=(i-1);a++) {
            h.append(ar.get(a)).append("\n");
        }
        return h.toString();

    }

    /**
     * This method returns the specific lines from the bottom of string
     * @param str - it is used to take the input from the user as command line argument
     * @param i - it is used for specifying the select number lines from bottom
     */
    public static String tail(String str, int i) {

        ArrayList<String> ar = new ArrayList<>(Arrays.asList(str.split("\n\r|\n|\r")));
        StringBuilder h = new StringBuilder();
        for (int a = ar.size() - i; a < ar.size(); a++) {
            h.append(ar.get(a)).append("\n");
        }
        return h.toString();
    }


    
    public static void main(String[] args) throws IOException {


        String[] a = args[0].split("\\|");          //collecting CLI in string array by space split
        if ((a.length == 6)&&(a[0].equals("cat") && a[2].equals("|"))
                && (a[3].equals("sort") && a[4].equals("|") && a[5].equals("uniq"))) {
            System.out.print(unique(sort(cat(a[1]))));

        }
        else if (a[0].equals("cat") && a[2].equals("|") && (a.length<=5) ) {
            switch (a[3]) {
                case "wc": {
                    System.out.print(wc(cat(a[1])));
                    break;
                }
                case "sort":
                    System.out.print(sort(cat(a[1])));
                    break;
                case "uniq":
                    System.out.print(unique(cat(a[1])));
                    break;
                case "grep":
                    System.out.print(grep(cat(a[1]), a[4]));
                    break;
                case "head":{
                    int m = Character.getNumericValue(a[4].charAt(1));
                    System.out.print(head(cat(a[1]),m));
                    break;
                }
                case "tail":{
                    int m = Character.getNumericValue(a[4].charAt(1));
                    System.out.print(tail(cat(a[1]),m));
                    break;
                }
                default:
                    System.out.print("I no input");
            }

        }

        else if(a[0].equals("cat") && a[3].equals("head") && a[6].equals("tail")) {

            int m = Character.getNumericValue(a[4].charAt(1));
            int n = Character.getNumericValue(a[7].charAt(1));
            System.out.print(tail(head(cat(a[1]),m),n));
        }
        
        else{
            System.out.print("no code exe");
        }
        

        }

    }






