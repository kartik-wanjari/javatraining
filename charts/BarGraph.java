/*
 *
 *  * Copyright (c) 2022.  - All Rights Reserved
 *  *  * Unauthorized copying or redistribution of this file in source and binary forms via any medium
 *  *  * is strictly prohibited-
 *  *  * @Author -kartiks.
 *
 */

package charts;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.category.DefaultCategoryDataset;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


/**
 * This class generate bar graph of price vs product of fake store API.
 */
public class BarGraph {
    public static Logger logger = LogManager.getLogger(BarGraph.class);

    /**
     * This method send http request and receive response
     * @param url API url
     * @return response from API
     */
    public static HttpResponse<String> response(String url) {
        HttpResponse<String> res = null;
        try {
            HttpRequest request;
            if (url != null) {
                request = HttpRequest.newBuilder().GET().uri(URI.create(url)).build();
            } else {
                throw new NullPointerException();
            }
            HttpClient client = HttpClient.newBuilder().build();
            res = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (NullPointerException ne){
            System.err.println("NullPointerException =>"+ne);
        }catch (IOException ioe){
            System.err.println( "IOException => "+ioe);
        }catch (InterruptedException ie){
            System.err.println( "InterruptedException => "+ie);
        }catch (Exception e){
            System.err.println("UnknownException => "+e);
        }
        return res;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        logger.info("Starting to fetch response from Fake store API using GET request");

        HttpResponse<String> res = response("https://fakestoreapi.com/products");
        JSONArray jsonArray = null;
        if (res.statusCode() == 200) {
            jsonArray = new JSONArray(res.body());
            logger.debug("Object Data: " + res.body());
        } else {
            logger.error("Status code: " + res.statusCode() + "response not fetched");
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        if(jsonArray!=null) {
            for (Object i : jsonArray) {

                JSONObject jsonObject = new JSONObject(i.toString());
                System.out.println(jsonObject);
                System.out.println(jsonObject.get("price").toString() + " " + jsonObject.get("title").toString());
                logger.debug("title: " + jsonObject);
                logger.debug("Price: " + jsonObject);
                dataset.addValue(Double.valueOf(jsonObject.get("price").toString()), "Products", jsonObject.get("title").toString());
            }
        }else logger.error("JsonArray is null");
        logger.info("Creating bar chart of temperature vs city");
        JFreeChart barChart = ChartFactory.createBarChart(
                "Product Price Comparison",
                "Products",
                "Price(USD)",
                dataset,
                PlotOrientation.HORIZONTAL,
                false, true, false);
        logger.info("Rendering price on bar");
        CategoryItemRenderer renderer = ((CategoryPlot)barChart.getPlot()).getRenderer();

        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setDefaultItemLabelsVisible(true);
        ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,
                TextAnchor.TOP_RIGHT);
        renderer.setDefaultPositiveItemLabelPosition(position);
        try {
            logger.info("Creating image Bar Graph");
            ChartUtils.saveChartAsJPEG(new File("/Users/azuga/Documents/Azuga Training/Charts/bargraph.jpeg"), barChart, 900, 700);
            System.out.println("bargraph.jpeg created");
            logger.info("End of program");
        } catch (IOException e) {
            System.err.println("IOException:" + e);
            logger.error("Destination path of file not valid");
        }
    }
}