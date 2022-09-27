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
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ChartsServiceImpl implements ChartsService{
    public static Logger logger = LogManager.getLogger(ChartsServiceImpl.class);

    /**
     * This method send http request and receive response
     * @param url API url
     * @return response from API
     */
    public static HttpResponse<String> res(String url) {
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

    /**
     * This method create piechart from response
     * @throws Exception thrown from
     */
    @Override
    public void piechart() throws Exception {

        logger.info("url passed");
        HttpResponse<String> response = res("https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=7&page=1&sparkline=false");

        if (response.statusCode() == 200) {
            logger.warn("response should not be null");
            JSONArray jsonArray;
            if (response.body().charAt(0) == '[') {
                jsonArray = new JSONArray(response.body());
                logger.info("response received");
                logger.debug(response.body());
            } else {
                logger.error("Json array not found");
                throw new Exception("Json array not found");
            }
            DefaultPieDataset pieDataset = new DefaultPieDataset<>();
            for (Object i : jsonArray) {
                if (i.toString().charAt(0) == '{') {
                    logger.debug(i);
                    JSONObject j = new JSONObject(i.toString());
                    logger.debug("Coin: " + j.get("id").toString() + "  Market cap: " + Long.valueOf(j.get("market_cap").toString()));
                    pieDataset.setValue(j.get("id").toString(), Long.valueOf(j.get("market_cap").toString()));
                } else {
                    throw new JSONException("");
                }
            }
            logger.info("Adding data to chart");
            JFreeChart chart = ChartFactory.createPieChart("Crypto Market-cap Summary", pieDataset, true, true, false);

            try {
                logger.info("Generating PieChart.jpeg");
                logger.warn("Destination Path should be valid");
                ChartUtils.saveChartAsJPEG(new File("/Users/azuga/Documents/Azuga Training/Charts/PieChart.jpeg"), chart, 500, 300);
                System.out.println("Pie chart created");
            } catch (Exception e) {
                System.err.println("error");
                logger.error("Chart not created");
            }
            logger.info("End of program");
        } else {
            logger.error("Status code: " + response.statusCode());
        }
    }
    @Override
    public void lineChart() throws IOException, InterruptedException {
        logger.info("Program started: Crypto Currency API");
        //fetch data
        HttpResponse<String> response = res("https://api.coingecko.com/api/v3/coins/near/market_chart?vs_currency=usd&days=7&interval=daily");

        if(response.statusCode()==200) {
            logger.info("Data fetched from crypto API");
            JSONArray jsonArray = new JSONArray();
            if(response.body().charAt(0)=='{') {
                logger.debug("Object data: "+response.body());
                JSONObject jsonObject = new JSONObject(response.body());
                System.out.println(jsonObject);
                jsonArray = jsonObject.getJSONArray("prices");
                logger.debug("Array: "+jsonArray);//fetching prices object values
                System.out.println(jsonArray);
            }
            logger.info("Collecting coordinates (day,price)");
            XYSeries xySeries = new XYSeries("near");
            for (int i = 0; i < jsonArray.length(); i++) {
                System.out.println(jsonArray.get(i));
                logger.debug("Index "+i+": "+jsonArray.get(i));
                String[] a = jsonArray.get(i).toString().replace("[", "").replace("]", "").split(",");
                System.out.println(a[1]);
                logger.debug("X-axis: "+ i+ "  Y-axis: "+a[1]);

                xySeries.add(i , Float.valueOf(a[1]));
            }
            logger.info("Generating dataset for XYSeries Collection");
            XYSeriesCollection dataset = new XYSeriesCollection(xySeries);
            logger.info("Generating XYLine graph");
            JFreeChart chart = ChartFactory.createXYLineChart("Past 7 days price of Near Protocol",
                    "Days", "Price", dataset, PlotOrientation.VERTICAL, true, true, false);

            try {
                logger.info("");
                logger.warn("Destination path must be valid");
                ChartUtils.saveChartAsJPEG(new File("/Users/azuga/Documents/Azuga Training/Charts/LineChart.jpeg"), chart, 500, 300);
                System.out.println("LineChart.jpeg created");
            } catch (Exception e) {
                System.err.println("error");
                logger.error("Destination path not valid");
            }
        }
        else {
            logger.error("Status code: "+response.statusCode());
        }

    }
    @Override
    public void multiLineChart() throws IOException, InterruptedException {
        logger.info("Program started");
        String[] coins = {"near","polkadot","leo-token","uniswap"};
        List<String> allResponse = new ArrayList<>();
        for(String coin: coins) {
            logger.trace(coin);
            HttpResponse<String> response = res("https://api.coingecko.com/api/v3/coins/" + coin + "/market_chart?vs_currency=usd&days=14&interval=daily");

            if(response.statusCode() ==200) {
                logger.info("Data fetched from Crypto API");
                allResponse.add(response.body());
            }
            else {
                logger.error("Status code: "+response.statusCode());
            }
        }
        System.out.println(allResponse.size());
        List<String> price = new ArrayList<>();
        for(String q : allResponse) {
            logger.debug(q);
            JSONObject jsonObject;
            if(q.charAt(0) == '{') {
                jsonObject = new JSONObject(q);
                logger.debug(jsonObject.toString());
                JSONArray jsonArray = jsonObject.getJSONArray("prices");

                price.add(jsonArray.toString());
            }
            else{
                logger.error("");
            }

        }
        System.out.println(price.get(0));
        int t = 0;
        XYSeriesCollection dataset = new XYSeriesCollection();
        for(String p : price) {
            XYSeries xySeries = new XYSeries(coins[t]);
            for (int i = 0; i < 15; i++) {

                String c = p.replaceAll("],\\[","]],\\[[");
                String[] a = c.split("],\\[");

                String[] b = a[i].replace("[","").replace("]","").split(",");
                System.out.println(b[1]);
                xySeries.add(i + 1, Float.valueOf(b[1]));

            }
            dataset.addSeries(xySeries);
            t++;
        }

        JFreeChart chart = ChartFactory.createXYLineChart("Past 14 days price",
                "Days", "Price", dataset, PlotOrientation.VERTICAL, true, true, false);
        try {
            ChartUtils.saveChartAsJPEG(new File("/Users/azuga/Documents/Azuga Training/Charts/MultipleLineChart.jpeg"), chart, 800, 500);
            System.out.println("Multiple Line Chart created");
        } catch (Exception e) {
            System.err.println("error");
        }
    }
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
    @Override
    public void barChart()  {
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
