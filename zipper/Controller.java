/*
 *
 *  * Copyright (c) 2022.  - All Rights Reserved
 *  *  * Unauthorized copying or redistribution of this file in source and binary forms via any medium
 *  *  * is strictly prohibited-
 *  *  * @Author -kartiks.
 *
 */

package zipper;

import java.io.File;

public class Controller {
    public static void main(String[] args) {
        Zip zip = new Zip();
        String zipPath = "/Users/azuga/Desktop/reports.zip";
        String path = "/Users/azuga/Desktop/zipper";
        File[] files = zip.getFiles(path);
        zip.makeZip(zipPath,files);

        Mail mail = new Mail();
        String[] receivers = {"wanjarik70@gmail.com",/*"prashantk@azuga.com","naveenen@azuga.com","satvikm@azuga.com",
                "suryaps@azuga.com","jasleen@codeops.tech","pruthvikp@azuga.com","ashoop@azuga.com",
                "adarshs@azuga.com","rishabh@azuga.com","vijayyv@azuga.com","sudharshan@codeops.tech",
                "indukurimr@azuga.com","lokanathk@azuga.com","dushyants@azuga.com","krupa@codeops.tech",
                "aparajitam@azuga.com","rajatt@azuga.com"*/};
        for(String r : receivers){
            mail.sendMail(r);
        }

        String openZip = "/Users/azuga/Desktop/OpenZip";
        UnZip unzip = new UnZip();
        unzip.unZip(zipPath,openZip,"");
    }
}