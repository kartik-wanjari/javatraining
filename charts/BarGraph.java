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
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * This class generate bar graph of temperature of different cities.
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
        logger.info("Starting to fetch response from weather API using GET request");
        String[] places = {"Delhi", "London", "Dubai", "Paris"};
        List<String> allResponse = new ArrayList<>();
        for (String i : places) {
            logger.debug("Fetching weather data of " + i);
            logger.info("Fetching data from weather api");
            logger.warn("Put valid weather api url");
            HttpResponse<String> res = response("http://api.weatherapi.com/v1/current.json?key=18858d6d3d5e43bb8db40153222009&q=" + i + "&aqi=no");

            if (res.statusCode() == 200) {
                allResponse.add(res.body());
                logger.debug("Object Data: " + res.body());
            } else {
                logger.error("Status code: " + res.statusCode());
            }
        }
        List<String> allTemp = new ArrayList<>();
        for (String i : allResponse) {

            JSONObject jsonObject = new JSONObject(i);
            logger.info("Fetching value of key(current) from JSONObject");
            JSONObject current = jsonObject.getJSONObject("current");
            logger.debug("Current Object: " + current.toString());
            allTemp.add(current.toString());
        }
        int m = 0;
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (String i : allTemp) {
            JSONObject temp = new JSONObject(i);
            dataset.addValue(Double.valueOf(temp.get("temp_c").toString()), "Celsius", places[m]);
            logger.debug("Celsius temperature: " + Double.valueOf(temp.get("temp_c").toString()));
            dataset.addValue(Double.valueOf(temp.get("temp_f").toString()), "Fahrenheit", places[m]);
            logger.debug("Fahrenheit temperature: " + Double.valueOf(temp.get("temp_f").toString()));
            m++;
        }
        logger.info("Creating bar chart of temperature vs city");
        JFreeChart barChart = ChartFactory.createBarChart(
                "Temperature Bar Graph",
                "City",
                "Temperature",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);
        try {
            logger.info("Creating image Bar Graph");
            ChartUtils.saveChartAsJPEG(new File("/Users/azuga/Documents/Azuga Training/Charts/Bar Graph.jpeg"), barChart, 500, 300);
            System.out.println("Bar Graph.JPEG created");
            logger.info("End of program");
        } catch (Exception e) {
            System.err.println("error");
            logger.error("Destination path of file not valid");
        }
    }
}
