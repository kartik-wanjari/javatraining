/*
 *
 *  * Copyright (c) 2022.  - All Rights Reserved
 *  *  * Unauthorized copying or redistribution of this file in source and binary forms via any medium
 *  *  * is strictly prohibited-
 *  *  * @Author -kartiks.
 *
 */

package oopsimpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;

public class Controller {
    public static Logger logger = LogManager.getLogger(Controller.class);
    public static void main(String[] args) throws Exception {
        logger.info("Creating model object");
        Model file = new Model(new File("/Users/azuga/Documents/Azuga Training/API/museum.csv"));
        System.out.println(file.getFile().getPath());
        logger.info("creating converter interface object");
        ConverterService converterService = new ConverterServiceImpl();
        logger.info("Invoking csv to html");
        converterService.csvToHtml(file.getFile());
        converterService.csvToPdf(file.getFile().getPath());
        converterService.jsonToXML("/Users/azuga/Documents/Azuga Training/API/museum for xml.json");

        ChartsService chartsService = new ChartsServiceImpl();
        chartsService.lineChart();
        chartsService.multiLineChart();
        chartsService.piechart();
        chartsService.barChart();
    }
}
