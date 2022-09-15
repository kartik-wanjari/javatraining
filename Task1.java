

import java.io.*;
import java.nio.file.*;
/*
 * Copyright (c) 2022.  - All Rights Reserved
 * Unauthorized copying or redistribution of this file in source and binary forms via any medium
 * is strictly prohibited-
 * @Author -kartiks (Kartik Wanjari)
 */

/**
 * This class mimics touch, rm, rmdir, mkdir and mv command
 */
public class Task1 {

    /**
     * This method is used to create, change and modify timestamps of a file.
     * @param path -It is used to take the String  as input
     */
    public static void touch(String path) {
        try {
            File f = new File(path);
            if (!f.exists() && f.createNewFile()) {
                System.out.println("File created: " + f.getName());
            } else
                System.out.println("File already exists");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    /**
     * This method is change timestamps of a file.
     * @param path -It is used to take the String  as input
     */
    public static void touch_am(String path) {
        File f = new File(path);
        if (f.exists()) {
            if(f.setLastModified(System.currentTimeMillis()))
                System.out.println("time changed");
        } else {
            touch(path);
            f.setLastModified(System.currentTimeMillis());
        }


    }

    /**
     * This method is used to move a file from one directory to others.
     * @param p1 -It is used to take the String  as input of path one
     * @param p2 -It is used to take the String  as input of destination path
     */
    public static void mv(String p1, String p2) {
        try {
            Files.move(Paths.get(p1), Paths.get(p2));
            System.out.println("File moved successfully");
        } catch (Exception e) {
            System.out.println("File cannot be moved");
            System.out.println(e.getMessage());

        }
    }

    /**
     * This method is used delete a file.
     * @param path -It is used to take the String  as input
     */
    public static void remFile(String path) {
        File f = new File(path);
        if (f.delete())
            System.out.println("File created successfully");
        else
            System.out.println("Error in deleting file or File doesn't exist");
    }

    /**
     * This method is used to create new directory.
     * @param path -It is used to take the String  as input
     */
    public static void mkdir(String path) {
        File f = new File(path);
        if (f.mkdir())
            System.out.println("File created successfully");
        else
            System.out.println("Error in creating file");
    }

    /**
     * This method is used to delete empty directory.
     * @param path -It is used to take the String  as input
     */
    public static void rmdir(String path) {
        File f = new File(path);
        String[] arr = f.list();
        if (arr != null) {
            if ( arr.length > 0 )
                System.out.println("Cannot be Deleted,it contains some files");
            else {
                if(f.delete())
                    System.out.println("File deleted successfully");
                else
                    System.out.println("File was not deleted");
            }
        }
    }


    /**
     * The Execution of programs starts from here i.e, main() method
     * @param args - it is used to take the input from the user as command line argument
     */
    public static void main(String[] args) {

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
                remFile(args[1]);
                break;

            default:
                System.out.println("command doesn't match");
        }

    }

}
