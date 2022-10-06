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

public class UnZip {
    private static final Logger logger = LogManager.getLogger(Mail.class);
    public void unZip(String targetZipFilePath, String destinationFolderPath, String password) {
        logger.info("Invoking unzip method");
        try {
            logger.info("Searching zip file");
            ZipFile zipFile = new ZipFile(targetZipFilePath);
            if (zipFile.isEncrypted()) {
                logger.info("File Encrypted, enter password");
                zipFile.setPassword(password.toCharArray());
                logger.info("File opened with password");
            }

            zipFile.extractAll(destinationFolderPath);
            logger.info("Unzip at location {}",destinationFolderPath);

        } catch (ZipException e) {
            logger.error("ZipException: "+e);
        }
    }
}
