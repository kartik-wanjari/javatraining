/*
 *
 *  * Copyright (c) 2022.  - All Rights Reserved
 *  *  * Unauthorized copying or redistribution of this file in source and binary forms via any medium
 *  *  * is strictly prohibited-
 *  *  * @Author -kartiks.
 *
 */

package restfulapi;

import java.io.IOException;

public class Controller {
    public static void main(String[] args) throws IOException {

        CsvToHtml html = new CsvToHtml();
        html.csvToHtml();

        JsonToXML xml = new JsonToXML();
        xml.jsonToXML();

        CsvToPdf pdf = new CsvToPdf();
        pdf.csvToPdf();


    }
}
