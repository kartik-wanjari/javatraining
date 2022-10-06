/*
 *
 *  * Copyright (c) 2022.  - All Rights Reserved
 *  *  * Unauthorized copying or redistribution of this file in source and binary forms via any medium
 *  *  * is strictly prohibited-
 *  *  * @Author -kartiks.
 *
 */

package test;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CheckSum {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        CheckSum obj = new CheckSum();
        obj.checkSum("/Users/azuga/Desktop/FilmsApi.html");
    }
    public String checkSum(String path) throws NoSuchAlgorithmException, IOException {
        String filepath = path;

        MessageDigest messageDigest = MessageDigest.getInstance("MD5");

        FileInputStream fileInput = new FileInputStream(filepath);
        byte[] dataBytes = new byte[1024];

        int bytesRead;

        while ((bytesRead = fileInput.read(dataBytes)) != -1) {
            messageDigest.update(dataBytes, 0, bytesRead);
        }
        byte[] digestBytes = messageDigest.digest();

        StringBuffer sb = new StringBuffer("");

        for (int i = 0; i < digestBytes.length; i++) {
            sb.append(Integer.toString((digestBytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        System.out.println("Checksum for the File: " + sb.toString());

        fileInput.close();
        return  sb.toString();
    }
}