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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * This class mimics touch, rm, rmdir, mkdir and mv linux command.
 */
public class Task1 {
    public static Logger logger = LogManager.getLogger(Task1.class);

    /**
     * This method make file.
     *
     * @param path destination path of file
     * @return file
     * @throws IOException                thrown when path invalid
     * @throws FileAlreadyExistsException thrown when file exists
     */
    public static File createFile(String path) throws IOException {
        logger.info("creating file to path {}", path);
        File file = new File(path);
        try {
            if (!file.exists() && file.createNewFile()) {
                return file;
            } else {
                logger.error("FileAlreadyExistsException: File already exists");
                throw new FileAlreadyExistsException("File already exists");
            }
        } catch (IOException e) {
            logger.error("IOException: " + e);
            throw new IOException(e);
        }
    }

    /**
     * This method finds file.
     *
     * @param path destination path of file
     * @return file
     * @throws FileNotFoundException thrown when file not found
     * @throws IOException           thrown when path is invalid
     */
    public static File getFile(String path) throws IOException {
        logger.info("Fetching file of path {}", path);
        try {
            File file = new File(path);
            if (file.exists()) {
                return file;
            } else {
                logger.error("FileNotFoundException: File already exists");
                throw new FileNotFoundException("File already exists");
            }
        } catch (IOException e) {
            logger.error("IOException: "+e.getMessage());
            throw new IOException("verify path");
        }
    }

    /**
     * This method is creates, change and modify timestamps of a file.
     *
     * @param path -destination path of file.
     */
    public static void touch(String path) throws IOException {
        logger.info("Creating file to path {}",path);
        File f = createFile(path);
        logger.info("File created: " + f.getName());
        System.out.println("File created: " + f.getName());
    }

    /**
     * This method is change timestamps of a file.
     *
     * @param path - file path.
     */
    public static void touch_am(String path) throws IOException {
        logger.info("Create or change timestamp of file");
        File file = new File(path);
        if (file.exists()) {
            if (file.setLastModified(System.currentTimeMillis()))
                System.out.println("time changed");
        } else if (file.setLastModified(System.currentTimeMillis())) {
            touch(path);
        }
    }

    /**
     * This method move file from one directory to others.
     *
     * @param p1 - path of current directory
     * @param p2 - path of destination directory
     */
    public static void mv(String p1, String p2) throws IOException {
        try {
            Files.move(Paths.get(p1), Paths.get(p2));
            System.out.println("File moved successfully");
        } catch (IOException e) {
            throw new IOException("Cannot move file, check paths");
        }
    }

    /**
     * This method delete a file.
     *
     * @param path - path of file
     */
    public static void rmFile(String path) throws IOException {
        File f = getFile(path);
        if (f.delete())
            System.out.println("File deleted successfully");
        else
            System.out.println("file not deleted");
    }

    /**
     * This method creates new directory.
     *
     * @param path - path of directory
     * @throws IOException thrown during passing of invalid path
     */
    public static void mkdir(String path) throws IOException {
        File f = createFile(path);
        if (f.mkdir())
            System.out.println("File created successfully");
        else
            System.out.println("Error in creating file");
    }

    /**
     * This method deletes empty directory.
     *
     * @param path - path of directory
     * @throws IOException thrown during passing of invalid path
     */
    public static void rmdir(String path) throws IOException {
        File f = getFile(path);
        String[] arr = f.list();
        if (arr != null) {
            if (arr.length > 0)
                System.out.println("Cannot be Deleted,it contains some files");
            else {
                if (f.delete())
                    System.out.println("Directory deleted successfully");
                else
                    System.out.println("Directory not deleted");
            }
        } else {
            throw new NullPointerException();
        }
    }

    public static void main(String[] args) throws IOException {

        switch (args[0]) {
            case "touch":
                touch(args[1]);
                break;
            case "touch-a":
            case "touch-m":
                touch_am(args[1]);
                break;
            case "mkdir":
                mkdir(args[1]);
                break;
            case "rmdir":
                rmdir(args[1]);
                break;
            case "mv":
                mv(args[1], args[2]);
                break;
            case "rm":
                rmFile(args[1]);
                break;
            default:
                System.out.println("command doesn't match");
        }

    }

}
