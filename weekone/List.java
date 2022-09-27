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
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermissions;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;


/**
 * This class mimics linux wc command and its options.
 */
public class List {
    public static Logger logger = LogManager.getLogger(List.class);
    private static SimpleDateFormat pdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    /**
     * This method finds file.
     *
     * @param path destination path of file
     * @return file
     */
    public static File[] getFileList(String path) {
        File file;
        File[] listFiles = null;
        try {
            logger.info("Fetching files from path: "+path);
            if(path!=null){
                file = new File(path);
                listFiles = file.listFiles();
            }
            else{
                logger.error("IOException: path is {}",path);
                throw new IOException("IOException: path invalid");
            }

        } catch (NullPointerException e) {
            logger.error("NullPointerException: Path is null");
            throw new NullPointerException(e.getMessage());
        }catch (IOException ioe){
            System.err.println("IOException: "+ioe);
        }

        return listFiles;
    }

    /**
     * This method give list of files and directories of current directory.
     *
     * @param path - directory address.
     */
    public static void ls(String path) {
        //getting list of files or directories from parent directory
        logger.info("Fetching list of files and directories from :"+path);
        File[] file = getFileList(path);
        for (File i : file) {
            if (!i.isHidden()) {
                logger.debug("File : "+i.getName());
                System.out.printf(i.getName() + "\t");
            }
        }
    }

    /**
     * This method give list of files and directories including hidden files
     *
     * @param path - directory address.
     */
    public static void ls_a(String path) {
        //getting list of files or directories
        logger.info("Fetching list of files and directories including hidden file from :"+path);
        File[] arr = getFileList(path);

        for (File i : arr) {
            logger.debug("File :"+i.getName() );
            System.out.printf(i + "\t");
        }
    }


    /**
     * This method give list of files and directories including files and their information in reverse order
     *
     * @param path - directory address.
     */
    public static void ls_ltr(String path) {
        logger.info("Fetching list of files and directories including files and their information in reverse order from :"+path);
        File[] files = getFileList(path);
        //mapping time and file name and sorting in reverse order
        Map<Long, File> mp = new TreeMap<>(Collections.reverseOrder());
        for (File obj : files) {
            mp.put(obj.lastModified(), obj);
        }
        mp.forEach((key, value) -> {
            try {
                Path p = Path.of(value.getPath());
                //getting file attributes
                PosixFileAttributes ats = Files.readAttributes(p, PosixFileAttributes.class);
                logger.debug(PosixFilePermissions.toString(ats.permissions()) + "\t"
                        + ats.owner().getName() + "\t" + ats.group().getName() + "\t"
                        + ats.size() / 1024 + "kb \t" + pdf.format(value.lastModified()) +
                        "\t" + value.getName() + "\t\n");
                System.out.print(PosixFilePermissions.toString(ats.permissions()) + "\t"
                        + ats.owner().getName() + "\t" + ats.group().getName() + "\t"
                        + ats.size() / 1024 + "kb \t" + pdf.format(value.lastModified()) +
                        "\t" + value.getName() + "\t\n");
            } catch (IOException e) {
                logger.error("IOException : "+e);
                e.printStackTrace();
            }
        });
    }

    /**
     * This method returns the list of files and directories including hidden files and their information.
     *
     * @param path - directory address.
     */
    public static void ls_la(String path) {
        logger.info("Fetching the list of files and directories including hidden files and their information from {}",path);
        File[] files = getFileList(path);
        try {
            for (File f1 : files) {
                Path p = Path.of(f1.getPath());

                PosixFileAttributes ats = Files.readAttributes(p, PosixFileAttributes.class);
                logger.debug(PosixFilePermissions.toString(ats.permissions()) + "\t" + (f1.isFile() ? 1
                        + "\t" : files.length + "\t") + ats.owner().getName() + "\t"
                        + ats.group().getName() + "\t" + ats.size() + "\t" + pdf.format(f1.lastModified()) + "\t"
                        + f1.getName() + "\n");
                System.out.print(PosixFilePermissions.toString(ats.permissions()) + "\t" + (f1.isFile() ? 1
                        + "\t" : files.length + "\t") + ats.owner().getName() + "\t"
                        + ats.group().getName() + "\t" + ats.size() + "\t" + pdf.format(f1.lastModified()) + "\t"
                        + f1.getName() + "\n");
            }
        } catch (IOException e) {
            logger.error("IOException: "+e);
            e.printStackTrace();
        }
    }

    /**
     * This method returns the list of files and directories in sorted order
     *
     * @param path - directory address.
     */
    public static void ls_X(String path) {
        logger.info("Fetching the list of files and directories in sorted order from {}",path);
        File[] fileList = getFileList(path);
        Arrays.sort(fileList);
        for (File i : fileList) {
            if (i.getName().charAt(0) != '.') {
                logger.debug("File: "+i);
                System.out.println(i);
            }
        }
    }

    /**
     * This method returns list of files and directories sorted by time.
     *
     * @param path - directory address.
     */
    public static String ls_t(String path) {
        logger.info("Fetching list of files and directories sorted by time from {}",path);
        File[] files = getFileList(path);
        Map<Long, String> mp = new TreeMap<>();
        for (File obj : files) {
            mp.put(obj.lastModified(), obj.getName());
        }
        StringBuilder sb = new StringBuilder();
        mp.forEach((key, value) -> sb.append(pdf.format(key)).append("\t").append(value).append("\n"));
        return sb.toString();
    }

    /**
     * This method returns the list of files and directories in reverse order by time.
     *
     * @param path - directory address.
     */
    private static void ls_T(String path) {
        logger.info("Fetching the list of files and directories in reverse order by time from {}",path);
        String str = ls_t(path);
        String[] sortedList = str.split("\n");
        Arrays.sort(sortedList);
        Arrays.stream(sortedList).forEach(System.out::println);
    }

    public static void main(String[] args) throws IOException {
        logger.info("Input from CLI :"+Arrays.toString(args));
        logger.info("Invoking ls command method through switch case");
        logger.debug("command: "+args[0]);
        switch (args[0]) {
            case "ls":
                logger.info("Invoking ls command");
                logger.debug("path: "+args[1]);
                ls(args[1]);
                break;
            case "ls-a":
                logger.info("Invoking ls-a command");
                logger.debug("path: "+args[1]);
                ls_a(args[1]);
                break;
            case "ls-la":
                logger.info("Invoking ls-la command");
                logger.debug("path: "+args[1]);
                ls_la(args[1]);
                break;
            case "ls-ltr":
                logger.info("Invoking ls-ltr command");
                logger.debug("path: "+args[1]);
                ls_ltr(args[1]);
                break;
            case "ls-X":
                logger.info("Invoking ls-X command");
                logger.debug("path: "+args[1]);
                ls_X(args[1]);
            case "ls-t":
                logger.info("Invoking ls-t command");
                logger.debug("path: "+args[1]);
                System.out.println(ls_t(args[1]));
            case "ls-T":
                logger.info("Invoking ls-T command");
                logger.debug("path: "+args[1]);
                ls_T(args[1]);
                break;
            default:
                logger.error("{} command does not exist",args[0]);
                System.out.println("no such command exists");
        }
    }
}
