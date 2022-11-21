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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class Controller {
    public static Logger logger = LogManager.getLogger(Controller.class);
    public static void main(String[] args) throws Exception {
        Model file = new Model(new File("/Users/azuga/Documents/Azuga Training/API/museum.csv"));
        Model json = new Model("/Users/azuga/Documents/Azuga Training/API/museum.json");
        System.out.println(file.getFile().getPath());

        ConverterService converterService = new ConverterServiceImpl();
        converterService.csvToHtml(file.getFile());
        converterService.csvToPdf(file.getFile().getPath());
        converterService.jsonToXML(json.getJson());

        ChartsService chartsService = new ChartsServiceImpl();
        chartsService.lineChart();
        chartsService.multiLineChart();
        chartsService.piechart();
        chartsService.barChart();
    }
}
