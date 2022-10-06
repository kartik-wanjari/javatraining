/*
 *
 *  * Copyright (c) 2022.  - All Rights Reserved
 *  *  * Unauthorized copying or redistribution of this file in source and binary forms via any medium
 *  *  * is strictly prohibited-
 *  *  * @Author -kartiks.
 *
 */

package zipper;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Arrays;

public class Zip {
    private static final Logger logger = LogManager.getLogger(Zip.class);
    public File[] getFiles(String path){
        logger.info("Getting files from {}",path);
        File file;
        File[] files = null;
        if(path!=null) {
            file = new File(path);

            files = file.listFiles();
            logger.debug("List of files: " + Arrays.toString(files));
        }
        else {
            logger.error("Path is null");
        }
        return files;
    }
    public void makeZip(String zipPath,File[] files){
        try {
            new ZipFile(zipPath).addFiles(Arrays.asList(files));
        }catch(ZipException e){
            logger.error("ZipException: "+e);
        }
    }
}
