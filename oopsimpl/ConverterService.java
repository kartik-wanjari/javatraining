/*
 *
 *  * Copyright (c) 2022.  - All Rights Reserved
 *  *  * Unauthorized copying or redistribution of this file in source and binary forms via any medium
 *  *  * is strictly prohibited-
 *  *  * @Author -kartiks.
 *
 */

package oopsimpl;

import com.itextpdf.text.DocumentException;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.IOException;

public interface ConverterService {
     void csvToHtml(File file);
     void csvToPdf(String path) throws IOException, DocumentException, CsvValidationException;
     void jsonToXML(String path);

}
